<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FoudIT</title>
</head>
<body>
    <form action="/FoundIT/c" method="get">
    <input type="hidden" name="reqtype" value="SEARCH_JOB">
      <table>
        <tr>
          <td><input type="text" name="search" value="search..."></td>
        </tr>

        <tr>
          <td colspan="2" align="left">
            <input type="submit" value="search job">
          </td>
        </tr>
      </table>
    </form>

</body>
</html>