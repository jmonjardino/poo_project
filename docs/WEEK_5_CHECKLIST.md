# Week 5 — Integration & Quality

Goal: Integrate all game pillars (World, Player, NPCs, Systems), balance gameplay (resources, difficulty), verify code quality (Javadoc, Architecture), and prepare the project walkthrough.

## Prerequisites
- Week 4 complete: AI State Machines, Expanded Crafting, Persistence functionality.
- Feature set is effectively feature-complete according to requirements.

## Environment Setup
- IntelliJ
  - Run full project inspection/analyze code to find dead code or issues.
- Run Configuration
  - Main class: `jogo.Jogo`.

## Week 5 Tasks
1) Integration & Balancing
- Ensure all systems play together: broken blocks drop items -> items crafted -> tools used -> NPCs interact.
- Balance:
  - Recipe costs vs Resource scarcity.
  - Enemy difficulty (Health/Damage) vs Player capability.
  - Day/Night cycle speed (if implemented) or World scale.

2) Code Quality Review
- **Refactor**: Remove duplication, improve variable names, simplify complex methods.
- **Documentation**:
  - Complete Javadoc for all public methods/classes.
  - Ensure Architecture Note (1-2 pages) covers decisions.
- **Visibility**: Ensure private/protected/public are used correctly.

3) Functional Polish
- Complete any "Pending" functionality from previous weeks.
- Ensure no "Happy Path" crashes.
- Verify Exception Handling prints friendly messages, not stack traces to UI.

4) Walkthrough Preparation
- Script/Bullet points for a 3-5 minute video/demo.
- Scenes to capture:
  - Object Model (Code view).
  - Block Types (Visual demo).
  - Crafting (UI demo).
  - NPCs (AI demo).
  - Generation (World demo).
  - Persistence (Save/Load demo).

## Deliverables (Week 5)
- Integrated and Balanced Game.
- Refactored Codebase (Clean Code).
- Complete Javadoc and Architecture Documentation.
- Walkthrough Script/Video (3-5 min).

## Acceptance Criteria (Self‑check)
- Game loop is endless and fun (Explore -> Gather -> Craft -> Survive).
- Code passes linter checks (no critical warnings).
- UML matches final code structure.
- Walkthrough covers all required rubric points (A-F).

## Common Issues & Fixes
- **Spaghetti Code**: Use Extract Method to break up `simpleUpdate` or big logic blocks.
- **Missing Docs**: Check `public` methods; unnecessary to document trivial getters/setters if clear, but document logic.
- **Performance**: If frame rate drops, check for object creation loops or heavy physics calls per frame.

## Next (preview of Week 6)
- Buffer week.
- Minor bug fixes.
- UX Improvements (Prompts, cleaner HUD).
- Final Polish.
