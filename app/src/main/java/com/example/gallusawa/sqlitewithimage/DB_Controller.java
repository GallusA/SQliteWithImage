package com.example.gallusawa.sqlitewithimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import static android.R.attr.data;

/**
 * Created by gallusawa on 8/8/17.
 */

public class DB_Controller extends SQLiteOpenHelper {

    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "TEST.db", factory, version);
    }

    public void queryData(String sql) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public void insertData(String firstname, String lastname, byte[] image) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO STUDENTS VALUES (NULL, ?,?,?)";

        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);

        statement.bindString(1, firstname);
        statement.bindString(2, lastname);
        statement.bindBlob(3, image);

        statement.executeInsert();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql, null);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE STUDENTS( ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT UNIQUE, LASTNAME TEXT); ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP THE TABLE IF EXISTS STUDENT; ");
        onCreate(sqLiteDatabase);
    }

    public void insert_student(String firstname, String lastname) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("FIRSTNAME", firstname);
        contentValues.put("LASTNAME", lastname);
     /*   Imagehelper help=new Imagehelper(this);

        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            help.insert(byteArray);
       */

        this.getWritableDatabase().insertOrThrow("STUDENT", "", contentValues);


    }
    public void delete_student(String firstname){
        this.getWritableDatabase().delete("STUDENT", "FIRSTNAME='" + firstname + " ' ", null);
    }

    public void update_student(String old_firstname, String new_firstname){
        this.getWritableDatabase().execSQL("UPDATE STUDENT SET FIRSTNAME= ' " + new_firstname + " ' WHERE FIRSTNAME = ' " + old_firstname + " ' ");
    }
    public void list_all_students(TextView textView) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM STUDENTS", null);

        textView.setText("");

        while(cursor.moveToNext()){
            textView.append(cursor.getString(1) + " " + cursor.getString(2) + "\n");

        }
    }
}
