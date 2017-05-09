package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.UserService;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resolvedCommand(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		resolvedCommand(request, response);
	}

	private void resolvedCommand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getParameter("reqtype");
		if (requestType == null) {
			response.sendRedirect("index.jsp");
<<<<<<< HEAD
		} else if (requestType.equals("GO_REGISTER")) {
			response.sendRedirect("/FoundIT/register/register.jsp");
		} else if (requestType.equals("REGISTER")) {
			userService.register(request, response);
		} else if(requestType.equals("CREATE_POLL")){
			
=======
		} else if (requestType.equals("GO_REGISTER_EMPLOYEE")) {
			response.sendRedirect("/FoundIT/register/register_employee.jsp");
		} else if (requestType.equals("REGISTER_EMPLOYEE")) {
			UserService.register_ee(request, response);
		} else {
			response.sendRedirect("index.jsp");
>>>>>>> 9ac1e5987ea2555e986e214e7f17ce14be12c43d
		}
	}
}
