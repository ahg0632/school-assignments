package utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ConfigurationManager class.
 * Tests singleton pattern, configuration loading, caching, and error handling.
 * Appropriate for a school project.
 */
@DisplayName("ConfigurationManager Tests")
class ConfigurationManagerTest {

    private ConfigurationManager configManager;

    @BeforeEach
    void setUp() {
        // Get singleton instance
        configManager = ConfigurationManager.getInstance();
        // Clear cache to ensure clean state
        configManager.clearCache();
    }

    /**
     * Tests singleton pattern implementation.
     */
    @Test
    @DisplayName("Singleton Pattern")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSingletonPattern() {
        // Test that getInstance returns the same instance
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();
        
        assertSame(instance1, instance2, "Singleton instances should be the same");
        assertNotNull(instance1, "Singleton instance should not be null");
    }

    /**
     * Tests game constants configuration.
     */
    @Test
    @DisplayName("Game Constants Configuration")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameConstants() {
        // Test that game constants are accessible
        assertDoesNotThrow(() -> {
            // Test getting game constants with default values
            Integer tileSize = configManager.getGameConstant("TILE_SIZE", 32);
            assertNotNull(tileSize, "Tile size should not be null");
            assertTrue(tileSize > 0, "Tile size should be positive");
            
            // Test getting non-existent constant with default
            String nonExistent = configManager.getGameConstant("NON_EXISTENT", "default");
            assertEquals("default", nonExistent, "Should return default value for non-existent key");
        }, "Game constants should be accessible without exceptions");
    }

    /**
     * Tests rendering configuration.
     */
    @Test
    @DisplayName("Rendering Configuration")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderingConfig() {
        // Test that rendering config is accessible
        assertDoesNotThrow(() -> {
            // Test getting rendering config with default values
            Integer windowWidth = configManager.getRenderingConfig("WINDOW_WIDTH", 1024);
            assertNotNull(windowWidth, "Window width should not be null");
            assertTrue(windowWidth > 0, "Window width should be positive");
            
            // Test getting non-existent config with default
            Boolean nonExistent = configManager.getRenderingConfig("NON_EXISTENT", false);
            assertEquals(false, nonExistent, "Should return default value for non-existent key");
        }, "Rendering config should be accessible without exceptions");
    }

    /**
     * Tests weapon mappings configuration.
     */
    @Test
    @DisplayName("Weapon Mappings Configuration")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testWeaponMappings() {
        // Test that weapon mappings are accessible
        assertDoesNotThrow(() -> {
            // Test getting weapon mapping with default values
            String weaponType = configManager.getWeaponMapping("SWORD", "Blade");
            assertNotNull(weaponType, "Weapon type should not be null");
            
            // Test getting non-existent mapping with default
            Integer nonExistent = configManager.getWeaponMapping("NON_EXISTENT", 0);
            assertEquals(0, nonExistent, "Should return default value for non-existent key");
        }, "Weapon mappings should be accessible without exceptions");
    }

    /**
     * Tests caching functionality.
     */
    @Test
    @DisplayName("Caching Functionality")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCaching() {
        // Test setting and getting cached values
        String testKey = "test_cache_key";
        String testValue = "test_cache_value";
        
        // Set cached value
        configManager.setCachedValue(testKey, testValue);
        
        // Get cached value
        String retrievedValue = configManager.getCachedValue(testKey, "default");
        assertEquals(testValue, retrievedValue, "Should retrieve cached value");
        
        // Test getting non-existent cached value
        String nonExistent = configManager.getCachedValue("non_existent_key", "default");
        assertEquals("default", nonExistent, "Should return default for non-existent cached key");
    }

    /**
     * Tests cache clearing functionality.
     */
    @Test
    @DisplayName("Cache Clearing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCacheClearing() {
        // Set some cached values
        configManager.setCachedValue("key1", "value1");
        configManager.setCachedValue("key2", "value2");
        
        // Clear cache
        configManager.clearCache();
        
        // Verify cache is cleared
        String value1 = configManager.getCachedValue("key1", "default1");
        String value2 = configManager.getCachedValue("key2", "default2");
        
        assertEquals("default1", value1, "Should return default after cache clear");
        assertEquals("default2", value2, "Should return default after cache clear");
    }

    /**
     * Tests configuration reloading.
     */
    @Test
    @DisplayName("Configuration Reloading")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testConfigurationReloading() {
        // Test that reloading configurations doesn't throw exceptions
        assertDoesNotThrow(() -> {
            configManager.reloadConfigurations();
        }, "Reloading configurations should not throw exceptions");
        
        // Verify that configurations are still accessible after reload
        assertDoesNotThrow(() -> {
            configManager.getGameConstant("TILE_SIZE", 32);
            configManager.getRenderingConfig("WINDOW_WIDTH", 1024);
            configManager.getWeaponMapping("SWORD", "Blade");
        }, "Configurations should be accessible after reload");
    }

    /**
     * Tests getting all configuration maps.
     */
    @Test
    @DisplayName("All Configuration Maps")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAllConfigurationMaps() {
        // Test getting all game constants
        Map<String, Object> gameConstants = configManager.getAllGameConstants();
        assertNotNull(gameConstants, "Game constants map should not be null");
        
        // Test getting all rendering configs
        Map<String, Object> renderingConfigs = configManager.getAllRenderingConfigs();
        assertNotNull(renderingConfigs, "Rendering configs map should not be null");
        
        // Test getting all weapon mappings
        Map<String, Object> weaponMappings = configManager.getAllWeaponMappings();
        assertNotNull(weaponMappings, "Weapon mappings map should not be null");
    }

    /**
     * Tests configuration existence checks.
     */
    @Test
    @DisplayName("Configuration Existence Checks")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testConfigurationExistenceChecks() {
        // Test checking for existing game constants
        // Note: The actual constants depend on the loaded configuration files
        assertFalse(configManager.hasGameConstant("NON_EXISTENT"), "Should not have non-existent constant");
        
        // Test checking for existing rendering configs
        // Note: The actual configs depend on the loaded configuration files
        assertFalse(configManager.hasRenderingConfig("NON_EXISTENT"), "Should not have non-existent config");
        
        // Test checking for existing weapon mappings
        // Note: The actual mappings depend on the loaded configuration files
        assertFalse(configManager.hasWeaponMapping("NON_EXISTENT"), "Should not have non-existent mapping");
    }

    /**
     * Tests error handling and robustness.
     */
    @Test
    @DisplayName("Error Handling and Robustness")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandling() {
        // Test that null keys are handled gracefully
        assertDoesNotThrow(() -> {
            configManager.getGameConstant(null, "default");
            configManager.getRenderingConfig(null, "default");
            configManager.getWeaponMapping(null, "default");
            configManager.getCachedValue(null, "default");
        }, "Null keys should be handled gracefully");
        
        // Test that null values can be cached
        assertDoesNotThrow(() -> {
            configManager.setCachedValue("null_key", null);
            Object retrieved = configManager.getCachedValue("null_key", "default");
            // The implementation returns the default value when null is cached
            assertEquals("default", retrieved, "Should return default value when null is cached");
        }, "Null values should be handled gracefully");
    }

    /**
     * Tests type safety and generic methods.
     */
    @Test
    @DisplayName("Type Safety and Generic Methods")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTypeSafety() {
        // Test different types with generic methods
        assertDoesNotThrow(() -> {
            // Test Integer type
            Integer intValue = configManager.getGameConstant("TILE_SIZE", 32);
            assertTrue(intValue instanceof Integer, "Should return Integer type");
            
            // Test String type
            String stringValue = configManager.getRenderingConfig("WINDOW_WIDTH", "1024");
            assertTrue(stringValue instanceof String, "Should return String type");
            
            // Test Boolean type
            Boolean boolValue = configManager.getWeaponMapping("SWORD", true);
            assertTrue(boolValue instanceof Boolean, "Should return Boolean type");
        }, "Generic methods should handle different types correctly");
    }

    /**
     * Tests configuration manager initialization.
     */
    @Test
    @DisplayName("Configuration Manager Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testConfigurationManagerInitialization() {
        // Test that configuration manager is properly initialized
        assertNotNull(configManager, "ConfigurationManager should not be null");
        
        // Test that basic configurations are loaded
        assertDoesNotThrow(() -> {
            configManager.getGameConstant("TILE_SIZE", 32);
            configManager.getRenderingConfig("WINDOW_WIDTH", 1024);
            configManager.getWeaponMapping("SWORD", "Blade");
        }, "Basic configurations should be loaded on initialization");
    }
} 