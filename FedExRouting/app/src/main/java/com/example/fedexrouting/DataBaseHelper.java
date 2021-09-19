package com.example.fedexrouting;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

/**
 * Thomas Bridges, Charles Porter, Joel Bernhardt
 * Technocrats
 * FedExRouting
 *
 * Contributions
 * Charles Porter - 100%
 *
 * DataBaseHelper handles all of our database. Currently the ID and password is hardcoded in, but
 * if there was a server where this info was stored, the data would be pulled and checked against
 * that.
 * Currently this class creates the database and then is able to check if the info from the login
 * page matches a username and password
 *
 */

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String DRIVER_TABLE = "DRIVER_TABLE";
    public static final String COLUMN_DRIVER_ID = "DRIVER_ID";
    public static final String COLUMN_DRIVER_PASSWORD = "DRIVER_PASSWORD";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "driver.db", null, 1);
    }

    //code that generates new table
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creates the table
        String createTableStatement = "CREATE TABLE " + DRIVER_TABLE + " (" + COLUMN_DRIVER_ID + " INTEGER, " + COLUMN_DRIVER_PASSWORD + " TEXT)";
        db.execSQL(createTableStatement);

        //adds the username and passwords. any of these can be used as credentials for login
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (12563, 'zEKRL&^')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (78242, '@sMKyBo')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (97425, 'Gu!BjLm')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (11111, 'admin')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (15191, 'q&dm6fN')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (43512, '%$S*fj!')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (94523, 'W&9Zn&d')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (52512, '4&xRPuZ')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (58523, 'lP#fvQ#')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (94285, 'R$wGjjo')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (08234, '7t%HAgK')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (79242, 'Y4Jwme@')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (08412, 'QxJQA^%')");
        db.execSQL("INSERT INTO " + DRIVER_TABLE+ "(DRIVER_ID, DRIVER_PASSWORD) VALUES (57141, 'Ml@A%&P')");
    }

    //version of db changes -> upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //Checks to see if the info entered into the textfields matches the info that is stored in
    //the database
    public Boolean checkUserPass(String user, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DRIVER_TABLE + " WHERE " + COLUMN_DRIVER_ID + " = ? AND " + COLUMN_DRIVER_PASSWORD + " = ?", new String[] {user, pass});
        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }
}
