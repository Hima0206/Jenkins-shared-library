<!DOCTYPE html>
<html>
<head>
  <title>Jenkins Build Details</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 0;
    }

    h1 {
      background-color: ${HEADER_COLOR}; 
      color: #ffffff;
      padding: 20px;
      margin: 0;
    }

    table {
      width: 80%;
      margin: 20px auto;
      border-collapse: collapse;
    }

    th, td {
      padding: 10px;
      text-align: left;
      border: 1px solid #ddd;
    }

    th {
      background-color: #0074d9;
      color: #ffffff;
    }

    a {
      text-decoration: none;
      color: #0074d9;
    }

    a:hover {
      text-decoration: underline;
    }

    .status {
      font-weight: bold;
      color: ${STATUS_COLOR}; 
    }

    .secret-status {
      font-weight: bold;
      color: ${SECRET_COLOR}; 
    }
  </style>
</head>
<body>

<h1>Jenkins Build Details</h1>

<table>
  <thead>
    <tr>
      <th>Name</th>
      <th>Value</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Build Number</td>
      <td>${BUILD_NUMBER}</td>
    </tr>
    <tr>
      <td>Job Name</td>
      <td>${JOB_NAME}</td>
    </tr>
    <tr>
      <td>Build Status</td>
      <td class="status">${BUILD_STATUS}</td>
    </tr>
    <tr>
      <td>Build Duration</td>
      <td>${BUILD_DURATION}</td>
    </tr>
    <tr>
      <td>Build URL</td>
      <td><a href="${BUILD_URL}" target="_blank">${BUILD_URL}</a></td>
    </tr>
    <tr>
      <td>Branch</td>
      <td><a href="${JOB_DISPLAY_URL}" target="_blank">${GIT_BRANCH}</a></td>
    </tr>
    <tr>
      <td>Last Commit</td>
      <td>${LAST_COMMIT_ID} - ${LAST_COMMIT_MESSAGE} (by ${LAST_COMMIT_AUTHOR})</td>
    </tr>
    <tr>
      <td>Gitleaks Scan</td>
      <td class="secret-status">
        ${GITLEAKS_STATUS} <br>
        Commits scanned: ${GITLEAKS_COMMITS_SCANNED} <br>
        Leaks found: ${GITLEAKS_LEAKS_FOUND}
      </td>
    </tr>
  </tbody>
</table>

</body>
</html>
