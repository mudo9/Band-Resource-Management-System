package uk.ac.sheffield.bandproject.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import uk.ac.sheffield.bandproject.BandProjectApplication;
import uk.ac.sheffield.bandproject.Controller.BandController;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BandController.class)
@ContextConfiguration(classes = BandProjectApplication.class)
public class BandControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Mock
    private BindingResult bindingResult;


    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldReturnTrainingBandPage() throws Exception {
        mockMvc.perform(get("/director/training-band"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/training-band"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldReturnSeniorBandPage() throws Exception {
        mockMvc.perform(get("/director/senior-band"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/senior-band"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldReturnError_trainingBand_whenNotPresentUserId() throws Exception {
        mockMvc.perform(get("/director/training-band/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/training-band-member"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "User not found"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldReturnUser_trainingBand_whenPresentUserId() throws Exception {
        // setup vars
        User user = new User();
        // setup return
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/director/training-band/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/training-band-member"))
                .andExpect(model().attributeExists("bandMember"))
                .andExpect(model().attribute("bandMember", user));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldReturnError_seniorBand_whenNotPresentUserId() throws Exception {
        mockMvc.perform(get("/director/senior-band/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/senior-band-member"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "User not found"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldReturnUser_seniorBand_whenPresentUserId() throws Exception {
        // setup vars
        User user = new User();
        // setup return
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/director/senior-band/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/senior-band-member"))
                .andExpect(model().attributeExists("bandMember"))
                .andExpect(model().attribute("bandMember", user));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldShowTrainingBandNewPage() throws Exception {
        mockMvc.perform(get("/director/training-band/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/addTrainingBandMember"))
                .andExpect(model().attributeExists("bandMember"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldShowSeniorBandNewPage() throws Exception {
        mockMvc.perform(get("/director/senior-band/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("director/addSeniorBandMember"))
                .andExpect(model().attributeExists("bandMember"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR", username="director", password="password")
    public void shouldAddUserToTrainingBand_byEmail() throws Exception {
        User user = new User();
        when(userService.addBandToUser(anyString(), anyLong())).thenReturn(user);

        mockMvc.perform(post("/director/training-band")
                        .with(csrf().asHeader())
                        .param("email", "example@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/training-band"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR", username="director", password="password")
    public void shouldAddUserToSeniorBand_byEmail() throws Exception {
        User user = new User();
        when(userService.addBandToUser(anyString(), anyLong())).thenReturn(user);

        mockMvc.perform(post("/director/senior-band")
                        .with(csrf().asHeader())
                        .param("email", "example@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/senior-band"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR", username="director", password="password")
    public void shouldAddUserToTrainingBand_byFullName() throws Exception {
        User user = new User();
        when(userService.addBandToUser(anyString(), anyLong())).thenReturn(user);

        mockMvc.perform(post("/director/training-band/by-fullName")
                        .with(csrf().asHeader())
                        .param("fullName", "full name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/training-band"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR", username="director", password="password")
    public void shouldAddUserToSeniorBand_byFullName() throws Exception {
        User user = new User();
        when(userService.addBandToUser(anyString(), anyLong())).thenReturn(user);

        mockMvc.perform(post("/director/senior-band/by-fullName")
                        .with(csrf().asHeader())
                        .param("fullName", "full name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/senior-band"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldDeleteTrainingBandMember_byId() throws Exception {
        doNothing().when(userService).deleteBandMember(anyLong(), anyLong());

        mockMvc.perform(delete("/director/training-band/1")
            .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(content().string("Training band member deleted successfully"));
    }

    @Test
    @WithMockUser(roles="DIRECTOR")
    public void shouldDeleteSeniorBandMember_byId() throws Exception {
        doNothing().when(userService).deleteBandMember(anyLong(), anyLong());

        mockMvc.perform(delete("/director/senior-band/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(content().string("Senior band member deleted successfully"));
    }
}
