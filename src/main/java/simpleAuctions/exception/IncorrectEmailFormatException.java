package simpleAuctions.exception;

public class IncorrectEmailFormatException extends Exception {
    public IncorrectEmailFormatException() {
        System.out.println("Niepoprawny adres e-mail.");
    }
}
