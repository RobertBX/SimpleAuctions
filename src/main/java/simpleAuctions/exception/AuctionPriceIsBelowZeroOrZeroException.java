package simpleAuctions.exception;

public class AuctionPriceIsBelowZeroOrZeroException extends Exception {
    public AuctionPriceIsBelowZeroOrZeroException() {
        System.out.println("auction price is below zero or zero");
    }
}
