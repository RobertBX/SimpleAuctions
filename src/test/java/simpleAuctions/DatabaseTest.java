package simpleAuctions;

import org.junit.Before;
import org.junit.Test;
import simpleAuctions.auction.Auction;
import simpleAuctions.auction.AuctionController;
import simpleAuctions.category.Category;
import simpleAuctions.category.CategoryController;
import simpleAuctions.user.User;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DatabaseTest {
    final String testAuctionFilePath = "TestDatabaseAuction.csv";
    Database database = Database.getInstance();

    @Before
    public void clearAllDatabaseCollections() {
        database.getAllAuctionsByNumber().clear();
        database.getAllUsersByNickname().clear();
        database.getAllCategoriesByName().clear();
        database.getAllAuctions().clear();
    }

    @Test
    public void testIfCategoriesAreStoredCorrectly() throws Exception {
        CategoryController.createCategoryTree();
        assertEquals(database.getAllCategoriesByName().size(), 13);
        assertNotNull(database.getCategoryByName("Samochody"));
        assertNotNull(database.getCategoryByName("Stormtrooperzy"));
        assertNotNull(database.getCategoryByName("Elektroniczne konie"));
        assertNotNull(database.getCategoryByName("Telewizory"));
        assertNull(database.getCategoryByName("NullCategory"));
        Category testCategory = new Category("Test");
        database.addCategoryToAllCategories(testCategory);
        assertNotNull(database.getCategoryByName("Test"));
        assertEquals(database.getAllCategoriesByName().size(), 13);
    }

    @Test
    public void testIfAuctionsAreStoredCorrectly() throws Exception {
        Category testCategory = new Category("Test");
        User janek = new User("janek", "janek",  "janek", "janek@gmail.com", "janek", "janek");
        Auction testAuction1 = AuctionController.createAuction(janek, "Title1", "descr1", testCategory, BigDecimal.valueOf(10), testAuctionFilePath);
        Auction testAuction2 = AuctionController.createAuction(janek, "Title2", "descr2", testCategory, BigDecimal.valueOf(12.0), testAuctionFilePath);
        Auction testAuction3 = AuctionController.createAuction(janek, "Title3", "descr3", testCategory, BigDecimal.valueOf(13.0), testAuctionFilePath);
        Auction testAuction4 = AuctionController.createAuction(janek, "Title4", "descr4", testCategory, BigDecimal.valueOf(14.0), testAuctionFilePath);
        assertEquals(database.getAllAuctionsByNumber().size(), 4);
        assertEquals(database.getAllAuctions().size(), 4);
        assertNotNull(database.getAuctionByNumber(testAuction1.getAuctionNumber()));
        assertNotNull(database.getAuctionByNumber(testAuction2.getAuctionNumber()));
        assertNotNull(database.getAuctionByNumber(testAuction3.getAuctionNumber()));
        assertNotNull(database.getAuctionByNumber(testAuction4.getAuctionNumber()));
        assertEquals(database.getAuctionByNumber(testAuction1.getAuctionNumber()), testAuction1);
        assertEquals(database.getAuctionByNumber(testAuction2.getAuctionNumber()), testAuction2);
        assertEquals(database.getAuctionByNumber(testAuction3.getAuctionNumber()), testAuction3);
        assertEquals(database.getAuctionByNumber(testAuction4.getAuctionNumber()), testAuction4);
    }

    @Test
    public void testIfUsersAreStoredCorrectly() throws Exception{
        Database database = Database.getInstance();
        User janek = new User("janek", "janek",  "janek", "janek@gmail.com", "janek", "janek");
        User janek2 = new User("janek", "janek",  "janek", "janek@gmail.com", "janek", "janek2");
        User janek3 = new User("janek", "janek",  "janek", "janek@gmail.com", "janek", "janek3");
        User janek4 = new User("piotrek", "janek",  "janek", "janek@gmail.com", "janek", "janek4");
        database.addUserToAllUsers(janek);
        database.addUserToAllUsers(janek2);
        database.addUserToAllUsers(janek3);
        database.addUserToAllUsers(janek4);
        assertEquals(database.getAllUsersByNickname().size(), 4);
        assertEquals(database.getUserByNickname("janek3").getName(), "janek");
        assertEquals(database.getUserByNickname("janek4").getName(), "piotrek");
    }

    @Test
    public void testRemoveAuction () throws Exception {
        Category testCategory = new Category("Test");
        User janek = new User("janek", "janek",  "janek", "janek@gmail.com", "janek", "janek");
        Auction testAuction1 = AuctionController.createAuction(janek, "Title1", "descr1", testCategory, BigDecimal.valueOf(10), testAuctionFilePath);
        Auction testAuction2 = AuctionController.createAuction(janek, "Title2", "descr2", testCategory, BigDecimal.valueOf(12.0), testAuctionFilePath);
        assertEquals(database.getAllAuctions().size(), 2);
        assertNotNull(database.getAuctionByNumber(testAuction1.getAuctionNumber()));
        assertNotNull(database.getAuctionByNumber(testAuction2.getAuctionNumber()));
        database.removeAuction(testAuction1);
        database.removeAuction(testAuction2);
        assertNull(database.getAuctionByNumber(testAuction1.getAuctionNumber()));
        assertNull(database.getAuctionByNumber(testAuction2.getAuctionNumber()));
        assertEquals(database.getAllAuctions().size(), 0);
    }
}
