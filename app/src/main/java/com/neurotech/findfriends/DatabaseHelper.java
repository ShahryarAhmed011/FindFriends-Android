package com.neurotech.findfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "ContactsDB";
    private static final int DATABASE_VERSION = 2;

    //---------------Family Tree Table--------------------------------------------------
    private static final String CONTACT_NUMBERS_TABLE = "Contact_Numbers_Table";

    private static final String ID_COL = "id";
    private static final String DISPLAY_NAME_COL = "Display_Name";
    private static final String PHONE_NUMBER_COL = "Phone_number";

    public static DatabaseHelper mDatabaseHelper;

    public static synchronized DatabaseHelper getInstance(Context context) {

        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(context);
        }
        return mDatabaseHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String mFamilyTreeTable = "CREATE TABLE " + CONTACT_NUMBERS_TABLE + "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + DISPLAY_NAME_COL + " TEXT,"
                + PHONE_NUMBER_COL + " TEXT,"
                + " ''); ";

        Log.v("DatabaseHelper", "Family Tree Table Created" + " ");
        db.execSQL(mFamilyTreeTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_NUMBERS_TABLE);
        onCreate(db);
    }

    public void addContact(ContactNumber contactNumber) {


        ContentValues values = new ContentValues();
        values.put(DISPLAY_NAME_COL, String.valueOf(contactNumber.getmDisplayName()));
        values.put(PHONE_NUMBER_COL, String.valueOf(contactNumber.getmPhoneNumber()));


        SQLiteDatabase db = getWritableDatabase();
        db.insert(CONTACT_NUMBERS_TABLE, null, values);
        db.close();
    }


    public ArrayList<ContactNumber> getAllNumber() {

        ArrayList<ContactNumber> contactNumbersList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        String[] field = {DISPLAY_NAME_COL,
                PHONE_NUMBER_COL};

        Cursor cursor = db.query(CONTACT_NUMBERS_TABLE, field, null, null, null, null, null);

        int mDisplayName = cursor.getColumnIndex(DISPLAY_NAME_COL);
        int mPhoneNumber = cursor.getColumnIndex(PHONE_NUMBER_COL);


        // int mPositionInTreeNode = cursor.getColumnIndex(POSITION_IN_TREE_NODE_COL);


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String displayName = cursor.getString(mDisplayName);
            String phoneNumber = cursor.getString(mPhoneNumber);

            contactNumbersList.add(new ContactNumber(displayName, phoneNumber));
        }

        cursor.close();
        return contactNumbersList;

    }


    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, CONTACT_NUMBERS_TABLE);

        db.close();
        return count;
    }


    public boolean checkIfNodeExist() {

        SQLiteDatabase db = this.getWritableDatabase();

        String count = "SELECT count(*) FROM " + CONTACT_NUMBERS_TABLE;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            return true;

        }
//leave
        else {
            return false;
//populate table

        }


    }


    public void deleteAll(){

        SQLiteDatabase db = getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        db.delete(CONTACT_NUMBERS_TABLE, null, null);
        db.close();
    }

   }