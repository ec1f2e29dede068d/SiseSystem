package w.c.controller;

import java.util.ArrayList;

import w.c.data.SessionHandler;

public class MyController {

    private static SessionHandler sessionHandler;

    public static boolean login(String username, String password) {
        sessionHandler = new SessionHandler(username, password);
        return sessionHandler.login();
    }

    public static ArrayList<String> getSyllabus() {
        return sessionHandler.getSyllabus();
    }

    public static ArrayList<String> getSyllabus(int schoolYear, int semester) {
        return sessionHandler.getSyllabus(schoolYear, semester);
    }

    public static ArrayList<String> getPersonalInfo() {
        return sessionHandler.getPersonalInfoAndScore().get("personalInfo");
    }

    public static ArrayList<String> getScore() {
        return sessionHandler.getPersonalInfoAndScore().get("score");
    }

    public static ArrayList<String> getElectiveScore() {
        return sessionHandler.getPersonalInfoAndScore().get("electiveScore");
    }

    public static ArrayList<String> getAttendance() {
        return sessionHandler.getAttendance(0);
    }

    public static ArrayList<String> getTestTime() {
        return sessionHandler.getTestTime();
    }

    public static void getIndexContent() {
        sessionHandler.getIndexContent();
    }

    public static void logOut() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                sessionHandler.logOut();
            }
        }.start();
    }

}

