## Projeto – IscteCraft

### 1 Introdução

```
O objetivo é conceber e implementar um mini‑jogo em 3D , em primeira pessoa, inspirado
em mecânicas tipo Minecraft , ao qual chamaremos IscteCraft. O projeto assenta num
motor de apoio já fornecido (camada “engine”) e numa camada de jogo que cada grupo
implementa em Java, neutra em relação ao motor.
```
```
O mundo é voxel ‑ based (blocos). No arranque existe apenas um tipo de material
disponível ao estudante: Pedra (Stone). O motor já disponibiliza movimento básico
(WASD, salto, câmara/mira) e expõe interações (tecla E ) e uma ação de clique do rato
(partir bloco) para alavancar as vossas mecânicas. As partes gráficas, físicas e de input são
geridas pelo motor; vocês concentram‑se no modelo de objetos, regras de jogo,
persistência e erros.
```
### Programação Orientada a Objetos

### 2025/


### 2 Objetivos e Aprendizagem

```
Para além dos pilares técnicos de POO, o projeto implica fazer um jogo : pensar
e implementar regras e mecânicas simples , articular sistemas (inventário,
crafting, NPCs) para formar um loop de jogo (explorar → recolher → construir →
progredir), e testar e divertirem-se a iterar no equilíbrio e na experiência.
Valorizamos que o resultado seja jogável e divertido , ainda que pequeno, e que
as decisões de design estejam alinhadas com os princípios de programação
orientada a objetos.
```
```
No final, deverão demonstrar domínio de:
```
- **Classes e objetos** , encapsulamento e APIs limpas.
- **Hierarquias** sensatas, **classes abstratas** e **interfaces**.
- **Polimorfismo** (sobrescrita, programação contra abstrações).
- **Coleções** (gestão de objetos do mundo, inventário, receitas, etc.).
- **Exceções** (lançamento/propagação e tratamento) para erros de jogo/IO.
- **Leitura e escrita de ficheiros** para **Save/Load** e **Highscores** (leaderboard).

### 3 Ambiente e Motor (Resumo)

```
O motor (shell sobre jMonkeyEngine) disponibiliza AppStates para input, mundo
de vóxeis, jogador em primeira pessoa, rendering de placeholders e
picking/raycast de interações. A vossa camada define classes puras de jogo (sem
imports do motor) em jogo.gameobject.* (ex.: Item , Character , Terrain ). O motor vai
automaticamente mapear estes objetos para visuais genéricos.
```

```
Controlos por omissão (executável)
```
- Capturar/soltar rato: **Tab**
- Mover: **W/A/S/D**
- **Saltar** : Espaço
- **Interagir** com objetos (raycast): **E** → chama **Item.onInteract()**
- **Partir bloco** (voxel): **Botão esquerdo do rato**
- Respawn: **R**
- Alternar modos de rendering para depuração: **L**
- Sair: **ESC**

```
Vóxeis : paleta por omissão com Air e Stone (id 0 e 1). Podem registar novos tipos
de bloco nas semanas seguintes (madeira, terra, areia, etc.).
```
```
Regra de ouro: a camada de jogo não usa tipos do motor (jME). Guardem apenas
estado e lógica ; deixem o motor renderizar/colidir/interagir.
```
### 4 Escopo do Jogo (Funcionalidades Mínimas)

```
Conforme o plano semanal (Sec. 7), ao terminar o projeto o vosso IscteCraft deve
incluir:
```
1. **Terreno com múltiplos tipos de bloco** (≥ 6 no total, contando Air e Stone)
    com diferenças **visuais** e pelo menos uma **diferença funcional** (ex.:
    atrito/velocidade, queda, sem interação, minar de forma mais lenta, etc.).
2. **Objetos do mundo** (≥ 5 **GameObjects** distintos e ≥ 3 interativos via **E** ) com
    responsabilidades claras e encapsuladas.
3. **Inventário + Crafting** com **≥ 4 receitas** válidas + tratamento de erros
    (receitas inválidas, recursos insuficientes, etc.).


4. **NPCs** : ≥ 2 aliados e ≥ 2 inimigos com **IA simples** e polimórfica (ex.: updateAI()
    com estados ou estratégias).
5. **Geração de terreno** rudimentar e **determinística** (para a mesma seed),
    usando vários tipos de bloco e parâmetros documentados.
6. **Persistência** : **Save/Load** do estado essencial (posição/itens/seed, etc.) e
    **Highscore** (ex.: tempo até objetivo, blocos recolhidos, inimigos derrotados).
    Formato livre e bem tratado com **exceções**.
7. **Documentação** : Javadoc público, UML (inicial→final), nota arquitectural (1– 2
    páginas) e **registo de uso de IA**.

### 5 Regras e Restrições

- **Não alterar** o código do motor salvo indicação expressa. Estendam via
    objetos de jogo e APIs expostas.
- Classes de jogo **puras** (sem jME). Motores/renderização/physics só no lado
    engine.
- **Interação E** : preferencialmente dirigida a objetos Item/Prop. Voxel picking
    está exposto; podem usá-lo para colocar/alternar blocos.
- **Clique esquerdo** já destrói/afeta vóxeis; podem ligar isto a “ferramentas” no
    vosso modelo (ex.: picareta melhora eficiência).
- **Tratamento de erros** : todas as operações de IO e crafting devem
    lançar/propagar exceções específicas e **registar** feedback (logs/GUI).
- **Ficheiros** : escolhidos por vocês (txt/json/binário). Expliquem decisões e
    garantam robustez (faltas/corrupção → mensagens úteis, defaults seguros).


### 6 Critérios Objetivos (Rubrica por Pilares)

```
A) Terreno / Tipos de Vóxel ( 15 %)
```
- ≥ 4 novos tipos de bloco (além de Air e Stone); cada tipo com **visual**
    distinto e **propriedade funcional** (ex.: Sólido vs. Semissólido / Escorregadio
    / Queimadura / Luz / Etc).
- Registo correto na paleta e **uso efetivo** no mundo.
**B) Objetos e Interações ( 15 %)**
- ≥ 5 GameObjects distintos (Item/Prop/Character) com APIs claras e
documentação.
- ≥ 3 interações via **E** com **efeitos observáveis** (estado, logs, alterações no
mundo/inventário).
**C) Inventário e Crafting ( 15 %)**
- Inventário com capacidade realista (≥ 6 slots ou justificado).
- ≥ 4 receitas documentadas; validações + erros bem tratados.
**D) NPCs ( 15 %)**
- ≥ 2 aliados (seguir, trocar, curar, dialogar simples) e ≥ 2 inimigos
(patrulhar/perseguir/atacar) com **polimorfismo**.
**E) Geração de Terreno ( 15 %)**
- Algoritmo simples (heightmap/ruído/regras) que distribui tipos de bloco;
parâmetros e seed documentados; determinístico.
**F) Save/Load & Highscores ( 15 %)**
- Funcionalidades de salvar e carregar alterações, objetos, estados, etc do
mundo.
- Mecanismo de high score com base nalgum critério definido por vocês (por
tempo, por pontuação, etc) que utilize filas.


### 7 Execução do Trabalho e Plano Sugerido de Projeto

```
É encorajada a discussão entre colegas sobre o trabalho, mas estritamente proibida
a troca de código. A partilha de código em trabalhos diferentes será penalizada. Serão
usados meios de verificação de plágio e os trabalhos plagiados serão comunicados à
organização do curso.
Peça regularmente ao docente para que reveja consigo o trabalho, quer durante as
aulas, quer no decorrer do trabalho autónomo. Desse modo, pode evitar algumas más
opções iniciais que podem aumentar muito a quantidade de trabalho.
```
```
Sugestão de plano por semana:
```
```
Semana 1 — Fundamentos
```
- Ler a documentação do motor, correr o projeto. Criar 2 – 3 classes concretas
    neutras (ex.: Porta, Flor).
- Registar 1 objeto no GameRegistry; validar rendering/picking. **Só Pedra** no
    mundo.
- Entregáveis: classes + Javadoc, modelação inicial, nota curta de
    responsabilidades.
**Semana 2 — Hierarquias e Vóxeis**
- Estender hierarquia (GameObject→Character/Terrain/Item).
- Adicionar **≥ 2 novos tipos de bloco** (ex.: Terra, Madeira) com propriedades
funcionais.
- Atualizar UML; demo curta (screenshot/vídeo) a mostrar os tipos no
mundo.
**Semana 3 — Sistemas (Inventário, Crafting, NPCs), Geração v**
- Definir **interfaces** de capacidades (ex.: Interactable, Usable, Craftable).
- **Inventário+Crafting** : implementar fluxo mínimo; **≥ 2 receitas** esta semana.
- **NPCs** : pelo menos 1 aliado e 1 inimigo com updateAI() básico.
- **Geração v1** : altura/ruído simples que coloca ≥ 2 tipos de bloco.
**Semana 4 — Polimorfismo, IA e Erros**
- Pequena **máquina de estados** para inimigos; comportamento distinto
para aliados.


- **Crafting** : chegar a **≥ 4 receitas** ; começar **Persistência** (Save/Load +
    Highscore). Tratar erros de IO com exceções.
- **Geração v2** : parâmetros/biomas simples; manter determinismo.
- Adicionar testes/harness pequenos para contratos/polimorfismo.
**Semana 5 — Integração e Qualidade**
- Integrar pilares; balancear materiais/receitas/dificuldade.
- Continuação do desenvolvimento de qualquer funcionalidade pendente.
- Verificar a visibilidade, duplicação, nomes, etc de métodos e classes;
completar documentação.
- Preparar **_walkthrough_** de **3 – 5 min** : modelo de objetos, tipos de bloco,
crafting, NPCs, geração, persistência e demo.
**Semana 6 — Polimento e Demo Final**
- _Buffer_ para polir, corrigir bugs, otimizar geração e UX (prompts, HUD).

### 8 Entregáveis

- Código Java **compilável e a correr**.
- Classes de jogo **neutras em relação ao motor** , com **Javadoc** público
    completo.
- **Persistência** (Save/Load + Highscore) funcional e robusta.
- **UML** (inicial→final) e **nota arquitetural** (1–2 páginas) com decisões e
    trade-offs.
- **Registo de uso de IA** (ver política abaixo).

### 9 Avaliação

```
O trabalho será classificado de 0 a 20. A avaliação será feita do seguinte modo:
Serão excluídos (não aceites para discussão e classificados com zero valores)
os trabalhos que:
```
- Apresentem erros de sintaxe;
- Apresentem um funcionamento muito limitado, sem que componentes
    essenciais do projeto estejam implementadas. Por exemplo, se a gestão


```
de colisões dos objetos não está implementada é considerado que o
trabalho não está utilizável;
```
- Não é utilizada herança (por exemplo no comportamento e
    características das criaturas) e coleções (por exemplo para guardar os
    vários objetos presentes numa cena);
- O comportamento de todo do programa é concentrado num método,
    numa classe ou não demonstrem que sabem usar corretamente classes
    e objetos para modularizar o código.

Os seguintes critérios serão utilizados para a avaliação dos trabalhos:

- Utilização correta de classes, herança e coleções;
- O comportamento do programa não deverá estar concentrado num ou dois
    métodos / classes;
- As classes deverão ser desenhadas de acordo com as boas práticas;
- Deverão ser utilizadas as coleções adequadas para cada situação;
- Deverá existir uma boa proteção dos atributos de cada classe;
- Métodos deverão ter um tamanho razoável e um único objetivo;
- Deve ser feita uma utilização adequada das exceções para tratar erros de
    programação e situações de exceção;
- Serão valorizados elementos extra que contribuam para a melhoria do
    projeto;
- **Em suma** : correção e diversidade de tipos de bloco/NPCs; bom design de
    POO; coesão; encapsulamento; uso adequado de coleções; métodos
    pequenos e focados; exceções bem usadas; jogo jogável de ponta a ponta;
    estrutura que escala; elementos extra pertinentes.


### 10 Política de Uso de IA (Obrigatória)

```
Permitido (com documentação):
```
- Geração de _boilerplate_ : getters/setters, equals/hashCode/toString, DTOs
    simples.
- Esclarecimentos sobre sintaxe de linguagem/biblioteca.
- _Brainstorming_ de _edge cases_ ou cenários de teste.
- Rascunhos de comentários/Javadoc que depois revêm e editam.
- Algoritmos específicos para poupar trabalho.
**Não permitido** :
- Prompts “guarda-chuva” (ex.: _"Constrói todo o meu projeto/jogo com base
neste enunciado"_ ).
- Colar código gerado sem revisão/compreensão.
- Substituir o vosso esforço de **design** (ex.: pedir hierarquia/contratos ao LLM).
- Usar LLMs para código que depois **não consigam explicar/defender**.
**Requisitos de documentação (obrigatório):**
- Manter um **registo contínuo** num documento com:
o **Contexto** (o que estavas a trabalhar, e porquê precisavas de ajuda)
o **Prompt** (texto exato)
o **Output** (apenas a parte relevante)
o **Decisão** (usar/alterar/rejeitar)
o **Reflexão** (1–3 frases sobre o que aprendeste)
- Para casos de “completar a linha”, deves conseguir explicar o que foi
adicionado e documentar se for não-trivial.
**Barra de qualidade**
- A responsabilidade pelo **código e correção** é vossa. Rever, testar e
reescrever.
- O output deve respeitar o vosso **design** e convenções de código.
- **Devem ser capazes de explicar qualquer código assistido por IA**.


**Integridade académica**

- Falta de documentação do uso de IA pode ser tratada como **violação de**
    **integridade**.
- Conteúdo gerado por IA que não consigam explicar será **desqualificado** e
    a nota do projeto será **0**.

## Isto é uma versão preliminar do enunciado, pelo que poderá

## haver alterações!

# Bom trabalho!


