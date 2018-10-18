package simpleAuctions.auction;

import simpleAuctions.category.Category;
import simpleAuctions.category.CategoryController;
import simpleAuctions.Database;
import simpleAuctions.exception.*;
import simpleAuctions.user.User;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.out;

public class AuctionController {

    public static BigDecimal bidUp(Auction auction, BigDecimal bidUp, User user, String filepath) throws Exception {
        Database database = Database.getInstance();
        if (auction.isFinished() == true) {
            throw new AuctionIsFinishedException();
        }
        if (auction.getWinner().equals(user) || auction.getSeller().equals(user)) {
            throw new YouCantBidUpYourOwnAuctionException();
        }
        BigDecimal newPrice = bidUp;
        if (newPrice.compareTo(auction.getPrice()) < 0) {
            throw new PriceTooLowException();
        } else {
            database.removeAuction(auction);
            auction.setPrice(newPrice);
            auction.setWinner(user);
            auction.setBids(auction.getBids() + 1);
            database.addAuctionToAllAuctions(auction);
            System.out.println("Wygrywasz tę aukcję!");
            SaveAuctionOnDisk.getInstance().clearFile(filepath);
            for (Auction a : database.getAllAuctions()) {
                SaveAuctionOnDisk.getInstance().writeCsvFile(filepath, a);
            }
            if (auction.getBids() == 3) {
                System.out.println("aukcja zakonczona, zwyciezył " + user.getNick());
                auction.setFinished(true);
                user.getMyWonList().add(auction);
            }
        }
        return auction.getPrice();
    }

    public static Auction createAuction(User currentUser, String title, String description, Category category, BigDecimal price, String filepath) throws Exception {
        Database database = Database.getInstance();
        Random rd = new Random();
        Integer auctNumber = rd.nextInt(10000);
        while (database.getAuctionByNumber(auctNumber) != null) {
            auctNumber = rd.nextInt(10000);
        }
        Auction newAuction = new Auction(title, description, category, currentUser, null, price, auctNumber, 0);
        database.addAuctionToAllAuctions(newAuction);
        currentUser.getMySellingList().add(newAuction);
        category.addAuction(newAuction);
        SaveAuctionOnDisk.getInstance().writeCsvFile(filepath, newAuction);
        return newAuction;
    }

    public static Auction createAuctionMain(User currentUser, Category allcategories, String filepath) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj tytuł aukcji");
        String title = sc.nextLine();
        System.out.println("Podaj opis");
        String description = sc.nextLine();
        System.out.println("Podaj cenę wywoławczą");
        BigDecimal price = BigDecimal.valueOf(1);
        boolean priceCheck = true;
        while (priceCheck) {
            try {
                price = sc.nextBigDecimal();
                priceCheck = false;
            } catch (InputMismatchException e) {
                priceCheck = true;
                System.out.println("Cena musi być liczbą");
                price = BigDecimal.valueOf(1);
                sc.nextLine();
            }
        }
        Category category = viewAuctionByCategories(allcategories);
        boolean auctionCheck = true;
        while (auctionCheck) {
            try {
                Auction newAuction = AuctionController.createAuction(currentUser, title, description, category, price, filepath);
                auctionCheck = false;
                System.out.println("Wystawiłeś nową aukcję");
                return newAuction;
            } catch (NotFinalCategoryException e) {
                category = viewAuctionByCategories(allcategories);
                auctionCheck = true;
            } catch (NullPointerException npe) {
                System.out.println("Nie ma takiej kategorii, podaj kategorie finalna");
                category = viewAuctionByCategories(allcategories);
                auctionCheck = true;
            }
        }
        return null;
    }

    public static Category viewAuctionByCategories(Category allcategories) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Database database = Database.getInstance();
        CategoryController.printCategories(allcategories, 0, out);
        System.out.println("Wybierz kategorie");
        String chosenCategory = scanner.nextLine();
        try {
            System.out.println(database.getCategoryByName(chosenCategory).getAuctions());
        } catch (NullPointerException npe) {
            System.out.println("Zła kategoria");
            chosenCategory = scanner.next();
        }
        return database.getCategoryByName(chosenCategory);
    }

    public static List<Auction> viewSellersAuctions(User loggedInUser) {
        return loggedInUser.getMySellingList();
    }

    public static List<Auction> viewWonAuctions(User loggedInUser) {
        return loggedInUser.getMyWonList();
    }
}