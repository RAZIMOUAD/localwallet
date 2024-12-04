package org.example.utils;

public class SessionManager {

    private static Integer loggedInUserId;
    private static  String loggedInUsername;

    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    public static Integer getLoggedInUserId() {
        return loggedInUserId;
    }
    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static void clearSession() {
        loggedInUserId = null;
    }

    public static boolean isUserLoggedIn() {
        return loggedInUserId != null;
    }
}
