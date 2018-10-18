package simpleAuctions.user;

import simpleAuctions.Database;


import java.io.BufferedReader;
import java.io.FileReader;

public class LoadUserFromDisk {
    private static LoadUserFromDisk instance;

    private LoadUserFromDisk() {
    }

    public static synchronized LoadUserFromDisk getInstance() {
        if (instance == null) {
            instance = new LoadUserFromDisk();
        }
        return instance;
    }

    private static final String COMMA_SEPARATOR = ",";
    private static final int USER_NAME = 0;
    private static final int USER_LASTNAME = 1;
    private static final int USER_ADDRESS = 2;
    private static final int USER_MAIL = 3;
    private static final int USER_PASSWORD = 4;
    private static final int USER_NICK = 5;

    public void readFileCSV(String fileName) throws Exception {
        Database database = Database.getInstance();
        BufferedReader fileReader = null;
        String line = "";
        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String[] data = line.split(COMMA_SEPARATOR);
                if (data.length > 0) {
                    User user = new User(data[USER_NAME], data[USER_LASTNAME], data[USER_ADDRESS],
                            data[USER_MAIL], data[USER_PASSWORD], data[USER_NICK]);
                    database.addUserToAllUsers(user);
                } else {
                    System.out.println("Wystąpił błąd przy wczytywaniu użytkownika");
                }
            }
        } catch (Exception e) {
            System.out.println("Wystąpił błąd przy wczytywaniu użytkownika");
        }
    }
}