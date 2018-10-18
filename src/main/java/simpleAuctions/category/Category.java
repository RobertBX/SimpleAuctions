package simpleAuctions.category;


import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import simpleAuctions.auction.Auction;
import simpleAuctions.exception.EmptyCategoryNameException;

@Getter
@Setter
public class Category {
    private String name;
    private List<Auction> auctions;
    private List<Category> subcategories;

    public Category(String name) throws EmptyCategoryNameException {
        this.name = name;
        if (name.length() == 0) {
            throw new EmptyCategoryNameException();
        }
        this.auctions = new LinkedList<>();
        this.subcategories = new LinkedList<>();
    }

    public void addAuction(Auction auction) {
        this.auctions.add(auction);
    }

    public void addSubcategory(Category category) {
        this.subcategories.add(category);
    }

    @Override
    public String toString() {
        return  name ;
    }
}
