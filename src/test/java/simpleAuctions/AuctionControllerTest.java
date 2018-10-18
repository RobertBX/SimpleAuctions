package simpleAuctions;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import simpleAuctions.auction.Auction;
import simpleAuctions.auction.AuctionController;
import simpleAuctions.category.Category;
import simpleAuctions.category.CategoryController;
import simpleAuctions.exception.*;
import simpleAuctions.user.User;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class AuctionControllerTest {

    final String testAuctionFilePath = "TestDatabaseAuction.csv";
    User testSeller = new User("", "",  "", "mymsil@mymail.com", "ieterw", "Seller");
    Category category = new Category("Test");

    public AuctionControllerTest() throws Exception {
    }

    Database database = Database.getInstance();
    Map<Integer, Auction> allAuctions = database.getAllAuctionsByNumber();

    @Before
    public void allAuctionsSizeSetToZero() {
        database.getAllAuctionsByNumber().clear();
        database.getAllAuctions().clear();
        allAuctions.clear();
    }

    @Rule
    public final TextFromStandardInputStream systemInMock
            = emptyStandardInputStream();

    @Test(expected = EmptyTitleException.class)
    public void testCreateAuctionThrowEmptyTitleException() throws Exception {
        Auction auction = AuctionController.createAuction(testSeller, "", "lol", category, BigDecimal.valueOf(342), testAuctionFilePath);
    }

    @Test(expected = AuctionPriceIsBelowZeroOrZeroException.class)
    public void testCreateAuctionThrowBelowZeroException() throws Exception {
        Auction auction = AuctionController.createAuction(testSeller, "fsd", "lol", category, BigDecimal.valueOf(-32), testAuctionFilePath);

    }

    @Test(expected = EmptyDescriptionException.class)
    public void testCreateAuctionThrowEmptyDescribtionEmpty() throws Exception {
        Auction auction = AuctionController.createAuction(testSeller, "sfd", "", category, BigDecimal.valueOf(342), testAuctionFilePath);
    }

    @Test
    public void testCreateAuction() throws Exception {
        Auction testAuction1 = null;

        testAuction1 = AuctionController.createAuction(testSeller, "Title1", "Description1", category, BigDecimal.valueOf(150), testAuctionFilePath);

        Auction testAuction2 = null;


        testAuction2 = AuctionController.createAuction(testSeller, "Title2", "Description2", category, BigDecimal.valueOf(372.5), testAuctionFilePath);
        User defaultWinner = new User("defwin", "defwin",  "defwin", "defwin@deffault.com", "defwin", "Nikt nie zalicytowal tej aukcji");

        TestCase.assertEquals(testSeller.getNick(), testAuction1.getSeller().getNick());
        TestCase.assertEquals(testAuction1.getTitle(), "Title1");
        TestCase.assertEquals(testAuction1.getDescription(), "Description1");
        TestCase.assertEquals(testAuction1.getCategory(), category);
        TestCase.assertEquals(testAuction1.getPrice(), new BigDecimal(150));
        TestCase.assertEquals(testAuction1.getWinner(), defaultWinner);

        TestCase.assertEquals(testSeller.getNick(), testAuction2.getSeller().getNick());
        TestCase.assertEquals(testAuction2.getTitle(), "Title2");
        TestCase.assertEquals(testAuction2.getDescription(), "Description2");
        TestCase.assertEquals(testAuction2.getCategory(), category);
        TestCase.assertEquals(testAuction2.getPrice(), new BigDecimal(372.5));
        TestCase.assertEquals(testAuction2.getWinner(), defaultWinner);
        TestCase.assertEquals(database.getAllAuctionsByNumber().size(), 2);

        List<Auction> testList = new LinkedList<>();
        testList.add(testAuction1);
        testList.add(testAuction2);
        TestCase.assertEquals(testSeller.mySellingList, testList);
    }

    @Test
    public void testViewSellersAuctions() throws Exception {
        Auction testAuction1 = AuctionController.createAuction(testSeller, "Title1", "Description1", category, BigDecimal.valueOf(150.0), testAuctionFilePath);
        Auction testAuction2 = AuctionController.createAuction(testSeller, "Title2", "Description2", category, BigDecimal.valueOf(372.5), testAuctionFilePath);

        List<Auction> testList = new LinkedList<>();
        testList.add(testAuction1);
        testList.add(testAuction2);
        assertEquals(AuctionController.viewSellersAuctions(testSeller), testList);
    }

    @Test
    public void testViewWonAuctions() throws Exception {
        Auction testAuction1 = AuctionController.createAuction(testSeller, "Title1", "Description1", category, BigDecimal.valueOf(150.0), testAuctionFilePath);
        Auction testAuction2 = AuctionController.createAuction(testSeller, "Title2", "Description2", category, BigDecimal.valueOf(372.5), testAuctionFilePath);

        List<Auction> testList2 = new LinkedList<>();
        testList2.add(testAuction1);
        testList2.add(testAuction2);
        testSeller.getMyWonList().add(testAuction1);
        testSeller.getMyWonList().add(testAuction2);
        assertEquals(AuctionController.viewWonAuctions(testSeller), testList2);
    }

    @Test
    public void testIfBidUpSetsPriceToNewPrice() throws Exception {
        Category cat = new Category("Test");
        User user1 = new User("saf", "sdf",  "432", "fsdfsdaa@gmail.com", "fddgs", "32");
        User user2 = new User("sad", "sdf",  "432", "fsdfsdaa@gmail.com", "fgfdgdds", "32");

        Auction sad = new Auction("lol", "fs", cat, user1, user1, BigDecimal.valueOf(1000), 0, 0);
        BigDecimal result = AuctionController.bidUp(sad, BigDecimal.valueOf(1524), user2, testAuctionFilePath);
        BigDecimal expected = BigDecimal.valueOf(1524);
        assertEquals(expected, result);
    }

    @Test
    public void testIsBidUpIncreaseBids() throws Exception {
        Category cat = new Category("Test");
        User user1 = new User("sad", "sdf",  "432", "fsdfsdaa@gmail.com", "fdswer", "32");
        User user2 = new User("saf", "sdf",  "432", "fsdfsdaa@gmail.com", "fdstre", "32");
        User user3 = new User("s2", "sdf",  "432", "fsdfsdaa@gmail.com", "fdsfgd", "32");
        Auction auction = new Auction("lol", "fs", cat, user1, user1, BigDecimal.valueOf(1000), 0, 0);
        BigDecimal bidUpAuction = AuctionController.bidUp(auction, BigDecimal.valueOf(1524), user2, testAuctionFilePath);
        BigDecimal bidUpAuction1 = AuctionController.bidUp(auction, BigDecimal.valueOf(1624), user3, testAuctionFilePath);
        int result = auction.getBids();
        assertEquals(2, result);

    }

    @Test
    public void testIsPersonWhoBidIsTheNewWinner() throws Exception {
        Category cat = new Category("Test");
        User user1 = new User("sad", "sdf",  "432", "fsdaa@gmail.com", "fgdgds", "32");
        User user2 = new User("saf", "sdf",  "432", "fsdaa@gmail.com", "fgdfgdds", "32");
        User user3 = new User("s2", "sdf",  "432", "fsdfsdaa@gmail.com", "fdfgdgdfs", "32");
        Auction auction = new Auction("lol", "fs", cat, user1, user1, BigDecimal.valueOf(1000), 0, 0);
        BigDecimal bidUpAuction = AuctionController.bidUp(auction, BigDecimal.valueOf(1524), user2, testAuctionFilePath);
        BigDecimal bidUpAuction2 = AuctionController.bidUp(auction, BigDecimal.valueOf(1524), user3, testAuctionFilePath);
        User winner = auction.getWinner();
        assertEquals(user3, auction.getWinner());
    }

    @Test(expected = PriceTooLowException.class)
    public void testIsExceptionIsThrown() throws Exception {
        Category cat = new Category("Test");
        User user1 = new User("sad", "sdf",  "432", "fsdaa@gmail.com", "ffdgdfds", "32");
        User user2 = new User("saf", "sdf",  "432", "fsdaa@gmail.com", "fdfdgds", "32");

        Auction auction = new Auction("lol", "fs", cat, user1, user1, BigDecimal.valueOf(1000), 0, 0);
        BigDecimal bidUpAuction = AuctionController.bidUp(auction, BigDecimal.valueOf(194), user2, testAuctionFilePath);
    }

    @Test
    public void testIsAuctionIsAddedToUserWonList() throws Exception {
        Category cat = new Category("Test");
        User user1 = new User("sad", "Kowalski",  "432", "fsdaa@gmail.com", "fdgfds", "32");
        User user2 = new User("sad", "Jan",  "432", "fsdaa@gmail.com", "fdfdgs", "32");
        User user3 = new User("saf", "sf",  "432", "fsdaa@gmail.com", "gfdfds", "32");
        Auction auction = new Auction("lol", "fs", cat, user1, user1, BigDecimal.valueOf(1000), 0, 0);
        BigDecimal bidUpAuction = AuctionController.bidUp(auction, BigDecimal.valueOf(1524), user2, testAuctionFilePath);
        BigDecimal bidUpAuction2 = AuctionController.bidUp(auction, BigDecimal.valueOf(1524), user3, testAuctionFilePath);
        BigDecimal bidUpAuction3 = AuctionController.bidUp(auction, BigDecimal.valueOf(1724), user2, testAuctionFilePath);
        boolean expected = user1.myWonList.contains(auction);
        boolean result = auction.getWinner() == user1;
        assertEquals(expected, result);
    }

    @Test(expected = YouCantBidUpYourOwnAuctionException.class)
    public void testYouCantBidUpYourOwnAuction() throws Exception {
        Category cat = new Category("Test");
        User seller = new User("sad", "sdf",  "432", "fsdaa@gmail.com", "dfgfds", "32");
        User user1 = new User("sa", "sdf",  "432", "fsdaa@gmail.com", "ffgdds", "32");
        Auction auction = new Auction("lol", "fs", cat, seller, user1, BigDecimal.valueOf(1000), 0, 0);
        BigDecimal bidUpAuction = AuctionController.bidUp(auction, BigDecimal.valueOf(1194), seller, testAuctionFilePath);
    }

    @Test(expected = YouCantBidUpYourOwnAuctionException.class)
    public void testYouCantBidUpAuctionThatYouBiddedUp() throws Exception {
        Category cat = new Category("Test");
        User seller = new User("sad", "sdf",  "432", "fsdaa@gmail.com", "fdfgds", "32");
        User user1 = new User("sa", "sdf",  "432", "fsdaa@gmail.com", "ffdgds", "32");
        Auction auction = new Auction("lol", "fs", cat, seller, user1, BigDecimal.valueOf(1000), 0, 0);
        BigDecimal bidUpAuction = AuctionController.bidUp(auction, BigDecimal.valueOf(1194), user1, testAuctionFilePath);
        BigDecimal bidUpAuction1 = AuctionController.bidUp(auction, BigDecimal.valueOf(1294), user1, testAuctionFilePath);
    }

    @Test(expected = AuctionIsFinishedException.class)
    public void testAuctionIsFinishedException() throws Exception {
        Category cat = new Category("Test");
        User seller = new User("sad", "sdf",  "432", "fsdaa@gmail.com", "dfgfds", "32");
        User user1 = new User("sa", "sdf",  "432", "fsdaa@gmail.com", "ffgdds", "32");
        Auction auction = new Auction("lol", "fs", cat, seller, seller, BigDecimal.valueOf(1000), 0, 2);
        AuctionController.bidUp(auction, BigDecimal.valueOf(1194), user1, testAuctionFilePath);
        AuctionController.bidUp(auction, BigDecimal.valueOf(1500), seller, testAuctionFilePath);
    }

    @Test(expected = AuctionPriceIsBelowZeroOrZeroException.class)
    public void testAuctionPriceIsBelowZeroOrZeroExceptionInCreateAuctionWhenZero() throws Exception {
        Auction testAuction1 = AuctionController.createAuction(testSeller, "Title1", "Description1", category, BigDecimal.valueOf(0.0), testAuctionFilePath);
    }

    @Test(expected = AuctionPriceIsBelowZeroOrZeroException.class)
    public void testAuctionPriceIsBelowZeroOrZeroExceptionInCreateAuctionWhenBelowZero() throws Exception {
        Auction testAuction1 = AuctionController.createAuction(testSeller, "Title1", "Description1", category, BigDecimal.valueOf(-5.0), testAuctionFilePath);
    }

    @Test
    public void testViewAuctionByCategories() throws Exception {
        Category allcategories = CategoryController.createCategoryTree();
        String input = "Zabawki";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Category testCat = AuctionController.viewAuctionByCategories(allcategories);
        assertEquals(testCat.getName(), "Zabawki");

        String input2 = "Samochody";
        InputStream in2 = new ByteArrayInputStream(input2.getBytes());
        System.setIn(in2);
        Category testCat2 = AuctionController.viewAuctionByCategories(allcategories);
        assertEquals(testCat2.getName(), "Samochody");
    }

    @Test
    public void testCreateAuctionMain() throws Exception {
        Category allcategories = CategoryController.createCategoryTree();
        systemInMock.provideLines("TestTitle", "TestDescription", "150", "Samochody");
        Auction newauction = AuctionController.createAuctionMain(testSeller, allcategories, testAuctionFilePath);
        assertEquals(newauction.getTitle(), "TestTitle");
        assertEquals(newauction.getDescription(), "TestDescription");
        assertEquals(newauction.getCategory().getName(), "Samochody");
        assertEquals(newauction.getPrice(), new BigDecimal(150));
        assertEquals(newauction.getSeller(), testSeller);
    }

    @After
    public void clearFile() throws IOException {
        FileWriter fw = new FileWriter(testAuctionFilePath, false);
        fw.write("");
        fw.close();
    }


}
