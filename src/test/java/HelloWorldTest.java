import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void testRestAssured() {
        String string = "вкрыфкрыкрыкрырырыу";

        Assertions.assertTrue(string.length()>=15, "Строка не длиннее 15 символов");

    }
}
