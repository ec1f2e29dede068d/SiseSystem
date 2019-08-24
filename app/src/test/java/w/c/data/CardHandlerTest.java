package w.c.data;

import org.junit.Test;

import java.util.Scanner;

public class CardHandlerTest {
    private CardHandler cardHandler = new CardHandler();

    @Test
    public void setCookie() {
        cardHandler.setCookie1();
    }

    @Test
    public void getRandomImg() {
        cardHandler.getRandomImg();
    }

    @Test
    public void login() {
        cardHandler.setCookie1();
        cardHandler.getRandomImg();
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        String password = scanner.nextLine();
        String checkCode = scanner.nextLine();
        scanner.close();
        cardHandler.login(username,checkCode);
    }
}
