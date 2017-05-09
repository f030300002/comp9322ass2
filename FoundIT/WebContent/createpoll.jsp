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
    <input type="hidden" name="reqtype" value="CREATE_POLL">
      <table>
        <tr>
          <td>Title</td>
          <td><input type="text" name="title"></td>
        </tr>
        <tr>
          <td>Description</td>
          <td><input type="text" name="description"></td>
        </tr>
         <tr>
          <td>OptionType</td>
          <td><input type="text" name="optiontype"></td>
        </tr>
        <tr>
          <td>Option</td>
          <td id="container"><input type="text" id="o" name="option"></td>
        </tr>
        <tr> 
        		<td><input type="button" id="add" value="add further options"></td>
        		<script type="text/javascript">
		        var add = document.getElementById('add'),
		            container = document.getElementById('container');
		        add.onclick = function(){
		           var input = document.createElement("input");
		               input.setAttribute('type', 'text')
		               input.setAttribute('id', 'o')
		               input.setAttribute('name', 'option')
		            container.appendChild(input);
		            input.focus();
		        }
		    </script>
        </tr>
        <tr>
          <td>Comments</td>
          <td><input type="text" name="comments"></td>
        </tr>
        <tr>
          <td>FinalChoice</td>
          <td><input type="text" name="finalchoice"></td>
        </tr>
        <tr>
          <td colspan="2" align="left">
            <input type="submit" value="create">
          </td>
        </tr>
      </table>
    </form>

</body>
</html>