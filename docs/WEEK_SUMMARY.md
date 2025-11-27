# Sumário Semanal — IscteCraft (versão compacta)

## Semana 1 — Fundamentos

- Jogo.java
  - Liga AppStates principais; regista `assets`; inicia física e viewport.
- InputAppState.java
  - Mapeia teclas (WASD, Espaço, E, L, R, Tab) e expõe `consume*`.
- WorldAppState.java
  - Cria voxel world, luzes, física; calcula spawn; breaking de vóxeis.
- RenderAppState.java
  - Nó `GameObjects`; sincroniza posições; cria geometrias de `Player/Enemy/Ally/Item`.

## Semana 2 — HUD, Interações, Inventário

- HudAppState.java
  - Crosshair; texto de inventário com nomes e quantidades; `statusText` com TTL.
  - Tipos: `100 Axe`, `200 Wood`, `210 Planks`, `300 Workbench`.
- InteractionAppState.java
  - Raycast para itens; recolhe com `E`; mapeia tipo do item para código.
- StackingInventory.java
  - Slots com pilhas; `capacity`, `getItemTypeAt`, `getCountAt`, `add`, `remove`, `removeAt`, `hasSpaceFor`, `getCount`.
- RenderAppState.java
  - `Wood` usa textura `oak_planks.png` (Nearest, Repeat).

## Semana 3 — Crafting V1

- InputAppState.java
  - Adiciona `C` (menu), `1` e `2` (craft rápido).
- Recipe.java
  - Estrutura imutável: `name`, `inputs{tipo→qty}`, `outputType`, `outputQty`.
- RecipeBook.java
  - Receitas: `Wood x2 → Planks x4`; `Planks x4 → Workbench x1`.
- CraftingService.java
  - `canCraft(inv, recipe)` valida; `craft(inv, recipe)` atómico com rollback.
- CraftingAppState.java
  - Toggle do menu (`C`), lista receitas, `tryCraft` integra com HUD.
- Jogo.java
  - Anexa `HudAppState` e `CraftingAppState`; spawn de `Axe` e `Wood` perto do jogador.

## Classes e APIs

- GameObject.java
  - Guarda identidade (`name`) e posição lógica (`position`).
  - `setPosition` (Vec3/x,y,z) atualiza o estado; render/física são responsabilidade dos AppStates.
- Item.java
  - Base para itens interativos; `onInteract()` é o ponto de extensão chamado quando o raio atinge o objeto e o jogador pressiona `E`.
- CollectibleItem.java
  - `itemValue` e `collected` definem valor e estado de recolha.
  - `onInteract` marca idempotentemente o item como recolhido e regista uma mensagem; útil para testes de fluxo.
- BreakableItem.java
  - Modelo de ferramenta com `durability/maxDurability`, `toolType/tier`, `efficiency`.
  - `onInteract` informa estado atual; a gestão de durabilidade será usada em ações de vóxel futuras.
- Wood.java
  - `woodType` distingue a madeira; `onInteract` regista informação para depuração.
- Character.java
  - Saúde com limites (`health/maxHealth`) e operações: `setHealth` corta valores para [0,max], `isAlive`, `takeDamage`, `heal`.
- Player.java
  - Possui `StackingInventory` com capacidade 8 e limite de pilha 64; `getInventory` expõe o modelo para crafting/pickup.
- StackingInventory.java
  - Slots internos (`itemType`, `count`); `capacity` devolve nº de slots.
  - `getItemTypeAt/getCountAt` expõe estado por índice.
  - `add(itemType, qty)` preenche pilhas existentes e depois slots vazios; lança `InventoryFullException` se sobrar.
  - `remove(itemType, qty)` verifica disponibilidade total e retira distribuindo; liberta slot quando `count==0`.
  - `removeAt(slotIndex, qty)` remove de um slot específico com validação de índices.
  - `hasSpaceFor(itemType, qty)` simula inserção (pilhas compatíveis → slots vazios) para garantir capacidade antes de craft.
  - `getCount(itemType)` soma total por tipo; `isFull/getFreeSlots` fornecem métricas de ocupação.
- InputAppState.java
  - `initialize` mapeia teclas e regista listeners; `cleanup` remove mappings.
  - `onAction` atualiza flags (movimento, saltar, interagir, craft) e respeita `mouseCaptured` para ações como `Break/Interact`.
  - `onAnalog` acumula `mouseDX/DY` enquanto capturado.
  - `consume*` devolvem e limpam pedidos (one‑shot) para evitar repetição.
  - `getMovementXZ` devolve vetor XZ de intenção em coordenadas de jogo; `isSprinting` lê estado do sprint.
  - `setMouseCaptured` alterna visibilidade do cursor e zera deltas para evitar saltos.
- PlayerAppState.java
  - `initialize` cria o nó do jogador, componente de física (`BetterCharacterControl`), luz local e câmara; obtém spawn recomendado do mundo.
  - `update` gere respawn, imprime coordenadas, pausa controlo se rato solto, aplica mouse‑look (yaw/pitch), deslocação com sprint, salto apenas no chão, e segue a posição com a luz.
  - `respawn` warpa para spawn e reseta pitch; `computeWorldMove` converte input XZ no mundo pela orientação (yaw).
  - `applyViewToCamera` posiciona a câmara na cabeça (`eyeHeight`); `refreshPhysics` reanexa o controlo após rebuilds do mundo; `cleanup` remove controlos/luzes.
- WorldAppState.java
  - `initialize` constrói nó do mundo, luz ambiente/direcional, voxel world (gera, constrói malhas, física) e spawn.
  - `update` quebra vóxeis quando solicitado (raycast curto), reconstrói chunks e refresca física do jogador; alterna debug de rendering.
  - `getRecommendedSpawnPosition` e `getVoxelWorld` expõem estado; `cleanup` remove nós e controlos de física.
- RenderAppState.java
  - Mantém `gameNode` e `instances` para Spatials de cada GameObject.
  - `update` cria Spatials em falta, sincroniza posições e limpa removidos.
  - `createSpatialFor` mapeia tipos para geometrias e materiais; `Wood` usa textura `oak_planks.png` com material `Lighting`.
  - `colored` cria materiais com cores base e brilho suave; `cleanup` desfaz a cena de jogo.
- InteractionAppState.java
  - `update` lança um raio a partir da câmara; se colidir com um item, determina tipo e adiciona ao inventário, removendo do `registry`; caso contrário, imprime vóxel atingido.
  - `findRegistered` sobe na hierarquia do `Spatial` até encontrar o objeto registado; `itemTypeFor` resolve classes para códigos (ex.: `BreakableItem→100`, `Wood→200`).
- HudAppState.java
  - `initialize` cria `crosshair`, `inventoryText` e `statusText`.
  - `inventorySummary` agrega quantidades por tipo e mostra nomes amigáveis; `nameForType` cobre `Axe/Wood/Planks/Workbench`.
  - `showStatus` define mensagem temporária e TTL; `update` atualiza mira, texto e expira mensagens.
  - `cleanup` remove elementos do HUD.
- CraftingAppState.java
  - `update` alterna menu com `C` e, quando aberto, processa `1/2` para receitas rápidas.
  - `listRecipes` constrói linha compacta com nomes; `tryCraft` valida/aplica via `CraftingService` e informa no HUD.
- Recipe.java
  - Estrutura imutável: entradas ordenadas (`LinkedHashMap`), tipo/quantidade de saída e nome para UI.
- RecipeBook.java
  - Catálogo estático pequeno (V1) com duas receitas; `getAll` e `get` facilitam UI e execução.
- CraftingService.java
  - `canCraft` verifica recursos por tipo e espaço para saída; `craft` aplica remoção e adição com rollback em caso de exceção, devolvendo sucesso/falha.

## Uso Rápido

- Apanhar: `E`.
- Crafting: `C` abre; `1` Planks; `2` Workbench.
- HUD: inventário no topo; mensagens de crafting abaixo.
