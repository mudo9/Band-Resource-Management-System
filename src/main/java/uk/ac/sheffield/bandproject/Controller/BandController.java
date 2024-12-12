package uk.ac.sheffield.bandproject.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.bandproject.Model.MusicPart;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Service.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling band management operations.
 */
@Controller
@RequestMapping("/director") // Base URL mapping for all director-related actions
public class BandController {

    private final UserService userService; // Service to handle user-related logic

    /**
     * Constructor to inject UserServiceImpl dependency.
     *
     * @param userService the user service for user-related operations.
     */
    public BandController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetches and displays all members in the Training Band.
     *
     * @param model the model to add attribute for rendering.
     * @return the name of the training-band HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/training-band")
    public String directorTrainingBand(Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }


        // Retrieve all users assigned to the Training Band
        List<User> users = userService.getUsersByBand("Training");
        model.addAttribute("users", users); // Add the users to the model for rendering
        return "director/training-band"; // Render the training-band page
    }

    /**
     * Fetches and displays all members in the Senior Band.
     *
     * @param model the model to add attribute for rendering.
     * @return the name of the senior-band HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/senior-band")
    public String directorSeniorBand(Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }


        // Retrieve all users assigned to the Senior Band
        List<User> users = userService.getUsersByBand("Senior");
        model.addAttribute("users", users); // Add the users to the model for rendering
        return "director/senior-band"; // Render the senior-band page
    }

    /**
     * Fetches details of a specific Training Band member by ID.
     *
     * @param userId the ID of the band member.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("training-band/{userId}")
    public String getTrainingBandMemberById(@PathVariable Long userId, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        Optional<User> user = userService.getUserById(userId); // Fetch user by ID

        if (user.isPresent()) {
            model.addAttribute("bandMember", user.get()); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found"); // Display error if user not found
        }
        return "director/training-band-member"; // Render the training band member page
    }

    /**
     * Fetches details of a specific Senior Band member by ID.
     *
     * @param userId the ID of the band member.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("senior-band/{userId}")
    public String getSeniorBandMemberById(@PathVariable Long userId, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        Optional<User> user = userService.getUserById(userId); // Fetch user by ID

        if (user.isPresent()) {
            model.addAttribute("bandMember", user.get()); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found"); // Display error if user not found
        }
        return "director/senior-band-member"; // Render the senior band member page
    }

    /**
     * Renders the form to add a new member to the Training Band.
     *
     * @param model the model to add an empty user object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("training-band/new")
    public String showAddTrainingBandMemberForm(Model model) {
        model.addAttribute("bandMember", new User()); // Add an empty user object for the form
        return "director/addTrainingBandMember"; // Render the form page
    }

    /**
     * Renders the form to add a new member to the Senior Band.
     *
     * @param model the model to add an empty user object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("senior-band/new")
    public String showAddSeniorBandMemberForm(Model model) {
        model.addAttribute("bandMember", new User()); // Add an empty user object for the form
        return "director/addSeniorBandMember"; // Render the form page
    }

    /**
     * Adds a user to the Training Band by email.
     *
     * @param userEmail the email of the user.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the training band page.
     */
    @PostMapping("training-band")
    public String addTrainingBandMember(@RequestParam("email") String userEmail, RedirectAttributes redirectAttributes) {
        try {
            Long bandId = 2L; // ID for the Training Band
            User updatedUser = userService.addBandToUser(userEmail, bandId);
            if (updatedUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Error adding user to training band - User does not exist");
            } else {
                redirectAttributes.addFlashAttribute("successMessage",
                        "User added to training band successfully");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Handle any errors
        }

        return "redirect:/director/training-band"; // Redirect to the training band page
    }

    /**
     * Adds a user to the Senior Band by email.
     *
     * @param userEmail the email of the user.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the senior band page.
     */
    @PostMapping("senior-band")
    public String addSeniorBandMember(@RequestParam("email") String userEmail, RedirectAttributes redirectAttributes) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        try {
            Long bandId = 1L; // ID for the Senior Band
            User updatedUser = userService.addBandToUser(userEmail, bandId);
            if (updatedUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Error adding user to senior band - User does not exist");
            } else {
                redirectAttributes.addFlashAttribute("successMessage",
                        "User added to senior band successfully");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Handle any errors
        }

        return "redirect:/director/senior-band"; // Redirect to the senior band page
    }

    /**
     * Adds a user to the Training Band by full name.
     *
     * @param userFullName the full name of the user.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the training band page.
     */
    @PostMapping("training-band/by-fullName")
    public String addTrainingBandMemberByFullName(@RequestParam("fullName") String userFullName,
                                                  RedirectAttributes redirectAttributes) {
        try {
            Long bandId = 2L; // ID for the Training Band
            userService.addBandToUserByFullName(userFullName, bandId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "User added to Training Band successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // Handle any errors
        }
        return "redirect:/director/training-band";
    }

    /**
     * Adds a user to the Senior Band by full name.
     *
     * @param userFullName the full name of the user.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the senior band page.
     */
    @PostMapping("senior-band/by-fullName")
    public String addSeniorBandMemberByFullName(@RequestParam("fullName") String userFullName,
                                                RedirectAttributes redirectAttributes) {
        try {
            Long bandId = 1L; // ID for the Senior Band
            userService.addBandToUserByFullName(userFullName, bandId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "User added to Senior Band successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // Handle any errors
        }
        return "redirect:/director/senior-band";
    }

    /**
     * Delete a Training Band member by ID.
     *
     * @param userId the ID of the band member to be deleted.
     * @return a response entity indicating the result of the delete operation.
     */
    @DeleteMapping("/training-band/{userId}")
    @ResponseBody
    public ResponseEntity<?> deleteTrainingBandMember(@PathVariable Long userId) {
        try {
            Long bandId = 2L; // ID for the Training Band
            userService.deleteBandMember(userId, bandId);
            return ResponseEntity.ok("Training band member deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting training band member: " + e.getMessage());
        }
    }

    /**
     * Delete a Senior Band member by ID.
     *
     * @param userId the ID of the band member to be deleted.
     * @return a response entity indicating the result of the delete operation.
     */
    @DeleteMapping("/senior-band/{userId}")
    @ResponseBody
    public ResponseEntity<?> deleteSeniorBandMember(@PathVariable Long userId) {
        try {
            Long bandId = 1L; // ID for the Senior Band
            userService.deleteBandMember(userId, bandId);
            return ResponseEntity.ok("Senior band member deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting senior band member: " + e.getMessage());
        }
    }
}

