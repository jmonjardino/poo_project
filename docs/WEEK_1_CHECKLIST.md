# Week 1 — Foundations Checklist

Goal: get the project building and running, define a few game‑layer classes (engine‑neutral), and validate interaction/render wiring. Complete the deliverables listed at the end.

## Prerequisites
- Install a compatible JDK and align Gradle/JVM:
  - Option A: JDK `17` (safe with Gradle `7.6.3`).
  - Option B: Upgrade Gradle to `8.8+` and use JDK `21`.
- macOS + LWJGL: ensure JVM starts on the first thread.

## Environment Setup
- IntelliJ Project SDK
  - Open `File > Project Structure…` and set `Project SDK` to `JDK 17` (or `21` if Gradle 8.x).
  - In `Settings > Build, Execution, Deployment > Build Tools > Gradle`, set `Gradle JVM` to the same JDK.
- Gradle wrapper (if upgrading)
  - Run: `./gradlew wrapper --gradle-version 8.8 --distribution-type bin`.
  - Verify `gradle/wrapper/gradle-wrapper.properties` has `distributionUrl=https://services.gradle.org/distributions/gradle-8.8-bin.zip`.
- macOS display fix
  - Add JVM option `-XstartOnFirstThread`:
    - IntelliJ Run Configuration → Main class `jogo.Jogo` → `VM options`: `-XstartOnFirstThread`.
    - Or in `build.gradle` `applicationDefaultJvmArgs = ["-XstartOnFirstThread"]`.

## Build & Run
- From terminal (project root):
  - `./gradlew clean run`
- From IntelliJ:
  - Create/Use a Run Configuration with Main class `jogo.Jogo`.
  - Run it; verify a window opens with sky‑blue background and input works.

## Controls (runtime)
- `Tab` capture/release mouse
- Move: `W/A/S/D`
- Jump: `Space`
- Interact: `E`
- Break voxel: Left Mouse Button
- Respawn: `R`
- Render toggles: `L`

## Code Map (engine wiring already provided)
- Main class: `src/main/java/jogo/Jogo.java` (entry point `jogo.Jogo`: lines 23–33)
- World: `src/main/java/jogo/appstate/WorldAppState.java` (initializes voxel world)
- Voxel world: `src/main/java/jogo/voxel/VoxelWorld.java` (`generateLayers()` is your terrain hook)
- Interaction: `src/main/java/jogo/appstate/InteractionAppState.java` (routes `E` to `Item.onInteract()` and exposes voxel pick)

## Week 1 Tasks
## DONE
1) Read the engine docs 
- `docs/ENGINE.md` (or `docs/ENGINE_PT.md`). Note: student classes must be engine‑neutral (no jME imports).

## DONE 
2) Run the project end‑to‑end
- Confirm the window opens and you can move, jump, and toggle mouse capture.
- If display creation fails on macOS, add `-XstartOnFirstThread` and retry.

## DONE
3) Create 2–3 concrete game‑layer classes
- Under `src/main/java/jogo/gameobject/*` create classes extending your base hierarchy (e.g., `Item`, `Terrain`, `Character`).
- Add fields, constructors, getters/setters, `toString()` (and `equals()/hashCode()` if useful).
- Keep them engine‑neutral: store only state/logic (e.g., name, position as logical vector, stats), no jME types.

## DONE
4) Register 1 object for rendering/interaction
- Use the provided `GameRegistry` in `Jogo.simpleInitApp()` to add one object instance so you can see it in the scene.
- Validate picking of items via `E` prints a log (see `InteractionAppState`).

## DONE
5) Terrain visibility sanity check
- In `VoxelWorld.generateLayers()`, place a simple strip/plate of `STONE` near spawn to verify voxel rendering (temporary for Week 1).
- Rebuild meshes/physics is handled at init; later edits use `rebuildDirtyChunks(...)`.

6) Documentation artifacts
- Add Javadoc to each new public class and public methods.
- Create a brief UML sketch (classes and relationships) and save it under `docs/`.
- Write a short note explaining object choices, responsibilities, and how they interact.

## Deliverables (Week 1)
- New engine‑neutral classes (2–3) with Javadoc.
- UML sketch in `docs/` showing classes and fields/relationships.
- Short note in `docs/` explaining the design intent and responsibilities.
- Project builds and runs; you can move/jump and interact with at least one registered object.

## Acceptance Criteria (Self‑check)
- The app runs without GLFW/thread errors on macOS (or equivalent on other OS).
- At least one object is registered and visible/interactable (logging on `E`).
- New classes adhere to OO basics: encapsulation, small methods, clear APIs.
- All public types/methods have Javadoc.
- UML and design note committed under `docs/`.

## Common Issues & Fixes
- Gradle/JDK mismatch
  - Use JDK `17` (with Gradle `7.6.3`) or upgrade to Gradle `8.8+` for JDK `21`.
- Blue screen only
  - Add a small voxel strip/plate in `generateLayers()` near spawn to confirm visibility.
- Mac display error
  - Add `-XstartOnFirstThread` to VM options.
- Interact key does nothing
  - Ensure `InputAppState` is attached and mouse is captured (`Tab`).

## Next (preview of Week 2)
- Extend hierarchies and add ≥2 new `VoxelBlockType`s with distinct visuals and at least one functional difference.
- Update UML to reflect new types and overridden behavior.