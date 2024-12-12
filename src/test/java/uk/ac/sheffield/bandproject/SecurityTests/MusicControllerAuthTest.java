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
import uk.ac.sheffield.bandproject.Controller.MusicController;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Service.MusicOrderServiceImpl;
import uk.ac.sheffield.bandproject.Service.MusicPartServiceImpl;
import uk.ac.sheffield.bandproject.Service.MusicSetService;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MusicControllerAuthTest {
    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private MusicSetService musicSetService;

    @Mock
    private MusicOrderServiceImpl musicOrderService;

    @Mock
    private MusicPartServiceImpl musicPartService;

    @InjectMocks
    private MusicController thisController;

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
        String viewName = thisController.music(model);
        assertEquals("login", viewName);

        viewName = thisController.childMusic(1L, model);
        assertEquals("login", viewName);

        viewName = thisController.committeeMemberMusic(model);
        assertEquals("login", viewName);
    }

    @Test
    public void shouldReturnMusicView_whenUserIsAuthenticated(){
        // setup mock data
        User user = new User(1L, "example@example.com", "unhashed", "1209385", "user",
                new ParentChildRelationship(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>());
        List<MusicOrder> musicOrdersFulfilled = Arrays.asList(new MusicOrder(), new MusicOrder());
        List<MusicOrder> musicOrdersReady = Arrays.asList(new MusicOrder(), new MusicOrder());
        List<MusicOrder> musicOrderNotReady = Arrays.asList(new MusicOrder(), new MusicOrder());
        List<MusicPart> musicPartNeeded = Arrays.asList(new MusicPart(), new MusicPart());
        List<MusicPart> musicPartHave = Arrays.asList(new MusicPart(), new MusicPart());

        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(musicOrderService.getUserMusicOrderFulfilled(1L)).thenReturn(musicOrdersFulfilled);
        when(musicOrderService.getUserMusicOrderReady(1L)).thenReturn(musicOrdersReady);
        when(musicOrderService.getUserMusicOrderNotReady(1L)).thenReturn(musicOrderNotReady);
        when(musicPartService.getUserMusicPartNeeded(1L)).thenReturn(musicPartNeeded);
        when(musicPartService.getUserMusicPart(1L)).thenReturn(musicPartHave);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.music(model);
        assertEquals("music", viewName);

        verify(model, times(1)).addAttribute("fullName", "user");
        verify(model, times(1)).addAttribute("user", Optional.of(user).get());
        verify(model, times(1)).addAttribute("musicOrdersFulfilled", musicOrdersFulfilled);
        verify(model, times(1)).addAttribute("musicOrdersReady", musicOrdersReady);
        verify(model, times(1)).addAttribute("musicOrderNotReady", musicOrderNotReady);
        verify(model, times(1)).addAttribute("musicPartNeeded", musicPartNeeded);
        verify(model, times(1)).addAttribute("musicPartHave", musicPartHave);
    }

    @Test
    public void shouldReturnChildMusicView_whenUserIsAuthenticated(){
        // setup return
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.childMusic(1L, model);
        assertEquals("/child/music", viewName);
    }

    @Test
    public void shouldReturnCommitteeMemberMusicView_whenUserIsAuthenticated(){
        // setup vars
        List<MusicSet> musicSets = Arrays.asList(new MusicSet(), new MusicSet());
        List<MusicOrder> musicOrders = Arrays.asList(new MusicOrder(), new MusicOrder());

        // setup returns
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(musicSetService.getAllMusicSets()).thenReturn(musicSets);
        when(musicOrderService.getAllMusicOrderReady()).thenReturn(musicOrders);
        SecurityContextHolder.setContext(securityContext);

        // test
        String viewName = thisController.committeeMemberMusic(model);
        assertEquals("committee-member/music", viewName);

        verify(model, times(1)).addAttribute("musicSets", musicSets);
        verify(model, times(1)).addAttribute("musicOrders", musicOrders);
    }
}
