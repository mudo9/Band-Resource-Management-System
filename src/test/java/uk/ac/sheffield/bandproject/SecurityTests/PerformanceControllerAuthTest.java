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
import uk.ac.sheffield.bandproject.Controller.PerformanceController;
import uk.ac.sheffield.bandproject.Model.Performance;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Service.PerformanceServiceImpl;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PerformanceControllerAuthTest {
    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PerformanceServiceImpl performanceService;

    @InjectMocks
    private PerformanceController thisController;

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

        // tests
        String viewName = thisController.performance(model);
        assertEquals("login", viewName);

        viewName = thisController.childPerformance(1L, model);
        assertEquals("login", viewName);

        viewName = thisController.committeeMemberPerformance(model);
        assertEquals("login", viewName);
    }

    @Test
    public void shouldReturnPerformancesView_whenUserIsAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.performance(model);
        assertEquals("performances", viewName);
    }

    @Test
    public void shouldReturnChildPerformanceView_whenUserIsAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.childPerformance(1L, model);
        assertEquals("/child/performances", viewName);
    }

    @Test
    public void shouldReturnCommitteeMemberPerformanceView_whenUserIsAuthenticated(){
        // setup vars
        List<Performance> performances = Arrays.asList(new Performance(), new Performance());

        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(performanceService.getAllPerformances()).thenReturn(performances);
        SecurityContextHolder.setContext(securityContext);

        // tests
        String viewName = thisController.committeeMemberPerformance(model);
        verify(model, times(1)).addAttribute("performances", performances);
        assertEquals("committee-member/performances", viewName);
    }
}
