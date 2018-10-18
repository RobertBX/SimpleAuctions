package simpleAuctions;

import simpleAuctions.category.Category;
import simpleAuctions.auction.Auction;
import simpleAuctions.user.User;

import java.util.*;


public class Database {

    private static Database instance;

    private Database() {
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    private Map<String, Category> allCategoriesByName = new HashMap<>();
    private Map<Integer, Auction> allAuctionsByNumber = new HashMap<>();
    private List<Auction> auctions = new ArrayList<>();
    private Map<String, User> allUsersByNickname = new HashMap<>();

    //regarding Categories
    public void addCategoryToAllCategories (Category category) {
        allCategoriesByName.put(category.getName(), category);
    }
    public Category getCategoryByName (String catname) {
        try {
            Category category = allCategoriesByName.get(catname);
            return category;
        } catch (NullPointerException npe) {
            System.out.println("Nie ma takiej kategorii");
        }
        return null;
    }
    public Map<String, Category> getAllCategoriesByName () {
        return allCategoriesByName;
    }

    // regarding Auctions
    public void addAuctionToAllAuctions (Auction auction) {
        allAuctionsByNumber.put(auction.getAuctionNumber(), auction);
        auctions.add(auction);
    }

    public void removeAuction (Auction auction) {
        auctions.remove(auction);
        allAuctionsByNumber.remove(auction.getAuctionNumber());
    }

    public List<Auction> getAllAuctions () {
        return auctions;
    }

    public Auction getAuctionByNumber (Integer number) {
        try {
            Auction auction = allAuctionsByNumber.get(number);
            return auction;
        } catch (NullPointerException npe) {
            System.out.println("Nie ma takiej aukcji");
        }
        return null;
    }
    public Map<Integer, Auction> getAllAuctionsByNumber() {
        return allAuctionsByNumber;
    }

    //regarding Users
    public Map<String, simpleAuctions.user.User> getAllUsersByNickname() {
        return allUsersByNickname;
    }
    public void addUserToAllUsers (User user) {
        allUsersByNickname.put(user.getNick(), user);
    }
    public User getUserByNickname (String nickname) {
        try {
            User user = allUsersByNickname.get(nickname);
            return user;
        } catch (NullPointerException npe) {
            System.out.println("Nie ma takiego użytkownikaa");
        }
        return null;
    }
}
