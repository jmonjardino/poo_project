package jogo.persistence;

/**
 * Exceção lançada quando ocorre um erro durante operações de Save/Load.
 * Encapsula erros de IO e outros problemas de persistência.
 */
public class SaveException extends Exception {

    public SaveException(String message) {
        super(message);
    }

    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
