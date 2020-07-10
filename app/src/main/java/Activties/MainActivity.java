package Activties;

import android.content.Intent;
import android.os.Bundle;

import com.example.grocery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import Data.DatabaseHandler;
import Model.Grocery;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem,groceryQuantity;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        byPassActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopupDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void createPopupDialog(){
        dialogBuilder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        groceryItem=(EditText)view.findViewById(R.id.groceryitem);
        groceryQuantity=(EditText)view.findViewById(R.id.groceryquantity);
        saveButton=(Button)view.findViewById(R.id.saveButton_id);
        dialogBuilder.setView(view);
        dialog=dialogBuilder.create();
        dialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save to database
                if(!groceryItem.getText().toString().isEmpty() && !groceryQuantity.getText().toString().isEmpty()){
                    saveGrocerytoDB(v);
                }

            }
        });


    }

    private void saveGrocerytoDB(View v) {
        Grocery grocery = new Grocery();
        String newGrocery = groceryItem.getText().toString();
        String newGroceryQuantity = groceryQuantity.getText().toString();
        grocery.setName(newGrocery);
        grocery.setQty(newGroceryQuantity);
           db.addGrocery(grocery);
       Snackbar.make(v, "Item Saved!!!", Snackbar.LENGTH_LONG).show();
      // Log.d("Item Added ID: " , String.valueOf(db.countGrocery()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,ListActivity.class));

            }
        },1200);
    }
    public void byPassActivity(){
        if(db.countGrocery()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }
}
