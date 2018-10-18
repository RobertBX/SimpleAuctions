package simpleAuctions.exception;

public class EmptyCategoryNameException extends Exception {
    public EmptyCategoryNameException (){
        System.out.println("Nie podałeś nazwy kategorii ");
    }
}
