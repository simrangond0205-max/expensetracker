package com.SpringBootMVC.ExpensesTracker.service;

import com.SpringBootMVC.ExpensesTracker.DTO.CustomUserDetails;
import com.SpringBootMVC.ExpensesTracker.DTO.WebUser;
import com.SpringBootMVC.ExpensesTracker.entity.Client;
import com.SpringBootMVC.ExpensesTracker.entity.Role;
import com.SpringBootMVC.ExpensesTracker.entity.User;
import com.SpringBootMVC.ExpensesTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleService roleService;
    ClientService clientService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ClientService clientService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Transactional
    @Override
    public User findOrCreateGoogleUser(String firstName, String lastName, String email) {
        User existingUser = userRepository.findByUserName(email);
        if (existingUser != null) {
            return existingUser;
        }

        Role standardRole = roleService.findRoleByName("ROLE_STANDARD");
        if (standardRole == null) {
            throw new IllegalStateException("ROLE_STANDARD role not found.");
        }

        Client client = new Client();
        client.setFirstName(firstName != null ? firstName : "Google");
        client.setLastName(lastName != null ? lastName : "User");
        client.setEmail(email);

        User user = new User();
        user.setUserName(email);
        user.setPassword(passwordEncoder.encode("google-" + UUID.randomUUID()));
        user.setEnabled(true);
        user.setClient(client);
        user.setRoles(Arrays.asList(standardRole));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void save(WebUser webUser) {
        Client client = new Client();
        client.setFirstName(webUser.getFirstName());
        client.setLastName(webUser.getLastName());
        client.setEmail(webUser.getEmail());
        User user = new User();
        user.setUserName(webUser.getUsername());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setClient(client);
        user.setEnabled(true);
        user.setRoles(Arrays.asList(roleService.findRoleByName("ROLE_STANDARD")));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null)
            throw new UsernameNotFoundException("user not found");
        if (user.getClient() == null)
            throw new UsernameNotFoundException("user profile not found");
        return new CustomUserDetails(user.getUserName(), user.getPassword(),
                mapRolesToAuthorityes(user.getRoles()), user.getClient().getId());
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorityes(Collection<Role> roles) {
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
