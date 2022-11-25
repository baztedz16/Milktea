package com.example.storelocator;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class adapter_receivables extends RecyclerView.Adapter<adapter_receivables.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_payables> list;
    public adapter_receivables(Context context, ArrayList<helper_payables> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listreceivables, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_payables user = list.get(position);
        //holder.getOrder.setVisibility(View.INVISIBLE);
        holder.PaymentReference.setText(user.getRider()+user.getDate_topay().replace("/",""));
        holder.amount.setText(user.getAmount());
        holder.payref.setText(user.getReference_no());
        holder.rider.setText(user.getRider());
        //String orderid = (String) holder.orderid.getText();

        holder.approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Your About to acceot the Payment!");
                alert.setTitle("Payment Process");

                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("payables").child(user.getRider()+user.getDate_topay().replace("/",""));
                        //reference.setValue("sample");
                        reference.child("status").setValue("Approved");
//                    //OR
//                    String YouEditTextValue = edittext.getText().toString();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();


            }
        });
        holder.rject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    final EditText edittext = new EditText(context);
                    alert.setMessage("Enter Your Reference Number!");
                    alert.setTitle("Payment Process");

                    alert.setView(edittext);

                    alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //What ever you want to do with the value
                            Editable YouEditTextValue = edittext.getText();

                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("payables").child(user.getRider()+user.getDate_topay().replace("/",""));
                            //reference.setValue("sample");
                            reference.child("status").setValue(edittext.getText().toString());
//                    //OR
//                    String YouEditTextValue = edittext.getText().toString();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // what ever you want to do with No option.
                        }
                    });

                    alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView PaymentReference,amount,payref,rider;
        Button approved,rject;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            PaymentReference = itemView.findViewById(R.id.PaymentReference);
            amount = itemView.findViewById(R.id.amount);
            payref = itemView.findViewById(R.id.payref);
            rider = itemView.findViewById(R.id.rider);
            approved = itemView.findViewById(R.id.approved);
            rject = itemView.findViewById(R.id.rject);
        }
    }
}
