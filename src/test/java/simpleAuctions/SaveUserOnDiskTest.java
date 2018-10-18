package simpleAuctions;

import org.junit.Before;
import org.junit.Test;
import simpleAuctions.exception.*;
import simpleAuctions.user.LoadUserFromDisk;
import simpleAuctions.user.SaveUserOnDisk;
import simpleAuctions.user.User;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class SaveUserOnDiskTest {
    final String testUserFilePath = "TestDatabaseUser.csv";
    Database database = Database.getInstance();

    @Before
    public void clearCollections() throws IOException {
        database.getAllUsersByNickname().clear();
        database.getAllAuctionsByNumber().clear();
        database.getAllCategoriesByName().clear();
        try {
            FileWriter fw =new FileWriter("TestDatabaseUser.csv", false);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testUserSaving() throws UserWithSameNicknameExists, PasswordTooShortException, EmptyNickException,  IOException, IncorrectEmailFormatException {
        User testUser = new User("RandomName", "RandomLastName",  "RandomAddress", "RandomMail@gmail.com", "RandomPassword", "RandomNick");
        SaveUserOnDisk.getInstance().writeCsvFile(testUserFilePath, testUser);
        BufferedReader fr = new BufferedReader(new FileReader(testUserFilePath));
        assertEquals("RandomName,RandomLastName,RandomAddress,RandomMail@gmail.com,RandomPassword,RandomNick", fr.readLine());
    }

    @Test
    public void testUsersLoading() throws Exception {
        try {
            FileWriter fw =new FileWriter(testUserFilePath, false);
            fw.write("name,lastName,birthDay,address,mail,password,nick\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(database.getAllUsersByNickname().size(), 0);
        User testUser = new User("randomName", "randomLastName",  "randomAddress", "randomMail@gmail.com", "randomPassword", "randomNick");
        SaveUserOnDisk.getInstance().writeCsvFile(testUserFilePath, testUser);
        LoadUserFromDisk.getInstance().readFileCSV(testUserFilePath);
        Database database = Database.getInstance();
        User newUser = database.getUserByNickname("randomNick");
        assertEquals(testUser, database.getAllUsersByNickname().get("randomNick"));
        assertEquals(newUser.getName(), "randomName");
        assertEquals(database.getAllUsersByNickname().size(), 1);
    }
}





