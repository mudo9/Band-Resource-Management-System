package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Repository.MusicOrderRepository;
import uk.ac.sheffield.bandproject.Service.MusicOrderServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class MusicOrderServiceImplTest {

    @Mock
    private MusicOrderRepository musicOrderRepository;

    @InjectMocks
    private MusicOrderServiceImpl musicOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllMusicOrders_whenGetAllMusicOrdersIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder1 = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, owner, null, LocalDate.of(2024,2,2),
                "TEST STATUS", new HashSet<>());
        when(musicOrderRepository.findAll()).thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getAllMusicOrders();
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnMusicOrder_whenValidIdIsGiven() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
        when(musicOrderRepository.findById(1L)).thenReturn(Optional.of(musicOrder));

        Optional<MusicOrder> foundMusicOrder = musicOrderService.getMusicOrderById(1L);
        assertTrue(foundMusicOrder.isPresent());
        assertEquals(LocalDate.of(2024,1,1), foundMusicOrder.get().getDate());
    }

    @Test
    public void shouldDeleteMusicOrder() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
        when(musicOrderRepository.findById(1L)).thenReturn(Optional.of(musicOrder));
        doNothing().when(musicOrderRepository).deleteById(1L);

        musicOrderService.deleteMusicOrder(1L);
        verify(musicOrderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldReturnMusicOrdersOfSpecificUser_whenGetOrdersByUserIdIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder1 = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, owner, null, LocalDate.of(2024,2,2),
                "TEST STATUS", new HashSet<>());
        when(musicOrderRepository.findByOwnerId(1L)).thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getOrdersByUserId(1L);
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnNotReadyMusicOrdersOfSpecificUser_whenGetUserMusicOrderNotReadyIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder1 = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "NOT_READY", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, owner, null, LocalDate.of(2024,2,2),
                "NOT_READY", new HashSet<>());
        when(musicOrderRepository.findByStatusAndOwnerId("NOT_READY", 1L))
                .thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getUserMusicOrderNotReady(1L);
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnReadyMusicOrdersOfSpecificUser_whenGetUserMusicOrderReadyIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder1 = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "READY", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, owner, null, LocalDate.of(2024,2,2),
                "READY", new HashSet<>());
        when(musicOrderRepository.findByStatusAndOwnerId("READY", 1L))
                .thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getUserMusicOrderReady(1L);
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnFulfilledMusicOrdersOfSpecificUser_whenGetUserMusicOrderFulfilledIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder1 = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "FULFILLED", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, owner, null, LocalDate.of(2024,2,2),
                "FULFILLED", new HashSet<>());
        when(musicOrderRepository.findByStatusAndOwnerId("FULFILLED", 1L))
                .thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getUserMusicOrderFulfilled(1L);
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnNotReadyMusicOrdersOfChild_whenGetChildMusicOrderNotReadyIsCalled() {
        User parent = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child = new User(2L, "test@test.com", "password", "1234567",
                "TestChild", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        MusicOrder musicOrder1 = new MusicOrder(1L, parent, child, LocalDate.of(2024,1,1),
                "NOT_READY", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, parent, child, LocalDate.of(2024,2,2),
                "NOT_READY", new HashSet<>());
        when(musicOrderRepository.findByStatusAndChildId("NOT_READY", 2L))
                .thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getChildMusicOrderNotReady(2L);
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnReadyMusicOrdersOfChild_whenGetChildMusicOrderReadyIsCalled() {
        User parent = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child = new User(2L, "test@test.com", "password", "1234567",
                "TestChild", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        MusicOrder musicOrder1 = new MusicOrder(1L, parent, child, LocalDate.of(2024,1,1),
                "READY", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, parent, child, LocalDate.of(2024,2,2),
                "READY", new HashSet<>());
        when(musicOrderRepository.findByStatusAndChildId("READY", 2L))
                .thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getChildMusicOrderReady(2L);
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnFulfilledMusicOrdersOfChild_whenGetChildMusicOrderFulfilledIsCalled() {
        User parent = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child = new User(2L, "test@test.com", "password", "1234567",
                "TestChild", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        MusicOrder musicOrder1 = new MusicOrder(1L, parent, child, LocalDate.of(2024,1,1),
                "FULFILLED", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, parent, child, LocalDate.of(2024,2,2),
                "FULFILLED", new HashSet<>());
        when(musicOrderRepository.findByStatusAndChildId("FULFILLED", 2L))
                .thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getChildMusicOrderFulfilled(2L);
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReturnAllMusicPartsOfMusicOrder_whenGetPartsByMusicOrderIdIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
        MusicPart musicPart1 = new MusicPart(1L, "TestPart1", new MusicSet(), new HashSet<>());
        MusicPart musicPart2 = new MusicPart(2L, "TestPart2", new MusicSet(), new HashSet<>());

        musicOrder.getMusicParts().add(musicPart1);
        musicOrder.getMusicParts().add(musicPart2);

        when(musicOrderRepository.findById(1L)).thenReturn(Optional.of(musicOrder));

        List<MusicPart> musicParts = musicOrderService.getPartsByMusicOrderId(1L);
        assertEquals(2, musicParts.size());
        assertTrue(musicParts.contains(musicPart1));
        assertTrue(musicParts.contains(musicPart2));
    }

    @Test
    public void shouldReturnAllReadyMusicOrders_whenGetAllMusicOrderReadyIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder musicOrder1 = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "READY", new HashSet<>());
        MusicOrder musicOrder2 = new MusicOrder(2L, owner, null, LocalDate.of(2024,2,2),
                "READY", new HashSet<>());
        when(musicOrderRepository.findByStatus("READY")).thenReturn(Arrays.asList(musicOrder1, musicOrder2));

        List<MusicOrder> musicOrders = musicOrderService.getAllMusicOrderReady();
        assertEquals(2, musicOrders.size());
        assertEquals(LocalDate.of(2024,1,1), musicOrders.get(0).getDate());
        assertEquals(LocalDate.of(2024,2,2), musicOrders.get(1).getDate());
    }

    @Test
    public void shouldReadyMusicOrderStatus_whenReadyMusicOrderIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder existingMusicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
        MusicOrder updatedMusicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "READY", new HashSet<>());
        when(musicOrderRepository.save(existingMusicOrder)).thenReturn(updatedMusicOrder);

        MusicOrder result = musicOrderService.readyMusicOrder(existingMusicOrder);
        assertNotNull(result);
        assertEquals("READY", result.getStatus());
    }

    @Test
    public void shouldFulfillMusicOrderStatus_whenFulfillMusicOrderIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicOrder existingMusicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "TEST STATUS", new HashSet<>());
        MusicOrder updatedMusicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "FULFILLED", new HashSet<>());
        when(musicOrderRepository.save(existingMusicOrder)).thenReturn(updatedMusicOrder);

        MusicOrder result = musicOrderService.readyMusicOrder(existingMusicOrder);
        assertNotNull(result);
        assertEquals("FULFILLED", result.getStatus());
    }
}
