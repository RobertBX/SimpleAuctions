package simpleAuctions;

import simpleAuctions.auction.Auction;
import simpleAuctions.auction.AuctionController;
import simpleAuctions.auction.LoadAuctionFromDisk;
import simpleAuctions.category.Category;
import simpleAuctions.category.CategoryController;
import simpleAuctions.exception.*;
import simpleAuctions.user.*;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.out;
import static simpleAuctions.Main.State.LOGGED_IN;

public class Main {

    public enum State {
        INIT,
        DURING_LOGIN,
        DURING_REGISTRATION,
        DURING_VIEVING_AUCTION,
        LOGGED_IN,
        STOP,
    }

    public static void main(String[] args) throws Exception {
        final String auctionsFilePath = "databaseAuction.csv";
        final String usersFilePath = "databaseUser.csv";
        Database database = Database.getInstance();
        Category allcategories = CategoryController.createCategoryTree();
        LoadUserFromDisk.getInstance().readFileCSV(usersFilePath);
        LoadAuctionFromDisk.getInstance().loadAuctionCSV(auctionsFilePath);
        Scanner sc = new Scanner(System.in);
        User currentUser = null;
        State state = State.INIT;
        while (state != State.STOP) {
            try {
                switch (state) {
                    case INIT: {
                        printInitialMenu();
                        int answer = sc.nextInt();
                        switch (answer) {
                            case 1:
                                state = State.DURING_LOGIN;
                                break;
                            case 2:
                                state = State.DURING_REGISTRATION;
                                break;
                            case 0:
                                state = State.STOP;
                                break;
                            default:
                                System.out.println("Zła odpowiedź");
                                state = State.INIT;
                                break;
                        }
                        break;
                    }
                    case DURING_LOGIN: {
                        try {
                            currentUser = UserController.loginUserInteraction();
                        } catch (WrongPasswordException e) {
                            break;
                        }
                        if (currentUser != null) {
                            state = LOGGED_IN;
                        } else {
                            state = State.DURING_LOGIN;
                        }
                        break;
                    }
                    case DURING_REGISTRATION: {
                        try {
                            currentUser = UserController.createUserMain(usersFilePath);
                        } catch (PasswordTooShortException | IncorrectEmailFormatException e) {
                            break;
                        }
                        if (currentUser != null) {
                            state = LOGGED_IN;
                            break;
                        }
                        state = State.DURING_REGISTRATION;
                        break;
                    }
                    case STOP: {
                        state = State.STOP;
                    }
                    case DURING_VIEVING_AUCTION: {
                        System.out.println("Podaj nr aukcji, która Cię interesuje");
                        Auction currentAuction = database.getAuctionByNumber(sc.nextInt());
                        printAuctionMenu();
                        int choice = sc.nextInt();
                        try {
                            if (choice == 1) {
                                printMenuForBidding(currentAuction.getTitle(), currentAuction.getPrice());
                                BigDecimal price = new BigDecimal(sc.nextDouble());
                                try {
                                    AuctionController.bidUp(currentAuction, price, currentUser, auctionsFilePath);
                                } catch (YouCantBidUpYourOwnAuctionException | AuctionIsFinishedException e) {
                                    break;
                                }

                            } else if (choice == 2) {
                                System.out.println(currentAuction.getDescription());
                                break;
                            } else {
                                break;
                            }
                        } catch (NullPointerException npe) {
                            System.out.println("Wybrałeś błędny numer aukcji");
                            break;
                        }
                    }
                    case LOGGED_IN: {
                        printLoggedInMenu();
                        int answer = sc.nextInt();
                        switch (answer) {
                            case 1: {
                                try {
                                    AuctionController.createAuctionMain(currentUser, allcategories, auctionsFilePath);
                                } catch (EmptyTitleException | EmptyDescriptionException | AuctionPriceIsBelowZeroOrZeroException e) {
                                    break;
                                }
                                break;
                            }
                            case 2: {
                                CategoryController.printCategories(allcategories, 0, out);
                                System.out.println("Wybierz kategorię");
                                sc.nextLine();
                                String chosenCat = sc.nextLine();
                                try {
                                    System.out.println(CategoryController.listAuctionsByCategory(database.getCategoryByName(chosenCat)));
                                } catch (NullPointerException npe) {
                                    System.out.println("Nie ma takiej kategorii");
                                    break;
                                }
                                state = State.DURING_VIEVING_AUCTION;
                                break;
                            }
                            case 3: {
                                System.out.println(AuctionController.viewSellersAuctions(currentUser));
                                break;
                            }
                            case 4: {
                                System.out.println(AuctionController.viewWonAuctions(currentUser));
                                break;
                            }
                            case 5: {
                                state = State.INIT;
                                break;
                            }
                            default: {
                                System.out.println("Zła odpowiedź");
                                state = LOGGED_IN;
                                break;
                            }
                        }
                        break;
                    }
                }
            } catch (InputMismatchException ime) {
                System.out.println("Zła odpowiedź");
                state = State.INIT;
                sc.next();
            }
        }
    }

    public static void printInitialMenu() {
        System.out.println("Dzień dobry,\n Co chcesz zrobić ? \n 1 - Zaloguj się \n 2 - Zarejestruj się \n 0 - wyjdź");
    }

    public static void printLoggedInMenu() {
        System.out.println("Co chcesz zrobić? \n 1. Wystaw przedmiot \n 2. Pokaż aukcje wg kategorii \n " +
                "3. Wyświetl moje aukcje \n 4. Wyświetl aukcje, które wygrałem \n 5. Wyloguj  się");
    }

    public static void printAuctionMenu() {
        System.out.println("Co chcesz zrobic ? \n 1.Zalicytuj \n 2.Zobacz opis \n 0.wróć");
    }

    public static void printMenuForBidding(String auctionTitle, BigDecimal currentprice) {
        System.out.println("Licytujesz przedmiot: " + auctionTitle + ". Obecna kwota: " + currentprice + ". Podaj kwotę");
    }
}