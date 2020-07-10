package Activties;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.grocery.R;

public class DetailsActivity extends AppCompatActivity {
private TextView itemNameDetails, qtyDetails, dateAddedDetails;
private int groceryid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        itemNameDetails = (TextView) findViewById(R.id.itemDetailsID);
        qtyDetails = (TextView) findViewById(R.id.QtyDetailsID);
        dateAddedDetails = (TextView) findViewById(R.id.dateAddedDetailsID);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            itemNameDetails.setText(bundle.getString("name"));
            qtyDetails.setText(bundle.getString("quantity"));
            dateAddedDetails.setText(bundle.getString("Date"));
            groceryid = bundle.getInt("Id");

        }
    }

}
