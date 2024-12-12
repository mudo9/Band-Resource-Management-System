package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.MusicOrder;
import uk.ac.sheffield.bandproject.Model.MusicPart;

import java.util.List;
import java.util.Optional;

public interface MusicOrderService{
    List<MusicOrder> getAllMusicOrders();
    Optional<MusicOrder> getMusicOrderById(Long id);
    List<MusicOrder> getUserMusicOrderFulfilled(Long userId);
    List<MusicOrder> getUserMusicOrderReady(Long userId);
    List<MusicOrder> getUserMusicOrderNotReady(Long userId);
    List<MusicOrder> getChildMusicOrderFulfilled(Long childId);
    List<MusicOrder> getChildMusicOrderReady(Long childId);
    List<MusicOrder> getChildMusicOrderNotReady(Long childId);
    void deleteMusicOrder(Long id);
    List<MusicOrder> getOrdersByUserId(Long userId);
    MusicOrder createMusicOrder(Long userId);
    MusicOrder createChildMusicOrder(Long userId, Long childId);
    MusicOrder addMusicPartToMusicOrder(Long musicOrderId, MusicPart musicPart);
    List<MusicPart> getPartsByMusicOrderId(Long musicOrderId);
    List<MusicOrder> getAllMusicOrderReady();
    List<MusicOrder> getAllMusicOrderFulfilled();
    MusicOrder readyMusicOrder(MusicOrder musicOrder);
    MusicOrder fulfillMusicOrder(MusicOrder musicOrder);
}
