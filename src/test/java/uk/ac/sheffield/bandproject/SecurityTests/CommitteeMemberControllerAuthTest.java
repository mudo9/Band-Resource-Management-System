package uk.ac.sheffield.bandproject.SecurityTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import uk.ac.sheffield.bandproject.Controller.CommitteeMemberController;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CommitteeMemberControllerAuthTest {
    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private CommitteeMemberController thisController ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnLoginView_whenUserIsNotAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.directorCommittee(model);
        assertEquals("login", viewName);
    }

    @Test
    public void shouldReturnCommitteeView_whenUserIsAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.directorCommittee(model);
        assertEquals("director/committee", viewName);
    }
}
