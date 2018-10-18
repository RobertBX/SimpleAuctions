package simpleAuctions.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        System.out.println("Błędne hasło");
    }
}
