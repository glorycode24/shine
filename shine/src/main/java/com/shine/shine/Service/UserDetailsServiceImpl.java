package com.shine.shine.Service;

import com.shine.shine.Repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // "username" here is actually the email address sent from the login form.
        return usersRepository.findByEmail(username)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with email: " + username));
    }
}