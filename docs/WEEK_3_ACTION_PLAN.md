# Semana 3 — Sistemas (Inventário, Crafting, NPCs), Geração v1

## Objetivo
- Definir interfaces de capacidades, implementar inventário + crafting mínimos (≥2 receitas), adicionar NPCs básicos (≥1 aliado, ≥1 inimigo) com `updateAI()`, e começar geração v1 determinística.

## Tarefas
- Interfaces: `Interactable`, `HasAI`, `Tickable` com contratos mínimos e engine‑neutral.
- Inventário: capacidade ≥6, operações add/remove, empilhamento e exceções específicas.
- Crafting: `Recipe` + serviço de crafting com validações e 2 receitas.
- NPCs: `Ally` e `Enemy` com `updateAI(ctx)` polimórfico (seguir/perseguir/atacar/curar/logs).
- Geração v1: altura/ruído seedado que coloca ≥2 tipos.
- Integração: ciclo que chama `updateAI()` (AppState dedicado) e HUD para feedback.
- Documentação: Javadoc, UML e nota curta.

## Entregáveis
- Interfaces e base abstrata (se necessário); inventário funcional; crafting ≥2 receitas; NPCs básicos; geração v1.

## Critérios de aceitação
- Operações de inventário e crafting validadas; NPCs mostram transições em logs; geração coloca tipos com a mesma seed.

