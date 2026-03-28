# MrDav Snake Game

A classic Snake game built in Java using a custom lightweight game framework and Swing for desktop rendering.

## Gameplay

- The snake moves continuously around a 10×13 grid
- Click the **bottom-left** area to turn left, **bottom-right** to turn right
- Eat the stains to grow and score points — the snake speeds up every 100 points
- Click the **top-left** corner to pause
- Top 5 high scores are saved between sessions

## Build & Run

**Run the game:**
```bash
java -cp out com.david.framework.desktop.DesktopGame
```

**Recompile after changing source files:**
```bash
javac -d out $(find src -name "*.java") && cp assets/* out/
```

Requires JDK 11 or later.

## Project Structure

```
MrDav/
├── assets/                         # Images (PNG) and sounds (WAV)
├── out/                            # Compiled classes + assets (run from here)
└── src/
    └── com/david/
        ├── framework/              # Platform-agnostic interfaces
        │   ├── Game.java           # Core game lifecycle
        │   ├── Screen.java         # Base class for all screens
        │   ├── Graphics.java       # 2D rendering interface
        │   ├── Input.java          # Touch/mouse and keyboard events
        │   ├── Audio.java          # Sound and music loading
        │   ├── Sound.java          # Short sound effect playback
        │   ├── Music.java          # Background music playback
        │   ├── FileIO.java         # Asset and file I/O
        │   ├── Pixmap.java         # Image handle interface
        │   ├── Pool.java           # Generic object pooling utility
        │   └── desktop/            # Desktop (Swing/Java2D) implementations
        │       ├── DesktopGame.java      # JFrame + game loop + main()
        │       ├── DesktopGraphics.java  # BufferedImage + Graphics2D rendering
        │       ├── DesktopPixmap.java    # Wraps BufferedImage
        │       ├── DesktopInput.java     # Mouse and keyboard event handling
        │       ├── DesktopAudio.java     # javax.sound.sampled audio loader
        │       ├── DesktopSound.java     # Per-play Clip for sound effects
        │       ├── DesktopMusic.java     # Looping Clip for background music
        │       └── DesktopFileIO.java    # Classpath assets + home dir file I/O
        └── mrdav/                  # Game logic and screens
            ├── LoadingScreen.java  # Loads all assets, then transitions to menu
            ├── MainMenuScreen.java # Main menu with sound toggle
            ├── GameScreen.java     # Core gameplay (Ready/Running/Paused/GameOver)
            ├── HelpScreen.java     # Help page 1 of 3
            ├── HelpScreen2.java    # Help page 2 of 3
            ├── HelpScreen3.java    # Help page 3 of 3
            ├── HighScoreScreen.java# Top 5 scores display
            ├── Assets.java         # Static references to all loaded assets
            ├── Settings.java       # Sound toggle + high score persistence
            ├── World.java          # Game simulation (grid, tick, collision)
            ├── Snake.java          # Snake direction and turn logic
            ├── SnakePart.java      # Single segment of the snake
            └── Stain.java          # Food item with randomised type
```
