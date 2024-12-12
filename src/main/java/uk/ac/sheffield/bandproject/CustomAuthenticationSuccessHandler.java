package uk.ac.sheffield.bandproject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CHILD"))) {

            // Invalidate the session to log out the committee_member immediately
            request.getSession().invalidate();

            // Redirect to the logout page or to a different location
            response.sendRedirect("/logout");
        } else {
            String redirectUrl = "/performance";

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_DIRECTOR")) {
                    redirectUrl = "/director/committee";
                    break;
                }
                else if (authority.getAuthority().equals("ROLE_COMMITTEE_MEMBER")) {
                    redirectUrl = "/committee-member/performance";
                }
            }

            response.sendRedirect(redirectUrl);
        }


    }
}
