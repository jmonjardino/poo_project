# Week 3 — Systems: Crafting, Inventory, NPCs, Generation (stub)

Goal: design capability interfaces, implement a minimal inventory + crafting flow (≥2 recipes this week), add basic NPCs (≥1 ally, ≥1 enemy) with a simple `updateAI()` tick, and begin terrain generation v1 (height function or seeded noise placing ≥2 voxel types).

## Prerequisites
- Week 1 & Week 2 complete: project builds/runs, hierarchy extended, and at least two custom voxel types visible.
- JDK/Gradle aligned; macOS VM option `-XstartOnFirstThread` set in Run Configuration.

## Environment Setup
- IntelliJ
  - Verify `Project SDK` and `Gradle JVM` consistency (JDK 17 with Gradle 7.6.3 or JDK 21 with Gradle 8.8+).
  - Reload Gradle project; ensure tests/harness can run from IDE.
- Run Configuration
  - Main class: `jogo.Jogo`.

## Build & Run
- Terminal: `./gradlew clean run`
- IntelliJ: Run ‘Jogo.main()’; confirm window, movement, interaction.

## Code Map
- Game objects: `src/main/java/jogo/gameobject/*`
- Interaction: `src/main/java/jogo/appstate/InteractionAppState.java` (routes `E` to `Item.onInteract()`; voxel pick available)
- Voxel world: `src/main/java/jogo/voxel/VoxelWorld.java` (`generateLayers()`; `setBlock(...)`)
- Voxel palette: `src/main/java/jogo/voxel/VoxelPalette.java`

## Week 3 Tasks
1) Define Capability Interfaces
- Create small interfaces to express behaviors/capabilities (examples):
  - `Interactable` — objects that respond to player interaction.
  - `Craftable` or `RecipeProvider` — exposes crafting metadata.
  - `Tickable` — objects that update per frame or per logical tick.
- Keep contracts minimal and engine‑neutral; document pre/postconditions.

2) Inventory (minimal)
- Implement `Inventory` with ≥6 slots or a justified capacity.
- Operations: add/remove, capacity checks, optional stacking.
- Error handling: throw domain exceptions (e.g., `InventoryFullException`).
- Create a simple harness to validate operations (no engine types).

3) Crafting (initial ≥2 recipes)
- Define `Recipe` and a `CraftingSystem` with validation:
  - Verify inputs, produce outputs, update inventory.
  - Handle errors: `InvalidRecipeException`, `InsufficientResourcesException`.
- Implement at least 2 recipes this week; plan to reach ≥4 by Week 5.
- Add a small harness to exercise crafting flows.

4) NPCs (basic)
- Add ≥1 ally and ≥1 enemy class extending your `Character` hierarchy.
- Implement `updateAI(playerState)` with simple behavior:
  - Ally: follow within range, optionally heal/trade/log.
  - Enemy: patrol or chase when in range; simple attack condition.
- Encapsulate states (idle/chase/attack) as enums or strategy classes; keep logic deterministic.

5) Terrain Generation v1 (stub)
- Implement a simple height function or seeded noise to place ≥2 voxel types (e.g., sand low, dirt mid, stone high).
- Persist or store a `seed` and generation parameters for determinism (no random without seeding).
- Keep generation light—focus on verifying placement and visibility.

6) Integration Hooks (minimal)
- `InteractionAppState` remains the engine route for `E` → `Item.onInteract()`.
- For voxel interaction, use `pickFirstSolid` hit (`cell`, `normal`, `distance`) to plan later “place block” mechanics (do not code it yet if out of scope for Week 3).

7) Documentation
- Javadoc on new interfaces/classes and public methods.
- Update UML to include interfaces, abstract classes, and relationships.
- Short note in `docs/` describing inventory structure, recipe validation, NPC behaviors, and generation approach.

## Deliverables (Week 3)
- Capability interfaces and at least one abstract base (if needed).
- `Inventory` with core operations and error handling.
- `CraftingSystem` with ≥2 recipes and validations.
- Basic NPCs (≥1 ally, ≥1 enemy) exhibiting distinct behaviors via `updateAI()`.
- Terrain generation v1 placing ≥2 voxel types deterministically.
- Javadoc, updated UML, and a short design note.

## Acceptance Criteria (Self‑check)
- Inventory operations pass harness tests for add/remove/capacity.
- Crafting validates inputs, produces outputs, and throws proper exceptions.
- NPCs demonstrate behavior changes in logs or state transitions.
- World shows multiple voxel types placed by generation v1 for the same seed.
- Public APIs documented; UML reflects interfaces and extensions; design note committed.

## Common Issues & Fixes
- Inventory edge cases
  - Handle stack limits and capacity checks explicitly; test with boundary conditions.
- Crafting validation
  - Provide explicit error messages; avoid silent failures.
- NPC AI complexity
  - Keep state transitions simple and deterministic; avoid random unless seeded.
- Generation performance
  - Use small areas for initial tests; optimize loops later.

## Next (preview of Week 4)
- Evolve AI with state machines/strategies; add more recipes to reach ≥4.
- Begin persistence (Save/Load and highscore) and harden error handling.
- Enhance terrain generation with parameters/biomes while maintaining determinism.