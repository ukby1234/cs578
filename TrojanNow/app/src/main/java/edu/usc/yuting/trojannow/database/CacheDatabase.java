package edu.usc.yuting.trojannow.database;

import edu.usc.yuting.trojannow.login.User;

/**
 * Created by chengyey on 3/30/15.
 */
public class CacheDatabase {
    private static CacheDatabase instance = null;
    private User currentUser = null;
    private CacheDatabase() {
        /*
        A singleton constructor for a local cache database
        Initialize the connection to a database
         */
    }

    public static CacheDatabase getCacheDatabase() {
        /*
        A singleton stub for a database connection
         */
        if (instance == null) {
            instance = new CacheDatabase();
        }
        return instance;
    }

    public void queryDatabase(String query) {
        /*
        Query the local cache database, with ability to update and delete
         */
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
