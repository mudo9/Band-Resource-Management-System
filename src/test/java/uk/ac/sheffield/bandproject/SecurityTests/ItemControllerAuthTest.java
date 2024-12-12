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
import uk.ac.sheffield.bandproject.Controller.ItemController;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Service.InstrumentService;
import uk.ac.sheffield.bandproject.Service.InstrumentServiceImpl;
import uk.ac.sheffield.bandproject.Service.MiscellaneousServiceImpl;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ItemControllerAuthTest {
    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private InstrumentServiceImpl instrumentService;

    @Mock
    private MiscellaneousServiceImpl miscellaneousService;

    @InjectMocks
    private ItemController thisController;

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
        String viewName = thisController.items(model);
        assertEquals("login", viewName);
    }

    @Test
    public void shouldReturnCommitteeView_whenUserIsAuthenticated(){
        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(instrumentService.getAllInstruments()).thenReturn(Arrays.asList(new Instrument(), new Instrument()));
        when(miscellaneousService.getAllMiscellaneous()).thenReturn(Arrays.asList(new Miscellaneous(), new Miscellaneous()));
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(new User()));
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.items(model);
        assertEquals("committee-member/items", viewName);
    }
}
