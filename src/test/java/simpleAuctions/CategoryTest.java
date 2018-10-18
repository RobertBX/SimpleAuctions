package simpleAuctions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import simpleAuctions.auction.Auction;
import simpleAuctions.auction.AuctionController;
import simpleAuctions.category.Category;
import simpleAuctions.category.CategoryController;
import simpleAuctions.exception.*;
import simpleAuctions.user.User;

import java.io.*;
import java.math.BigDecimal;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CategoryTest {
    final String testAuctionFilePath = "TestDatabaseAuction.csv";
    PrintStream out;
    Database database = Database.getInstance();

    @Before
    public void setup() {
        this.out = mock(PrintStream.class);
    }

    @Before
    public void deleteCollections() {
        database.getAllAuctionsByNumber().clear();
        database.getAllUsersByNickname().clear();
        database.getAllCategoriesByName().clear();
        database.getAllCategoriesByName().clear();
    }

    @Test
    public void testPrintCategories() throws EmptyCategoryNameException {
        Category category = new Category("Test");
        CategoryController.printCategories(category, 1, out);
        verify(out).println("Test");
    }

    @Test
    public void testCreateCategoryTree() throws EmptyCategoryNameException {
        CategoryController.createCategoryTree();
        assertEquals(database.getAllCategoriesByName().size(), 13);
        assertNotNull(database.getCategoryByName("Samochody"));
        assertNotNull(database.getCategoryByName("Elektronika"));
        assertNotNull(database.getCategoryByName("Zabawki"));
        assertNotNull(database.getCategoryByName("Zabawki z Hanem Solo"));
        assertNotNull(database.getCategoryByName("Empetrójki"));
        assertNotNull(database.getCategoryByName("Telewizory"));
        assertNotNull(database.getCategoryByName("Stormtrooperzy"));
        assertNotNull(database.getCategoryByName("Elektroniczne konie"));
        assertNotNull(database.getCategoryByName("Misie pluszowe"));
        assertNotNull(database.getCategoryByName("All"));
        assertNull(database.getCategoryByName("Null"));
        assertEquals(database.getCategoryByName("Elektronika").getSubcategories().size(), 3);
        assertEquals(database.getCategoryByName("All").getSubcategories().size(), 3);
        assertEquals(database.getCategoryByName("Zabawki z 'Gwiezdnych Wojen'").getSubcategories().size(), 2);
        assertEquals(database.getCategoryByName("Stormtrooperzy").getSubcategories().size(), 0);
    }

    @Test
    public void testIfAuctionsAreAddedToCategoriesWhenCreated() throws Exception {
        CategoryController.createCategoryTree();
        User testSeller = new User("", "",  "", "piotrek@gmail.com", "ieterw", "Seller");
        Auction testAuction1 = AuctionController.createAuction(testSeller, "Stormtrooper1", "Descr", database.getCategoryByName("Stormtrooperzy"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction2 = AuctionController.createAuction(testSeller, "Stormtrooper2", "Descr", database.getCategoryByName("Stormtrooperzy"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction3 = AuctionController.createAuction(testSeller, "TV", "Descr", database.getCategoryByName("Telewizory"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction4 = AuctionController.createAuction(testSeller, "Samochody", "Descr", database.getCategoryByName("Samochody"), new BigDecimal(10.0), testAuctionFilePath);

        assertEquals(database.getCategoryByName("Stormtrooperzy").getAuctions().size(), 2);
        assertEquals(database.getCategoryByName("Telewizory").getAuctions().size(), 1);
        assertEquals(database.getCategoryByName("Samochody").getAuctions().size(), 1);
        assertEquals(database.getCategoryByName("Empetrójki").getAuctions().size(), 0);
        assertEquals(database.getCategoryByName("Zabawki").getAuctions().size(), 0);
    }

    @Test
    public void testListAuctionsByCategory() throws Exception {
        CategoryController.createCategoryTree();

        User testSeller = new User("", "",  "", "tetdude@tested.com", "ieterw", "Seller");
        Auction testAuction1 = AuctionController.createAuction(testSeller, "Stormtrooper1", "Descr", database.getCategoryByName("Stormtrooperzy"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction2 = AuctionController.createAuction(testSeller, "Stormtrooper2", "Descr", database.getCategoryByName("Stormtrooperzy"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction3 = AuctionController.createAuction(testSeller, "Misiu1", "Descr", database.getCategoryByName("Misie pluszowe"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction4 = AuctionController.createAuction(testSeller, "Han Solo 1", "Descr", database.getCategoryByName("Zabawki z Hanem Solo"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction5 = AuctionController.createAuction(testSeller, "Pejczyk1", "Descr", database.getCategoryByName("Zabawki sadomaso"), new BigDecimal(10.0), testAuctionFilePath);
        Auction testAuction6 = AuctionController.createAuction(testSeller, "Pejczyk2", "Descr", database.getCategoryByName("Zabawki sadomaso"), new BigDecimal(10.0), testAuctionFilePath);

        assertEquals(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki")).size(), 6);
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki")).contains(testAuction1));
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki")).contains(testAuction2));
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki")).contains(testAuction3));
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki")).contains(testAuction4));
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki")).contains(testAuction5));
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki")).contains(testAuction6));

        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Stormtrooperzy")).contains(testAuction1));
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki z 'Gwiezdnych Wojen'")).contains(testAuction1));
        assertTrue(CategoryController.listAuctionsByCategory(database.getCategoryByName("All")).contains(testAuction1));

        assertFalse(CategoryController.listAuctionsByCategory(database.getCategoryByName("Samochody")).contains(testAuction1));
        assertFalse(CategoryController.listAuctionsByCategory(database.getCategoryByName("Elektroniczne konie")).contains(testAuction1));
        assertFalse(CategoryController.listAuctionsByCategory(database.getCategoryByName("Zabawki sadomaso")).contains(testAuction1));
    }

    @Test(expected = NotFinalCategoryException.class)
    public void testNotFinalCategoryException() throws Exception {
        Database database = Database.getInstance();
        CategoryController.createCategoryTree();
        User testSeller = new User("testttt", "testttt",  "eeeee", "test69@yahoo.com", "ieterw", "Seller");
        Auction newAuction = AuctionController.createAuction(testSeller, "Title", "Descr", database.getCategoryByName("Zabawki"), new BigDecimal(23.0), testAuctionFilePath);
    }

    @Test(expected = EmptyCategoryNameException.class)
    public void testEmptyCategoryNameException() throws Exception {
        Category category = new Category("");
    }

    @Test
    public void testAddSubcategory() throws EmptyCategoryNameException {
        Category cate = new Category("one");
        Category cate2 = new Category("two");
        assertEquals(cate.getSubcategories().size(), 0);
        assertEquals(cate2.getSubcategories().size(), 0);
        cate.addSubcategory(cate2);
        assertEquals(cate.getSubcategories().size(), 1);
    }

    @After
    public void clearFile () throws IOException {
        FileWriter fw = new FileWriter(testAuctionFilePath, false);
        fw.write("");
        fw.close();
    }
}
