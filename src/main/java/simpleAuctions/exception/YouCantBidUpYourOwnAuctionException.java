package simpleAuctions.exception;

public class YouCantBidUpYourOwnAuctionException extends Exception {
    public YouCantBidUpYourOwnAuctionException(){
        System.out.println("You cant bid up auction which you bidded up earlier or you created");
    }
}
