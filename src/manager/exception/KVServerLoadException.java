package manager.exception;

public class KVServerLoadException extends RuntimeException {
    public KVServerLoadException (final String message) {
        super(message);
    }
}
