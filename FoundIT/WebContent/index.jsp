<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FoudIT</title>
</head>
<body>

    <form action="/FoundIT/c" method="post">
    <input type="hidden" name="reqtype" value="LOG_IN">
      <table>
        <tr>
          <td>User Name (Email)</td>
          <td><input type="text" name="userName"></td>
        </tr>
        <tr>
          <td>Password</td>
          <td><input type="password" name="password"></td>
        </tr>
        <tr>
          <td colspan="2" align="left">
            <input type="submit" value="log in">
          </td>
        </tr>
      </table>
    </form>
    
    <form action="/FoundIT/c" method="post">
    <input type="hidden" name="reqtype" value="GO_REGISTER">
      <table>
        <tr>
          <td align="center">
            <input type="submit" value="register">
          </td>
        </tr>
      </table>
    </form>

</body>
</html>