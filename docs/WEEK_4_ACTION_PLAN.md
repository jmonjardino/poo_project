# Semana 4 — Polimorfismo, IA e Erros

## Objetivo
- Evoluir IA com máquinas de estados/estratégias, diferenciar aliados/inimigos, robustecer crafting com erros e iniciar persistência.

## Tarefas
- Implementar estados (`Idle`, `Patrol`, `Chase`, `Attack`) e transições determinísticas.
- Aliados: seguir, trocar, curar; Inimigos: patrulhar, perseguir, atacar.
- Expandir crafting para ≥4 receitas; lançar exceções claras (`InvalidRecipe`, `InsufficientResources`).
- Persistência v1: guardar posição/inventário/seed (formato simples JSON/TXT).
- Geração v2: parâmetros/biomas simples; manter determinismo.
- Testes/harness para contratos e polimorfismo.

## Entregáveis
- Demo de estados; crafting com ≥4 receitas; primeiros saves/loads.

