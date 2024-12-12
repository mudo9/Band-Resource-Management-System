package uk.ac.sheffield.bandproject.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.ui.Model;
import uk.ac.sheffield.bandproject.Controller.PerformanceController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PerformanceControllerTest {

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private PerformanceController performanceController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnLoginViewWhenUserNotAuthenticatedOnPerformance(){
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        String viewName = performanceController.performance(model);
        assertEquals("login", viewName);
    }

    @Test
    public void shouldReturnLoginViewWhenCommitteeMemberNotAuthenticatedOnCommitteeMemberPerformance(){
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        String viewName = performanceController.committeeMemberPerformance(model);
        assertEquals("login", viewName);
    }
}
