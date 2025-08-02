package model.items;

import enums.CharacterClass;
import model.characters.Character;

/**
 * Abstract base class for all items in the Mini Rogue Demo.
 * Provides common attributes and behavior for consumables, key items, and equipment.
 */
public abstract class Item {
    // MANDATORY: Core item attributes
    protected String name;
    protected int potency;
    protected CharacterClass classType;

    /**
     * MANDATORY: Constructor for Item
     *
     * @param name Item's display name
     * @param potency Item's power/effectiveness value
     * @param classType Character class this item is designed for
     */
    public Item(String name, int potency, CharacterClass classType) {
        this.name = name;
        this.potency = potency;
        this.classType = classType;
    }

    /**
     * MANDATORY: Constructor for class-neutral items
     *
     * @param name Item's display name
     * @param potency Item's power/effectiveness value
     */
    public Item(String name, int potency) {
        this.name = name;
        this.potency = potency;
        this.classType = null; // Usable by any class
    }

    /**
     * MANDATORY: Use the item on a character
     *
     * @param character The character using the item
     * @return true if item was successfully used
     */
    public abstract boolean use(Character character);

    /**
     * MANDATORY: Check if item can be used by character class
     *
     * @param characterClass The class to check compatibility with
     * @return true if character can use this item
     */
    public boolean is_usable_by(CharacterClass characterClass) {
        return classType == null || classType == characterClass;
    }

    // MANDATORY: Getters
    public String get_name() { return name; }
    public int get_potency() { return potency; }
    public CharacterClass get_class_type() { return classType; }

    @Override
    public String toString() {
        String classInfo = (classType != null) ? " (" + classType.get_class_name() + ")" : "";
        return name + classInfo;
    }
} 