# Week 2 — Hierarchies & Voxel Types Checklist

Goal: extend the class hierarchy (inheritance/interfaces) and add multiple voxel block types with distinct visuals and at least one functional difference, then validate their usage in the world.

## Prerequisites
- Project builds and runs (Week 1 complete): `jogo.Jogo` launches; input and interaction are working.
- JDK/Gradle combo is compatible (e.g., JDK 17 + Gradle 7.6.3 or JDK 21 + Gradle 8.8+).
- Optional: keep a temporary voxel strip/plate for quick visual confirmation.

## Environment Setup
- IntelliJ
  - Confirm `Project SDK` and `Gradle JVM` match your chosen JDK.
  - Reload Gradle project after dependency or wrapper changes.
- Run Configuration
  - Main class: `jogo.Jogo`.
  - macOS: keep `-XstartOnFirstThread` in VM options.

## Build & Run
- Terminal: `./gradlew clean run`
- IntelliJ: Run ‘Jogo.main()’ and verify window, input, and HUD.

## Code Map (relevant for Week 2)
- Base hierarchy to extend: `src/main/java/jogo/gameobject/*`
- Voxel palette/types: `src/main/java/jogo/voxel/VoxelPalette.java`, `src/main/java/jogo/voxel/VoxelBlockType.java`
- Example type: `src/main/java/jogo/voxel/blocks/StoneBlockType.java`
- World generation hook: `src/main/java/jogo/voxel/VoxelWorld.java` (`generateLayers()` and `setBlock(...)`)

## Week 2 Tasks
1) Extend Hierarchy
- Add 1–2 concrete subclasses across the provided hierarchy (e.g., `Character` → `Enemy/Ally`, `Terrain` → `Tree/Rock`, `Item` → `Pickaxe/Key`).
- Prefer composition for shared data; use inheritance when a clear “is‑a” relationship exists.
- Add small, focused methods (capabilities) and keep public APIs clean.

2) Define Capability Interfaces (lightweight)
- Introduce at least one interface to express behavior (e.g., `Interactable`, `Tickable`, `Harvestable`).
- Keep them small; no engine types; document contracts in Javadoc.

3) Add ≥2 New Voxel Block Types
- Create custom `VoxelBlockType` subclasses (e.g., `DirtBlockType`, `WoodBlockType`, `SandBlockType`).
- Distinct visuals: different color/texture via material generation; reuse `ProcTextures` if available.
- Functional difference: at least one property or rule per type (e.g., solidity, hardness, friction, slip behavior).

4) Register Types in the Palette
- Use `voxelWorld.getPalette().register(new YourBlockType())` to get IDs.
- Store IDs for use when populating the world.

5) Use Types in the World
- In `generateLayers()`, assign cells to your new types so they render.
- Keep generation simple (strip/patches or a tiny height rule) focused on visual verification.
- After placement, meshes/physics are built; later edits require `rebuildDirtyChunks(...)`.

6) Documentation Updates
- Javadoc: public classes/methods for new subclasses and interfaces.
- UML: update diagram to show hierarchy extensions and interfaces.
- Short note in `docs/` describing the new types, their visuals, and functional distinctions.

## Deliverables (Week 2)
- 1–2 new hierarchy subclasses with clean encapsulation and small methods.
- At least 2 new `VoxelBlockType`s registered and used in the world.
- Visual verification (screenshots/video) showing distinct materials.
- Javadoc on public members; updated UML; short design note in `docs/`.

## Acceptance Criteria (Self‑check)
- The app runs and shows your new voxel types in the scene.
- Each new voxel type has a distinct visual and at least one functional difference.
- Subclasses/interfaces are engine‑neutral and demonstrate sensible OO design.
- Public APIs are small, clear, and documented.
- UML and design note are updated and committed.

## Common Issues & Fixes
- Types look the same
  - Adjust material color/texture; verify `VoxelBlockType.getMaterial(...)` usage.
- Nothing appears after placement
  - Ensure `setBlock(...)` uses correct IDs; confirm chunk rebuild and bounds.
- Overuse of inheritance
  - Prefer interfaces or composition for shared behavior; avoid deep trees.

## Next (preview of Week 3)
- Implement minimal `Inventory` and `Crafting` with ≥2 recipes.
- Add basic NPCs (≥1 ally, ≥1 enemy) with a simple `updateAI()`.
- Start terrain generation v1 (height function or seeded noise) placing ≥2 voxel types.