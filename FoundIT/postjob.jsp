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
    <input type="hidden" name="reqtype" value="POST_JOB">
      <table>
        <tr>
        	  <td>CompanyName</td>
          <td><input type="text" name="companyname"></td>
        </tr>
         <tr>
        	  <td>SalaryRate</td>
          <td><input type="text" name="salaryrate"></td>
        </tr>
         <tr>
        	  <td>PositionType</td>
          <td><input type="text" name="positiontype"></td>
        </tr>
         <tr>
        	  <td>Location</td>
          <td><input type="text" name="location"></td>
        </tr>
        <tr>
        	  <td>JobDescription</td>
          <td><input type="text" name="jobdescription"></td>
        </tr>

        <tr>
          <td colspan="2" align="left">
            <input type="submit" value="post job">
          </td>
        </tr>
      </table>
    </form>

</body>
</html>