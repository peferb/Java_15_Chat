package se.peferb.javachat.util;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class LocalStorage {

    private SharedPreferences preferences;
    private Set<String> messageIds;

    // pref keys
    private final String MESSAGE_IDS = "messageIds";

    public LocalStorage(SharedPreferences preferences) {
        this.preferences = preferences;
        this.messageIds = getAlreadyStoredMessageIds();
    }

    private Set<String> getAlreadyStoredMessageIds() {
        return preferences.getStringSet(MESSAGE_IDS, new HashSet<String>());
    }

    public void add(String messageId) {
        messageIds.add(messageId);
        preferences.edit().putStringSet(MESSAGE_IDS, messageIds);
    }

    public boolean contains(String messageId) {
        return messageIds.contains(messageId);
    }
}
