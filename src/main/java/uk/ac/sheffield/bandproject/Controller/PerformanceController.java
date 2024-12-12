package uk.ac.sheffield.bandproject.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Service.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for handling performance management operations.
 */
@Controller
public class PerformanceController {

    // Injecting required services to handle business logic for performances
    private final PerformanceService performanceService;
    private final UserService userService;
    private final PerformanceMemberServiceImpl performanceMemberService;
    private final MusicSetServiceImpl musicSetService;

    /**
     * Constructor to inject dependencies.
     *
     * @param performanceService the performance service for performance-related operations.
     * @param userService the user service for user-related operations.
     * @param performanceMemberService the performance member service for performance member related operations.
     * @param musicSetService the music set service for music set related operations.
     */
    public PerformanceController(PerformanceService performanceService, UserServiceImpl userService,
                                 PerformanceMemberServiceImpl performanceMemberService,
                                 MusicSetServiceImpl musicSetService) {
        this.performanceService = performanceService;
        this.userService = userService;
        this.performanceMemberService = performanceMemberService;
        this.musicSetService = musicSetService;
    }

    /**
     * Displays all performances for a user.
     *
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/performance")
    public String performance(Model model) {
        // Retrieve authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        Optional<User> userOptional = userService.getUserByEmail(authentication.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user); // Add user details to the model
            Set<Band> bands = user.getBands();
            List<Map<String, Object>> performanceDetails = new ArrayList<>();

            // Iterate through each band the user belongs to
            for (Band band : bands) {
                List<Performance> performances = performanceService.getPerformanceByBand(band.getId());
                for (Performance performance : performances) {
                    Optional<PerformanceMember> performanceMemberOptional = performanceMemberService
                            .findByUserIdAndBandIdAndPerformanceId(user.getId(), band.getId(), performance.getId());

                    if (performanceMemberOptional.isPresent()) {
                        // Only include performances where the user is a member
                        Boolean availability = performanceMemberOptional
                                .map(PerformanceMember::getAvailability)
                                .orElse(false);

                        // Build performance details map
                        Map<String, Object> performanceDetail = new HashMap<>();
                        performanceDetail.put("performance", performance);
                        performanceDetail.put("band", band);
                        performanceDetail.put("availability", availability);
                        performanceDetails.add(performanceDetail);
                    }
                }
            }
            model.addAttribute("performanceDetails", performanceDetails); // Add performance details to the model
        }
        return "performances"; // Return the view for performances
    }

    /**
     * Displays performances for a specific child.
     *
     * @param childId the ID of the child.
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/child/{childId}/performance")
    public String childPerformance(@PathVariable Long childId, Model model) {
        // Retrieve authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        Optional<User> childOpt = userService.getUserById(childId);
        if (childOpt.isPresent()) {
            User child = childOpt.get();
            model.addAttribute("child", child); // Add child details to the model
            Set<Band> bands = child.getBands();
            List<Map<String, Object>> performanceDetails = new ArrayList<>();

            // Iterate through each band the child belongs to
            for (Band band : bands) {
                List<Performance> performances = performanceService.getPerformanceByBand(band.getId());
                for (Performance performance : performances) {
                    Optional<PerformanceMember> performanceMemberOptional = performanceMemberService
                            .findByUserIdAndBandIdAndPerformanceId(child.getId(), band.getId(), performance.getId());

                    if (performanceMemberOptional.isPresent()) {
                        // Only include performances where the child is a member
                        Boolean availability = performanceMemberOptional
                                .map(PerformanceMember::getAvailability)
                                .orElse(false);

                        // Build performance details map
                        Map<String, Object> performanceDetail = new HashMap<>();
                        performanceDetail.put("performance", performance);
                        performanceDetail.put("band", band);
                        performanceDetail.put("availability", availability);
                        performanceDetails.add(performanceDetail);
                    }
                }
            }
            model.addAttribute("performanceDetails", performanceDetails); // Add performance details to the model
        }
        return "/child/performances"; // Return the view for child's performances
    }

    /**
     * Displays all performances for committee members.
     *
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/committee-member/performance")
    public String committeeMemberPerformance(Model model) {
        // Retrieve authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        List<Performance> performances = performanceService.getAllPerformances();
        model.addAttribute("performances", performances); // Add performances to the model
        return "committee-member/performances"; // Return the view for committee members
    }

    /**
     * Displays a specific performance by its ID.
     *
     * @param userId the ID of the user.
     * @param bandId the ID of the band.
     * @param performanceId the ID of the performance.
     * @param model the model to add attribute for rendering.
     * @return the performance HTML template.
     */
    @GetMapping("/performance/{userId}/{bandId}/{performanceId}")
    public String getPerformanceById(@PathVariable Long userId,
                                     @PathVariable Long bandId,
                                     @PathVariable Long performanceId,
                                     Model model) {
        Optional<Performance> performance = performanceService.getPerformanceById(performanceId);
        Optional<PerformanceMember> performanceMember = performanceMemberService
                .findByUserIdAndBandIdAndPerformanceId(userId, bandId, performanceId);
        if (performance.isPresent() && performanceMember.isPresent()) {
            model.addAttribute("performance", performance.get()); // Add performance details to the model
            model.addAttribute("performanceMember", performanceMember.get());
        } else {
            model.addAttribute("errorMessage", "Performance not found"); // Add error message if not found
        }
        return "performance"; // Return the performance view
    }

    /**
     * Displays a specific performance to committee members.
     *
     * @param id the ID of the performance.
     * @param model the model to add attribute for rendering.
     * @return the performance HTML template.
     */
    @GetMapping("committee-member/performance/{id}")
    public String getPerformanceByIdCommitteeMember(@PathVariable Long id, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        Optional<Performance> performance = performanceService.getPerformanceById(id);
        if (performance.isPresent()) {
            model.addAttribute("performance", performance.get()); // Add performance details to the model
            Set<Band> bands = performance.get().getBands();
            for (Band band : bands) {
                if (band.getName().equals("Senior")) {
                    model.addAttribute("seniorBand", band); // Add senior band details to the model
                }
                if (band.getName().equals("Training")) {
                    model.addAttribute("trainingBand", band); // Add training band details to the model
                }
            }
            List<PerformanceMember> players = performanceMemberService.findByPerformanceIdAndAvailability(id, Boolean.TRUE);
            model.addAttribute("players", players); // Add available players to the model
            List<MusicSet> musicSets = musicSetService.getAllMusicSets();
            model.addAttribute("musicSets", musicSets); // Add all music sets to the model
        } else {
            model.addAttribute("errorMessage", "Performance not found"); // Add error message if not found
        }
        return "committee-member/performance"; // Return the performance view for committee members
    }

    /**
     * Displays the form to add a new performance.
     *
     * @param model the model to add an empty performance object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("committee-member/performance/new")
    public String showAddPerformanceForm(Model model) {
        model.addAttribute("performance", new Performance()); // Add a new performance object for the form
        List<MusicSet> musicSets = musicSetService.getAllMusicSets(); // Fetch all available music sets
        model.addAttribute("musicSets", musicSets); // Add music sets to the model
        return "committee-member/addPerformance"; // Return the view to add a new performance
    }

    /**
     * Adds a new performance.
     *
     * @param performance the performance object.
     * @param seniorBand optional flag indicating if the senior band is involved in the performance.
     * @param trainingBand optional flag indicating if the training band is involved in the performance.
     * @param bindingResult the result of validation on the performance object.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the performances page.
     */
    @PostMapping("/committee-member/performance")
    public String addPerformance(Performance performance,
                                 @RequestParam(value = "seniorBand", required = false) Boolean seniorBand,
                                 @RequestParam(value = "trainingBand", required = false) Boolean trainingBand,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and redirect back to the add form with error messages
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating performance: " + bindingResult.getAllErrors().get(0));
            redirectAttributes.addFlashAttribute("location", performance.getLocation());
            redirectAttributes.addFlashAttribute("date", performance.getDate());
            redirectAttributes.addFlashAttribute("time", performance.getTime());
            return "redirect:performance/new"; // Redirect to the add form
        }

        try {
            // Save the new performance
            Performance savedPerformance = performanceService.savePerformance(performance);

            // Associate bands with the performance based on input
            if (Boolean.TRUE.equals(seniorBand)) {
                performanceService.addBandToPerformance(savedPerformance.getId(), 1L); // Add senior band
            }
            if (Boolean.TRUE.equals(trainingBand)) {
                performanceService.addBandToPerformance(savedPerformance.getId(), 2L); // Add training band
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Performance created successfully"); // Add success message
            return "redirect:/committee-member/performance"; // Redirect to the list of performances
        } catch (Exception e) {
            // Handle exceptions and redirect back to the add form with error details
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating performance " + e.getMessage());
            redirectAttributes.addFlashAttribute("location", performance.getLocation());
            redirectAttributes.addFlashAttribute("date", performance.getDate());
            redirectAttributes.addFlashAttribute("time", performance.getTime());
            return "redirect:performance/new"; // Redirect to the add form
        }
    }

    /**
     * Updates a performance member's availability.
     *
     * @param userId the ID of the user.
     * @param bandId the ID of the band.
     * @param performanceId the ID of the performance.
     * @param performanceMember the performance member object.
     * @param bindingResult the result of validation on the performance member object.
     * @return a response entity indicating the result of the update operation.
     */
    @PutMapping("/performance/{userId}/{bandId}/{performanceId}")
    @ResponseBody
    public ResponseEntity<?> updatePerformanceMember(@PathVariable Long userId,
                                                     @PathVariable Long bandId,
                                                     @PathVariable Long performanceId,
                                                     @Valid @RequestBody PerformanceMember performanceMember,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating availability: " +
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            // Update the performance member's details
            performanceMemberService.updatePerformanceMember(userId, bandId, performanceId, performanceMember);
            return ResponseEntity.ok("Performance member updated successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating availability: " + e.getMessage());
        }
    }

    /**
     * Updates a performance.
     *
     * @param id the ID of the performance.
     * @param performance the performance object.
     * @param musicSetIds the list of music set ID to add.
     * @param seniorBand optional flag indicating if the senior band is involved in the performance.
     * @param trainingBand optional flag indicating if the training band is involved in the performance.
     * @param bindingResult the result of validation on the performance object.
     * @return a response entity indicating the result of the update operation.
     */
    @PutMapping("/committee-member/performance/{id}")
    @ResponseBody
    public ResponseEntity<?> updatePerformance(@PathVariable Long id,
                                               @Valid @RequestBody Performance performance,
                                               @RequestParam(value = "musicSets") List<Long> musicSetIds,
                                               @RequestParam(value = "seniorBand", required = false) Boolean seniorBand,
                                               @RequestParam(value = "trainingBand", required = false) Boolean trainingBand,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating performance: " +
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            performance.setId(id); // Set the ID of the performance to update

            // Fetch and associate the provided music sets with the performance
            Set<MusicSet> musicSet = musicSetIds
                    .stream()
                    .map(musicSetId -> musicSetService.getMusicSetById(musicSetId)
                            .orElseThrow(() -> new IllegalArgumentException("Music Set not found")))
                    .collect(Collectors.toSet());

            if (!musicSet.isEmpty()) {
                performance.setMusicSets(musicSet); // Associate music sets if provided
            }

            // Update the performance details
            performanceService.updatePerformance(id, performance);

            // Update the band associations
            if (Boolean.TRUE.equals(seniorBand)) {
                performanceService.addBandToPerformance(id, 1L); // Add senior band
            } else {
                performanceService.removeBandFromPerformance(id, 1L); // Remove senior band
            }
            if (Boolean.TRUE.equals(trainingBand)) {
                performanceService.addBandToPerformance(id, 2L); // Add training band
            } else {
                performanceService.removeBandFromPerformance(id, 2L); // Remove training band
            }
            return ResponseEntity.ok("Performance updated successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating performance " + e.getMessage());
        }
    }

    /**
     * Deletes a performance.
     *
     * @param id the ID of the performance.
     * @return a response entity indicating the result of the delete operation.
     */
    @DeleteMapping("/committee-member/performance/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePerformance(@PathVariable Long id) {
        try {
            // Delete the performance by its ID
            performanceService.deletePerformance(id);
            return ResponseEntity.ok("Performance deleted successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting performance " + e.getMessage());
        }
    }
}

