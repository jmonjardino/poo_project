package jogo.persistence;

import jogo.gameobject.StackingInventory;
import jogo.gameobject.character.Player;

import java.io.*;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Serviço responsável por guardar e carregar o estado do jogo.
 * Utiliza formato de texto simples (properties-like) para fácil leitura e
 * depuração.
 */
public final class SaveService {

    /** Diretório onde os saves são guardados. */
    private static final String SAVE_DIR = "saves";

    /** Nome do ficheiro de save por omissão. */
    private static final String DEFAULT_SAVE_FILE = "game.sav";

    /** Construtor privado - classe utilitária. */
    private SaveService() {
    }

    /**
     * Guarda o estado atual do jogo para um ficheiro.
     * 
     * @param playerX   posição X do jogador
     * @param playerY   posição Y do jogador
     * @param playerZ   posição Z do jogador
     * @param player    o jogador (para inventário e saúde)
     * @param worldSeed seed do mundo
     * @param filename  nome do ficheiro (sem diretório)
     * @throws SaveException se ocorrer erro de IO
     */
    public static void save(float playerX, float playerY, float playerZ,
            Player player, long worldSeed, String filename) throws SaveException {
        if (player == null) {
            throw new SaveException("Player não pode ser null");
        }

        // Garantir que o diretório existe
        Path saveDir = Paths.get(SAVE_DIR);
        try {
            Files.createDirectories(saveDir);
        } catch (IOException e) {
            throw new SaveException("Não foi possível criar diretório de saves: " + SAVE_DIR, e);
        }

        Path savePath = saveDir.resolve(filename != null ? filename : DEFAULT_SAVE_FILE);

        // Construir SaveData
        Map<Integer, Integer> invMap = new LinkedHashMap<>();
        StackingInventory inv = player.getInventory();
        for (int i = 0; i < inv.capacity(); i++) {
            int type = inv.getItemTypeAt(i);
            int count = inv.getCountAt(i);
            if (type > 0 && count > 0) {
                invMap.merge(type, count, Integer::sum);
            }
        }

        SaveData data = new SaveData(playerX, playerY, playerZ, player.getHealth(),
                invMap, worldSeed, player.getSelectedSlot());

        // Escrever para ficheiro em formato legível
        try (PrintWriter writer = new PrintWriter(new FileWriter(savePath.toFile()))) {
            writer.println("# IscteCraft Save File");
            writer.println("# Não editar manualmente!");
            writer.println();
            writer.println("playerX=" + data.getPlayerX());
            writer.println("playerY=" + data.getPlayerY());
            writer.println("playerZ=" + data.getPlayerZ());
            writer.println("playerHealth=" + data.getPlayerHealth());
            writer.println("selectedSlot=" + data.getSelectedSlot());
            writer.println("worldSeed=" + data.getWorldSeed());
            writer.println();
            writer.println("# Inventário: itemType=count");
            for (Map.Entry<Integer, Integer> entry : data.getInventory().entrySet()) {
                writer.println("inv." + entry.getKey() + "=" + entry.getValue());
            }
            System.out.println("[SaveService] Jogo guardado em: " + savePath.toAbsolutePath());
        } catch (IOException e) {
            throw new SaveException("Erro ao guardar jogo: " + e.getMessage(), e);
        }
    }

    /**
     * Carrega o estado do jogo de um ficheiro.
     * 
     * @param filename nome do ficheiro (sem diretório)
     * @return SaveData com o estado carregado
     * @throws SaveException          se ocorrer erro de IO
     * @throws CorruptedSaveException se o ficheiro estiver corrompido
     */
    public static SaveData load(String filename) throws SaveException {
        Path savePath = Paths.get(SAVE_DIR, filename != null ? filename : DEFAULT_SAVE_FILE);

        if (!Files.exists(savePath)) {
            throw new SaveException("Ficheiro de save não encontrado: " + savePath);
        }

        SaveData data = new SaveData();
        Map<Integer, Integer> inventory = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(savePath.toFile()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();

                // Ignorar comentários e linhas vazias
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int eqIdx = line.indexOf('=');
                if (eqIdx < 0) {
                    throw new CorruptedSaveException("Linha " + lineNum + " inválida: " + line);
                }

                String key = line.substring(0, eqIdx).trim();
                String value = line.substring(eqIdx + 1).trim();

                try {
                    switch (key) {
                        case "playerX" -> data.setPlayerX(Float.parseFloat(value));
                        case "playerY" -> data.setPlayerY(Float.parseFloat(value));
                        case "playerZ" -> data.setPlayerZ(Float.parseFloat(value));
                        case "playerHealth" -> data.setPlayerHealth(Integer.parseInt(value));
                        case "selectedSlot" -> data.setSelectedSlot(Integer.parseInt(value));
                        case "worldSeed" -> data.setWorldSeed(Long.parseLong(value));
                        default -> {
                            if (key.startsWith("inv.")) {
                                int itemType = Integer.parseInt(key.substring(4));
                                int count = Integer.parseInt(value);
                                inventory.put(itemType, count);
                            }
                            // Ignorar chaves desconhecidas para compatibilidade futura
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new CorruptedSaveException("Valor inválido na linha " + lineNum + ": " + value, e);
                }
            }
        } catch (IOException e) {
            throw new SaveException("Erro ao ler ficheiro de save: " + e.getMessage(), e);
        }

        data.setInventory(inventory);
        System.out.println("[SaveService] Jogo carregado de: " + savePath.toAbsolutePath());
        System.out.println("[SaveService] " + data);
        return data;
    }

    /**
     * Verifica se existe um ficheiro de save.
     * 
     * @param filename nome do ficheiro
     * @return true se existir
     */
    public static boolean saveExists(String filename) {
        Path savePath = Paths.get(SAVE_DIR, filename != null ? filename : DEFAULT_SAVE_FILE);
        return Files.exists(savePath);
    }

    /**
     * Aplica os dados carregados ao jogador.
     * 
     * @param data   dados carregados
     * @param player jogador para atualizar
     */
    public static void applyToPlayer(SaveData data, Player player) {
        if (data == null || player == null)
            return;

        // Limpar inventário atual
        StackingInventory inv = player.getInventory();
        for (int i = 0; i < inv.capacity(); i++) {
            int count = inv.getCountAt(i);
            if (count > 0) {
                try {
                    inv.removeAt(i, count);
                } catch (Exception e) {
                    // Ignorar erros de limpeza
                }
            }
        }

        // Adicionar itens do save
        for (Map.Entry<Integer, Integer> entry : data.getInventory().entrySet()) {
            try {
                inv.add(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                System.out.println("[SaveService] Aviso: não foi possível adicionar item " +
                        entry.getKey() + " x" + entry.getValue());
            }
        }

        // Restaurar saúde e slot selecionado
        player.setHealth(data.getPlayerHealth());
        player.setSelectedSlot(data.getSelectedSlot());
    }
}
