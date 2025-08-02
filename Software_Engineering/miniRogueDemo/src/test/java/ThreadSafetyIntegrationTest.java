import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import model.map.Map;
import model.characters.Player;
import model.characters.Enemy;
import model.items.Item;
import model.items.Consumable;
import model.equipment.Weapon;
import model.equipment.Armor;
import enums.CharacterClass;
import enums.Direction;
import utilities.Position;

/**
 * Comprehensive thread safety integration tests.
 * Consolidates functionality from all individual thread safety tests.
 * Tests concurrent access to all major system components.
 * Appropriate for a school project.
 */
@DisplayName("Thread Safety Integration Tests")
class ThreadSafetyIntegrationTest extends BaseThreadSafetyTest {

    private Map testMap;
    private Player testPlayer;
    private Enemy testEnemy;
    private List<Item> testItems;
    private List<Weapon> testWeapons;
    private List<Armor> testArmor;
    
    private static final int MAP_THREAD_COUNT = 6;
    private static final int ITEM_THREAD_COUNT = 4;
    private static final int EQUIPMENT_THREAD_COUNT = 4;
    private static final int CHARACTER_THREAD_COUNT = 4;
    private static final int INPUT_THREAD_COUNT = 4;

    @BeforeEach
    void setUp() {
        super.setUp();
        
        // Initialize test components
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        testEnemy = new Enemy("TestEnemy", CharacterClass.WARRIOR, new Position(1, 1), "AGGRESSIVE");
        testMap = new Map(1, Map.FloorType.REGULAR);
        
        // Initialize test collections
        testItems = new ArrayList<>();
        testWeapons = new ArrayList<>();
        testArmor = new ArrayList<>();
        
        // Add test items
        for (int i = 0; i < 10; i++) {
            testItems.add(new Consumable("TestPotion" + i, 10, "health"));
        }
        
        // Add test equipment
        for (int i = 0; i < 5; i++) {
            testWeapons.add(new Weapon("TestWeapon" + i, 10, 5, CharacterClass.WARRIOR, 1, Weapon.WeaponType.IMPACT, "test_weapon.png", "Impact"));
            testArmor.add(new Armor("TestArmor" + i, 5, 3, 2, CharacterClass.WARRIOR, 1, "test_armor.png", "Universal"));
        }
    }

    @Override
    protected void setUpComponent() {
        // Component setup is done in setUp()
    }

    @Override
    protected void tearDownComponent() {
        // Clean up resources
        testItems.clear();
        testWeapons.clear();
        testArmor.clear();
    }

    @Override
    protected boolean isStateConsistent() {
        // Check that all components maintain consistent state
        return testPlayer != null && 
               testEnemy != null && 
               testMap != null &&
               testItems.size() >= 0 &&
               testWeapons.size() >= 0 &&
               testArmor.size() >= 0;
    }

    // ==================== MAP SYSTEM THREAD SAFETY ====================

    /**
     * Tests map system thread safety with parameterized thread counts.
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6})
    @DisplayName("Map System Thread Safety")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testMapSystemThreadSafety(int threadCount) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(threadCount);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            // Submit map generation tasks
            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        
                        // Perform map operations
                        for (int j = 0; j < 20; j++) {
                            Map map = new Map(1, Map.FloorType.REGULAR);
                            Map bossMap = new Map(2, Map.FloorType.BOSS);
                            Map bonusMap = new Map(3, Map.FloorType.BONUS);
                            
                            // Access map properties
                            map.get_width();
                            map.get_height();
                            map.get_current_floor();
                            map.get_rooms();
                            
                            bossMap.get_width();
                            bossMap.get_height();
                            bossMap.get_current_floor();
                            
                            bonusMap.get_width();
                            bonusMap.get_height();
                            bonusMap.get_current_floor();
                            
                            Thread.sleep(2);
                        }
                        successCount.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        deadlockDetected.set(true);
                    } finally {
                        completionLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            boolean completed = completionLatch.await(15, TimeUnit.SECONDS);

            assertTrue(completed, "Map system thread safety test should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should occur in map system");
            assertEquals(threadCount, successCount.get(), "All map system threads should complete successfully");
            
        } finally {
            executor.shutdown();
        }
    }

    // ==================== ITEM SYSTEM THREAD SAFETY ====================

    /**
     * Tests item system thread safety with parameterized thread counts.
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6})
    @DisplayName("Item System Thread Safety")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testItemSystemThreadSafety(int threadCount) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(threadCount);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            // Submit item operation tasks
            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        
                        // Perform item operations
                        for (int j = 0; j < 30; j++) {
                            Item item = new Consumable("ThreadItem" + threadId + "_" + j, 10, "health");
                            
                            // Test item properties
                            item.get_name();
                            item.get_potency();
                            item.get_class_type();
                            
                            // Test item usage
                            item.use(testPlayer);
                            
                            Thread.sleep(1);
                        }
                        successCount.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        deadlockDetected.set(true);
                    } finally {
                        completionLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            boolean completed = completionLatch.await(15, TimeUnit.SECONDS);

            assertTrue(completed, "Item system thread safety test should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should occur in item system");
            assertEquals(threadCount, successCount.get(), "All item system threads should complete successfully");
            
        } finally {
            executor.shutdown();
        }
    }

    // ==================== EQUIPMENT SYSTEM THREAD SAFETY ====================

    /**
     * Tests equipment system thread safety with parameterized thread counts.
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6})
    @DisplayName("Equipment System Thread Safety")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testEquipmentSystemThreadSafety(int threadCount) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(threadCount);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            // Submit equipment operation tasks
            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        
                        // Perform equipment operations
                        for (int j = 0; j < 25; j++) {
                            Weapon weapon = new Weapon("ThreadWeapon" + threadId + "_" + j, 10, 5, CharacterClass.WARRIOR, 1, Weapon.WeaponType.IMPACT, "test_weapon.png", "Impact");
                            Armor armor = new Armor("ThreadArmor" + threadId + "_" + j, 5, 3, 2, CharacterClass.WARRIOR, 1, "test_armor.png", "Universal");
                            
                            // Test equipment properties
                            weapon.get_name();
                            weapon.get_attack_power();
                            weapon.get_mp_power();
                            
                            armor.get_name();
                            armor.get_defense_value();
                            armor.get_atk_defense();
                            
                            // Test equipment equipping
                            testPlayer.equip_weapon(weapon);
                            testPlayer.equip_armor(armor);
                            
                            Thread.sleep(1);
                        }
                        successCount.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        deadlockDetected.set(true);
                    } finally {
                        completionLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            boolean completed = completionLatch.await(15, TimeUnit.SECONDS);

            assertTrue(completed, "Equipment system thread safety test should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should occur in equipment system");
            assertEquals(threadCount, successCount.get(), "All equipment system threads should complete successfully");
            
        } finally {
            executor.shutdown();
        }
    }

    // ==================== CHARACTER SYSTEM THREAD SAFETY ====================

    /**
     * Tests character system thread safety with parameterized thread counts.
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6})
    @DisplayName("Character System Thread Safety")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testCharacterSystemThreadSafety(int threadCount) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(threadCount);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            // Submit character operation tasks
            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        
                        // Perform character operations
                        for (int j = 0; j < 30; j++) {
                            Player player = new Player("ThreadPlayer" + threadId + "_" + j, CharacterClass.WARRIOR, new Position(0, 0));
                            Enemy enemy = new Enemy("ThreadEnemy" + threadId + "_" + j, CharacterClass.WARRIOR, new Position(1, 1), "AGGRESSIVE");
                            
                            // Test character properties
                            player.get_name();
                            player.get_current_hp();
                            player.get_max_hp();
                            player.get_position();
                            
                            enemy.get_name();
                            enemy.get_current_hp();
                            enemy.get_max_hp();
                            enemy.get_position();
                            
                            // Test character interactions
                            player.take_damage(5);
                            enemy.take_damage(3);
                            
                            Thread.sleep(1);
                        }
                        successCount.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        deadlockDetected.set(true);
                    } finally {
                        completionLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            boolean completed = completionLatch.await(15, TimeUnit.SECONDS);

            assertTrue(completed, "Character system thread safety test should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should occur in character system");
            assertEquals(threadCount, successCount.get(), "All character system threads should complete successfully");
            
        } finally {
            executor.shutdown();
        }
    }

    // ==================== INTEGRATED THREAD SAFETY TESTS ====================

    /**
     * Tests integrated thread safety across all systems.
     */
    @Test
    @DisplayName("Integrated System Thread Safety")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testIntegratedSystemThreadSafety() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(4); // 4 different system types
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            // Map system thread
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < 10; i++) {
                        Map map = new Map(1, Map.FloorType.REGULAR);
                        map.get_width();
                        map.get_height();
                        Thread.sleep(5);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });

            // Item system thread
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < 15; i++) {
                        Item item = new Consumable("IntegratedItem" + i, 10, "health");
                        item.get_name();
                        item.use(testPlayer);
                        Thread.sleep(3);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });

            // Equipment system thread
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < 12; i++) {
                        Weapon weapon = new Weapon("IntegratedWeapon" + i, 10, 5, CharacterClass.WARRIOR, 1, Weapon.WeaponType.IMPACT, "test_weapon.png", "Impact");
                        testPlayer.equip_weapon(weapon);
                        Thread.sleep(4);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });

            // Character system thread
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < 20; i++) {
                        testPlayer.take_damage(1);
                        testPlayer.get_current_hp();
                        Thread.sleep(2);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });

            startLatch.countDown();
            boolean completed = completionLatch.await(25, TimeUnit.SECONDS);

            assertTrue(completed, "Integrated thread safety test should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should occur in integrated system");
            assertEquals(4, successCount.get(), "All integrated system threads should complete successfully");
            
        } finally {
            executor.shutdown();
        }
    }

    @Override
    protected void performConcurrentOperation(int threadId) throws InterruptedException {
        // Default implementation for the base class
        // This is overridden by specific test methods above
        Thread.sleep(10);
    }
} 