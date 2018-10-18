package simpleAuctions.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;
import simpleAuctions.Database;
import simpleAuctions.auction.Auction;
import simpleAuctions.exception.*;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class User {

    public String getNick() {
        return nick;
    }

    private String name;
    private String lastName;
    private String address;
    private String mail;
    private String password;
    private String nick;

    public List<Auction> mySellingList = new LinkedList<Auction>();
    public List<Auction> myWonList = new LinkedList<Auction>();

    Database database = Database.getInstance();
    public User(String name, String lastName, String address, String mail, String password, String nick) throws PasswordTooShortException, EmptyNickException, UserWithSameNicknameExists, IncorrectEmailFormatException {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.mail = mail;
        this.password = password;
        if (password.length() < 5) {
            throw new PasswordTooShortException();
        }
        if (database.getUserByNickname(nick) == null) {
            this.nick = nick;
        } else {
            throw new UserWithSameNicknameExists();
        }
        if (nick.length() == 0) {
            throw new EmptyNickException();
        }
        boolean valid = EmailValidator.getInstance().isValid(mail);
        if (!valid) {
            throw new IncorrectEmailFormatException();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "nick='" + nick + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(address, user.address) &&
                Objects.equals(mail, user.mail) &&
                Objects.equals(password, user.password) &&
                Objects.equals(nick, user.nick);
    }
}
