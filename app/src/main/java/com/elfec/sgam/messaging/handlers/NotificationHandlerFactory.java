package com.elfec.sgam.messaging.handlers;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by drodriguez on 07/06/2016.
 * Factory for notifications
 */
public class NotificationHandlerFactory {
    private static final String INSTALLATION_KEY = "installation";
    private static Map<String, Class<? extends INotificationHandler>> sInstances;

    /**
     * This class can't be instanced just use its {@link #create(String)} method
     */
    private NotificationHandlerFactory() {
    }

    static {
        sInstances = new HashMap<>();
        sInstances.put(INSTALLATION_KEY, InstallationNotificationHandler.class);
    }

    /**
     * Creates an instance of the respective handler for the given type of notification
     * @param type type of notification
     * @return the handler for the notification type, null if no handler registered
     *
     */
    public static @Nullable INotificationHandler create(String type) {
        Class<? extends INotificationHandler> clazz = sInstances.get(type);
        if (clazz == null)
            return null;
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new IllegalStateException(clazz.getName()+" must have a parameterless " +
                    "constructor");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
