package ge.artkme.webapps.exception;

public class DBManagerException extends Exception {

    private static final long serialVersionUID = 1L;

    public DBManagerException(String message, Throwable throwable) {
        super(message, throwable);

    }

    public DBManagerException(String msg, String shortMessage, Throwable throwable) {
        super(msg, throwable);
    }

    public DBManagerException(String message) {
        super(message);

    }

    public DBManagerException(Throwable throwable) {
        super(throwable);

    }
}
