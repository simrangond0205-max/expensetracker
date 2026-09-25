package com.SpringBootMVC.ExpensesTracker.security;

import com.SpringBootMVC.ExpensesTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Autowired
    public GoogleOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");

        if (email != null && !email.isBlank()) {
            userService.findOrCreateGoogleUser(firstName, lastName, email);
        }

        return oauth2User;
    }
}
