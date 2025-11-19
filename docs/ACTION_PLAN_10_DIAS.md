# Plano de Ação — 10 Dias para IscteCraft

Objetivo: concluir um jogo funcional e avaliável segundo o PRD, cumprindo os mínimos: ≥6 tipos de bloco, ≥5 GameObjects (≥3 com interação via `E`), inventário + crafting (≥4 receitas, com erros), NPCs (≥2 aliados, ≥2 inimigos com IA polimórfica), geração determinística, persistência (Save/Load + Highscores), documentação.

## Dia 1 — Setup e Fundamentos
- Ambiente
  - Instalar/selecionar JDK suportada (17–21) ou atualizar Gradle wrapper para 8.8.
  - Verificar build/run: `./gradlew run`.
- Leitura e exploração
  - Ler `docs/ENGINE.md`/`ENGINE_PT.md`, entender limites: camada de jogo sem jME (apenas estado/lógica).
  - Rodar o executável, confirmar controlos e interação básica.
- Base de domínio
  - Criar 2–3 classes neutras (`Item`, `Terrain`, `Character` concretos) com construtores, getters/setters, `toString()`.
  - Esboço UML inicial (classes e relações).
- Entregáveis
  - Projeto a compilar e correr.
  - UML inicial e Javadoc base nas novas classes.

## Dia 2 — Vóxeis: Paleta e Propriedades
- Paleta
  - Adicionar ≥2 novos tipos de bloco (ex.: `Dirt`, `Wood`) com materiais distintos e pelo menos 1 diferença funcional (ex.: fricção, solidez, dureza).
- World API
  - Criar helpers para set/get de blocos, marcar chunks “dirty” e rebuild meshes/physics após alterações.
  - Garantir `getBlock` devolve `AIR` fora de limites.
- Testes práticos
  - Colocar blocos de teste no mundo e confirmar rendering/physics.
- Entregáveis
  - Paleta com ≥4 tipos (contando `Air` e `Stone`).
  - Capturas do mundo com blocos visíveis e diferenças funcionais demonstráveis.

## Dia 3 — Geração de Terreno v1 (Determinística)
- Algoritmo
  - Implementar heightmap/ruído simples mapeando alturas a tipos (ex.: areia baixa, terra média, pedra alta).
  - Parametrizar seed e constantes; guardar seed/params para reprodutibilidade.
- Spawn e performance
  - Garantir `getRecommendedSpawn()` coloca o jogador em zona válida.
  - Medir tempo de geração; otimizar loops básicos.
- Entregáveis
  - Mundo gerado com múltiplos tipos e repetível pela mesma seed.
  - Documentar parâmetros e mapeamentos.

## Dia 4 — Objetos e Interações
- GameObjects
  - Criar ≥5 objetos (mistura `Item/Prop/Character`), com responsabilidades claras e encapsulação.
- Interações via `E`
  - Implementar `Item.onInteract()` em ≥3 objetos; usar `InteractionAppState` para roteamento.
  - Logar efeitos observáveis (estado/GUI/logs).
- Registo
  - Usar `GameRegistry` para render/interaction index; validar que picking em itens funciona.
- Entregáveis
  - ≥3 interações funcionais com feedback.
  - Javadoc nas APIs públicas de objetos.

## Dia 5 — Inventário
- Estrutura
  - Implementar `Inventory` com ≥6 slots, stacks e limites.
  - Operações: adicionar/remover/inspecionar; validações de capacidade.
- Erros
  - Exceções específicas (ex.: `InventoryFullException`) e mensagens claras.
- Testes
  - Pequenos testes/harness (sem engine) para operações de inventário.
- Entregáveis
  - Inventário funcional, demonstrado em logs/GUI simples.

## Dia 6 — Crafting (≥4 Receitas)
- Modelação
  - `Recipe`, `CraftingSystem` com validações (itens suficientes, receita válida).
- Erros
  - `InvalidRecipeException`, `InsufficientResourcesException`; feedback em logs/GUI.
- Conteúdo
  - Definir ≥4 receitas úteis e coerentes com o loop de jogo.
- Testes
  - Harness para receitas (entrada → saída; erros).
- Entregáveis
  - Crafting a funcionar com erros tratados e documentação das receitas.

## Dia 7 — NPCs com IA Polimórfica
- Hierarquia
  - `Ally` e `Enemy` (≥2 de cada), com `updateAI()` polimórfico.
- IA
  - Máquina de estados simples (ex.: `Idle`, `Patrol`, `Chase`, `Attack`), transições por distância/linha de visão.
  - Aliados: seguir, trocar, curar; Inimigos: patrulhar, perseguir, atacar.
- Integração
  - Atualizar loop do mundo para chamar `updateAI()`; usar apenas dados e regras (sem jME na camada de jogo).
- Entregáveis
  - Demonstração visual e logs do comportamento distinto de aliados vs. inimigos.

## Dia 8 — Persistência: Save/Load + Highscores
- Save/Load
  - Formato (JSON/txt). Guardar posição do jogador, inventário, seed e parâmetros de geração, estados críticos de objetos/NPCs.
  - Robustez: defaults seguros em falta/corrupção; `SaveLoadException`.
- Highscores
  - Métrica definida (tempo/pontos/blocos/minas/inimigos derrotados).
  - Estrutura com fila (queue) para entradas; leitura/escrita; ordenação/limite.
- Entregáveis
  - Botões/atalhos para salvar/carregar; ficheiros gerados; leaderboard persistente.

## Dia 9 — Integração, UX e QA
- Loop de jogo
  - Explorar → recolher → construir → progredir; ligar crafting com uso de materiais recolhidos.
- Interações com vóxeis
  - Usar `pickFirstSolid` que devolve `hit` (`cell`, `normal`, `distance`).
  - Implementar “place block”: posição adjacente com `normal`:
    - `adjacent = (cell.x + (int) normal.x, cell.y + (int) normal.y, cell.z + (int) normal.z)`
- Qualidade
  - Balancear materiais/receitas/dificuldade; verificar nomes/visibilidade/duplicações.
  - Corrigir bugs; otimizar geração e UX (prompts/HUD).
- Entregáveis
  - Build estável e jogável de ponta a ponta; checklist de bugs resolvidos.

## Dia 10 — Polimento e Documentação Final
- Documentação
  - Javadoc público completo.
  - UML final (comparar com inicial, justificar mudanças).
  - Nota arquitetural (1–2 páginas): decisões, trade-offs, exceções, persistência, IA, geração.
  - Registo de uso de IA conforme política (contexto/prompt/output/decisão/reflexão).
- Demo
  - Preparar walkthrough 3–5 min: modelo de objetos, tipos de bloco, crafting, NPCs, geração, persistência e demo.
- Entregáveis
  - Pacote final pronto para avaliação; vídeo/capturas; ficheiros de persistência e leaderboard exemplares.

## Critérios de Conclusão (Checklist)
- Vóxeis: ≥6 tipos totais, visuais distintos e pelo menos 1 diferença funcional cada.
- Objetos/Interações: ≥5 objetos, ≥3 interações via `E` com efeitos observáveis.
- Inventário + Crafting: inventário (≥6 slots), ≥4 receitas com validações e erros tratados.
- NPCs: ≥2 aliados e ≥2 inimigos com IA polimórfica e estados distintos.
- Geração: determinística (seed + parâmetros), documentada e coerente.
- Persistência: Save/Load robusto + Highscores com fila; exceções específicas e feedback.
- Código: OO limpo, encapsulamento, coleções adequadas, métodos pequenos e focados, sem jME na camada de jogo.
- Documentação: Javadoc, UML inicial→final, nota arquitetural, registo de uso de IA.
- Jogabilidade: experiência completa com loop funcional e estabilidade.

## Riscos e Mitigações
- Incompatibilidade Gradle/JDK: ajustar JDK (17–21) ou atualizar wrapper para 8.8.
- Perf e memória na geração: chunking e rebuild seletivo; medir e otimizar laços quentes.
- Polimorfismo confuso: preferir interfaces e estados bem nomeados; testes de comportamento.
- Persistência frágil: try/catch com defaults, validações de schema, mensagens úteis ao utilizador.

## Notas de Implementação (Sem Código)
- Camada de jogo não importa jME; motor renderiza/colide/interage.
- `pickFirstSolid(cam, reach)` devolve `Hit`: usar `cell`, `normal`, `distance` para “break” e “place block”.
- Exceções devem propagar e ser registadas (logs/GUI).
- Geração deve ser reprodutível; evitar aleatório sem seed.