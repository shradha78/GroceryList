package UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import Activties.DetailsActivity;
import Data.DatabaseHandler;
import Model.Grocery;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
private Context context;
private List<Grocery> groceryItems;
private AlertDialog.Builder alertDialogBuilder;
private AlertDialog dialog;
private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryItems.get(position);
        holder.groceryName.setText(grocery.getName());
        holder.groceryQty.setText(grocery.getQty());
        holder.dateAdded.setText(grocery.getDateAdded());



    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView groceryName, groceryQty, dateAdded;
        public Button editButton, deleteButton;
        public int id;

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;
            groceryName = (TextView) view.findViewById(R.id.name);
            groceryQty = (TextView)   view.findViewById(R.id.qty);
            dateAdded = (TextView) view.findViewById(R.id.dateadded);
            editButton = (Button) view.findViewById(R.id.editId);
            deleteButton = (Button) view.findViewById(R.id.deleteId);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  int position = getAdapterPosition();
                  Grocery grocery = groceryItems.get(position);
                  Intent intent = new Intent(context, DetailsActivity.class);
                  intent.putExtra("name", grocery.getName());
                  intent.putExtra("quantity", grocery.getQty());
                  intent.putExtra("Id", grocery.getId());
                  intent.putExtra("Date", grocery.getDateAdded());
                  context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
               case R.id.editId :
                   int pos= getAdapterPosition();
                   Grocery gro = groceryItems.get(pos);
                   editItem(gro);

                break;
                case R.id.deleteId :
                    int position= getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    deleteItem(grocery.getId());
                    break;
            }

        }
        public void deleteItem(final int id){
           alertDialogBuilder = new AlertDialog.Builder(context);
           layoutInflater =LayoutInflater.from(context);
           View view = layoutInflater.inflate(R.layout.confirmation_dialog , null);
           Button noButton = (Button) view.findViewById(R.id.noButtonID);
           Button yesButton =(Button) view.findViewById(R.id.yesButtonID);
           alertDialogBuilder.setView(view);
           dialog = alertDialogBuilder.create();
           dialog.show();
           noButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                 dialog.dismiss();
               }
           });
           yesButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   DatabaseHandler db= new DatabaseHandler(context);
                   db.deleteGrocery(id);
                   groceryItems.remove(getAdapterPosition());
                   notifyItemRemoved(getAdapterPosition());
                   dialog.dismiss();

               }
           });
        }
        public void editItem(final Grocery grocery){
            alertDialogBuilder = new AlertDialog.Builder(context);
            layoutInflater =LayoutInflater.from(context);
            final View view = layoutInflater.inflate(R.layout.popup , null);
           final EditText groceryItem = (EditText) view.findViewById(R.id.groceryitem);
            final EditText groceryqty = (EditText) view.findViewById(R.id.groceryquantity);
            final TextView TileName = (TextView) view.findViewById(R.id.tile);
            TileName.setText("Edit Grocery");
            Button saveButton = (Button) view.findViewById(R.id.saveButton_id);
            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQty(groceryqty.getText().toString());
                    if(!groceryItem.getText().toString().isEmpty() && !groceryqty.getText().toString().isEmpty()){
                        db.UpdateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }
                    else
                    {
                        Snackbar.make(view,"Add item and quantity",Snackbar.LENGTH_LONG).show();
                    }
                dialog.dismiss();
                }
            });

        }
    }

}
