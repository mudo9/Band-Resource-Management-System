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
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Service.*;

import java.util.*;

/**
 * Controller for handling authentication and user management operations.
 */
@Controller
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor to inject required services.
     *
     * @param userService the user service for user-related operations.
     * @param authenticationManager the authentication manager for handling authentication.
     */
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Renders login page
     *
     * @return the name of the login HTML template.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Renders "My Account" page.
     *
     * @param model the model to add user attributes for rendering.
     * @return the name of the my-account HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/my-account")
    public String myAccountPage(Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login";
        }
        Optional<User> user = userService.getUserByEmail(authentication.getName());
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        }
        return "my-account";
    }

    /**
     * Updates the authenticated user's account details.
     *
     * @param id the ID of the user.
     * @param user the updated user details.
     * @param bindingResult the result of validation on the user object.
     * @return a response entity indicating the result of the update operation.
     */
    @PutMapping("/my-account/{id}")
    @ResponseBody
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
                                                 @Valid @RequestBody User user,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating account: " +
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            // Update the existing user with new details
            user.setId(id);
            userService.updateAccount(id, user);
            return ResponseEntity.ok("user updated successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user " + e.getMessage());
        }
    }

    /**
     * Renders the registration page.
     *
     * @param model the model to add a blank user object.
     * @return the name of the register HTML template.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Add an empty user object to the model
        return "register"; // Returns the registration page
    }

    /**
     * Handles user registration.
     *
     * @param user the user object to register.
     * @param result the result of validation on the user object.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template.
     */
    @PostMapping("/register")
    public String performRegister(@Valid @ModelAttribute User user,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Return to registration page if there are validation errors
            return "register";
        }
        try {
            // Attempt to register the user
            userService.register(user);
        } catch (Exception e) {
            // Handle registration errors
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        model.addAttribute("successMessage", "User created successfully, please login");
        return "/login"; // Redirect to login page after successful registration
    }

    /**
     * Renders the loans page.
     *
     * @param model the model to add attributes for rendering.
     * @return the name of the loans HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/loans")
    public String loans(Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        model.addAttribute("email", authentication.getName()); // Add user's email to the model
        return "loans"; // Returns the loans.html page
    }

    /**
     * Renders the parent overview page for director.
     *
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/director/parents")
    public String directorParents(Model model) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        List<User> children = userService.getAllChildren(); // Fetch all child users
        model.addAttribute("children", children); // Add children to the model
        return "/director/parents"; // Returns the director/parents.html page
    }

    /**
     * Renders the loans page for a specific child.
     *
     * @param childId the ID of the child whose loans to display.
     * @param model the model to add child attributes for rendering.
     * @return the name of the HTML template.
     */
    @GetMapping("/child/{childId}/loans")
    public String loans(@PathVariable Long childId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Optional<User> childOpt = userService.getUserById(childId); // Fetch child by ID
        if (childOpt.isPresent()) {
            User child = childOpt.get();
            model.addAttribute("fullName", child.getFullName()); // Add child details to the model
            model.addAttribute("child", child);
        }
        return "/child/loans"; // Returns the child's loans page
    }

    /**
     * Renders the form to add a new child.
     *
     * @param model the model to add an empty user object for the form.
     * @return the name of the add child form HTML template.
     */
    @GetMapping("child/new")
    public String showCreateChildForm(Model model) {
        model.addAttribute("child", new User()); // Add an empty user object for the form
        return "addChild"; // Displays the add child form
    }

    /**
     * Renders the view of children associated with a parent.
     *
     * @param model the model to add attributes for rendering.
     * @return the name of the view child HTML template.
     */
    @GetMapping("child/view")
    public String showViewChildForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Optional<User> parentOpt = userService.getUserByEmail(authentication.getName());
        if (parentOpt.isPresent()) {
            User parent = parentOpt.get();
            List<User> children = userService.getParentsChildren(parent); // Fetch parent's children
            model.addAttribute("children", children);
            model.addAttribute("parent", parent);
        }
        return "viewChild"; // Displays the view child page
    }

    /**
     * Renders the detailed view of a child.
     *
     * @param childId the ID of the child.
     * @param parentId the ID of the parent associated with the child.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or redirects to another page.
     */
    @GetMapping("/child/{childId}")
    public String childView(@PathVariable Long childId, @RequestParam Long parentId, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        Optional<User> childOpt = userService.getUserById(childId); // Fetch child by ID
        Optional<User> parentOpt = userService.getUserById(parentId); // Fetch parent by ID
        if (childOpt.isPresent() && parentOpt.isPresent()) {
            User child = childOpt.get();
            User parent = parentOpt.get();
            // Check if parent matches the child's parent relationship
            ParentChildRelationship parentRelationship = child.getParentRelationship();
            if (parentRelationship.getParent().equals(parent)) {
                model.addAttribute("child", child); // Add child details to the model
                model.addAttribute("parent", parent);
                model.addAttribute("email", child.getEffectiveEmail());
                model.addAttribute("phoneNumber", child.getEffectivePhoneNumber());
                return "redirect:/child/{childId}/performance"; // Redirect to child's music page
            } else {
                return "performances"; // Redirect to performance if parent-child mismatch
            }
        }
        return "performances"; // Redirect to performance if child or parent not found
    }

    /**
     * Adds a new child to the system and associates it with the authenticated parent.
     *
     * @param childName the name of the child to be added.
     * @return the name of the HTML template after adding the child.
     */
    @PostMapping("child")
    public String addChild(@RequestParam String childName) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        User child = new User();
        child.setFullName(childName); // Set child's name
        Optional<User> parentOpt = userService.getUserByEmail(authentication.getName());
        if (parentOpt.isPresent()) {
            User parent = parentOpt.get();
            userService.addParentToChild(child, parent); // Associate parent with child
        }
        return "performances"; // Redirect after adding the child
    }
}
