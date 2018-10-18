package simpleAuctions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import simpleAuctions.exception.*;
import simpleAuctions.user.User;
import simpleAuctions.user.UserController;

import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class UserControllerTest {
    final String testUserFilePath = "TestDatabaseUser.csv";
    Database database = Database.getInstance();

    public UserControllerTest() throws PasswordTooShortException, EmptyNickException, UserWithSameNicknameExists, IncorrectEmailFormatException {
    }

    @Before
    public void clearCollections() {
        database.getAllUsersByNickname().clear();
        database.getAllCategoriesByName().clear();
        database.getAllAuctionsByNumber().clear();
    }

    @Before
    public void clearFile() {
        try {
            FileWriter fw = new FileWriter(testUserFilePath, false);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public final TextFromStandardInputStream systemInMock
            = emptyStandardInputStream();

    @Test
    public void testCreateUser() throws UserWithSameNicknameExists, EmptyNickException, PasswordTooShortException, EmptyCategoryNameException, EmptyTitleException, EmptyDescriptionException, AuctionPriceIsBelowZeroOrZeroException, IncorrectEmailFormatException {
        User testUser = UserController.createUser("Test", "Testson",  "Testtown", "Test@test.com", "testpass", "tester", testUserFilePath);
        assertEquals(testUser.getName(), "Test");
        assertEquals(testUser.getLastName(), "Testson");
        assertEquals(testUser.getAddress(), "Testtown");
        assertEquals(testUser.getMail(), "Test@test.com");
        assertEquals(testUser.getPassword(), "testpass");
        assertEquals(testUser.getNick(), "tester");
        assertEquals(testUser, database.getUserByNickname("tester"));
        assertNotNull(database.getUserByNickname("tester").getMySellingList());
        assertNotNull(database.getUserByNickname("tester").getMyWonList());
    }

    @Test
    public void testLogin() throws Exception {
        User testSeller = new User("", "",  "", "eee@eee.com", "SellerPassword", "Seller");
        database.addUserToAllUsers(testSeller);
        assertEquals(UserController.login("Seller", "SellerPassword"), testSeller);
        assertEquals(UserController.login("nonExistingDude", "noPassword"), null);
    }

    @Test(expected = EmptyNickException.class)
    public void testIsEmptyNickExceptionIsThrown() throws Exception {
        User testUser = new User("re", "reg",  "konwaliowa", "gdf@ooo.com", "testtest", "");
    }

    @Test(expected = PasswordTooShortException.class)
    public void testIsPasswordToShortExceptionIsThrown() throws Exception {
        User testUser = new User("re", "reg",  "konwaliowa", "gdf@oo.com", "test", "ijaj");
    }

    @Test(expected = UserWithSameNicknameExists.class)
    public void testIfUserWithSameNicknameExists() throws Exception {
        User testUser = new User("re", "reg",  "konwaliowa", "gdfdd@odo.com", "testtest", "mateusz");
        database.addUserToAllUsers(testUser);
        User testUser2 = new User("rde", "rdeg",  "konwaliowad", "gdfh@sds.com", "testbtest", "mateusz");
    }

    @Test(expected = IncorrectEmailFormatException.class)
    public void testIfEmailExceptionIsThrownWhenEmailAddressIsRandom() throws Exception {
        User testUser = new User("re", "reg",  "konwaliowa", "exceptionplease", "testtest", "mateusz");
    }

    @Test(expected = IncorrectEmailFormatException.class)
    public void testIfEmailExceptionIsThrownWhenEmailAddressLacksAt() throws Exception {
        User testUser = new User("re", "reg",  "konwaliowa", "exceptionplease.com", "testtest", "mateusz");
    }

    @Test(expected = IncorrectEmailFormatException.class)
    public void testIfEmailExceptionIsThrownWhenEmailAddressLacksEnding() throws Exception {
        User testUser = new User("re", "reg",  "konwaliowa", "exception@please", "testtest", "mateusz");
    }

    @Test(expected = IncorrectEmailFormatException.class)
    public void testIfEmailExceptionIsThrownWhenWeirdDigitsAreUsed() throws Exception {
        User testUser = new User("re", "reg",  "konwaliowa", "ęxceptióń@please??ł#%((]}żź?.ćóm", "testtest", "mateusz");
    }

    @Test(expected = WrongPasswordException.class)
    public void testIfWrongPasswordExceptionIsThrownDuringLogin() throws Exception {
        User user = new User(null, null,  null, "Test@test.com", "password", "mytestnickname");
        database.addUserToAllUsers(user);
        UserController.login("mytestnickname", "WrongPassword");
    }

    @Test
    public void testCreateUserMain () throws Exception {
        systemInMock.provideLines("TestName", "TestLastName", "TestAddress", "test@test.com", "testNick", "testPass", "testPass");
        User testUser = UserController.createUserMain(testUserFilePath);

        assertEquals(testUser.getNick(), "testNick");
        assertEquals(testUser.getPassword(), "testPass");
        assertEquals(testUser.getMail(), "test@test.com");
        assertEquals(testUser.getName(), "TestName");
        assertEquals(testUser.getLastName(), "TestLastName");
        assertEquals(testUser.getAddress(), "TestAddress");
    }

    @Test
    public void testLoginUserInteraction() throws Exception {
        User testUser = new User("", "",  "", "mymsil@mymail.com", "testpass", "testnick");
        database.addUserToAllUsers(testUser);
        systemInMock.provideLines("testnick", "testpass");
        User loggedInUser = UserController.loginUserInteraction();
        assertEquals(loggedInUser, testUser);
    }

}
