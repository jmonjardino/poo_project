package jogo.gameobject; // Declara o package onde a classe reside

/** Inventário com pilhas por slot e validações determinísticas. */
public final class StackingInventory { // Classe final para evitar herança acidental
    /** Exceção: inventário sem espaço suficiente. */
    public static final class InventoryFullException extends RuntimeException {
        public InventoryFullException(String m) {
            super(m);
        }
    } // Define exceção específica

    /** Exceção: item não encontrado ou quantidade insuficiente. */
    public static final class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(String m) {
            super(m);
        }
    } // Define exceção específica

    /** Exceção: quantidade inválida (≤0 ou fora dos limites). */
    public static final class InvalidQuantityException extends RuntimeException {
        public InvalidQuantityException(String m) {
            super(m);
        }
    } // Define exceção específica

    /** Exceção: tentativa de exceder limite de pilha. */
    public static final class StackLimitExceededException extends RuntimeException {
        public StackLimitExceededException(String m) {
            super(m);
        }
    } // Define exceção específica

    /** Estrutura interna de slot: guarda tipo de item e contagem. */
    private static final class Slot { // Slot interno
        int itemType; // Identificador de tipo de item (>0 significa ocupado)
        int count; // Quantidade no slot (0 significa vazio)

        Slot() {
            this.itemType = 0;
            this.count = 0;
        } // Inicializa como vazio

        boolean isEmpty() {
            return count == 0;
        } // Verifica se o slot está vazio

        int space(int stackLimit) {
            return isEmpty() ? stackLimit : (stackLimit - count);
        } // Espaço restante no slot
    }

    private final Slot[] slots; // Array de slots do inventário
    private final int stackLimit; // Limite por pilha (quantidade máxima por slot)

    /** Construtor com capacidade e limite por pilha. */
    public StackingInventory(int capacity, int stackLimit) { // Cria inventário
        if (capacity <= 0)
            throw new IllegalArgumentException("capacidade deve ser > 0"); // Valida capacidade
        if (stackLimit <= 0)
            throw new IllegalArgumentException("limite de pilha deve ser > 0"); // Valida limite de pilha
        this.stackLimit = stackLimit; // Guarda limite
        this.slots = new Slot[capacity]; // Aloca array de slots
        for (int i = 0; i < capacity; i++)
            slots[i] = new Slot(); // Inicializa cada slot
    }

    /** Devolve a capacidade total (número de slots). */
    public int capacity() {
        return slots.length;
    } // Getter de capacidade

    public int getItemTypeAt(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= slots.length)
            throw new IndexOutOfBoundsException("índice de slot fora de limites");
        return slots[slotIndex].itemType;
    }

    public int getCountAt(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= slots.length)
            throw new IndexOutOfBoundsException("índice de slot fora de limites");
        return slots[slotIndex].count;
    }

    /** Indica se o inventário está cheio (sem espaço adicional). */
    public boolean isFull() { // Checa se há espaço em algum slot
        for (Slot s : slots)
            if (s.isEmpty() || s.space(stackLimit) > 0)
                return false; // Se encontrar espaço, não está cheio
        return true; // Caso contrário, está cheio
    }

    /** Conta quantos slots livres existem (vazios). */
    public int getFreeSlots() { // Calcula slots vazios
        int free = 0; // Acumulador
        for (Slot s : slots)
            if (s.isEmpty())
                free++; // Incrementa quando vazio
        return free; // Retorna total
    }

    /** Soma total de um tipo de item no inventário. */
    public int getCount(int itemType) { // Conta por tipo
        int total = 0; // Acumulador
        for (Slot s : slots)
            if (!s.isEmpty() && s.itemType == itemType)
                total += s.count; // Soma contagens
        return total; // Retorna total
    }

    /** Adiciona quantidade de um tipo, respeitando pilhas e capacidade. */
    public void add(int itemType, int qty) { // Operação de adicionar
        if (itemType <= 0)
            throw new IllegalArgumentException("tipo de item deve ser > 0"); // Valida tipo
        if (qty <= 0)
            throw new InvalidQuantityException("quantidade deve ser > 0"); // Valida quantidade
        int remaining = qty; // Quantidade por inserir
        for (Slot s : slots) { // Primeiro, preencher pilhas existentes
            if (remaining == 0)
                break; // Sai se já inseriu tudo
            if (!s.isEmpty() && s.itemType == itemType) { // Se slot tem mesmo tipo
                int space = s.space(stackLimit); // Calcula espaço disponível
                if (space > 0) { // Se há espaço
                    int put = Math.min(space, remaining); // Quantidade a colocar
                    s.count += put; // Atualiza contagem
                    remaining -= put; // Atualiza restante
                }
            }
        }
        for (Slot s : slots) { // Depois, usar slots vazios
            if (remaining == 0)
                break; // Sai se terminou
            if (s.isEmpty()) { // Slot vazio
                s.itemType = itemType; // Define tipo
                int put = Math.min(stackLimit, remaining); // Quantidade a colocar
                s.count = put; // Inicializa contagem
                remaining -= put; // Atualiza restante
            }
        }
        if (remaining > 0)
            throw new InventoryFullException("not enough space for itemType=" + itemType + ", remaining=" + remaining); // Erro
                                                                                                                        // se
                                                                                                                        // sobrou
    }

    /** Remove quantidade de um tipo, distribuindo pelos slots. */
    public void remove(int itemType, int qty) { // Operação de remover por tipo
        if (itemType <= 0)
            throw new IllegalArgumentException("tipo de item deve ser > 0"); // Valida tipo
        if (qty <= 0)
            throw new InvalidQuantityException("quantidade deve ser > 0"); // Valida quantidade
        int available = getCount(itemType); // Total disponível
        if (available < qty)
            throw new ItemNotFoundException(
                    "insufficient quantity for itemType=" + itemType + ", have=" + available + ", need=" + qty); // Falta
                                                                                                                 // recurso
        int remaining = qty; // Quantidade a remover
        for (Slot s : slots) { // Percorre slots para retirar
            if (remaining == 0)
                break; // Termina se já removeu tudo
            if (!s.isEmpty() && s.itemType == itemType) { // Slot do tipo
                int take = Math.min(s.count, remaining); // Quantidade a tirar
                s.count -= take; // Atualiza contagem
                remaining -= take; // Atualiza restante
                if (s.count == 0)
                    s.itemType = 0; // Liberta slot se vazio
            }
        }
    }

    /** Remove quantidade num índice de slot específico. */
    public void removeAt(int slotIndex, int qty) { // Remoção direta por slot
        if (slotIndex < 0 || slotIndex >= slots.length)
            throw new IndexOutOfBoundsException("índice de slot fora de limites"); // Valida índice
        if (qty <= 0)
            throw new InvalidQuantityException("quantidade deve ser > 0"); // Valida quantidade
        Slot s = slots[slotIndex]; // Obtém slot
        if (s.isEmpty())
            throw new ItemNotFoundException("slot está vazio"); // Erro se vazio
        if (qty > s.count)
            throw new InvalidQuantityException("quantidade excede contagem do slot"); // Erro se excede
        s.count -= qty; // Atualiza contagem
        if (s.count == 0)
            s.itemType = 0; // Liberta slot
    }

    /** Verifica se há espaço para inserir uma quantidade de um tipo. */
    public boolean hasSpaceFor(int itemType, int qty) { // Simulação de inserção
        if (itemType <= 0 || qty <= 0)
            return false; // Validação mínima
        int remaining = qty; // Quantidade a simular
        for (Slot s : slots) { // Considera espaços em pilhas existentes
            if (!s.isEmpty() && s.itemType == itemType) { // Slot compatível
                int space = s.space(stackLimit); // Espaço
                if (space > 0) { // Se houver
                    int put = Math.min(space, remaining); // Simula inserir
                    remaining -= put; // Atualiza restante
                }
            }
        }
        for (Slot s : slots) { // Considera slots vazios
            if (remaining == 0)
                break; // Já cabe
            if (s.isEmpty()) { // Slot vazio
                int put = Math.min(stackLimit, remaining); // Simula inserir
                remaining -= put; // Atualiza restante
            }
        }
        return remaining == 0; // Cabe tudo?
    }

    @Override // Converte estado do inventário para string legível
    public String toString() { // Construção de string
        StringBuilder sb = new StringBuilder(); // Buffer
        sb.append("Inventory["); // Prefixo
        for (int i = 0; i < slots.length; i++) { // Percorre slots
            Slot s = slots[i]; // Slot atual
            sb.append(i).append(":"); // Índice
            if (s.isEmpty())
                sb.append("-"); // Marca vazio
            else
                sb.append(s.itemType).append("x").append(s.count); // Marca tipo e contagem
            if (i < slots.length - 1)
                sb.append(", "); // Separador
        }
        sb.append("]"); // Sufixo
        return sb.toString(); // Resultado
    }

    /** Harness simples: exerce operações de add/remove e imprime estado. */

}
