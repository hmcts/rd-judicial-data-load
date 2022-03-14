<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sending Email for lower level auth</title>

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
                <p>Following JO profiles where with Base Location Id's newly found :</p>
                </br>
                <div>
                    <table style="border-collapse: expression('separate', cellSpacing='15px'); border-spacing: 15px">
                        <tr bgcolor="#eaeaea">
                            <td style="text-align: center">Per Code</td>
                            <td style="text-align: center">Object Id</td>
                            <td style="text-align: center">Per Id</td>
                            <td style="text-align: center">Base Location ID</td>
                        </tr>
                        <#list appointments as appointment>
                            <tr style="border: 1px solid black;">
                                <td style="text-align: left">${appointment.personalCode}</td>
                                <td style="text-align: center">${appointment.objectId}</td>
                                <td style="text-align: center">${appointment.perId}</td>
                                <td style="text-align: center">${appointment.baseLocationId}</td>
                            </tr>
                         </#list>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</body>
</html>