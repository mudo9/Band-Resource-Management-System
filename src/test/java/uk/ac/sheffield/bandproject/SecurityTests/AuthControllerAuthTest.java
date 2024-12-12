package uk.ac.sheffield.bandproject.SecurityTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.ui.Model;
import uk.ac.sheffield.bandproject.Controller.AuthController;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerAuthTest {
    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAccountView_whenUserIsAuthenticated(){
        // setup vars
        User user = new User();

        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        SecurityContextHolder.setContext(securityContext);

        // testing
        String viewName = authController.myAccountPage(model);
        verify(model, times(1)).addAttribute("user", user);
        assertEquals("my-account", viewName);
    }

    @Test
    public void shouldReturnLoansView_whenUserIsAuthenticated(){
        // setup vars
        User user = new User();

        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        SecurityContextHolder.setContext(securityContext);

        // testing
        String viewName = authController.loans(model);
        verify(model, times(1)).addAttribute("email", "user");
        assertEquals("loans", viewName);
    }

    @Test
    public void shouldReturnDirectorParentsView_whenUserIsAuthenticated(){
        // setup vars
        User user = new User();
        List<User> children = Arrays.asList(new User(), new User());

        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(userService.getAllChildren()).thenReturn(children);
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = authController.directorParents(model);
        verify(model, times(1)).addAttribute("children", children);
        assertEquals("/director/parents", viewName);
    }

    @Test
    public void shouldReturnLoginView_whenUserIsNotAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = authController.myAccountPage(model);
        assertEquals("login", viewName);

        viewName = authController.loans(model);
        assertEquals("login", viewName);

        viewName = authController.directorParents(model);
        assertEquals("login", viewName);
    }

    @Test
    public void shouldThrow_whenShowViewChildFormUserIsNotAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(null);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        SecurityContextHolder.setContext(securityContext);

        // test
        assertThrows(NullPointerException.class, () -> authController.showViewChildForm(model));
    }

    @Test
    public void shouldNotAddChild_whenUserIsNotAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        SecurityContextHolder.setContext(securityContext);

        // test
        assertThrows(NullPointerException.class, () -> authController.addChild("some name"));
    }


}
