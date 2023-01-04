package com.example.storelocator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class adapter_userlist extends RecyclerView.Adapter<adapter_userlist.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_user> list;
    public adapter_userlist(Context context, ArrayList<helper_user> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_account, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_user user = list.get(position);
        //holder.getOrder.setVisibility(View.INVISIBLE);
        holder.accountname.setText(user.getFullname());
        holder.accountype.setText(user.getAccountype());
       if(user.getActivation().equals("1")){
           holder.status.setText("Account Active");
       }else{
           holder.status.setText("Account Inactive");
       }
        //String orderid = (String) holder.orderid.getText();

        holder.activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Status","Activated");
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Your Activating This Account");
                alert.setTitle("Account Update");

                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("users").child(user.getUsername());
                        //reference.setValue("sample");
                        reference.child("activation").setValue("1");
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
        holder.deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Status","Deactivated");
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Your DeActivationg This Account");
                alert.setTitle("Account Update");

                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("users").child(user.getUsername());
                        //reference.setValue("sample");
                        reference.child("activation").setValue("0");
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView accountname,accountype,status;
        Button activate,deactivate;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            accountname = itemView.findViewById(R.id.AccountName);
            accountype = itemView.findViewById(R.id.Accountype);
            status = itemView.findViewById(R.id.status);
            activate = itemView.findViewById(R.id.activate);
            deactivate = itemView.findViewById(R.id.deactivate);
        }
    }
}
