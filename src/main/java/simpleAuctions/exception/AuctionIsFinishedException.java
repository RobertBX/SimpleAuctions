package simpleAuctions.exception;

public class AuctionIsFinishedException extends Exception {
    public AuctionIsFinishedException() {
        System.out.println("Aukcja zakończona");
    }
}
