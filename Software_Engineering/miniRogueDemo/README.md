# Mini Rogue Demo

A Java-based roguelike game built with Swing GUI, featuring real-time combat, character progression, and procedural dungeon generation.

## 🎮 Installation & Setup

### Option 1: Standalone Executable (Recommended)

**For Windows users who want the easiest setup:**

1. **Download the executable bundle** from the releases page
2. **Extract the folder** to your desired location
3. **Double-click `MiniRogueDemo.exe`** to run the game
4. **No Java installation required!**

**Benefits:**
- ✅ No Java installation needed
- ✅ No command line setup required
- ✅ Professional Windows application
- ✅ Includes all dependencies

**File size:** ~50-80MB (includes embedded Java runtime)

### Option 2: Java JAR (Traditional)

**For users who prefer the traditional Java approach:**

#### Prerequisites

Before building the game, you need to install the following dependencies:

#### 1. Java Development Kit (JDK) 17+

**Windows:**
1. Download OpenJDK 17 from [Adoptium](https://adoptium.net/)
2. Run the installer and follow the setup wizard
3. Verify installation: Open Command Prompt and run `java -version`

**macOS:**
```bash
# Using Homebrew
brew install openjdk@17

# Or download from Adoptium
# https://adoptium.net/
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**Linux (Fedora/RHEL):**
```bash
sudo dnf install java-17-openjdk-devel
```

#### 2. Gradle Build Tool

**Windows:**
1. Download Gradle from [gradle.org](https://gradle.org/releases/)
2. Extract to `C:\Gradle`
3. Add `C:\Gradle\gradle-8.x\bin` to your PATH environment variable
4. Verify installation: Open Command Prompt and run `gradle -version`

**macOS:**
```bash
# Using Homebrew
brew install gradle

# Or download from gradle.org and add to PATH
```

**Linux:**
```bash
# Using package manager
sudo apt install gradle  # Ubuntu/Debian
sudo dnf install gradle  # Fedora/RHEL

# Or download from gradle.org and add to PATH
```

### Build from Source

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/miniRogueDemo.git
   cd miniRogueDemo
   ```

2. **Build the project:**
   ```bash
   gradle clean build
   ```

3. **Run the game:**
   ```bash
   gradle run
   ```

4. **Create executable JAR:**
   ```bash
   gradle jar
   ```
   The JAR file will be created at: `build/libs/MiniRogueDemo.jar`

5. **Run the JAR file:**
   ```bash
   java -jar build/libs/MiniRogueDemo.jar
   ```

6. **Create standalone executable (Windows):**
   ```bash
   gradle buildNative
   ```
   The executable will be created at: `build/dist/MiniRogueDemo/MiniRogueDemo.exe`

### Troubleshooting

**"gradle: command not found"**
- Ensure Gradle is installed and added to your PATH
- Try running `./gradlew` instead of `gradle` (Gradle Wrapper)

**"java: command not found"**
- Ensure JDK 17+ is installed and added to your PATH
- Verify with `java -version`

**Build errors:**
- Ensure you have JDK 17+ (not just JRE)
- Try `gradle clean build --info` for detailed error messages
- Check that all dependencies are properly installed

**Executable won't run:**
- Ensure you're running the .exe from the extracted folder (not just the .exe file)
- Check that antivirus software isn't blocking the application
- Try running as administrator if needed

## 🎯 Game Overview

Mini Rogue Demo is a real-time roguelike game where you explore procedurally generated dungeons, battle enemies, collect items, and progress through increasingly challenging floors.

## 🎮 How to Play

### Getting Started

1. **Launch the game** - You'll see the main menu with character class options
2. **Choose your character class:**
   - **Warrior**: High health, melee combat, tank role
   - **Mage**: Low health, powerful magic, ranged attacks
   - **Rogue**: Balanced stats, stealth abilities, critical hits
   - **Ranger**: Medium health, ranged combat, distance attacks

### Basic Controls

| Action | Key |
|--------|-----|
| **Move** | WASD |
| **Aim** | Mouse (move mouse to aim) or Arrow Keys |
| **Toggle Mouse Aiming** | M |
| **Attack** | Space Bar (or Left Mouse Button when using mouse aiming) |
| **Use Item** | Right Mouse Button |
| **Open Inventory** | I |
| **Open Equipment** | E |
| **Pause Game** | ESC |
| **Menu Navigation** | Arrow Keys + Enter |

### Gameplay Mechanics

#### 🗺️ Exploration
- **Movement**: Use WASD to move your character
- **Aiming**: Move mouse to aim or use arrow keys
- **Mouse Aiming Toggle**: Press M to enable/disable mouse aiming mode
- **Field of View**: You can only see areas within your character's vision range
- **Floors**: Each floor has multiple rooms connected by corridors
- **Stairs**: Find the stairs to progress to the next floor

#### ⚔️ Combat
- **Real-time Combat**: Enemies and you can attack simultaneously
- **Aiming System**: 
  - **Mouse Aiming**: Move mouse to aim (default)
  - **Arrow Key Aiming**: Use arrow keys to aim (alternative)
  - **Toggle Mode**: Press M to switch between mouse and arrow key aiming
  - **Mouse Attack**: When using mouse aiming, left-click to attack
- **Attack Types**:
  - **Melee**: Close-range attacks (Warrior, Rogue)
  - **Ranged**: Distance attacks (Mage, Ranger)
  - **Magic**: Special abilities with mana cost (Mage)
- **Combat Stats**:
  - **Attack**: Determines damage dealt
  - **Defense**: Reduces damage taken
  - **Speed**: Affects movement and attack rate
  - **Range**: Distance for ranged attacks
  - **Mana**: Used for magic abilities

#### 🎒 Inventory & Items
- **Inventory Panel**: Press 'I' to open
- **Item Types**:
  - **Weapons**: Increase attack power and range
  - **Armor**: Increase defense
  - **Consumables**: Health potions, buff items
  - **Keys**: Open locked doors
  - **Equipment**: Tiered items with stat modifiers

#### 🛡️ Equipment System
- **Equipment Panel**: Press 'E' to open
- **Tier System**: Equipment has tiers 1-5 (higher = better)
- **Stat Modifiers**: Equipment provides bonuses to stats
- **Scrapping**: Convert unwanted equipment to scrap resources
- **Upgrade Crystals**: Use scrap to create upgrade materials

#### 🏥 Health & Survival
- **Health Bar**: Monitor your health in the stats panel
- **Health Potions**: Restore health when low
- **Status Effects**: Temporary buffs from consumables
- **Death**: Game over when health reaches zero

### Character Classes Deep Dive

#### 🗡️ Warrior
- **Role**: Tank, melee specialist
- **Starting Stats**: High health, good defense
- **Weapon Type**: Blade weapons
- **Playstyle**: Close combat, absorb damage, protect allies

#### 🔮 Mage
- **Role**: Magic damage dealer
- **Starting Stats**: Low health, high mana
- **Weapon Type**: Magic weapons
- **Playstyle**: Ranged magic attacks, powerful spells, mana management

#### 🗡️ Rogue
- **Role**: Stealth, critical damage
- **Starting Stats**: Balanced stats
- **Weapon Type**: Blade weapons
- **Playstyle**: Stealth mechanics, critical hits, agility

#### 🏹 Ranger
- **Role**: Ranged damage dealer
- **Starting Stats**: Medium health, good range
- **Weapon Type**: Distance weapons
- **Playstyle**: Ranged attacks, kiting enemies, precision shots

### Floor Types

#### 🏰 Regular Floors
- Standard dungeon layout
- Multiple rooms and corridors
- Balanced enemy and item distribution

#### 👑 Boss Floors
- Fewer, larger rooms
- Boss enemy in largest room
- More enemies and items
- Higher difficulty

#### 💎 Bonus Floors
- Special 3-room layout
- Guaranteed Floor Key in exit room
- Wider corridors
- Chance for enemies and items

### Advanced Strategies

#### 🎯 Combat Tips
1. **Kite Enemies**: Use ranged attacks and movement to avoid damage
2. **Use Terrain**: Position yourself advantageously in rooms
3. **Manage Resources**: Conserve health potions for difficult encounters
4. **Equipment Synergy**: Combine items that complement your class

#### 🗺️ Exploration Tips
1. **Clear Rooms**: Explore fully to find all items and enemies
2. **Check Corners**: Items and enemies can hide in room corners
3. **Plan Routes**: Consider the path to stairs when exploring
4. **Resource Management**: Don't waste items on easy encounters

#### 🛡️ Survival Tips
1. **Health Management**: Keep health above 50% when possible
2. **Item Usage**: Use consumables strategically, not immediately
3. **Equipment Upgrades**: Prioritize better equipment when found
4. **Class Synergy**: Use items that benefit your character class

### Game States

#### 🏠 Main Menu
- Character class selection
- Game options and settings
- High score display

#### 🎮 In-Game
- Real-time gameplay
- Inventory and equipment management
- Combat and exploration

#### ⏸️ Paused
- Game paused, menu accessible
- Save/load options
- Settings adjustment

#### 🏁 Game Over
- Score display
- High score entry
- Restart option

## 🎮 Tips for Success

1. **Learn Your Class**: Each character class has unique strengths and playstyles
2. **Explore Thoroughly**: Don't rush - explore every room for items and enemies
3. **Manage Resources**: Health potions and mana are limited
4. **Upgrade Equipment**: Better equipment significantly improves your chances
5. **Use Consumables**: Don't hoard items - use them when needed
6. **Practice Combat**: Learn enemy patterns and your attack timing
7. **Plan Ahead**: Consider your route and resource usage

## 🛠️ Technical Information

- **Language**: Java 17
- **GUI Framework**: Swing/AWT
- **Build System**: Gradle
- **Architecture**: MVC (Model-View-Controller)
- **Dependencies**: JUnit 5 (testing), Mockito (testing)
- **Distribution**: Native Windows executable with embedded JRE (jpackage)

## 🐛 Troubleshooting

### Common Issues

**Game won't start:**
- Ensure you have Java 17+ installed (for JAR version)
- Try running from command line to see error messages
- Check that the executable file is not blocked by antivirus

**Performance issues:**
- Close other applications to free up memory
- Ensure graphics drivers are up to date
- Try running in windowed mode

**Controls not responding:**
- Ensure the game window has focus
- Check that your keyboard/mouse are working
- Try restarting the game


For development setup and contribution guidelines, see the development documentation.

---

**Enjoy your adventure in Mini Rogue Demo!** 🎮⚔️🗡️ 
