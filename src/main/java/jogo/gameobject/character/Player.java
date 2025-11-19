package jogo.gameobject.character;

/**
 * Entidade de jogador neutra em relação ao motor, que suporta o controlador do jogador.
 * Aspetos visuais, física e input são geridos pelo PlayerAppState.
 */
public class Player extends Character {

    /** Constrói o jogador com um nome de apresentação por omissão. */
    public Player() {
        super("Player");
    }
}
