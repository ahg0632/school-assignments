package model.items;

import model.characters.Character;

/**
 * Simple concrete Item class for testing purposes.
 */
public class TestItem extends Item {
    
    public TestItem(String name, int potency) {
        super(name, potency);
    }
    
    @Override
    public boolean use(Character character) {
        // Simple test implementation - always returns true
        return true;
    }
} 