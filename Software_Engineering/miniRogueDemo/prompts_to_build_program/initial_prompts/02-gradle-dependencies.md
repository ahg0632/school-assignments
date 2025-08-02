## 02-gradle-dependencies.md

# CRITICAL REQUIREMENTS - Gradle Dependencies Configuration

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Configure Gradle build file for the Mini Rogue Demo project with exact dependencies and configurations specified below.

## BUILD.GRADLE CONFIGURATION

### **CRITICAL**: Create the following build.gradle file exactly:

```

plugins {
id 'java'
id 'application'
}

group = 'com.rogueDemo'
version = '1.0.0'

java {
sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17
}

repositories {
mavenCentral()
}

dependencies {
// JUnit 5 for testing
testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.2'
testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.2'
testImplementation 'org.junit.jupiter:junit-jupiter-params:5.9.2'

    // Mockito for testing
    testImplementation 'org.mockito:mockito-core:5.1.1'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.1.1'
    
    // Swing is part of JDK, no external dependency needed
    // java.awt and javax.swing are included in JDK
    }

application {
mainClass = 'controller.Main'
}

test {
useJUnitPlatform()
}

// Task to create executable JAR
jar {
archiveBaseName = 'miniRogueDemo'
archiveVersion = '1.0.0'

    manifest {
        attributes(
            'Main-Class': 'controller.Main',
            'Implementation-Title': 'Mini Rogue Demo',
            'Implementation-Version': project.version
        )
    }
    
    // Include all dependencies in JAR
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
    }

// Task to run the game
task runGame(type: JavaExec) {
group = 'application'
description = 'Run the Mini Rogue Demo game'
classpath = sourceSets.main.runtimeClasspath
mainClass = 'controller.Main'

    // JVM arguments for better performance
    jvmArgs = [
        '-Xmx512m',
        '-Xms256m',
        '-Djava.awt.headless=false'
    ]
    }

// Compiler options
compileJava {
options.encoding = 'UTF-8'
options.compilerArgs += ['-Xlint:unchecked', '-Xlint:deprecation']
}

compileTestJava {
options.encoding = 'UTF-8'
}

```

### **MANDATORY**: Dependency Requirements

1. **CRITICAL**: Java 17 with Swing support (built-in)
2. **CRITICAL**: JUnit 5 for comprehensive testing framework
3. **CRITICAL**: Mockito for mock object testing
4. **CRITICAL**: No external game libraries (use java.awt and javax.swing)
5. **CRITICAL**: Maven Central repository for dependency resolution

### **MANDATORY**: Build Configuration

**CRITICAL**: The build must support:
- Java compilation with UTF-8 encoding
- JUnit 5 test execution with parameterized tests
- Application running via `gradle run` or `gradle runGame`
- Executable JAR creation with all dependencies
- Main class execution: `controller.Main`

### **MANDATORY**: Package Dependencies

**CRITICAL**: Ensure these package relationships:
- `model` package contains game data and logic
- `view` package contains GUI components
- `controller` package contains main class and input handling
- `interfaces` package defines contracts
- `enums` and `utilities` provide support classes

### **MANDATORY**: Performance Configuration

**CRITICAL**: JVM settings:
- Maximum heap size: 512MB
- Initial heap size: 256MB
- Headless mode disabled for GUI
- Compiler warnings enabled for code quality

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Run `gradle clean build` - must succeed
2. **CRITICAL**: Run `gradle test` - must execute (no tests yet)
3. **CRITICAL**: Verify Java 17 compatibility
4. **CRITICAL**: Verify JUnit 5 is available for testing
5. **CRITICAL**: Verify application plugin configuration
6. **CRITICAL**: Test `gradle runGame` task creation

### **MANDATORY**: Testing Setup

**CRITICAL**: Create `src/test/java/` directory structure:
```

src/test/java/
├── model/
│   ├── characters/
│   ├── items/
│   └── gameLogic/
├── view/
└── controller/

```

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Gradle configuration must compile successfully and support the roguelike game architecture with proper dependencies and build tasks as specified above.
