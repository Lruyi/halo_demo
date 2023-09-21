<!DOCTYPE html>
<!-- complex_template.ftl -->
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>${name}'s Report</title>
    </head>
    <body>
    <h1>${name}'s Report</h1>

    <p><strong> ID:</strong> ${id}</p>
    <p><strong>Age:</strong> ${age}</p>
    <p><strong>Address:</strong> ${address}</p>

    <table border="1">
        <tr>
            <th>Subject</th>
            <th>Score</th>
        </tr>
        <#list scores as score>
            <tr>
                <td>${score.subject}</td>
                <td>${score.score}</td>
            </tr>
        </#list>
    </table>

    <#if photo??>
        <p><img src="${photo}" width="200" height="200"></p>
    </#if>

    <p style="page-break-before: always;"></p>
    </body>
    </html>