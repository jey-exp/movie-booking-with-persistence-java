package com.cineverse.service;

import com.cineverse.entity.Customer;
import com.cineverse.entity.User;
import com.cineverse.exception.InvalidCredentialsException;
import com.cineverse.exception.UserAlreadyExistsException;
import com.cineverse.exception.UserNotFoundException;
import com.cineverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password); // In a real app, hash the password
        return userRepository.save(customer);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (!user.getPassword().equals(password)) { // In a real app, compare hashed passwords
            throw new InvalidCredentialsException("Invalid password.");
        }
        return user;
    }
}
