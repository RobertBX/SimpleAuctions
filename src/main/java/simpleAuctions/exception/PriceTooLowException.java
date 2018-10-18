package simpleAuctions.exception;

public class PriceTooLowException extends Exception {

    public PriceTooLowException(){
        System.out.println("Price to low");
    }
}
