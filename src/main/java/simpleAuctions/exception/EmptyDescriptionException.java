package simpleAuctions.exception;

public class EmptyDescriptionException extends Exception {
    public EmptyDescriptionException() {
        System.out.println("Description is empty");
    }
}

