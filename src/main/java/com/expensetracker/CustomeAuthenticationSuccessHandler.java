package com.SpringBootMVC.ExpensesTracker.controller;

import com.SpringBootMVC.ExpensesTracker.entity.Client;
import com.SpringBootMVC.ExpensesTracker.entity.User;
import com.SpringBootMVC.ExpensesTracker.service.ClientService;
import com.SpringBootMVC.ExpensesTracker.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class CustomeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    UserService userService;
    ClientService clientService;

    @Autowired
    public CustomeAuthenticationSuccessHandler(UserService userService, ClientService clientService) {
        this.userService = userService;
        this.clientService = clientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            String email = (String) oAuth2User.getAttributes().get("email");
            if (email != null && !email.isBlank()) {
                username = email;
            }
        }

        User user = userService.findUserByUserName(username);
        if (user == null && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            String firstName = (String) oAuth2User.getAttributes().get("given_name");
            String lastName = (String) oAuth2User.getAttributes().get("family_name");
            String email = (String) oAuth2User.getAttributes().get("email");
            user = userService.findOrCreateGoogleUser(firstName, lastName, email);
        }

        if (user == null || user.getClient() == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User profile not found.");
            return;
        }

        Client client = user.getClient();
        HttpSession session = request.getSession();
        session.setAttribute("client", client);
        response.sendRedirect(request.getContextPath() + "/list");
    }
}
