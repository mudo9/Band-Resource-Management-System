package uk.ac.sheffield.bandproject.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Service.UserService;
import uk.ac.sheffield.bandproject.Service.UserServiceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling committee member management operations.
 */
@Controller
@RequestMapping("/director") // Base URL mapping for director-related actions
public class CommitteeMemberController {

    private final UserService userService; // Service to handle user-related operations

    /**
     * Constructor to inject the UserServiceImpl dependency
     *
     * @param userService the user service for user-related operations.
     */
    public CommitteeMemberController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays all committee members.
     *
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/committee")
    public String directorCommittee(Model model) {
        // Retrieve the current user's authentication info
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if the user is not authenticated
        }

        // Fetch all committee members from the service
        List<User> committeeMembers = userService.getCommitteeMembers();
        // Sort the committee members by their ID in ascending order
        committeeMembers.sort(Comparator.comparing(User::getId));
        // Add the sorted list to the model for rendering in the view
        model.addAttribute("committeeMembers", committeeMembers);
        return "director/committee"; // Render the committee view
    }

    /**
     * Displays details of a specific committee member by their ID.
     *
     * @param id the ID of the committee member.
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML if unauthenticated.
     */
    @GetMapping("/committee/{id}")
    public String getCommitteeMemberById(@PathVariable Long id, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        // Fetch the user by ID
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            // Add user details to the model if the user is found
            model.addAttribute("committeeMember", user.get());
        } else {
            // Add an error message to the model if the user is not found
            model.addAttribute("errorMessage", "User not found");
        }
        return "director/committee-member"; // Render the committee member details view
    }

    /**
     * Renders the form to add a new committee member.
     *
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template.
     */
    @GetMapping("/committee/new")
    public String showCommitteeMemberForm(Model model) {
        model.addAttribute("committeeMember", new User()); // Add a blank user object for the form
        return "director/addCommitteeMember"; // Render the add committee member form
    }

    /**
     * Promotes a user to a committee member by their email address.
     *
     * @param email the email of the user.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the committee member page.
     */
    @PostMapping("/committee")
    public String addCommitteeMember(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to promote the user to a committee member
            User promotedUser = userService.promoteUserToCommitteeMember(email);
            // Add a success message if the promotion is successful
            redirectAttributes.addFlashAttribute("successMessage",
                    "User promoted to committee member successfully");
        } catch (Exception e) {
            // Add an error message if an exception occurs
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:committee"; // Redirect to the committee list page
    }

    /**
     * Removes a committee member by their ID (demote them).
     *
     * @param id the ID of the committee member.
     * @return a response entity indicating the result of the remove operation.
     */
    @DeleteMapping("/committee/{id}")
    @ResponseBody
    public ResponseEntity<?> removeCommitteeMember(@PathVariable Long id) {
        try {
            // Attempt to demote the user from committee membership
            userService.demoteUserFromCommitteeMember(id);
            return ResponseEntity.ok("User demoted successfully"); // Return success response
        } catch (Exception e) {
            // Return an error response with the exception message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing user from committee members: " + e.getMessage());
        }
    }
}

