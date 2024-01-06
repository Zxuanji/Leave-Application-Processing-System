package javaCA.interceptor;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javaCA.model.UserSession;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {

		String uri = request.getRequestURI();
		if (uri.startsWith("/css/") || uri.startsWith("/image/") || uri.startsWith("/home/"))
			return true;

		if (uri.equalsIgnoreCase("/")) {
			response.sendRedirect("/home/login");
			return false;
		}
		
		HttpSession session = request.getSession();
		UserSession userSession = (UserSession) session.getAttribute("user");
		if (userSession == null) {
			response.sendRedirect("/home/login");
			return false;
		}
		
		List<String> roleSet = userSession.getUser().getRoleIds();
		
		if (uri.startsWith("/admin") && !roleSet.contains("admin")) {
			response.sendRedirect("/home/noAccess");
			return false;
		}
		
		if (uri.startsWith("/manager") && !roleSet.contains("manager")) {
			response.sendRedirect("/home/noAccess");
			return false;
		}
		
		return true;
	}
}
