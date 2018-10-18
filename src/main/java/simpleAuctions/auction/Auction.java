package simpleAuctions.auction;

import lombok.Getter;
import lombok.Setter;
import simpleAuctions.category.Category;
import simpleAuctions.exception.AuctionPriceIsBelowZeroOrZeroException;
import simpleAuctions.exception.EmptyDescriptionException;
import simpleAuctions.exception.EmptyTitleException;
import simpleAuctions.exception.NotFinalCategoryException;
import simpleAuctions.user.User;

import java.math.BigDecimal;

@Getter @Setter
public class Auction {

    private String title;
    private String description;
    private Category category;
    private User seller;
    private User winner;
    private BigDecimal price;
    int auctionNumber;
    private int bids = 0;
    private boolean isFinished = false;

    public Auction(String title, String description, Category category, User seller, User winner, BigDecimal price, int auctionNumber, int bids) throws Exception {
        this.title = title;
        if (title.length() == 0) {
            throw new EmptyTitleException();
        }
        this.description = description;
        if (description.length() == 0) {
            throw new EmptyDescriptionException();
        }
        this.category = category;
        if (category.getSubcategories().size() > 0) {
            throw new NotFinalCategoryException();
        }

        this.seller = seller;
        this.winner = winner;
        if(winner == null){
            User defaultWinner = new User("defwin","defwin","defwin","defwin@deffault.com","defwin","Nikt nie zalicytowal tej aukcji");
            setWinner(defaultWinner);
        }
        this.isFinished = false;
        this.price = price;
        if ((price.compareTo(BigDecimal.valueOf(0)) <= 0) || price.equals(BigDecimal.valueOf(0))) {
            throw new AuctionPriceIsBelowZeroOrZeroException();
        }
        this.bids = bids;
        this.auctionNumber = auctionNumber;
    }

    @Override
    public String toString() {
        return "\n" + "{" +
                "title='" + title + '\'' +
                ", category=" + category.getName() +
                ", seller=" + seller.getNick() +
                ", winner=" + winner.getNick() +
                ", price=" + price + "PLN" +
                ", auction number= " + auctionNumber +
                '}' ;
    }
}
