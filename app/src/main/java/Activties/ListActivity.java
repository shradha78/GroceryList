package Activties;

import android.content.Intent;
import android.os.Bundle;

import com.example.grocery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHandler;
import Model.Grocery;
import UI.RecyclerViewAdapter;

public class ListActivity extends AppCompatActivity {
 private RecyclerView recyclerView;
 private RecyclerViewAdapter recyclerViewAdapter;
 private List<Grocery> groceryList;
 private List<Grocery> listItems;
 private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem,groceryQuantity;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopDialog();
            }
        });
        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();
        groceryList = db.getAllGroceries();
        for(Grocery g : groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(g.getName());
            grocery.setQty("Quantity: " + g.getQty());
            grocery.setId(g.getId());
            grocery.setDateAdded("Added on: " + g.getDateAdded() );

            listItems.add(grocery);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(this,listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }
    public void createPopDialog(){
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

                    saveGrocerytoDB(v);

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
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        },1000);
    }

}
