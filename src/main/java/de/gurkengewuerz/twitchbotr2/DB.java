package de.gurkengewuerz.twitchbotr2;

import java.io.File;
import java.sql.Connection;

/**
 * Created by gurkengewuerz.de on 18.12.2016.
 */
public class DB {

    private boolean connected = false;
    private File sqliteFile;
    private Connection con;

    public DB(File sqliteFile) {
        this.sqliteFile = sqliteFile;
    }
}
