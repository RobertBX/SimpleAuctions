package simpleAuctions.category;

import simpleAuctions.auction.Auction;
import simpleAuctions.exception.EmptyCategoryNameException;
import simpleAuctions.Database;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;


public class CategoryController {

    public static void printCategories(Category category, int level, PrintStream out) {
          if (category.getName() != null) {
            out.print("+");
            for (int i = 0; i < level; i++) {
                out.print("-");
            }
            out.println(category.getName());
        }
        for (Category subcategory : category.getSubcategories()) {
            printCategories(subcategory, level + 1, out);
        }
    }

    public static Category createCategoryTree() throws EmptyCategoryNameException {
        Database database = Database.getInstance();
        Category test = new Category ("Test");
        database.addCategoryToAllCategories(test);
        Category uberCat = new Category("All");
        database.addCategoryToAllCategories(uberCat);
        Category cars = new Category("Samochody");
        database.addCategoryToAllCategories(cars);
        Category electr = new Category("Elektronika");
        database.addCategoryToAllCategories(electr);
        Category toys = new Category("Zabawki");
        database.addCategoryToAllCategories(toys);
        Category tv = new Category("Telewizory");
        database.addCategoryToAllCategories(tv);
        Category mp3 = new Category("Empetrójki");
        database.addCategoryToAllCategories(mp3);
        Category horse = new Category("Elektroniczne konie");
        database.addCategoryToAllCategories(horse);
        Category bears = new Category("Misie pluszowe");
        database.addCategoryToAllCategories(bears);
        Category sm = new Category("Zabawki sadomaso");
        database.addCategoryToAllCategories(sm);
        Category sw = new Category("Zabawki z 'Gwiezdnych Wojen'");
        database.addCategoryToAllCategories(sw);
        Category hansolo = new Category("Zabawki z Hanem Solo");
        database.addCategoryToAllCategories(hansolo);
        Category stormtrooper = new Category("Stormtrooperzy");
        database.addCategoryToAllCategories(stormtrooper);
        uberCat.addSubcategory(toys);
        uberCat.addSubcategory(electr);
        toys.addSubcategory(bears);
        toys.addSubcategory(sm);
        toys.addSubcategory(sw);
        electr.addSubcategory(tv);
        electr.addSubcategory(mp3);
        electr.addSubcategory(horse);
        sw.addSubcategory(hansolo);
        sw.addSubcategory(stormtrooper);
        uberCat.addSubcategory(cars);
        return uberCat;
    }

    public static List<Auction> listAuctionsByCategory(Category cat) {
        LinkedList<Auction> auctions = new LinkedList<>();
        listAuctionsByCategoryRecurse(cat, auctions);
        return auctions;
    }

    private static void listAuctionsByCategoryRecurse(Category cat, List<Auction> auctions) {
        auctions.addAll(cat.getAuctions());
        for (Category subcategory : cat.getSubcategories()) {
            listAuctionsByCategoryRecurse(subcategory, auctions);
        }
    }
}
