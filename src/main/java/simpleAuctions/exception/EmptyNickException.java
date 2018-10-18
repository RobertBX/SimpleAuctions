package simpleAuctions.exception;

public class EmptyNickException extends Exception {
    public EmptyNickException (){
        System.out.println("Nie podales zadnego nicku");
    }
}
