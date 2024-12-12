package uk.ac.sheffield.bandproject;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {
        String errorMessage = exception.getMessage();

        if(exception instanceof BadCredentialsException){
            errorMessage = "Invalid email or password";
        }

        //Redirect to login page with error message as a query parameter
        response.sendRedirect("/login?error=" + errorMessage);
    }
}
