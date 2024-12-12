package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MusicOrderServiceImpl implements MusicOrderService{
    LocalDate localDate = LocalDate.now();
    private final MusicOrderRepository musicOrderRepository;
    private final MusicPartRepository musicPartRepository;
    private final UserRepository userRepository;

    //Constructor
    public MusicOrderServiceImpl(MusicOrderRepository musicOrderRepository,
                                 MusicPartRepository musicPartRepository, UserRepository userRepository) {
        this.musicOrderRepository = musicOrderRepository;
        this.musicPartRepository = musicPartRepository;
        this.userRepository = userRepository;
    }

    //Retrieves all MusicOrder records from the database
    public List<MusicOrder> getAllMusicOrders() {
        return musicOrderRepository.findAll();
    }

    //Retrieves a specific MusicOrder by its ID
    public Optional<MusicOrder> getMusicOrderById(Long id) {
        return musicOrderRepository.findById(id);
    }

    //Creates a new MusicOrder for a specific user by their ID
    public MusicOrder createMusicOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        MusicOrder musicOrder = new MusicOrder();
        musicOrder.setOwner(user);
        musicOrder.setStatus("NOT_READY");
        musicOrder.setDate(localDate);
        return musicOrderRepository.save(musicOrder);
    }

    //Creates a new MusicOrder for a child associated with a specific user
    public MusicOrder createChildMusicOrder(Long userId, Long childId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + childId));
        MusicOrder musicOrder = new MusicOrder();
        musicOrder.setOwner(user);
        musicOrder.setChild(child);
        musicOrder.setStatus("NOT_READY");
        musicOrder.setDate(localDate);
        return musicOrderRepository.save(musicOrder);
    }

    //Deletes a MusicOrder
    public void deleteMusicOrder(Long id) {
        Optional<MusicOrder> musicOrderOptional = musicOrderRepository.findById(id);
        if (musicOrderOptional.isPresent()) {
            musicOrderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Music Order not found");
        }
    }

    //Retrieves all MusicOrders for a specific user
    public List<MusicOrder> getOrdersByUserId(Long userId) {
        return musicOrderRepository.findByOwnerId(userId);
    }

    //Retrieves MusicOrders for a user by their ID, and with the status not ready
    public List<MusicOrder> getUserMusicOrderNotReady(Long userId) {
        return musicOrderRepository.findByStatusAndOwnerId("NOT_READY", userId);
    }

    //Retrieves MusicOrders for a user by their ID, and with the status ready
    public List<MusicOrder> getUserMusicOrderReady(Long userId) {
        return musicOrderRepository.findByStatusAndOwnerId("READY", userId);
    }

    //Retrieves MusicOrders for a user by their ID, and with the status fulfilled
    public List<MusicOrder> getUserMusicOrderFulfilled(Long userId) {
        return musicOrderRepository.findByStatusAndOwnerId("FULFILLED", userId);
    }

    //Retrieves MusicOrders for a child by their ID, and with the status not ready
    public List<MusicOrder> getChildMusicOrderNotReady(Long childId) {
        return musicOrderRepository.findByStatusAndChildId("NOT_READY", childId);
    }

    //Retrieves MusicOrders for a child by their ID, and with the status ready
    public List<MusicOrder> getChildMusicOrderReady(Long childId) {
        return musicOrderRepository.findByStatusAndChildId("READY", childId);
    }

    //Retrieves MusicOrders for a child by their ID, and with the status fulfilled
    public List<MusicOrder> getChildMusicOrderFulfilled(Long childId) {
        return musicOrderRepository.findByStatusAndChildId("FULFILLED", childId);
    }

    //Adds a MusicPart to a specific MusicOrder by its ID and saves the new order
    public MusicOrder addMusicPartToMusicOrder(Long MusicOrderId, MusicPart musicPart) {
        Optional<MusicOrder> musicOrderOpt = musicOrderRepository.findById(MusicOrderId);

        if (musicOrderOpt.isPresent()) {
            MusicOrder musicOrder = musicOrderOpt.get();
            musicOrder.getMusicParts().add(musicPart);
            return musicOrderRepository.save(musicOrder);
        }
        else{
            throw new RuntimeException("Music part not found");
        }
    }

    //Retrieves all MusicParts associated with a specific MusicOrder
    public List<MusicPart> getPartsByMusicOrderId(Long musicOrderId) {
        return musicOrderRepository.findById(musicOrderId)
                .map(musicOrder -> new ArrayList<>(musicOrder.getMusicParts())) // Convert Set to List
                .orElseThrow(() -> new IllegalArgumentException("MusicOrder not found for ID: " + musicOrderId));
    }

    //Retrieves all MusicOrders with the status ready
    public List<MusicOrder> getAllMusicOrderReady() {
        return musicOrderRepository.findByStatus("READY");
    }

    //Retrieves all MusicOrders with the status fulfilled
    public List<MusicOrder> getAllMusicOrderFulfilled() {
        return musicOrderRepository.findByStatus("FULFILLED");
    }

    //Updates the status of a MusicOrder to ready
    public MusicOrder readyMusicOrder(MusicOrder musicOrder) {
        musicOrder.setStatus("READY");
        return musicOrderRepository.save(musicOrder);
    }

    //Updates the status of a MusicOrder to fulfilled
    public MusicOrder fulfillMusicOrder(MusicOrder musicOrder) {
        musicOrder.setStatus("FULFILLED");
        return musicOrderRepository.save(musicOrder);
    }


}
