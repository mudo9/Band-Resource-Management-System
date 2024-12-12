package uk.ac.sheffield.bandproject.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.bandproject.Model.Instrument;
import uk.ac.sheffield.bandproject.Model.Miscellaneous;
import uk.ac.sheffield.bandproject.Model.MusicPart;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Service.InstrumentService;
import uk.ac.sheffield.bandproject.Service.MiscellaneousService;

import java.util.*;

/**
 * Controller for handling item management operations.
 */
@Controller
@RequestMapping("/committee-member")
public class ItemController {
    private final InstrumentService instrumentService;
    private final MiscellaneousService miscellaneousService;

    /**
     * Constructor to inject dependencies.
     *
     * @param instrumentService the instrument service for instrument-related operations.
     * @param miscellaneousService the miscellaneous service for miscellaneous-related operations.
     */
    public ItemController(InstrumentService instrumentService, MiscellaneousService miscellaneousService){
        this.instrumentService = instrumentService;
        this.miscellaneousService = miscellaneousService;
    }

    /**
     * Displays all items.
     *
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/items")
    public String items(Model model){
        // Get the current authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // Redirect to login page if not authenticated
            return "login";
        }
        List<Instrument> instruments = instrumentService.getAllInstruments();
        List<Miscellaneous> miscellaneous = miscellaneousService.getAllMiscellaneous();
        // Add data to the model for committee member's items view
        model.addAttribute("instruments", instruments);
        model.addAttribute("miscellaneousItems", miscellaneous);
        return "committee-member/items"; // Return the view for committee members
    }


    // Instrument Mappings

    /**
     * Displays the form to add a new instrument.
     *
     * @param model the model to add an empty instrument object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("/instrument/new")
    public String showAddInstrumentForm(Model model) {
        // Add an empty instrument object to the model for form binding
        model.addAttribute("instrument", new Instrument());
        return "committee-member/addInstrument"; // Return the view for adding a new instrument
    }

    /**
     * Adds a new instrument.
     *
     * @param instrument the instrument object.
     * @param bindingResult the result of validation on the instrument object.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the specific page.
     */
    @PostMapping("/instrument")
    public String addInstrument(Instrument instrument, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors by adding them to redirect attributes
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating instrument: " + bindingResult.getAllErrors().get(0));
            redirectAttributes.addFlashAttribute("name", instrument.getName());
            redirectAttributes.addFlashAttribute("serialNumber", instrument.getSerialNumber());
            redirectAttributes.addFlashAttribute("make", instrument.getMake());
            return "redirect:instrument/new"; // Redirect back to the add form
        }

        try {
            // Save the new instrument to the database
            Instrument savedInstrument = instrumentService.saveInstrument(instrument);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Instrument created successfully - " + savedInstrument.getName());
            return "redirect:/committee-member/items"; // Redirect to the committee member's items page
        } catch (Exception e) {
            // Handle exceptions and log the error
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating instrument " + e.getMessage());
            redirectAttributes.addFlashAttribute("name", instrument.getName());
            redirectAttributes.addFlashAttribute("serialNumber", instrument.getSerialNumber());
            redirectAttributes.addFlashAttribute("make", instrument.getMake());
            return "redirect:instrument/new"; // Redirect back to the add form
        }
    }

    /**
     * Displays a specific instrument.
     *
     * @param id the ID of the instrument.
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/instrument/{id}")
    public String getInstrumentById(@PathVariable Long id, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        // Fetch the instrument by its ID
        Optional<Instrument> instrument = instrumentService.getInstrumentById(id);

        if (instrument.isPresent()) {
            // Add the instrument to the model if found
            model.addAttribute("instrument", instrument.get());
        } else {
            // Add an error message to the model if the instrument is not found
            model.addAttribute("error", "Instrument not found");
        }

        return "/committee-member/instrument"; // Return the view for the instrument details
    }

    /**
     * Updates an instrument.
     *
     * @param id the ID of the instrument.
     * @param instrument the instrument object.
     * @param bindingResult the result of validation on the instrument object.
     * @return a response entity indicating the result of the update operation.
     */
    @PutMapping("/instrument/{id}")
    @ResponseBody
    public ResponseEntity<?> updateInstrument(@PathVariable Long id,
                                            @Valid @RequestBody Instrument instrument,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating instrument: " +
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            // Update the existing instrument with new details
            instrument.setId(id);
            instrumentService.updateInstrument(id, instrument);
            return ResponseEntity.ok("Instrument updated successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating instrument " + e.getMessage());
        }
    }

    /**
     * Deletes an instrument.
     *
     * @param id the ID of the instrument.
     * @return a response entity indicating the result of the delete operation.
     */
    @DeleteMapping("instrument/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteInstrument(@PathVariable Long id) {
        try {
            // Delete the instrument by its ID
            instrumentService.deleteInstrument(id);
            return ResponseEntity.ok("Instrument deleted successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting instrument: " + e.getMessage());
        }
    }


    // Miscellaneous Mappings
    /**
     * Displays a miscellaneous.
     *
     * @param id the ID of the miscellaneous.
     * @param model the model to add attribute for rendering.
     * @return the name of the HTML template or login HTML template if unauthenticated.
     */
    @GetMapping("/miscellaneous/{id}")
    public String getMiscellaneousById(@PathVariable Long id, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            return "login"; // Redirect to login page if not authenticated
        }
        // Fetch the miscellaneous item by its ID
        Optional<Miscellaneous> miscellaneous = miscellaneousService.getMiscellaneousById(id);

        if (miscellaneous.isPresent()) {
            // Add the miscellaneous to the model if found
            model.addAttribute("miscellaneous", miscellaneous.get());
        } else {
            // Add an error message to the model if the miscellaneous is not found
            model.addAttribute("error", "Miscellaneous not found");
        }

        return "/committee-member/miscellaneous"; // Return the view for the miscellaneous details
    }


    /**
     * Displays the form to add a new miscellaneous.
     *
     * @param model the model to add an empty miscellaneous object for the form.
     * @return the name of the HTML template.
     */
    @GetMapping("/miscellaneous/new")
    public String showAddMiscellaneousForm(Model model) {
        // Add an empty miscellaneous object to the model for form binding
        model.addAttribute("miscellaneous", new Miscellaneous());
        return "committee-member/addMiscellaneous"; // Return the view for adding a new miscellaneous
    }

    /**
     * Adds a new miscellaneous.
     *
     * @param miscellaneous the miscellaneous object.
     * @param bindingResult the result of validation on the miscellaneous object.
     * @param redirectAttributes the attributes for redirecting with success or error messages.
     * @return redirects to the specific page.
     */
    @PostMapping("/miscellaneous")
    public String addMiscellaneous(Miscellaneous miscellaneous, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors by adding them to redirect attributes
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating miscellaneous: " + bindingResult.getAllErrors().get(0));
            redirectAttributes.addFlashAttribute("name", miscellaneous.getName());
            redirectAttributes.addFlashAttribute("quantity", miscellaneous.getQuantity());
            redirectAttributes.addFlashAttribute("make", miscellaneous.getMake());
            redirectAttributes.addFlashAttribute("specificForInstrument",
                    miscellaneous.getSpecificForInstrument());
            return "redirect:miscellaneous/new"; // Redirect back to the add form
        }

        try {
            // Save the new miscellaneous to the database
            Miscellaneous savedMiscellaneous = miscellaneousService.saveMiscellaneous(miscellaneous);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Miscellaneous created successfully - " + savedMiscellaneous.getName());
            return "redirect:/committee-member/items"; // Redirect to the committee member's items
        } catch (Exception e) {
            // Handle exceptions and log the error
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating instrument " + e.getMessage());
            redirectAttributes.addFlashAttribute("name", miscellaneous.getName());
            redirectAttributes.addFlashAttribute("quantity", miscellaneous.getQuantity());
            redirectAttributes.addFlashAttribute("make", miscellaneous.getMake());
            redirectAttributes.addFlashAttribute("specificForInstrument",
                    miscellaneous.getSpecificForInstrument());
            return "redirect:miscellaneous/new"; // Redirect back to the add form
        }
    }

    /**
     * Updates the miscellaneous.
     *
     * @param id the ID of the miscellaneous.
     * @param miscellaneous the miscellaneous object.
     * @param bindingResult the result of validation on the miscellaneous object.
     * @return a response entity indicating the result of the update operation.
     */
    @PutMapping("/miscellaneous/{id}")
    @ResponseBody
    public ResponseEntity<?> updateMiscellaneous(@PathVariable Long id,
                                              @Valid @RequestBody Miscellaneous miscellaneous,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating miscellaneous: " +
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            // Update the existing miscellaneous with new details
            miscellaneous.setId(id);
            miscellaneousService.updateMiscellaneous(id, miscellaneous);
            return ResponseEntity.ok("Miscellaneous updated successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating miscellaneous " + e.getMessage());
        }
    }

    /**
     * Deletes a miscellaneous.
     *
     * @param id the ID of the miscellaneous.
     * @return a response entity indicating the result of the delete operation.
     */
    @DeleteMapping("/miscellaneous/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMiscellaneous(@PathVariable Long id) {
        try {
            // Delete the miscellaneous by its ID
            miscellaneousService.deleteMiscellaneous(id);
            return ResponseEntity.ok("miscellaneous deleted successfully"); // Return success response
        } catch (Exception e) {
            // Handle exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting miscellaneous: " + e.getMessage());
        }
    }
}
