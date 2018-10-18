package simpleAuctions.exception;

public class NotFinalCategoryException extends Exception {
    public NotFinalCategoryException() {
        System.out.println("Wybrana kategoria posiada podkategorie. Wybierz kategorię finalną");
    }
}
