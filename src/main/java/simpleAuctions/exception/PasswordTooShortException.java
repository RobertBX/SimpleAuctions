package simpleAuctions.exception;

public class PasswordTooShortException extends Exception {
    public PasswordTooShortException() {
        System.out.println("Hasło powinno mieć co najmniej 5 znakó");
    }
}
