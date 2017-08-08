package com.example.gallusawa.sqlitewithimage;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText firstname, lastname;
    Button image;
    TextView textView;
    DB_Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        textView = (TextView) findViewById(R.id.textView);

        controller = new DB_Controller(this, "", null, 1);


    }

    public void btn_click(View view) {
        switch (view.getId()){
            case R.id.button_getimage:
                break;

            case R.id.button_add:
                try{
                    controller.insert_student(firstname.getText().toString(), lastname.getText().toString());
                } catch (SQLException e) {
                    Toast.makeText(MainActivity.this, " ALREADY EXITS ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_delete:
                controller.delete_student(firstname.getText().toString());
                break;
            case R.id.button_update:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("ENTER NEW FIRSTNAME");

                final EditText new_firstname = new EditText(this);
                dialog.setView(new_firstname);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        controller.update_student(firstname.getText().toString(), new_firstname.getText().toString());

                    }

                });

                dialog.show();

                break;
            case R.id.button_list:
                controller.list_all_students(textView);
                break;
        }
    }

    /**
     * Created by gallusawa on 8/8/17.
     */

        public static class DB_Controller extends SQLiteOpenHelper {

            public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
                super(context, "TEST.db", factory, version);
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
}
