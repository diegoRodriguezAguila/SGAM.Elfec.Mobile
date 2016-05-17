package com.elfec.sgam.local_storage;

import com.elfec.sgam.model.User;

import rx.Observable;

/**
 * Se encarga de mantener los datos de usuario en almacenamiento local
 */
public class UserDataStorage {

    public static final String USER_BOOK = "user.book";

    /**
     * Saves a user to the database with all its subclasses, it doesn't execute inmediately,
     * it creates an observable to be execute in the future
     * @param user to save
     * @return Observable of user
     */
    public Observable<User> saveUser(User user) {
        return RxPaper.book(USER_BOOK).write(user.getUsername(), user);
    }

    /**
     * Retrieves the user from the database
     * @param username of the user to retrieve
     * @return Observable of user
     */
    public Observable<User> getUser(String username) {
        return RxPaper.book(USER_BOOK).read(username);
    }

    /**
     * Deletes a user from the database and all its subclasses, it doesn't execute inmediately,
     * it creates an observable to be execute in the future
     * @param username of the user to be deleted
     * @return Observable with the deleted value
     */
    public Observable<User> deleteUser(String username) {
        return RxPaper.book(USER_BOOK).delete(username);
    }
}
