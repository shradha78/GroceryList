package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Grocery;
import Util.Constants;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context ctx;
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_Name , null , Constants.DB_Version);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Grocery_Table ="CREATE TABLE IF NOT EXISTS " + Constants.Table_Name + "(" + Constants.Key_ID + " INTEGER PRIMARY KEY,"
                                       + Constants.Key_grocery_name + " TEXT," + Constants.Key_qty_number + " TEXT," + Constants.Key_date_added
                                         + " LONG);";
        db.execSQL(Create_Grocery_Table);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.Table_Name);
        onCreate(db);
    }
    //Add Grocery
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.Key_grocery_name,grocery.getName());
        values.put(Constants.Key_qty_number,grocery.getQty());
        values.put(Constants.Key_date_added,java.lang.System.currentTimeMillis());
        //add row
        db.insert(Constants.Table_Name,null,values);
        Log.d("Saved!! ","Saved to DB");
    }
    //get Grocery
    public Grocery getGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constants.Table_Name, new String[] {Constants.Key_ID, Constants.Key_grocery_name,Constants.Key_qty_number,
                                Constants.Key_date_added},Constants.Key_ID + "=?" ,new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();

            Grocery grocery =new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.Key_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.Key_grocery_name)));
            grocery.setQty(cursor.getString(cursor.getColumnIndex(Constants.Key_qty_number)));
            //convert timestamp to something readable
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.Key_date_added))).getTime());
            grocery.setDateAdded(formatedDate);

            return grocery;
    }
    //get All Groceries
    public List<Grocery> getAllGroceries(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> list= new ArrayList<>();
        Cursor cursor = db.query(Constants.Table_Name, new String[] {Constants.Key_ID, Constants.Key_grocery_name,Constants.Key_qty_number,
                Constants.Key_date_added},null, null,null,null,Constants.Key_date_added + " DESC");
        if(cursor.moveToFirst()){
            do{
                Grocery grocery =new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.Key_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.Key_grocery_name)));
                grocery.setQty(cursor.getString(cursor.getColumnIndex(Constants.Key_qty_number)));
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.Key_date_added))).getTime());
                grocery.setDateAdded(formatedDate);

                list.add(grocery);
            }while (cursor.moveToNext());
        }
        return list;
    }
    //Updated Grocery
    public int UpdateGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.Key_grocery_name,grocery.getName());
        values.put(Constants.Key_qty_number,grocery.getQty());
        values.put(Constants.Key_date_added,java.lang.System.currentTimeMillis());
        //update row
        return db.update(Constants.Table_Name, values, Constants.Key_ID + "=?", new String[]{String.valueOf(grocery.getId())});

    }
    public void deleteGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.Table_Name,Constants.Key_ID + "=?" ,new String[]{String.valueOf(id)});
        db.close();


    }
    //get COunt
    public int countGrocery(){
        String countQuery="SELECT * FROM " + Constants.Table_Name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);
//        cursor.close();
        return cursor.getCount();

    }
}
