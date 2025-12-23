def call(String recipient) {
    script {
        // Load HTML template from shared library
        def tplContent = libraryResource "org/dcube/notification/notify.tpl"

        // Build status and colors
        def buildStatus = currentBuild.currentResult
        def statusColor = buildStatus == 'SUCCESS' ? 'green' : 'red'
        def headerColor = buildStatus == 'SUCCESS' ? '#28a745' : '#dc3545'

        // Git info
        def lastCommit = sh(script: "git log -1 --pretty=format:'%h|%an|%s'", returnStdout: true).trim().split('\\|')
        def gitBranch = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()

        // Gitleaks info
        def gitleaksReport = fileExists('gitleaks-report.json') ? readJSON(file: 'gitleaks-report.json') : [:]
        def leaksFound = gitleaksReport?.size() ?: 0
        def commitsScanned = gitleaksReport?.commits_scanned ?: '0'
        def secretStatus = leaksFound > 0 ? 'Secrets Found!' : 'No secrets detected'
        def secretColor = leaksFound > 0 ? 'red' : 'green'

        // Replace placeholders in template
        tplContent = tplContent.replace('${BUILD_STATUS}', buildStatus)
                               .replace('${STATUS_COLOR}', statusColor)
                               .replace('${HEADER_COLOR}', headerColor)
                               .replace('${BUILD_NUMBER}', "${BUILD_NUMBER}")
                               .replace('${JOB_NAME}', "${JOB_NAME}")
                               .replace('${BUILD_URL}', "${BUILD_URL}")
                               .replace('${JOB_DISPLAY_URL}', "${BUILD_URL}")  // Can adjust if you have job URL separate
                               .replace('${RUN_DISPLAY_URL}', "${BUILD_URL}")  // Can adjust if you have run URL separate
                               .replace('${GIT_BRANCH}', gitBranch)
                               .replace('${LAST_COMMIT_ID}', lastCommit[0])
                               .replace('${LAST_COMMIT_AUTHOR}', lastCommit[1])
                               .replace('${LAST_COMMIT_MESSAGE}', lastCommit[2])
                               .replace('${GITLEAKS_STATUS}', secretStatus)
                               .replace('${GITLEAKS_COMMITS_SCANNED}', "${commitsScanned}")
                               .replace('${GITLEAKS_LEAKS_FOUND}', "${leaksFound}")
                               .replace('${SECRET_COLOR}', secretColor)

        // Write final HTML to workspace
        writeFile file: "${WORKSPACE}/notify.html", text: tplContent
    }

    // Send email
    emailext(
        subject: "${JOB_NAME} - Build #${BUILD_NUMBER} - ${currentBuild.currentResult}",
        body: readFile("${WORKSPACE}/notify.html"),
        to: recipient,
        mimeType: 'text/html'
    )
}
