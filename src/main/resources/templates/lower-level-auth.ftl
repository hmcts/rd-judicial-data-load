<html>
<head>
</head>
<body>
<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
    <tr>
        <td align="center" bgcolor="#78ab46" style="padding: 40px 0 30px 0;">
            <img src="cid:logo.png" alt="https://www.techmagister.info" style="display: block;" />
        </td>
    </tr>
    <tr>
        <td bgcolor="#eaeaea" style="padding: 40px 30px 40px 30px;">
            <p>Dear ${name},</p>
            <p>${content}</p>
            <p>Sending Email using Spring Boot and Apache Freemarker</p>
            <#--<p>CLick here to verify your email <b>${token}</b></p>-->
            <p>Thanks</p>
        </td>
    </tr>
    <tr>
        <td bgcolor="#777777" style="padding: 30px 30px 30px 30px;">
            <p>${signature}</p>
            <p>${location}</p>
        </td>
    </tr>
</table>
</body>
</html>