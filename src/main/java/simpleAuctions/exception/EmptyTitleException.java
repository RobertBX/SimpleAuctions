package simpleAuctions.exception;

public class EmptyTitleException extends Exception {
    public EmptyTitleException() {
        System.out.println("Title is empty");
    }
}
