# Week 6 — Polishing & Demo Final

Goal: Use this buffer week to polish the experience, fix bugs, optimize performance/UX, and submit the final deliverables.

## Prerequisites
- Week 5 complete: Project is integrated, functional, and documented.
- Walkthrough video is planned/recorded.

## Environment Setup
- IntelliJ
  - Final cleanup: Remove unused imports, format code (Google Style or consistent style).
- Run Configuration
  - Main class: `jogo.Jogo`.

## Week 6 Tasks
1) Final Polish & Bug Fixes
- **UX**: Improve HudAppState (clearer fonts, better colors). Add prompts ("Press E to Interact").
- **Bugs**: Stress test the game.
  - Does saving/loading twice in a row work?
  - Do recipe errors crash the game?
  - Does falling off the world crash the game?
- **Optimization**: If generation is slow, optimize. If FPS is low, check geometry count.

2) Final Deliverables Assembly
- **Code**: Clean zip or repo link.
- **Javadoc**: Generated and readable.
- **Persistence**: Verify the "saved" files work on another machine/location if possible.
- **UML & Architecture**: Final versions saved as PDF/PNG.
- **AI Log**: Ensure `docs/AI_LOG.md` (or similar) is up to date with prompts/decisions.

3) Submission Check
- Verify against rubric:
  - [ ] A: Terrain (≥4 types + properties).
  - [ ] B: Objects (≥5 types, ≥3 interactions).
  - [ ] C: Crafting (≥4 recipes, Inventory).
  - [ ] D: NPCs (≥2 types, Polymorphism).
  - [ ] E: Generation (Parameters, Seed).
  - [ ] F: Persistence (Save/Load, Highscore).

## Deliverables (Final)
- Complete Project Source.
- Runnable distribution (if req).
- Documentation (Javadoc, UML, Architecture, AI Log).
- Walkthrough Video (3-5 min).

## Acceptance Criteria (Self‑check)
- Project unzip and run works immediately.
- No critical bugs found during "Happy Path" demo.
- All Rubric Pillars (A-F) are visibly implemented.

## Common Issues & Fixes
- **Last Minute Features**: AVOID adding big features now. Focus on stability.
- **Path Issues**: Verify file paths again (don't use hardcoded `C:/Users/You`).
- **Submission Format**: Check Moodle/Teacher requirements for file naming.

## Next
- Submit and Celebrate!
