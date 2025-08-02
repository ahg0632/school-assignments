## 03-core-interfaces.md

# CRITICAL REQUIREMENTS - Core MVC Interfaces Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the core MVC interfaces exactly as specified below for the Mini Rogue Demo game architecture.

## INTERFACE SPECIFICATIONS

### **CRITICAL**: Create GameModel.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Core interface for all game model components in the Mini Rogue Demo.
* Defines the contract for game state management and observer notifications.
*/
public interface GameModel {
/**
    * MANDATORY: Method called by model implementations to notify
    * interested observers of state changes.
    * 
    * @param event String describing the change event
    * @param data Object containing event-specific data
*/
void notify_observers(String event, Object data);

/**
    * MANDATORY: Add an observer to receive model change notifications
    * 
    * @param observer The GameObserver to add
*/
void add_observer(GameObserver observer);

/**
    * MANDATORY: Remove an observer from notifications
    * 
    * @param observer The GameObserver to remove
*/
void remove_observer(GameObserver observer);
}

```

### **CRITICAL**: Create GameView.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Core interface for all view components in the Mini Rogue Demo.
* Defines the contract for UI components that display game state.
*/
public interface GameView {
/**
    * MANDATORY: Update the view display based on model changes
*/
void update_display();

/**
    * MANDATORY: Get the controller managing this view
    * 
    * @return The GameController instance
*/
GameController get_controller();

/**
    * MANDATORY: Set the controller for this view
    * 
    * @param controller The GameController to set
*/
void set_controller(GameController controller);

/**
    * MANDATORY: Initialize view components
*/
void initialize_components();
}

```

### **CRITICAL**: Create GameController.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Core interface for all controller components in the Mini Rogue Demo.
* Defines the contract for handling user input and coordinating MVC components.
*/
public interface GameController {
/**
    * MANDATORY: Handle user input events
    * 
    * @param input String representing the user input
*/
void handle_input(String input);

/**
    * MANDATORY: Get the model this controller manages
    * 
    * @return The GameModel instance
*/
GameModel get_model();

/**
    * MANDATORY: Set the model for this controller
    * 
    * @param model The GameModel to manage
*/
void set_model(GameModel model);

/**
    * MANDATORY: Get the view this controller manages
    * 
    * @return The GameView instance
*/
GameView get_view();

/**
    * MANDATORY: Set the view for this controller
    * 
    * @param view The GameView to manage
*/
void set_view(GameView view);
}

```

### **CRITICAL**: Create GameObserver.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Observer interface for receiving notifications about game model changes.
* Implements the Observer pattern for loose coupling between model and view.
*/
public interface GameObserver {
/**
    * MANDATORY: Method called when observed model changes
    * 
    * @param event String describing what changed
    * @param data Object containing change-specific information
*/
void on_model_changed(String event, Object data);
}

```

### **MANDATORY**: Interface Design Principles

1. **CRITICAL**: All interfaces must be in the `interfaces` package
2. **CRITICAL**: Method names must use snake_case convention
3. **CRITICAL**: Complete JavaDoc documentation for all methods
4. **CRITICAL**: Observer pattern implementation for MVC communication
5. **CRITICAL**: Loose coupling between MVC components

### **MANDATORY**: Design Patterns Implemented

**CRITICAL**: These interfaces enforce:
- **Observer Pattern**: Models notify observers of state changes
- **MVC Pattern**: Clear separation of Model, View, Controller responsibilities
- **Strategy Pattern**: Interface-based polymorphism for different implementations
- **Command Pattern**: Input handling through controller interface

### **MANDATORY**: Usage Context

**CRITICAL**: These interfaces will be:
- Implemented by concrete model classes (GameLogic, BattleLogic)
- Implemented by view classes for GUI panels
- Implemented by controller classes for input handling
- Used throughout the game architecture for loose coupling

### **MANDATORY**: Verification Steps

1. **CRITICAL**: All interfaces compile successfully in interfaces package
2. **CRITICAL**: Method signatures match exactly as specified
3. **CRITICAL**: JavaDoc documentation is complete and error-free
4. **CRITICAL**: Interfaces are public and accessible
5. **CRITICAL**: No compilation warnings
6. **CRITICAL**: Observer pattern contracts are properly defined

### CRITICAL REQUIREMENT ###
**MANDATORY**: The core interfaces must be implemented exactly as specified above. These interfaces form the foundation of the MVC architecture for the Mini Rogue Demo and must be correct before proceeding to concrete implementations.
```