package pl.kurs.testdt5.helper;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("Could not find user " + login);
    }
}
