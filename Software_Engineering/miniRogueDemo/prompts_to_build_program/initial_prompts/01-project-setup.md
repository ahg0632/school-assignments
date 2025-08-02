## 01-project-setup.md

# CRITICAL REQUIREMENTS - Mini Rogue Demo Project Setup

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Create a complete Mini Rogue Demo project structure in Cursor IDE with the exact specifications below for a roguelike dungeon crawler game.

## PROJECT STRUCTURE REQUIREMENTS

### **CRITICAL**: Create the following directory structure exactly:

```

miniRogueDemo/
├── .gitignore
├── README.md
├── build.gradle
├── prompts/
│   └── (prompt files will be stored here)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── model/
│   │   │   │   ├── characters/
│   │   │   │   ├── items/
│   │   │   │   ├── equipment/
│   │   │   │   ├── map/
│   │   │   │   └── gameLogic/
│   │   │   ├── view/
│   │   │   │   ├── panels/
│   │   │   │   └── components/
│   │   │   ├── controller/
│   │   │   ├── interfaces/
│   │   │   ├── enums/
│   │   │   └── utilities/
│   │   └── resources/
│   │       ├── images/
│   │       │   ├── characters/
│   │       │   ├── items/
│   │       │   ├── tiles/
│   │       │   └── ui/
│   │       └── sounds/
│   └── test/
│       └── java/
│           ├── model/
│           ├── view/
│           └── controller/

```

### **MANDATORY**: Git Repository Setup

**CRITICAL**: Create `.gitignore` file with these exact contents:
```


# Build files

build/
.gradle/
gradle/
gradlew
gradlew.bat

# IDE files

.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# OS files

.DS_Store
Thumbs.db

# Java files

*.class
*.jar
*.war
*.ear
*.logs
*.log

# Backup files

*.bak
*.tmp
*~

```

### **MANDATORY**: Initial README.md

**CRITICAL**: Create `README.md` with project description:
```


# Mini Rogue Demo

A 2D pixel art roguelike dungeon crawler game written in Java using MVC architecture.

## Features

- Procedural dungeon generation
- Character classes (Warrior, Mage, Rogue)
- Real-time combat system
- Inventory and equipment upgrade system
- Boss battles and permadeath mechanics


## Requirements

- Java 17+
- Gradle 7.0+


## Build and Run

```bash
gradle build
gradle run
```


## Architecture

- MVC (Model-View-Controller) pattern
- Java Swing GUI
- Multithreaded enemy AI
- 2D pixel art graphics

```

### **MANDATORY**: Naming Convention Requirements

1. **CRITICAL**: All folder names must use camelCase
2. **CRITICAL**: All package names must use camelCase
3. **CRITICAL**: All class names must use CamelCase
4. **CRITICAL**: All variable and object names must use camelCase
5. **CRITICAL**: All function names must use snake_case

### **MANDATORY**: Project Configuration

**CRITICAL**: Prepare for Gradle configuration with:
- Java version: 17
- Project name: "miniRogueDemo"
- Group: "com.rogueDemo"
- Version: "1.0.0"

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Verify all directories exist as specified
2. **CRITICAL**: Verify .gitignore contains all required entries
3. **CRITICAL**: Verify README.md is created with project information
4. **CRITICAL**: Verify project structure matches diagram exactly
5. **CRITICAL**: Verify naming conventions are applied consistently

### CRITICAL REQUIREMENT ###
**MANDATORY**: Complete this setup phase before proceeding to any implementation. The project structure must be exactly as specified above to support the roguelike game architecture.
