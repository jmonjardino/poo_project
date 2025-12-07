# Week 4 — Polymorphism, AI, Persistence & Errors

Goal: Implement state machines/strategies for NPCs, expand crafting to ≥4 recipes, implement initial Persistence (Save/Load + Highscore) with robust error handling, and refine terrain generation (v2).

## Prerequisites
- Week 3 complete: Inventory, Basic Crafting (≥2 recipes), Simple NPCs, Generation v1.
- Codebase builds and runs; previous weeks' features are stable.

## Environment Setup
- IntelliJ
  - Ensure updated dependencies (if any).
  - Verify test harnesses from Week 3 run correctly.
- Run Configuration
  - Main class: `jogo.Jogo`.

## Build & Run
- Terminal: `./gradlew clean run`
- IntelliJ: Run ‘Jogo.main()’; verify Week 3 features persist.

## Week 4 Tasks
1) NPC AI (State Machines)
- Refine `Enemy`: Implement a small state machine (e.g., Enum: `IDLE`, `PATROL`, `CHASE`, `ATTACK`).
- Refine `Ally`: Implement distinct behavior (e.g., `FOLLOW`, `HEAL`, `TRADE`).
- Use Polymorphism: `updateAI()` behavior differs significantly between subclasses.

2) Crafting Expansion
- Add more items/blocks to support new recipes.
- Implement ≥2 additional recipes (Total ≥4).
- Ensure recipe validation handles invalid inputs gracefully.

3) Persistence (Save/Load & Highscore)
- **Save/Load**: Serialize/deserialize essential state (Player position, Inventory, World Seed, distinct modified blocks if applicable).
- **Highscore**: Implement a leaderboard (e.g., Top 5 scores based on time/enemies/blocks) using a file-based storage.
- **Error Handling**: Wrap IO operations in `try-catch`; throw specific exceptions (e.g., `CorruptSaveException`) and provide UI/Log feedback.

4) Terrain Generation v2 (Refinement)
- enhance `generateLayers()`:
  - Add parameters (frequency, amplitude) to generation.
  - Consider simple "biomes" (e.g., different top blocks based on height or 2nd noise map).
- Maintain determinism (same seed = same world).

5) Testing & Harness
- Add small tests/harnesses to verify:
  - Polymorphic calls (Enemy vs Ally).
  - Save/Load integrity (Save -> Restart -> Load -> Match).

## Deliverables (Week 4)
- AI with State Machines/Strategies (≥2 states per NPC).
- Crafting System with ≥4 working recipes.
- Persistence System (Save/Load + Highscore) with file IO.
- Generation v2 with parameters/biomes.
- Robust Error Handling for IO/Game Logic.

## Acceptance Criteria (Self‑check)
- NPCs exhibit distinct state behavior (e.g., Chase only when close).
- Crafting allows creation of complex items (≥4 recipes).
- Game state can be saved and loaded accurately.
- Highscores persist between sessions.
- IO errors (missing file, perm denied) are handled without crashing (logs/UI message).

## Common Issues & Fixes
- **File Paths**: Use relative paths or `user.home` to avoid permission issues.
- **Serialization**: Ensure `GameObject`s or data classes are `Serializable` or mapped to DTOs.
- **State Logic**: Avoid "spaghetti code" `if/else` chains; use Switch or Strategy pattern.

## Next (preview of Week 5)
- Integration of all pillars.
- Balancing (Recipes/Health/Damage).
- Documentation review (Javadoc, naming).
- Walkthrough preparation.
