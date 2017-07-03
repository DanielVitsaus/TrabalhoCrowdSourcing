package com.example.daniel.videostreaming.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daniel.videostreaming.models.User;

public final class LibraryClass {
    public static String PREF = "PREF";
    //private static Firebase firebase;
    //private static Firebase firebase_chat;
    //private static FirebaseStorage storage;
    private static User user;


    private LibraryClass() {
    }


    /*
    public static Firebase getFirebase() {
        if (firebase == null) {
            firebase = new Firebase("https://chatduvidas.firebaseio.com/");
        }

        return (firebase);
    }

    public static Firebase getFirebase_chat() {
        if (firebase_chat == null) {
            firebase_chat = new Firebase("https://chatduvidas.firebaseio.com/");
        }

        return (firebase_chat);
    }

    public static FirebaseStorage getStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }

        return storage;
    }
    */

    public static User getUser() {
        if (user == null) {
            user = new User();
        }

        return (user);
    }


    static public void saveSP(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    static public String getSP(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return (token);
    }

    static public void removeSP(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

}
