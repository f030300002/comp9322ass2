package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class userService {
	public static void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// do something
		request.getRequestDispatcher("/register/failed.html").forward(request, response);
	}
}
