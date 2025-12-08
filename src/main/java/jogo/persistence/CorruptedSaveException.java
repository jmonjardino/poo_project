package jogo.persistence;

/**
 * Exceção lançada quando o ficheiro de save está corrompido ou com formato
 * inválido.
 */
public class CorruptedSaveException extends SaveException {

    public CorruptedSaveException(String message) {
        super(message);
    }

    public CorruptedSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
