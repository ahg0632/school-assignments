package utilities;

import model.equipment.Weapon;
import enums.CharacterClass;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

/**
 * Manages weapon definitions loaded from JSON configuration
 * Eliminates hardcoded weapon creation throughout the codebase
 */
public class WeaponDefinitionManager {
    private static final Logger LOGGER = Logger.getLogger(WeaponDefinitionManager.class.getName());
    private static WeaponDefinitionManager instance;
    
    private static final String WEAPON_DEFINITIONS_PATH = "config/weapon_definitions.json";
    private static final String ARMOR_DEFINITIONS_PATH = "config/armor_definitions.json";
    
    private final Map<String, Object> weaponDefinitions;
    private final Map<String, Object> armorDefinitions;
    private final ConfigurationManager configManager;
    
    private WeaponDefinitionManager() {
        this.weaponDefinitions = new HashMap<>();
        this.armorDefinitions = new HashMap<>();
        this.configManager = ConfigurationManager.getInstance();
        loadDefinitions();
    }
    
    public static WeaponDefinitionManager getInstance() {
        if (instance == null) {
            instance = new WeaponDefinitionManager();
        }
        return instance;
    }
    
    /**
     * Load weapon and armor definitions from JSON files
     */
    private void loadDefinitions() {
        loadWeaponDefinitions();
        loadArmorDefinitions();
        LOGGER.info("Weapon and armor definitions loaded successfully");
    }
    
    /**
     * Load weapon definitions from JSON
     */
    private void loadWeaponDefinitions() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(WEAPON_DEFINITIONS_PATH)) {
            if (inputStream != null) {
                String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(jsonContent);
                parseJsonToMap(json, weaponDefinitions);
                LOGGER.info("Weapon definitions loaded from file");
            } else {
                LOGGER.warning("Weapon definitions file not found: " + WEAPON_DEFINITIONS_PATH);
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to load weapon definitions: " + e.getMessage());
        }
    }
    
    /**
     * Load armor definitions from JSON
     */
    private void loadArmorDefinitions() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ARMOR_DEFINITIONS_PATH)) {
            if (inputStream != null) {
                String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(jsonContent);
                parseJsonToMap(json, armorDefinitions);
                LOGGER.info("Armor definitions loaded from file");
            } else {
                LOGGER.warning("Armor definitions file not found: " + ARMOR_DEFINITIONS_PATH);
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to load armor definitions: " + e.getMessage());
        }
    }
    
    /**
     * Parse JSON object to Map recursively
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
     * Create a weapon from definition ID
     */
    public Weapon createWeapon(String weaponId) {
        Object weaponsObj = weaponDefinitions.get("weapons");
        if (!(weaponsObj instanceof Map)) {
            LOGGER.warning("No weapons found in definitions");
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> weapons = (Map<String, Object>) weaponsObj;
        
        Object weaponDefObj = weapons.get(weaponId);
        if (!(weaponDefObj instanceof Map)) {
            LOGGER.warning("Weapon definition not found: " + weaponId);
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> weaponDef = (Map<String, Object>) weaponDefObj;
        
        try {
            String name = (String) weaponDef.get("name");
            int attack = ((Number) weaponDef.get("attack")).intValue();
            int defense = ((Number) weaponDef.get("defense")).intValue();
            String classStr = (String) weaponDef.get("class");
            String typeStr = (String) weaponDef.get("type");
            int tier = ((Number) weaponDef.get("tier")).intValue();
            String image = (String) weaponDef.get("image");
            String category = (String) weaponDef.get("category");
            
            CharacterClass characterClass = CharacterClass.valueOf(classStr);
            Weapon.WeaponType weaponType = Weapon.WeaponType.valueOf(typeStr);
            
            return new Weapon(name, attack, defense, characterClass, tier, weaponType, "images/weapons/" + image, category);
        } catch (Exception e) {
            LOGGER.severe("Failed to create weapon from definition " + weaponId + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Create an armor from definition ID
     */
    public model.equipment.Armor createArmor(String armorId) {
        Object armorObj = armorDefinitions.get("armor");
        if (!(armorObj instanceof Map)) {
            LOGGER.warning("No armor found in definitions");
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> armor = (Map<String, Object>) armorObj;
        
        Object armorDefObj = armor.get(armorId);
        if (!(armorDefObj instanceof Map)) {
            LOGGER.warning("Armor definition not found: " + armorId);
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> armorDef = (Map<String, Object>) armorDefObj;
        
        try {
            String name = (String) armorDef.get("name");
            int attack = ((Number) armorDef.get("attack")).intValue();
            int defense = ((Number) armorDef.get("defense")).intValue();
            int mana = ((Number) armorDef.get("mana")).intValue();
            String classStr = (String) armorDef.get("class");
            int tier = ((Number) armorDef.get("tier")).intValue();
            String image = (String) armorDef.get("image");
            String category = (String) armorDef.get("category");
            
            CharacterClass characterClass = CharacterClass.valueOf(classStr);
            
            return new model.equipment.Armor(name, attack, defense, mana, characterClass, tier, "images/armor/" + image, category);
        } catch (Exception e) {
            LOGGER.severe("Failed to create armor from definition " + armorId + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get starting weapon for a character class
     */
    public Weapon getStartingWeapon(CharacterClass characterClass) {
        Object startingWeaponsObj = weaponDefinitions.get("starting_weapons");
        if (!(startingWeaponsObj instanceof Map)) {
            LOGGER.warning("No starting weapons found in definitions");
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> startingWeapons = (Map<String, Object>) startingWeaponsObj;
        
        String weaponId = (String) startingWeapons.get(characterClass.name());
        if (weaponId == null) {
            LOGGER.warning("No starting weapon found for class: " + characterClass);
            return null;
        }
        
        return createWeapon(weaponId);
    }
    
    /**
     * Get starting armor for a character class
     */
    public model.equipment.Armor getStartingArmor(CharacterClass characterClass) {
        Object startingArmorObj = armorDefinitions.get("starting_armor");
        if (!(startingArmorObj instanceof Map)) {
            LOGGER.warning("No starting armor found in definitions");
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> startingArmor = (Map<String, Object>) startingArmorObj;
        
        String armorId = (String) startingArmor.get(characterClass.name());
        if (armorId == null) {
            LOGGER.warning("No starting armor found for class: " + characterClass);
            return null;
        }
        
        return createArmor(armorId);
    }
    
    /**
     * Get all weapons for a character class
     */
    public List<Weapon> getWeaponsForClass(CharacterClass characterClass) {
        List<Weapon> weapons = new ArrayList<>();
        Object weaponsObj = weaponDefinitions.get("weapons");
        if (!(weaponsObj instanceof Map)) {
            return weapons;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> weaponsMap = (Map<String, Object>) weaponsObj;
        
        for (String weaponId : weaponsMap.keySet()) {
            Object weaponDefObj = weaponsMap.get(weaponId);
            if (weaponDefObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> weaponDef = (Map<String, Object>) weaponDefObj;
                String classStr = (String) weaponDef.get("class");
                if (CharacterClass.valueOf(classStr) == characterClass) {
                    Weapon weapon = createWeapon(weaponId);
                    if (weapon != null) {
                        weapons.add(weapon);
                    }
                }
            }
        }
        
        return weapons;
    }
    
    /**
     * Get random weapon for a character class
     */
    public Weapon getRandomWeaponForClass(CharacterClass characterClass) {
        List<Weapon> weapons = getWeaponsForClass(characterClass);
        if (weapons.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return weapons.get(random.nextInt(weapons.size()));
    }
    
    /**
     * Get enemy weapon for a character class
     */
    public Weapon getEnemyWeapon(CharacterClass characterClass) {
        Object enemyWeaponsObj = weaponDefinitions.get("enemy_weapons");
        if (!(enemyWeaponsObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> enemyWeapons = (Map<String, Object>) enemyWeaponsObj;
        
        Object weaponIdsObj = enemyWeapons.get(characterClass.name());
        if (!(weaponIdsObj instanceof Object[])) {
            return null;
        }
        
        Object[] weaponIds = (Object[]) weaponIdsObj;
        if (weaponIds.length == 0) {
            return null;
        }
        
        Random random = new Random();
        String weaponId = (String) weaponIds[random.nextInt(weaponIds.length)];
        
        // Check if it's an enemy-specific weapon
        Object enemyWeaponDefsObj = weaponDefinitions.get("enemy_weapon_definitions");
        if (enemyWeaponDefsObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> enemyWeaponDefs = (Map<String, Object>) enemyWeaponDefsObj;
            if (enemyWeaponDefs.containsKey(weaponId)) {
                return createEnemyWeapon(weaponId);
            }
        }
        
        // Fall back to regular weapon
        return createWeapon(weaponId);
    }
    
    /**
     * Create enemy weapon from definition
     */
    private Weapon createEnemyWeapon(String weaponId) {
        Object enemyWeaponDefsObj = weaponDefinitions.get("enemy_weapon_definitions");
        if (!(enemyWeaponDefsObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> enemyWeaponDefs = (Map<String, Object>) enemyWeaponDefsObj;
        
        Object weaponDefObj = enemyWeaponDefs.get(weaponId);
        if (!(weaponDefObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> weaponDef = (Map<String, Object>) weaponDefObj;
        
        try {
            String name = (String) weaponDef.get("name");
            int attack = ((Number) weaponDef.get("attack")).intValue();
            int defense = ((Number) weaponDef.get("defense")).intValue();
            String classStr = (String) weaponDef.get("class");
            String typeStr = (String) weaponDef.get("type");
            int tier = ((Number) weaponDef.get("tier")).intValue();
            String image = (String) weaponDef.get("image");
            String category = (String) weaponDef.get("category");
            
            CharacterClass characterClass = CharacterClass.valueOf(classStr);
            Weapon.WeaponType weaponType = Weapon.WeaponType.valueOf(typeStr);
            
            return new Weapon(name, attack, defense, characterClass, tier, weaponType, "images/weapons/" + image, category);
        } catch (Exception e) {
            LOGGER.severe("Failed to create enemy weapon from definition " + weaponId + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get enemy armor for a character class
     */
    public model.equipment.Armor getEnemyArmor(CharacterClass characterClass) {
        Object enemyArmorObj = armorDefinitions.get("enemy_armor");
        if (!(enemyArmorObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> enemyArmor = (Map<String, Object>) enemyArmorObj;
        
        Object armorIdsObj = enemyArmor.get(characterClass.name());
        if (!(armorIdsObj instanceof Object[])) {
            return null;
        }
        
        Object[] armorIds = (Object[]) armorIdsObj;
        if (armorIds.length == 0) {
            return null;
        }
        
        Random random = new Random();
        String armorId = (String) armorIds[random.nextInt(armorIds.length)];
        
        // Check if it's an enemy-specific armor
        Object enemyArmorDefsObj = armorDefinitions.get("enemy_armor_definitions");
        if (enemyArmorDefsObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> enemyArmorDefs = (Map<String, Object>) enemyArmorDefsObj;
            if (enemyArmorDefs.containsKey(armorId)) {
                return createEnemyArmor(armorId);
            }
        }
        
        // Fall back to regular armor
        return createArmor(armorId);
    }
    
    /**
     * Create enemy armor from definition
     */
    private model.equipment.Armor createEnemyArmor(String armorId) {
        Object enemyArmorDefsObj = armorDefinitions.get("enemy_armor_definitions");
        if (!(enemyArmorDefsObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> enemyArmorDefs = (Map<String, Object>) enemyArmorDefsObj;
        
        Object armorDefObj = enemyArmorDefs.get(armorId);
        if (!(armorDefObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> armorDef = (Map<String, Object>) armorDefObj;
        
        try {
            String name = (String) armorDef.get("name");
            int attack = ((Number) armorDef.get("attack")).intValue();
            int defense = ((Number) armorDef.get("defense")).intValue();
            int mana = ((Number) armorDef.get("mana")).intValue();
            String classStr = (String) armorDef.get("class");
            int tier = ((Number) armorDef.get("tier")).intValue();
            String image = (String) armorDef.get("image");
            String category = (String) armorDef.get("category");
            
            CharacterClass characterClass = CharacterClass.valueOf(classStr);
            
            return new model.equipment.Armor(name, attack, defense, mana, characterClass, tier, "images/armor/" + image, category);
        } catch (Exception e) {
            LOGGER.severe("Failed to create enemy armor from definition " + armorId + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all weapons
     */
    public List<Weapon> getAllWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        Object weaponsObj = weaponDefinitions.get("weapons");
        if (!(weaponsObj instanceof Map)) {
            return weapons;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> weaponsMap = (Map<String, Object>) weaponsObj;
        
        for (String weaponId : weaponsMap.keySet()) {
            Weapon weapon = createWeapon(weaponId);
            if (weapon != null) {
                weapons.add(weapon);
            }
        }
        
        return weapons;
    }
    
    /**
     * Get all armor
     */
    public List<model.equipment.Armor> getAllArmor() {
        List<model.equipment.Armor> armor = new ArrayList<>();
        Object armorObj = armorDefinitions.get("armor");
        if (!(armorObj instanceof Map)) {
            return armor;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> armorMap = (Map<String, Object>) armorObj;
        
        for (String armorId : armorMap.keySet()) {
            model.equipment.Armor armorPiece = createArmor(armorId);
            if (armorPiece != null) {
                armor.add(armorPiece);
            }
        }
        
        return armor;
    }
    
    /**
     * Reload definitions from JSON files
     */
    public void reloadDefinitions() {
        weaponDefinitions.clear();
        armorDefinitions.clear();
        loadDefinitions();
        LOGGER.info("Weapon and armor definitions reloaded");
    }
} 