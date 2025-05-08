package com.grpcstreamings.resturantapp.util;

import com.grpcstreamings.resturantapp.model.User;

public class SessionManager {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
