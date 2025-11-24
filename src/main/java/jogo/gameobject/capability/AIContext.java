package jogo.gameobject.capability;
import jogo.framework.math.Vec3;

/**
 * Contexto de execução para a IA de um GameObject.
 * Contém informações relevantes para a execução da IA, como a posição do jogador.
 */
public class AIContext {
    /** Posição atual do jogador no mundo. */
    public final Vec3 playerPos;

    /**
     * Constrói um AIContenxt com a posição do jogador fornecida.
     * @param playerPos posição atual do jogador no mundo
     */
    public AIContext(Vec3 playerPos) {
        this.playerPos = playerPos;
    }
}
