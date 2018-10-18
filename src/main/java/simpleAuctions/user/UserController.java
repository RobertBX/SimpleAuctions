package simpleAuctions.user;

import simpleAuctions.Database;
import simpleAuctions.exception.*;

import java.util.Scanner;

public class UserController {

    public static User createUser(String name, String lastname, String address, String email, String password, String nick, String filepath) throws UserWithSameNicknameExists, PasswordTooShortException, EmptyNickException, IncorrectEmailFormatException {
        Database database = Database.getInstance();
        User newUser = new User(name, lastname, address, email, password, nick);
        database.addUserToAllUsers(newUser);
        SaveUserOnDisk.getInstance().writeCsvFile(filepath, newUser);
        return newUser;
    }

    public static User login(String login, String passwordd) throws WrongPasswordException {
        Database database = Database.getInstance();
        try {
            if (database.getUserByNickname(login).getPassword().equals(passwordd)) {
                return database.getUserByNickname(login);
            } else {
                throw new WrongPasswordException();
            }
        } catch (NullPointerException npe) {
            System.out.println("Nie ma takiego użytkownika");
            return null;
        }
    }

    public static User loginUserInteraction() throws WrongPasswordException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj login");
        String login = sc.next();
        System.out.println("Podaj hasło");
        String password = sc.next();
        return login(login, password);
    }

    public static User createUserMain(String filepath) throws PasswordTooShortException, EmptyNickException, InterruptedException, UserWithSameNicknameExists, IncorrectEmailFormatException {
        Database database = Database.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj Imię");
        String userName = scanner.nextLine();
        System.out.println("Podaj Nazwisko");
        String userLastName = scanner.next();
        System.out.println("Podaj adres zamieszkania");
        String userAdrdess = scanner.next();
        System.out.println("Podaj e-mail");
        String userMail = scanner.next();
        String userNick = null;
        while (userNick == null) {
            System.out.println("Podaj nick");
            userNick = scanner.next();
            if (database.getUserByNickname(userNick) != null) {
                System.out.println("Taki użytkownik już istnieje. Podaj inny nick");
                userNick = null;
            }
        }
        System.out.println("Podaj hasło");
        String password = null;
        password = scanner.next();
        System.out.println("Powtórz hasło");
        String password2 = scanner.next();
        while (true) {
            if (password.equals(password2)) {
                break;
            } else {
                System.out.println("Hasła nie są takie same. Wpisz poprawne hasło");
                password2 = scanner.next();
            }
        }
        User newUser = createUser(userName, userLastName, userAdrdess, userMail, password, userNick, filepath);
        System.out.println("Zarejestrowano nowego użytkownika");
        return newUser;
    }
}
