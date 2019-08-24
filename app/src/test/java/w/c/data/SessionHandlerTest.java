package w.c.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SessionHandlerTest {
    SessionHandler sessionHandler;

    @Before
    public void setUp() throws Exception {
        System.out.println("before");
        sessionHandler = new SessionHandler("1740128143", "GKLwc51337hrxy");
        sessionHandler.login();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("after");
        sessionHandler.logOut();
    }

    @Test
    public void login() {
    }

    @Test
    public void getSyllabus() {
        System.out.println(sessionHandler.getSyllabus(2018, 1).size());
    }

    @Test
    public void logOut() {
    }

    @Test
    public void getIndexContent() {
    }

    @Test
    public void getPersonalInfoAndScore() {
    }

    @Test
    public void getAttendance() {
    }

    @Test
    public void getTestTime() {
    }

    @Test
    public void getViolation() {
    }
}