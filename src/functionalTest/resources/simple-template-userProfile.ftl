<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sending Email for user profile</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 15px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">

    <table align="center" border="0" cellpadding="0" cellspacing="0" width="1000" style="border-collapse: collapse;">
        <tr>
            <td style="padding: 40px 30px 40px 30px;">
                <p>Following JO profiles were deleted : </p>
                <p>Profiles with one Object ID having multiple Personal Codes </p>
                <div>
                    <table style="border-collapse: expression('separate', cellSpacing='15px'); border-spacing: 15px">
                        <tr bgcolor="#eaeaea">
                            <td style="text-align: center">Per Code</td>
                            <td style="text-align: center">Object Id</td>
                            <td style="text-align: center">Per Id</td>
                        </tr>
                        <#list objectIds as objectId>
                            <tr style="border: 1px solid black;">
                                <td style="text-align: left">${objectId.personalCode}</td>
                                <td style="text-align: center">${objectId.objectId}</td>
                                <td style="text-align: center">${objectId.perId}</td>
                            </tr>
                         </#list>
                    </table>
                 </div>
                 <hr>
                 <p>Profiles with one Personal Code having multiple Object IDs </p>
                                <div>
                                    <table style="border-collapse: expression('separate', cellSpacing='15px'); border-spacing: 15px">
                                        <tr bgcolor="#eaeaea">
                                            <td style="text-align: center">Per Code</td>
                                            <td style="text-align: center">Object Id</td>
                                            <td style="text-align: center">Per Id</td>
                                        </tr>
                                        <#list personalCodes as personalCode>
                                            <tr style="border: 1px solid black;">
                                                <td style="text-align: left">${personalCode.personalCode}</td>
                                                <td style="text-align: center">${personalCode.objectId}</td>
                                                <td style="text-align: center">${personalCode.perId}</td>
                                            </tr>
                                         </#list>
                                    </table>

                                </div>
            </td>
        </tr>
    </table>
</body>
</html>