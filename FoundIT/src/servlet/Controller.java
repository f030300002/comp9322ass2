package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.userService;

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
		} else if (requestType.equals("GO_REGISTER")) {
			response.sendRedirect("/FoundIT/register/register.jsp");
		} else if (requestType.equals("REGISTER")) {
			userService.register(request, response);
		} else if(requestType.equals("CREATE_POLL")){
			
		}
		
	}
}
