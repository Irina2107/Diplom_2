package user;
import org.apache.commons.lang3.RandomStringUtils;
public class UserGenerator {
    public User generic() {
        return new User("Tester", "P@ssw0rd123", "Ivan");
    }
    public User random() {
        return new User(RandomStringUtils.randomAlphanumeric(5, 10)+"@yandex.ru", "P@ssw0rd123", "Ivan"+RandomStringUtils.randomAlphanumeric(5, 10));
    }
}
