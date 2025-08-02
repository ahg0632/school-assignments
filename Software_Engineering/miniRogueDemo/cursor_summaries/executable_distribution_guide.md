# Executable Distribution Guide for Mini Rogue Demo

## Overview

This guide details the options for converting the Mini Rogue Demo Java application into a standalone .exe file that users can download and run without requiring Java to be pre-installed on their system.

## Current Application Analysis

### Dependencies
- **Java 17** with Swing/AWT (built-in GUI framework)
- **JUnit 5** and **Mockito** (testing dependencies only)
- **No external runtime dependencies** - only uses Java standard library
- **Resources**: Images and fonts in `src/main/resources`

### Current Build Configuration
- **Gradle build system** with executable JAR configuration
- **Main class**: `controller.Main`
- **JAR includes all dependencies** and resources
- **MVC architecture** with proper separation of concerns

## Distribution Options

### Option 1: JRE + JAR Distribution (Current State)

**What it provides:**
- Executable JAR file that includes all dependencies
- Users must have Java 17+ installed

**Implementation:**
```bash
gradle clean build jar
# JAR location: build/libs/miniRogueDemo-1.0.0.jar
```

**User Experience:**
- Download JAR file
- Install Java 17+ separately
- Run: `java -jar miniRogueDemo-1.0.0.jar`

**Pros:**
- ✅ Simple implementation (already configured)
- ✅ Small file size (~5-10MB)
- ✅ Standard Java distribution
- ✅ Maintains MVC architecture

**Cons:**
- ❌ Requires Java installation
- ❌ User must manage Java version compatibility
- ❌ Not truly standalone

**File Size:** ~5-10MB (JAR only)

---

### Option 2: jpackage Native Application (IMPLEMENTED)

**What it provides:**
- Native Windows application bundle with embedded JRE
- Truly standalone application
- Professional distribution format

**Implementation:**
```gradle
plugins {
    id 'java'
    id 'application'
}

// Build native application using jpackage
task buildNative(type: Exec) {
    dependsOn 'jar'
    commandLine 'jpackage', 
        '--input', 'build/libs', 
        '--name', 'MiniRogueDemo', 
        '--main-jar', 'MiniRogueDemo-1.0.0.jar', 
        '--main-class', 'controller.Main', 
        '--type', 'app-image', 
        '--dest', 'build/dist',
        '--runtime-image', System.getProperty('java.home')
}
```

**User Experience:**
- Download application folder
- Double-click MiniRogueDemo.exe to run immediately
- No Java installation required

**Pros:**
- ✅ Truly standalone - no Java required
- ✅ Native Windows application
- ✅ Professional distribution
- ✅ Maintains MVC architecture
- ✅ Modern Java tooling
- ✅ Simple implementation

**Cons:**
- ⚠️ Larger file size (~50-80MB)
- ⚠️ Platform-specific builds needed

**File Size:** ~50-80MB (includes embedded JRE)

---

### Option 3: GraalVM Native Image (Advanced)

**What it provides:**
- Native machine code compilation
- Smallest file size
- Fastest startup time

**Implementation:**
```gradle
plugins {
    id 'org.graalvm.buildtools.native' version '0.9.19'
}

graalvmNative {
    binaries {
        main {
            imageName = 'miniRogueDemo'
            mainClass = 'controller.Main'
        }
    }
}
```

**User Experience:**
- Download small .exe file
- Instant startup
- No runtime dependencies

**Pros:**
- ✅ Smallest file size (~10-20MB)
- ✅ Fastest startup time
- ✅ True native performance
- ✅ No runtime dependencies

**Cons:**
- ❌ Complex Swing/AWT native compilation
- ❌ Requires GraalVM installation
- ❌ Platform-specific builds
- ❌ Potential compatibility issues
- ❌ Overengineered for this use case

**File Size:** ~10-20MB

---

### Option 4: Launch4j Wrapper (Legacy)

**What it provides:**
- Native .exe launcher that wraps JAR
- Includes embedded JRE

**Implementation:**
```gradle
plugins {
    id 'com.github.johnrengelman.shadow' version '7.1.2'
}

// Create fat JAR
shadowJar {
    archiveBaseName = 'miniRogueDemo'
    archiveVersion = '1.0.0'
    manifest {
        attributes 'Main-Class': 'controller.Main'
    }
}

// Launch4j configuration
task createExe(type: Exec) {
    dependsOn shadowJar
    commandLine 'launch4j', 'launch4j-config.xml'
}
```

**User Experience:**
- Download .exe file
- Double-click to run
- No Java installation required

**Pros:**
- ✅ Reliable and well-tested
- ✅ Simple implementation
- ✅ No Java required for users

**Cons:**
- ❌ Requires Launch4j tool installation
- ❌ Larger file size
- ❌ Legacy approach
- ❌ Additional build dependency

**File Size:** ~60-100MB

## IMPLEMENTED APPROACH: jpackage (Option 2)

### Why jpackage was Chosen

1. **Simplicity**: Uses modern Java tooling with minimal configuration
2. **MVC Compliance**: No changes needed to existing architecture
3. **User Experience**: True standalone application - no Java installation required
4. **Professional**: Standard approach for Java application distribution
5. **Maintainable**: Leverages existing Gradle build system

### Detailed Implementation Changes

#### Phase 1: Build Configuration Changes

**File Modified:** `build.gradle`

**Original Configuration:**
```gradle
plugins {
    id 'java'
    id 'application'
}

// Task to create executable JAR
jar {
    archiveBaseName = 'MiniRogueDemo'
    manifest {
        attributes(
            'Main-Class': 'controller.Main',
            'Implementation-Title': 'Mini Rogue Demo'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
```

**Required Changes:**
```gradle
plugins {
    id 'java'
    id 'application'
}

// Task to create executable JAR
jar {
    archiveBaseName = 'MiniRogueDemo'
    manifest {
        attributes(
            'Main-Class': 'controller.Main',
            'Implementation-Title': 'Mini Rogue Demo'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}

// Build native application using jpackage
task buildNative(type: Exec) {
    dependsOn 'jar'
    commandLine 'jpackage', 
        '--input', 'build/libs', 
        '--name', 'MiniRogueDemo', 
        '--main-jar', 'MiniRogueDemo-1.0.0.jar', 
        '--main-class', 'controller.Main', 
        '--type', 'app-image', 
        '--dest', 'build/dist',
        '--runtime-image', System.getProperty('java.home')
}
```

**Key Changes Summary:**
1. **Added buildNative task** - Creates native application bundle
2. **Used app-image type** - Avoids WiX tools requirement
3. **Proper JAR dependency** - Ensures JAR is built first
4. **Runtime image specification** - Uses current Java installation

#### Phase 2: Build Commands

**Development Commands:**
```bash
# Build JAR and run tests
gradle clean build

# Create native application bundle
gradle buildNative
```

**Expected Output:**
- **JAR file**: `build/libs/MiniRogueDemo-1.0.0.jar`
- **Native bundle**: `build/dist/MiniRogueDemo/`
  - `MiniRogueDemo.exe` (507KB)
  - `app/MiniRogueDemo-1.0.0.jar`
  - `runtime/` (embedded JRE)

#### Phase 3: Distribution Structure

**Created Directory Structure:**
```
build/dist/MiniRogueDemo/
├── MiniRogueDemo.exe          # Main executable (507KB)
├── app/
│   ├── MiniRogueDemo-1.0.0.jar
│   ├── MiniRogueDemo.cfg
│   └── .jpackage.xml
└── runtime/                   # Embedded JRE
    ├── bin/
    ├── lib/
    └── ...
```

**Total Size:** ~50-80MB (includes embedded JRE)

### Troubleshooting Common Issues

#### Issue 1: WiX Tools Missing
**Error:**
```
Can not find WiX tools. Was looking for WiX v3 light.exe and candle.exe or WiX v4/v5 wix.exe and none was found
```

**Solution:** Use `app-image` type instead of `exe` type in jpackage command.

#### Issue 2: Plugin Conflicts
**Error:**
```
Cannot add task 'jpackageImage' as a task with that name already exists.
```

**Solution:** Remove conflicting plugins (`org.beryx.jlink`, `org.beryx.runtime`) and use simple jpackage approach.

#### Issue 3: JAR Name Mismatch
**Error:**
```
File not found: miniRogueDemo-1.0.0.jar
```

**Solution:** Ensure JAR name matches exactly: `MiniRogueDemo-1.0.0.jar`

### Testing Checklist

**Before Distribution:**
- [ ] Test on clean Windows machine (no Java installed)
- [ ] Verify all resources (images, fonts) are included
- [ ] Test all game functionality
- [ ] Verify MVC architecture integrity
- [ ] Test application startup time
- [ ] Verify memory usage

**User Experience Testing:**
- [ ] Double-click .exe launches game
- [ ] All game features work correctly
- [ ] No Java installation required
- [ ] Application closes properly
- [ ] No error messages on startup

### Trade-offs Analysis

| Aspect | jpackage (Implemented) | GraalVM | Launch4j | JAR Only |
|--------|----------------------|---------|----------|----------|
| **File Size** | ~50-80MB | ~10-20MB | ~60-100MB | ~5-10MB |
| **Startup Speed** | Fast | Very Fast | Medium | Fast |
| **Java Required** | No | No | No | Yes |
| **Implementation Complexity** | Low | High | Medium | Very Low |
| **MVC Compliance** | ✅ | ✅ | ✅ | ✅ |
| **User Experience** | Excellent | Excellent | Good | Poor |
| **Maintenance** | Low | High | Medium | Very Low |
| **WiX Tools Required** | No (app-image) | No | No | No |

### Final Implementation Summary

**Successfully Implemented:**
- ✅ Native Windows application bundle
- ✅ Standalone .exe with embedded JRE
- ✅ No external dependencies required
- ✅ Maintains existing MVC architecture
- ✅ Simple build configuration
- ✅ Professional distribution format

**Build Commands:**
```bash
gradle clean build    # Build JAR and run tests
gradle buildNative    # Create native application
```

**Distribution Files:**
- `build/dist/MiniRogueDemo/MiniRogueDemo.exe` - Standalone executable
- Complete application bundle with embedded JRE

**User Experience:**
- Download application folder
- Double-click MiniRogueDemo.exe
- Game runs immediately without Java installation

This implementation provides the best balance of simplicity, user experience, and maintainability while preserving the existing MVC architecture and providing a truly standalone Windows application. 