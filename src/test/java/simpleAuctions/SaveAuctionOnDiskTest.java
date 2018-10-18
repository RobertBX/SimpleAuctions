package simpleAuctions;

import org.junit.Before;
import org.junit.Test;
import simpleAuctions.auction.Auction;
import simpleAuctions.auction.LoadAuctionFromDisk;
import simpleAuctions.auction.SaveAuctionOnDisk;
import simpleAuctions.category.Category;
import simpleAuctions.category.CategoryController;
import simpleAuctions.user.User;

import java.io.*;
import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class SaveAuctionOnDiskTest {
    final String testAuctionFilePath = "TestDatabaseAuction.csv";
    Database database = Database.getInstance();
    SaveAuctionOnDisk save = SaveAuctionOnDisk.getInstance();

    User testSeller = new User("RandomName", "RandomLastName",  "RandomAddress", "RandomMail@gmail.com", "RandomPassword", "tomek");
    User testWinner = new User("RandomName2", "RandomLastName2",  "RandomAddress2", "RandomMail@gmail.pl", "RandomPassword2", "karolina");
    Category testCategory = new Category("Stormtrooperzy");
    Auction testAuction = new Auction("RandomTitle", "RandomDescription", testCategory, testSeller, testWinner, BigDecimal.valueOf(100), 1, 0);

    public SaveAuctionOnDiskTest() throws Exception {
    }

    @Before
    public void clearCollections() throws IOException {
        database.getAllUsersByNickname().clear();
        database.getAllAuctionsByNumber().clear();
        database.getAllCategoriesByName().clear();
        database.getAllAuctions().clear();
        try {
            FileWriter fw = new FileWriter(testAuctionFilePath, false);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveAuction () throws Exception {
        save.writeCsvFile(testAuctionFilePath, testAuction);
        BufferedReader fr = new BufferedReader(new FileReader(testAuctionFilePath));
        assertEquals("RandomTitle,RandomDescription,Stormtrooperzy,tomek,karolina,"+100+","+1+","+0, fr.readLine());
    }

    @Test
    public void testClearFile() throws IOException {
        save.writeCsvFile(testAuctionFilePath, testAuction);
        BufferedReader fr = new BufferedReader(new FileReader(testAuctionFilePath));
        save.clearFile(testAuctionFilePath);
        assertNull(fr.readLine());
    }

    @Test
    public void testLoadAuctionFromDisk () throws Exception {
        database.addUserToAllUsers(testSeller);
        database.addUserToAllUsers(testWinner);
        CategoryController.createCategoryTree();
        assertEquals(database.getAllAuctions().size(), 0);
        SaveAuctionOnDisk.getInstance().writeCsvFile(testAuctionFilePath, testAuction);
        LoadAuctionFromDisk.getInstance().loadAuctionCSV(testAuctionFilePath);
        assertEquals(database.getAllAuctions().size(), 1);
    }
}
