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
        def commitsScanned = sh(script: "git rev-list --count HEAD", returnStdout: true).trim()
        def buildDuration = currentBuild.durationString.replace(' and counting', '')
        
        // Replace placeholders in template
        tplContent = tplContent.replace('${BUILD_STATUS}', buildStatus)
                               .replace('${STATUS_COLOR}', statusColor)
                               .replace('${HEADER_COLOR}', headerColor)
                               .replace('${BUILD_NUMBER}', "${BUILD_NUMBER}")
                               .replace('${JOB_NAME}', "${JOB_NAME}")
                               .replace('${BUILD_URL}', "${BUILD_URL}")
                               .replace('${JOB_DISPLAY_URL}', "${BUILD_URL}") // Adjust if you have job URL separate
                               .replace('${RUN_DISPLAY_URL}', "${BUILD_URL}") // Adjust if you have run URL separate
                               .replace('${GIT_BRANCH}', gitBranch)
                               .replace('${LAST_COMMIT_ID}', lastCommit[0])
                               .replace('${LAST_COMMIT_AUTHOR}', lastCommit[1])
                               .replace('${LAST_COMMIT_MESSAGE}', lastCommit[2])
                               .replace('${BUILD_DURATION}', buildDuration)

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
