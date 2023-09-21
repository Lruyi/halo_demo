<!DOCTYPE html>
<!-- complex_template.ftl -->
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>${student.name}'s Report</title>
    </head>
    <body>
    <h1>${student.name}'s Report</h1>

    <p><strong>Student ID:</strong> ${student.id}</p>
    <p><strong>Age:</strong> ${student.age}</p>
    <p><strong>Address:</strong> ${student.address}</p>

    <table border="1">
        <tr>
            <th>Subject</th>
            <th>Score</th>
        </tr>
        <#list student.scores as score>
            <tr>
                <td>${score.subject}</td>
                <td>${score.score}</td>
            </tr>
        </#list>
    </table>

    <#if student.photo??>
        <p><img src="${student.photo}" width="200" height="200"></p>
    </#if>

    <p style="page-break-before: always;"></p>
    </body>
    </html>