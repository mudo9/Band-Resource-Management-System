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

import java.util.List;
import java.util.Optional;

@Controller
public class MusicController {
    // Services injected for handling business logic across the controller
    private final MusicSetService musicSetService;
    private final MusicPartService musicPartService;
    private final BandService bandService;
    private final MusicOrderService musicOrderService;
    private final UserService userService;

    /**
     * Constructor to initialize dependencies via Dependency Injection
     *
     * @param musicSetService the service for music set related operations.
     * @param musicPartService the service for music part related operations.
     * @param bandService the service for band related operations.
     * @param musicOrderService the service for music order related operations.
     * @param userService the user service for user-related operations.
     */
    public MusicController(MusicSetService musicSetService, MusicPartService musicPartService,
                           BandService bandService, MusicOrderService musicOrderService,
                           UserService userService) {
        this.musicSetService = musicSetService;
        this.musicPartService = musicPartService;
        this.bandService = bandService;
        this.musicOrderService = musicOrderService;
        this.userService = userService;
    }

    /**
     * Fetches and displays all music related information.
     *
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/music")
    public String music(Model model) {
        // Get the current user's authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // Redirect to login page if not authenticated
            return "login";
        }
        // Retrieve the authenticated user using their email
        Optional<User> user = userService.getUserByEmail(authentication.getName());
        if (user.isPresent()) {
            // Populate the model with user-specific data if user is found
            User currentUser = user.get();
            List<MusicOrder> musicOrdersFulfilled = musicOrderService.getUserMusicOrderFulfilled(currentUser.getId());
            List<MusicOrder> musicOrdersReady = musicOrderService.getUserMusicOrderReady(currentUser.getId());
            List<MusicOrder> musicOrderNotReady = musicOrderService.getUserMusicOrderNotReady(currentUser.getId());
            List<MusicPart> musicPartNeeded = musicPartService.getUserMusicPartNeeded(currentUser.getId());
            List<MusicPart> musicPartHave = musicPartService.getUserMusicPart(currentUser.getId());
            model.addAttribute("fullName", authentication.getName());
            model.addAttribute("user", currentUser);
            model.addAttribute("musicOrdersFulfilled", musicOrdersFulfilled);
            model.addAttribute("musicOrdersReady", musicOrdersReady);
            model.addAttribute("musicOrderNotReady", musicOrderNotReady);
            model.addAttribute("musicPartNeeded", musicPartNeeded);
            model.addAttribute("musicPartHave", musicPartHave);
        }
        return "music"; // Return the view name
    }

    /**
     * Renders the view of a child's music related information.
     *
     * @param childId the ID of the child.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/child/{childId}/music")
    public String childMusic(@PathVariable Long childId, Model model) {
        // Get the current authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // Redirect to login page if not authenticated
            return "login";
        }
        // Fetch the child user by ID
        Optional<User> childOpt = userService.getUserById(childId);
        if (childOpt.isPresent()) {
            User child = childOpt.get();
            // Retrieve parent details of the child
            User parent = child.getParentRelationship().getParent();
            // Fetch various music orders and parts for the child
            List<MusicOrder> musicOrdersFulfilled = musicOrderService.getChildMusicOrderFulfilled(child.getId());
            List<MusicOrder> musicOrdersReady = musicOrderService.getChildMusicOrderReady(child.getId());
            List<MusicOrder> musicOrderNotReady = musicOrderService.getChildMusicOrderNotReady(child.getId());
            List<MusicPart> musicPartNeeded = musicPartService.getUserMusicPartNeeded(child.getId());
            List<MusicPart> musicPartHave = musicPartService.getChildMusicPart(child.getId());
            // Add data to the model for the view
            model.addAttribute("fullName", child.getFullName());
            model.addAttribute("child", child);
            model.addAttribute("parent", parent);
            model.addAttribute("musicOrdersFulfilled", musicOrdersFulfilled);
            model.addAttribute("musicOrdersReady", musicOrdersReady);
            model.addAttribute("musicOrderNotReady", musicOrderNotReady);
            model.addAttribute("musicPartNeeded", musicPartNeeded);
            model.addAttribute("musicPartHave", musicPartHave);
        }
        return "/child/music"; // Return the child-specific music view
    }

    /**
     * Renders the view of the committee members' music related information.
     *
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/committee-member/music")
    public String committeeMemberMusic(Model model) {
        // Get the current authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // Redirect to login page if not authenticated
            return "login";
        }
        // Fetch all available music sets and ready orders for committee members
        List<MusicSet> musicSets = musicSetService.getAllMusicSets();
        List<MusicOrder> musicOrders = musicOrderService.getAllMusicOrderReady();
        List<MusicOrder> musicOrdersFulfilled = musicOrderService.getAllMusicOrderFulfilled();
        // Add data to the model for committee member's music view
        model.addAttribute("musicSets", musicSets);
        model.addAttribute("musicOrders", musicOrders);
        model.addAttribute("musicOrdersFulfilled", musicOrdersFulfilled);
        return "committee-member/music"; // Return the view for committee members
    }

    /**
     * Adds a new music order for the specified user.
     *
     * @param userId the ID of the user.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the music page.
     */
    @PostMapping("/music-order")
    public String addMusicOrder(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        // Create a new music order for the specified user
        try{
            musicOrderService.createMusicOrder(userId);
            redirectAttributes.addFlashAttribute("successMessage", "Music order created successfully");
        } catch(Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/music"; // Redirect to the main music page
    }

    /**
     * Fetches and displays a music order.
     *
     * @param musicOrderId the ID of the music order.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("music-order/{musicOrderId}")
    public String getMusicOrderById(@PathVariable Long musicOrderId, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        // Fetch the music order by its ID
        Optional<MusicOrder> musicOrder = musicOrderService.getMusicOrderById(musicOrderId);
        if (musicOrder.isPresent()) {
            // Add the music order to the model if found
            model.addAttribute("musicOrder", musicOrder.get());
        } else {
            // Display an error message if not found
            model.addAttribute("error", "Music Order not found");
        }
        // Fetch all parts associated with this music order
        List<MusicPart> musicParts = musicOrderService.getPartsByMusicOrderId(musicOrderId);
        model.addAttribute("musicParts", musicParts);
        return "/music-order"; // Return the specific music order view
    }

    /**
     * Fetches the music order details for a committee member.
     *
     * @param musicOrderId the ID of the music order.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("committee-member/music-order/{musicOrderId}")
    public String getMusicOrderByIdCommittee(@PathVariable Long musicOrderId, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        // Fetch the music order by its ID
        Optional<MusicOrder> musicOrder = musicOrderService.getMusicOrderById(musicOrderId);
        if (musicOrder.isPresent()) {
            // Add the music order to the model if found
            model.addAttribute("musicOrder", musicOrder.get());
        } else {
            // Display an error message if not found
            model.addAttribute("error", "Music Order not found");
        }
        // Fetch all parts associated with this music order
        List<MusicPart> musicParts = musicOrderService.getPartsByMusicOrderId(musicOrderId);
        model.addAttribute("musicParts", musicParts);
        return "/committee-member/music-order"; // Return the specific music order view
    }

    /**
     * Creates a music order for a child.
     *
     * @param childId the ID of the child.
     * @param ownerId the ID of the owner.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the child's music page.
     */
    @PostMapping("child/music-order")
    public String addChildMusicOrder(@RequestParam Long childId, @RequestParam Long ownerId,
                                     RedirectAttributes redirectAttributes) {
        // Create a new music order for a child, owned by the specified owner
        try{
            musicOrderService.createChildMusicOrder(ownerId, childId);
            redirectAttributes.addFlashAttribute("successMessage", "Music order created successfully");
        } catch(Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/child/" + childId + "/music"; // Redirect to the child's music page
    }

    /**
     * Displays the form to add a new music part to the music order.
     *
     * @param musicOrderId the ID of the music order.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template
     */
    @GetMapping("music-order/{musicOrderId}/new")
    public String showAddMusicPartOrderForm(@PathVariable Long musicOrderId, Model model) {
        // Fetch the music order by ID and add it to the model
        MusicOrder musicOrder = musicOrderService.getMusicOrderById(musicOrderId)
                .orElseThrow(() -> new RuntimeException("Music Order not found with id: " + musicOrderId));
        model.addAttribute("musicOrder", musicOrder);
        return "/addMusicPartOrder"; // Return the view to add a new music part to the order
    }

    /**
     * Adds a music part to a music order.
     *
     * @param musicOrderId the ID of the music order.
     * @param musicSetTitle the title of the music set.
     * @param musicSetArranger the arranger of the music set.
     * @param musicPartName the name of the music part.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the music order page.
     */
    @PostMapping("music-order/{musicOrderId}")
    public String addMusicPartToMusicOrder(@PathVariable Long musicOrderId,
                                          @RequestParam("musicSetTitle") String musicSetTitle,
                                          @RequestParam("musicSetArranger") String musicSetArranger,
                                          @RequestParam("musicPartName") String musicPartName,
                                          RedirectAttributes redirectAttributes) {
        try {
            // Fetch the music part matching the given details
            Optional<MusicPart> musicPart = musicPartService.getMusicPartForOrder(musicPartName, musicSetTitle,
                    musicSetArranger);
            if (musicPart.isPresent()) {
                try{
                    // Add the music part to the specified music order
                    MusicOrder updatedMusicOrder = musicOrderService.addMusicPartToMusicOrder(musicOrderId,
                            musicPart.get());
                    if (updatedMusicOrder == null) {
                        redirectAttributes.addFlashAttribute("errorMessage",
                                "Error adding music part to order");
                    } else {
                        redirectAttributes.addFlashAttribute("successMessage",
                                "Music part added to order");
                    }
                } catch (Exception e){
                    redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                }

            }
            else{
                redirectAttributes.addFlashAttribute("errorMessage", "Music part not found");
            }

        } catch (Exception e) {
            // Handle any exceptions that occur during processing
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/music-order/{musicOrderId}"; // Redirect to the music order page
    }

    /**
     * Marks the music order as ready.
     *
     * @param musicOrderId the ID of the music order.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to a specific page.
     */
    @PostMapping("music-order/{musicOrderId}/ready")
    public String readyMusicOrder(@PathVariable Long musicOrderId,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Fetch the music order by its ID
            Optional<MusicOrder> musicOrderOpt = musicOrderService.getMusicOrderById(musicOrderId);
            if (musicOrderOpt.isPresent()) {
                // Mark the music order as ready
                MusicOrder musicOrder = musicOrderOpt.get();
                MusicOrder updatedMusicOrder = musicOrderService.readyMusicOrder(musicOrder);
                if (updatedMusicOrder == null) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Error readying music order");
                } else {
                    redirectAttributes.addFlashAttribute("successMessage",
                            "Music order ready");
                }
                if(musicOrder.getChild() != null){
                    return "redirect:/child/" + musicOrder.getChild().getId() + "/music";
                }

            } else {
                // Add a generic error message if the music order is not found
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Error");
            }
        } catch (Exception e) {
            // Handle exceptions and add error details to redirect attributes
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/music"; // Redirect to the main music page
    }

    /**
     * Deletes a music order.
     *
     * @param musicOrderId the ID of the music order.
     * @return a response entity indicating the result of the delete operation.
     */
    @DeleteMapping("music-order/{musicOrderId}")
    @ResponseBody
    public ResponseEntity<?> deleteMusicOrder(@PathVariable Long musicOrderId) {
        try {
            // Delete the instrument by its ID
            musicOrderService.deleteMusicOrder(musicOrderId);
            return ResponseEntity.ok("Music order deleted successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting music order: " + e.getMessage());
        }
    }

    /**
     * Marks the music order as fulfilled.
     *
     * @param musicOrderId the ID of the music order.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the committee members' music page.
     */
    @PostMapping("committee-member/music-order/fulfill")
    public String fulfillMusicOrder(@RequestParam Long musicOrderId,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Fetch the music order by its ID
            Optional<MusicOrder> musicOrderOpt = musicOrderService.getMusicOrderById(musicOrderId);
            if (musicOrderOpt.isPresent()) {
                // Mark the music order as fulfilled
                MusicOrder musicOrder = musicOrderOpt.get();
                MusicOrder updatedMusicOrder = musicOrderService.fulfillMusicOrder(musicOrder);
                if (updatedMusicOrder == null) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Error Fulfilling music order");
                } else {
                    redirectAttributes.addFlashAttribute("successMessage",
                            "Music order fulfilled");
                }

            } else {
                // Add a generic error message if the music order is not found
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Error");
            }
        } catch (Exception e) {
            // Handle exceptions and add error details to redirect attributes
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/committee-member/music"; // Redirect to the committee member's music page
    }

    /**
     * Displays details of a music set.
     *
     * @param id the ID of the music set.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("committee-member/music-set/{id}")
    public String getMusicSetById(@PathVariable Long id, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        // Fetch the music set by its ID
        Optional<MusicSet> musicSet = musicSetService.getMusicSetById(id);

        if (musicSet.isPresent()) {
            // Add the music set to the model if found
            model.addAttribute("musicSet", musicSet.get());
            model.addAttribute("bands", musicSet.get().getBands());
        } else {
            // Add an error message to the model if the music set is not found
            model.addAttribute("error", "Music Set not found");
        }
        // Fetch all parts associated with the music set
        List<MusicPart> musicParts = musicPartService.getPartsByMusicSetId(id);
        model.addAttribute("musicParts", musicParts);
        return "/committee-member/music-set"; // Return the view for the music set details
    }

    /**
     * Displays the form to add a new music set.
     *
     * @param model the model to add an empty music set object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("committee-member/music-set/new")
    public String showAddMusicSetForm(Model model) {
        // Add an empty music set object to the model for form binding
        model.addAttribute("musicSet", new MusicSet());
        return "committee-member/addMusicSet"; // Return the view for adding a new music set
    }

    /**
     * Adds a music set.
     *
     * @param musicSet the music set to save.
     * @param bindingResult the result of validation on the music set object.
     * @param redirectAttributes the attributes for rendering with success or error messages.
     * @return redirects to the specific page.
     */
    @PostMapping("/committee-member/music-set")
    public String addMusicSet(MusicSet musicSet, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors by adding them to redirect attributes
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating student: " + bindingResult.getAllErrors().get(0));
            redirectAttributes.addFlashAttribute("title", musicSet.getTitle());
            redirectAttributes.addFlashAttribute("composer", musicSet.getComposer());
            redirectAttributes.addFlashAttribute("arranger", musicSet.getArranger());
            redirectAttributes.addFlashAttribute("suitableForMusicSet", musicSet.getSuitableForTraining());
            return "redirect:music-set/new"; // Redirect back to the add form
        }

        try {
            // Save the new music set to the database
            MusicSet savedMusicSet = musicSetService.saveMusicSet(musicSet);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Music set created successfully - " + savedMusicSet.getTitle());
            return "redirect:/committee-member/music"; // Redirect to the committee member's music page
        } catch (Exception e) {
            // Handle exceptions and log the error
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating student " + e.getMessage());
            redirectAttributes.addFlashAttribute("title", musicSet.getTitle());
            redirectAttributes.addFlashAttribute("composer", musicSet.getComposer());
            redirectAttributes.addFlashAttribute("arranger", musicSet.getArranger());
            redirectAttributes.addFlashAttribute("suitableForMusicSet", musicSet.getSuitableForTraining());
            return "redirect:music-set/new"; // Redirect back to the add form
        }
    }

    /**
     * Updates an existing music set.
     *
     * @param id the ID of the music set.
     * @param musicSet the updated music set details.
     * @param bindingResult the result of validation on the music set object.
     * @return a response entity indicating the result of the update operation.
     */
    @PutMapping("/committee-member/music-set/{id}")
    @ResponseBody
    public ResponseEntity<?> updateMusicSet(@PathVariable Long id,
                                            @Valid @RequestBody MusicSet musicSet,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating music part: " +
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            // Update the existing music set with new details
            musicSet.setId(id);
            musicSetService.updateMusicSet(id, musicSet);
            return ResponseEntity.ok("Music set updated successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating music set " + e.getMessage());
        }
    }

    /**
     * Updates an existing music part.
     *
     * @param musicSetId the ID of the music set.
     * @param musicPartId the ID of the music part.
     * @param musicPart the updated music part details.
     * @param bindingResult the result of validation on the music part object.
     * @return a response entity indicating the result of the update operation.
     */
    @PutMapping("/committee-member/music-set/{musicSetId}/music-part/{musicPartId}")
    @ResponseBody
    public ResponseEntity<?> updateMusicPart(@PathVariable Long musicSetId,@PathVariable Long musicPartId,
                                            @Valid @RequestBody MusicPart musicPart,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating music part: " +
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            // Update the existing music part with new details
            musicPart.setId(musicPartId);
            musicPartService.updateMusicPart(musicPartId, musicPart);
            return ResponseEntity.ok("Music part updated successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating music part " + e.getMessage());
        }
    }


    /**
     * Displays details of a music part.
     *
     * @param musicSetId the ID of the music set.
     * @param musicPartId the ID of the music part.
     * @param model the model to add attributes for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("committee-member/music-set/{musicSetId}/music-part/{musicPartId}")
    public String getMusicPartById(@PathVariable Long musicSetId, @PathVariable Long musicPartId,
                                   Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        Optional<MusicPart> musicPart = musicPartService.getMusicPartById(musicPartId);
        Optional<MusicSet> musicSet = musicSetService.getMusicSetById(musicSetId);
        if(musicPart.isPresent() && musicSet.isPresent()) {
            model.addAttribute("musicPart", musicPart.get());
            model.addAttribute("musicSet", musicSet.get());
        }
        return "/committee-member/music-part";
    }

    /**
     * Displays the form to add music set.
     *
     * @param musicSetId the ID of the music set.
     * @param model the model to add an empty music part object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("committee-member/music-set/{musicSetId}/music-part/new")
    public String showAddMusicPartForm(@PathVariable Long musicSetId, Model model) {
        // Fetch the music set by its ID
        MusicSet musicSet = musicSetService.getMusicSetById(musicSetId)
                .orElseThrow(() -> new RuntimeException("MusicSet not found with id: " + musicSetId));
        // Add an empty music part object and the music set to the model
        model.addAttribute("musicPart", new MusicPart());
        model.addAttribute("musicSet", musicSet);
        return "committee-member/addMusicPart"; // Return the view for adding a new music part
    }

    /**
     * Adds a music part.
     * @param musicSetId the ID of the music set.
     * @param musicPart the music part to save.
     * @param bindingResult the result of validation on the music part object.
     * @param redirectAttributes the attributes for rendering with success or error messages.
     * @return redirects to a specific page.
     */
    @PostMapping("/committee-member/music-set/{musicSetId}/music-part")
    public String addMusicPart(@PathVariable Long musicSetId, @ModelAttribute MusicPart musicPart,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // Handle validation errors by adding them to redirect attributes
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating music part: " + bindingResult.getAllErrors().get(0));
            redirectAttributes.addFlashAttribute("partName", musicPart.getPartName());
            return "redirect:/committee-member/music-set/" + musicSetId + "/music-part/new"; // Redirect to the add form
        }

        try {
            // Save the new music part to the database
            MusicPart createdMusicPart = musicPartService.createMusicPart(musicSetId, musicPart);
            MusicPart savedMusicPart = musicPartService.saveMusicPart(createdMusicPart);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Music part created successfully - " + savedMusicPart.getPartName());
            return "redirect:/committee-member/music-set/" + musicSetId; // Redirect to the music set details page
        } catch (Exception e) {
            // Handle exceptions and log the error
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating music part: " + e.getMessage());
            redirectAttributes.addFlashAttribute("partName", musicPart.getPartName());
            return "redirect:/committee-member/music-set/" + musicSetId + "/music-part/new"; // Redirect to the add form
        }
    }

    /**
     * Displays the form to add practice.
     *
     * @param musicSetId the ID of the music set.
     * @param model the model to add a music set object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("committee-member/music-set/{musicSetId}/practice/new")
    public String showAddPracticeForm(@PathVariable Long musicSetId, Model model) {
        // Fetch the music set by its ID
        MusicSet musicSet = musicSetService.getMusicSetById(musicSetId)
                .orElseThrow(() -> new RuntimeException("MusicSet not found with id: " + musicSetId));
        model.addAttribute("musicSet", musicSet); // Add the music set to the model
        return "committee-member/practiceMusicSet"; // Return the view for adding a practice session
    }

    /**
     * Adds music set to practice.
     *
     * @param musicSetId the ID of the music set.
     * @param bandName the name of the band.
     * @param redirectAttributes the attributes for rendering with success or error messages.
     * @return redirects to the music set details page.
     */
    @PostMapping("committee-member/music-set/{musicSetId}/practice")
    public String putMusicSetIntoPractice(@PathVariable Long musicSetId, @RequestParam("bandName") String bandName,
                                          RedirectAttributes redirectAttributes) {
        try {
            // Fetch the band by its name
            Optional<Band> band = bandService.getBandByName(bandName);
            if (band.isPresent()) {
                // Add the band to the music set
                try{
                    MusicSet updatedMusicSet = musicSetService.addBandToMusicSet(musicSetId, band.get().getId());
                    if (updatedMusicSet == null) {
                        redirectAttributes.addFlashAttribute("errorMessage",
                                "Error putting music set into practice");
                    } else {
                        redirectAttributes.addFlashAttribute("successMessage",
                                "Music set put into practice");
                    }
                } catch (Exception e){
                    redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                }
            }
            else{
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Band not found");
            }

        } catch (Exception e) {
            // Handle exceptions and add error details to redirect attributes
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/committee-member/music-set/{musicSetId}"; // Redirect to the music set details page
    }

    /**
     * Removes music set from practice.
     *
     * @param musicSetId the ID of the music set.
     * @return a response entity indicating the result of the remove operation.
     */
    @DeleteMapping("committee-member/music-set/{musicSetId}/practice")
    @ResponseBody
    public ResponseEntity<?> putMusicSetIntoStorage(@PathVariable Long musicSetId) {
        try {
            // Delete the practice session for the music set
            musicSetService.deletePractice(musicSetId);
            return ResponseEntity.ok("Music set put into storage successfully"); // Return success response

        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error putting music set into storage: " + e.getMessage());
        }
    }
}
