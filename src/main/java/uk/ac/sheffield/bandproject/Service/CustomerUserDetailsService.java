package uk.ac.sheffield.bandproject.Service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.UserRepository;
import java.util.*;
import java.util.stream.Collectors;

import javax.naming.NameNotFoundException;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //Constructor
    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Fetch the User from the database using the email
        User user = userRepository.findByEmailAndNoParent(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        //Map the user's roles to a list of GrantedAuthority objects
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        //Return a Spring Security User object that contains the user's email, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

}
