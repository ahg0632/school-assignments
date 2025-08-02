package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.charset.StandardCharsets;

/**
 * Manages game configuration files and provides centralized access to settings.
 * Follows OOD principles for better code organization and maintainability.
 */
public class ConfigurationManager {
    
    private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class.getName());
    private static ConfigurationManager instance;
    
    // Configuration file paths
    private static final String GAME_CONSTANTS_PATH = "config/game_constants.json";
    private static final String RENDERING_CONFIG_PATH = "config/rendering_config.json";
    private static final String WEAPON_MAPPINGS_PATH = "config/weapon_mappings.json";
    private static final String WEAPON_DEFINITIONS_PATH = "config/weapon_definitions.json";
    private static final String ARMOR_DEFINITIONS_PATH = "config/armor_definitions.json";
    
    // Configuration data storage
    private final Map<String, Object> gameConstants;
    private final Map<String, Object> renderingConfig;
    private final Map<String, Object> weaponMappings;
    
    // Cache for frequently accessed values
    private final Map<String, Object> valueCache;
    
    /**
     * Private constructor for singleton pattern
     */
    private ConfigurationManager() {
        this.gameConstants = new HashMap<>();
        this.renderingConfig = new HashMap<>();
        this.weaponMappings = new HashMap<>();
        this.valueCache = new HashMap<>();
        
        loadAllConfigurations();
    }
    
    /**
     * Get singleton instance
     * @return ConfigurationManager instance
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    /**
     * Load all configuration files
     */
    private void loadAllConfigurations() {
        try {
            loadGameConstants();
            loadRenderingConfig();
            loadWeaponMappings();
            LOGGER.info("All configuration files loaded successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load configuration files", e);
            loadDefaultConfigurations();
        }
    }
    
    /**
     * Load game constants configuration
     */
    private void loadGameConstants() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(GAME_CONSTANTS_PATH)) {
            if (inputStream != null) {
                String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(jsonContent);
                parseJsonToMap(json, gameConstants);
                LOGGER.info("Game constants loaded from file");
            } else {
                loadDefaultGameConstants();
                LOGGER.warning("Game constants file not found, using defaults");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load game constants, using defaults", e);
            loadDefaultGameConstants();
        }
    }
    
    /**
     * Load rendering configuration
     */
    private void loadRenderingConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(RENDERING_CONFIG_PATH)) {
            if (inputStream != null) {
                String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(jsonContent);
                parseJsonToMap(json, renderingConfig);
                LOGGER.info("Rendering config loaded from file");
            } else {
                loadDefaultRenderingConfig();
                LOGGER.warning("Rendering config file not found, using defaults");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load rendering config, using defaults", e);
            loadDefaultRenderingConfig();
        }
    }
    
    /**
     * Load weapon mappings configuration
     */
    private void loadWeaponMappings() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(WEAPON_MAPPINGS_PATH)) {
            if (inputStream != null) {
                String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(jsonContent);
                parseJsonToMap(json, weaponMappings);
                LOGGER.info("Weapon mappings loaded from file");
            } else {
                loadDefaultWeaponMappings();
                LOGGER.warning("Weapon mappings file not found, using defaults");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load weapon mappings, using defaults", e);
            loadDefaultWeaponMappings();
        }
    }
    
    /**
     * Parse JSON object to Map
     */
    private void parseJsonToMap(JSONObject json, Map<String, Object> targetMap) {
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                Map<String, Object> nestedMap = new HashMap<>();
                parseJsonToMap((JSONObject) value, nestedMap);
                targetMap.put(key, nestedMap);
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                Object[] arrayValues = new Object[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    arrayValues[i] = array.get(i);
                }
                targetMap.put(key, arrayValues);
            } else {
                targetMap.put(key, value);
            }
        }
    }
    
    /**
     * Load default game constants
     */
    private void loadDefaultGameConstants() {
        gameConstants.clear();
        
        // Game settings
        gameConstants.put("game.title", "Mini Rogue Demo");
        gameConstants.put("game.version", "1.0.0");
        gameConstants.put("game.fps", 60);
        gameConstants.put("game.debug_mode", false);
        
        // Display settings
        gameConstants.put("display.window_width", 1200);
        gameConstants.put("display.window_height", 800);
        gameConstants.put("display.tile_size", 32);
        gameConstants.put("display.ui_scale", 1.0);
        gameConstants.put("display.fullscreen", false);
        
        // Player settings
        gameConstants.put("player.starting_health", 100);
        gameConstants.put("player.starting_mana", 50);
        gameConstants.put("player.starting_level", 1);
        gameConstants.put("player.experience_base", 100);
        gameConstants.put("player.experience_multiplier", 1.5);
        gameConstants.put("player.stat_points_per_level", 5);
        gameConstants.put("player.max_level", 50);
        
        // Combat settings
        gameConstants.put("combat.base_attack_speed", 1.0);
        gameConstants.put("combat.base_attack_range", 1);
        gameConstants.put("combat.base_attack_damage", 10);
        gameConstants.put("combat.base_defense", 5);
        gameConstants.put("combat.critical_chance", 0.05);
        gameConstants.put("combat.critical_multiplier", 2.0);
        gameConstants.put("combat.dodge_chance", 0.05);
        gameConstants.put("combat.block_chance", 0.03);
        gameConstants.put("combat.block_reduction", 0.5);
        
        // Movement settings
        gameConstants.put("movement.base_movement_speed", 1.0);
        gameConstants.put("movement.diagonal_movement_cost", 1.4);
        gameConstants.put("movement.collision_checking", true);
        gameConstants.put("movement.smooth_movement", true);
        
        // Enemy settings
        gameConstants.put("enemy.base_health", 50);
        gameConstants.put("enemy.base_damage", 8);
        gameConstants.put("enemy.base_defense", 3);
        gameConstants.put("enemy.experience_reward", 20);
        gameConstants.put("enemy.gold_reward", 10);
        gameConstants.put("enemy.aggro_range", 8);
        gameConstants.put("enemy.patrol_radius", 3);
        gameConstants.put("enemy.respawn_time", 30000);
        
        // Map settings
        gameConstants.put("map.width", 50);
        gameConstants.put("map.height", 50);
        gameConstants.put("map.room_min_size", 5);
        gameConstants.put("map.room_max_size", 15);
        gameConstants.put("map.corridor_width", 3);
        gameConstants.put("map.wall_thickness", 1);
        gameConstants.put("map.door_chance", 0.3);
        gameConstants.put("map.trap_chance", 0.1);
    }
    
    /**
     * Load default rendering configuration
     */
    private void loadDefaultRenderingConfig() {
        renderingConfig.clear();
        
        // General rendering
        renderingConfig.put("rendering.general.vsync", true);
        renderingConfig.put("rendering.general.double_buffering", true);
        renderingConfig.put("rendering.general.anti_aliasing", true);
        renderingConfig.put("rendering.general.max_fps", 60);
        renderingConfig.put("rendering.general.min_fps", 30);
        
        // Sprite settings
        renderingConfig.put("rendering.sprites.animation_speed", 200);
        renderingConfig.put("rendering.sprites.sprite_cache_size", 1000);
        renderingConfig.put("rendering.sprites.sprite_compression", false);
        renderingConfig.put("rendering.sprites.pixel_perfect", true);
        
        // Effects settings
        renderingConfig.put("rendering.effects.particle_systems", true);
        renderingConfig.put("rendering.effects.max_particles", 1000);
        renderingConfig.put("rendering.effects.particle_lifetime", 2000);
        renderingConfig.put("rendering.effects.screen_shake", true);
        renderingConfig.put("rendering.effects.screen_flash", true);
        renderingConfig.put("rendering.effects.motion_blur", false);
        renderingConfig.put("rendering.effects.bloom_effect", false);
        
        // Lighting settings
        renderingConfig.put("rendering.lighting.enabled", true);
        renderingConfig.put("rendering.lighting.ambient_light", 0.3);
        renderingConfig.put("rendering.lighting.dynamic_lighting", true);
        renderingConfig.put("rendering.lighting.shadow_quality", "medium");
        renderingConfig.put("rendering.lighting.max_lights", 10);
        renderingConfig.put("rendering.lighting.light_falloff", 1.0);
        renderingConfig.put("rendering.lighting.fog_enabled", false);
        
        // UI settings
        renderingConfig.put("rendering.ui.font_rendering", "smooth");
        renderingConfig.put("rendering.ui.font_cache_size", 100);
        renderingConfig.put("rendering.ui.ui_scale", 1.0);
        renderingConfig.put("rendering.ui.ui_opacity", 0.9);
        renderingConfig.put("rendering.ui.tooltip_delay", 500);
        renderingConfig.put("rendering.ui.notification_duration", 3000);
        
        // Map settings
        renderingConfig.put("rendering.map.tile_rendering", "optimized");
        renderingConfig.put("rendering.map.tile_cache_size", 500);
        renderingConfig.put("rendering.map.fog_of_war", true);
        renderingConfig.put("rendering.map.fog_opacity", 0.7);
        renderingConfig.put("rendering.map.revealed_tiles_opacity", 0.3);
        
        // Character settings
        renderingConfig.put("rendering.characters.sprite_animation", true);
        renderingConfig.put("rendering.characters.idle_animation", true);
        renderingConfig.put("rendering.characters.walking_animation", true);
        renderingConfig.put("rendering.characters.attack_animation", true);
        renderingConfig.put("rendering.characters.death_animation", true);
        renderingConfig.put("rendering.characters.shadow_rendering", true);
        renderingConfig.put("rendering.characters.health_bar_rendering", true);
        
        // Weapon settings
        renderingConfig.put("rendering.weapons.weapon_swing_animation", true);
        renderingConfig.put("rendering.weapons.weapon_trail_effect", false);
        renderingConfig.put("rendering.weapons.weapon_glow", false);
        renderingConfig.put("rendering.weapons.projectile_trail", true);
        renderingConfig.put("rendering.weapons.projectile_glow", false);
        renderingConfig.put("rendering.weapons.impact_effects", true);
        
        // Projectile settings
        renderingConfig.put("rendering.projectiles.projectile_speed", 8.0);
        renderingConfig.put("rendering.projectiles.projectile_trail", true);
        renderingConfig.put("rendering.projectiles.projectile_glow", false);
        renderingConfig.put("rendering.projectiles.projectile_rotation", true);
        renderingConfig.put("rendering.projectiles.projectile_animation", true);
        renderingConfig.put("rendering.projectiles.impact_particles", true);
        
        // Combat settings
        renderingConfig.put("rendering.combat.damage_numbers", true);
        renderingConfig.put("rendering.combat.damage_flash", true);
        renderingConfig.put("rendering.combat.critical_hit_effects", true);
        renderingConfig.put("rendering.combat.block_effects", true);
        renderingConfig.put("rendering.combat.dodge_effects", true);
        renderingConfig.put("rendering.combat.heal_effects", true);
        renderingConfig.put("rendering.combat.status_effect_icons", true);
        
        // Performance settings
        renderingConfig.put("rendering.performance.culling_enabled", true);
        renderingConfig.put("rendering.performance.culling_distance", 20);
        renderingConfig.put("rendering.performance.batch_rendering", true);
        renderingConfig.put("rendering.performance.texture_atlas", true);
        renderingConfig.put("rendering.performance.shader_optimization", true);
        
        // Debug settings
        renderingConfig.put("rendering.debug.show_fps", false);
        renderingConfig.put("rendering.debug.show_memory_usage", false);
        renderingConfig.put("rendering.debug.show_render_stats", false);
        renderingConfig.put("rendering.debug.show_collision_boxes", false);
        renderingConfig.put("rendering.debug.show_pathfinding", false);
        renderingConfig.put("rendering.debug.show_ai_state", false);
    }
    
    /**
     * Load default weapon mappings
     */
    private void loadDefaultWeaponMappings() {
        weaponMappings.clear();
        
        // This would be populated with the weapon mappings from WeaponImageManager
        // For now, we'll leave it empty as the WeaponImageManager handles its own mappings
        LOGGER.info("Weapon mappings handled by WeaponImageManager");
    }
    
    /**
     * Load default configurations when files are not available
     */
    private void loadDefaultConfigurations() {
        loadDefaultGameConstants();
        loadDefaultRenderingConfig();
        loadDefaultWeaponMappings();
        LOGGER.info("Default configurations loaded");
    }
    
    /**
     * Get a game constant value
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @param <T> Type of the value
     * @return Configuration value
     */
    @SuppressWarnings("unchecked")
    public <T> T getGameConstant(String key, T defaultValue) {
        Object value = gameConstants.get(key);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                LOGGER.warning("Type mismatch for key: " + key);
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a rendering configuration value
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @param <T> Type of the value
     * @return Configuration value
     */
    @SuppressWarnings("unchecked")
    public <T> T getRenderingConfig(String key, T defaultValue) {
        Object value = renderingConfig.get(key);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                LOGGER.warning("Type mismatch for key: " + key);
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a weapon mapping value
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @param <T> Type of the value
     * @return Configuration value
     */
    @SuppressWarnings("unchecked")
    public <T> T getWeaponMapping(String key, T defaultValue) {
        Object value = weaponMappings.get(key);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                LOGGER.warning("Type mismatch for key: " + key);
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a cached value or compute and cache it
     * @param key Cache key
     * @param defaultValue Default value if not cached
     * @param <T> Type of the value
     * @return Cached value
     */
    @SuppressWarnings("unchecked")
    public <T> T getCachedValue(String key, T defaultValue) {
        return (T) valueCache.computeIfAbsent(key, k -> defaultValue);
    }
    
    /**
     * Set a cached value
     * @param key Cache key
     * @param value Value to cache
     */
    public void setCachedValue(String key, Object value) {
        valueCache.put(key, value);
    }
    
    /**
     * Clear the value cache
     */
    public void clearCache() {
        valueCache.clear();
        LOGGER.info("Configuration cache cleared");
    }
    
    /**
     * Reload all configurations
     */
    public void reloadConfigurations() {
        clearCache();
        loadAllConfigurations();
        LOGGER.info("All configurations reloaded");
    }
    
    /**
     * Get all game constants
     * @return Map of all game constants
     */
    public Map<String, Object> getAllGameConstants() {
        return new HashMap<>(gameConstants);
    }
    
    /**
     * Get all rendering configurations
     * @return Map of all rendering configurations
     */
    public Map<String, Object> getAllRenderingConfigs() {
        return new HashMap<>(renderingConfig);
    }
    
    /**
     * Get all weapon mappings
     * @return Map of all weapon mappings
     */
    public Map<String, Object> getAllWeaponMappings() {
        return new HashMap<>(weaponMappings);
    }
    
    /**
     * Check if a configuration key exists
     * @param key Configuration key
     * @return true if key exists
     */
    public boolean hasGameConstant(String key) {
        return gameConstants.containsKey(key);
    }
    
    /**
     * Check if a rendering configuration key exists
     * @param key Configuration key
     * @return true if key exists
     */
    public boolean hasRenderingConfig(String key) {
        return renderingConfig.containsKey(key);
    }
    
    /**
     * Check if a weapon mapping key exists
     * @param key Configuration key
     * @return true if key exists
     */
    public boolean hasWeaponMapping(String key) {
        return weaponMappings.containsKey(key);
    }
} 