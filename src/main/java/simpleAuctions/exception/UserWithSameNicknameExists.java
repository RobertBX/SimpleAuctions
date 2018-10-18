package simpleAuctions.exception;

public class UserWithSameNicknameExists extends Exception {
    public UserWithSameNicknameExists () {
        System.out.println("Taki użytkownik już istnieje, podaj inny login");
    }
}
