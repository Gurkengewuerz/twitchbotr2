package de.gurkengewuerz.twitchbotr2.database;

import java.io.File;
import java.util.HashMap;

/**
 * Created by gurkengewuerz.de on 18.12.2016.
 */
public class DB {
    private static HashMap<String, SQLite> databases = new HashMap<>();
    static {
        databases.put("main", new SQLite(new File("./data.db")));
    }

    public static SQLite get(String name){
        return databases.get(name);
    }
}
