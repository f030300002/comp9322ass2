package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

import dao.UserDao;

public class UserService {
	public static void register_ee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// do something
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String realName = request.getParameter("realName");
		String phoneNum = request.getParameter("phoneNum");
		String address = request.getParameter("address");
		
		// check if available
		if (userId == null || userId.trim().equals("") || password == null || password.trim().equals("")) {
			request.getRequestDispatcher("/register/failed.html").forward(request, response);
			return;
		}
		userId = userId + "ee";
		if (UserDao.hasUser(userId)) {
			request.getRequestDispatcher("/register/failed.html").forward(request, response);
			return;
		}
		
		// now it's available and go register
		Boolean registerSuccess = UserDao.registerEE(new User(userId, password, realName, phoneNum, address));
		
		if (registerSuccess)
			request.getRequestDispatcher("/userInterface/employee.jsp").forward(request, response);
		else
			request.getRequestDispatcher("/register/failed.html").forward(request, response);
	}
}
