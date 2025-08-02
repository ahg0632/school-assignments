package utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.imageio.ImageIO;

/**
 * Manages weapon image loading and mapping.
 * Uses ConfigurationManager for weapon mappings and provides centralized image caching.
 * Follows OOD principles: Single Responsibility, Singleton Pattern, DRY.
 */
public class WeaponImageManager {
    
    private static final Logger LOGGER = Logger.getLogger(WeaponImageManager.class.getName());
    private static WeaponImageManager instance;
    
    private final Map<String, BufferedImage> imageCache;
    private final ConfigurationManager configManager;
    
    /**
     * Private constructor for singleton pattern
     */
    private WeaponImageManager() {
        this.imageCache = new HashMap<>();
        this.configManager = ConfigurationManager.getInstance();
    }
    
    /**
     * Get singleton instance
     * @return WeaponImageManager instance
     */
    public static WeaponImageManager getInstance() {
        if (instance == null) {
            instance = new WeaponImageManager();
        }
        return instance;
    }
    
    /**
     * Get weapon image for a given weapon name
     * @param weaponName The name of the weapon
     * @return BufferedImage for the weapon, or null if not found
     */
    public BufferedImage getWeaponImage(String weaponName) {
        if (weaponName == null || weaponName.trim().isEmpty()) {
            return null;
        }
        
        String normalizedName = weaponName.toLowerCase().trim();
        String imagePath = getWeaponImagePath(normalizedName);
        
        if (imagePath == null) {
            LOGGER.warning("No image path found for weapon: " + weaponName);
            return null;
        }
        
        return loadImage(imagePath);
    }
    
    /**
     * Get weapon image path for a given weapon name
     * @param weaponName The normalized weapon name
     * @return Image file path or null if not found
     */
    public String getWeaponImagePath(String weaponName) {
        // Step 1: Try exact match from configuration
        String exactMatch = getExactWeaponMatch(weaponName);
        if (exactMatch != null) return exactMatch;
        
        // Step 2: Try intelligent fallback based on weapon type and naming patterns
        String fallbackMatch = getIntelligentFallback(weaponName);
        if (fallbackMatch != null) return fallbackMatch;
        
        // Step 3: Try generic type-based fallback
        return getGenericTypeFallback(weaponName);
    }
    
    /**
     * Get exact weapon match from configuration
     * @param weaponName The normalized weapon name
     * @return Image path or null if not found
     */
    private String getExactWeaponMatch(String weaponName) {
        Object weaponMappingsObj = configManager.getWeaponMapping("weapon_mappings", new HashMap<>());
        if (!(weaponMappingsObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> weaponMappings = (Map<String, Object>) weaponMappingsObj;
        
        // Check each weapon category
        for (String category : weaponMappings.keySet()) {
            Object categoryObj = weaponMappings.get(category);
            if (!(categoryObj instanceof Map)) {
                continue;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> categoryWeapons = (Map<String, Object>) categoryObj;
            if (categoryWeapons.containsKey(weaponName)) {
                Object result = categoryWeapons.get(weaponName);
                return result instanceof String ? (String) result : null;
            }
        }
        
        return null;
    }
    
    /**
     * Get intelligent fallback based on weapon patterns
     * @param weaponName The normalized weapon name
     * @return Image path or null if not found
     */
    private String getIntelligentFallback(String weaponName) {
        Object fallbackMappingsObj = configManager.getWeaponMapping("fallback_mappings", new HashMap<>());
        if (!(fallbackMappingsObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> fallbackMappings = (Map<String, Object>) fallbackMappingsObj;
        
        // Check staff patterns
        Object staffPatternsObj = fallbackMappings.get("staff_patterns");
        if (staffPatternsObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> staffPatterns = (Map<String, Object>) staffPatternsObj;
            Object patternObj = staffPatterns.get("pattern");
            Object templateObj = staffPatterns.get("template");
            
            if (patternObj instanceof String && templateObj instanceof String) {
                String pattern = (String) patternObj;
                String template = (String) templateObj;
                
                Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                Matcher matcher = regex.matcher(weaponName);
                if (matcher.find()) {
                    String number = matcher.group(1);
                    return template.replace("{0}", number);
                }
            }
        }
        
        // Check other pattern categories
        String[] patternCategories = {"sword_patterns", "axe_patterns", "bow_patterns", "magic_patterns"};
        for (String category : patternCategories) {
            Object patternsObj = fallbackMappings.get(category);
            if (patternsObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> patterns = (Map<String, Object>) patternsObj;
                for (String key : patterns.keySet()) {
                    if (weaponName.contains(key)) {
                        Object result = patterns.get(key);
                        return result instanceof String ? (String) result : null;
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get generic type-based fallback
     * @param weaponName The normalized weapon name
     * @return Image path or null if not found
     */
    private String getGenericTypeFallback(String weaponName) {
        Object fallbackMappingsObj = configManager.getWeaponMapping("fallback_mappings", new HashMap<>());
        if (!(fallbackMappingsObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> fallbackMappings = (Map<String, Object>) fallbackMappingsObj;
        
        Object genericPatternsObj = fallbackMappings.get("generic_patterns");
        if (genericPatternsObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> genericPatterns = (Map<String, Object>) genericPatternsObj;
            for (String key : genericPatterns.keySet()) {
                if (weaponName.contains(key)) {
                    Object result = genericPatterns.get(key);
                    return result instanceof String ? (String) result : null;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Load image from path
     * @param imagePath The path to the image
     * @return BufferedImage or null if loading failed
     */
    private BufferedImage loadImage(String imagePath) {
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }
        
        try {
            BufferedImage image = ImageIO.read(
                getClass().getClassLoader().getResourceAsStream("images/weapons/" + imagePath)
            );
            if (image != null) {
                imageCache.put(imagePath, image);
                LOGGER.fine("Loaded weapon image: " + imagePath);
            }
            return image;
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.warning("Failed to load weapon image: " + imagePath + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Clear the image cache
     */
    public void clearCache() {
        imageCache.clear();
        LOGGER.info("Weapon image cache cleared");
    }
    
    /**
     * Reload configuration and clear cache
     */
    public void reloadConfiguration() {
        configManager.reloadConfigurations();
        clearCache();
        LOGGER.info("Weapon configuration reloaded");
    }
} 