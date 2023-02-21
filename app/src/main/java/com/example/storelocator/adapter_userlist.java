package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storelocator.email.GMailSender;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

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
//        holder.accountype.setText(user.getAccountype());
//        holder.storeListAdd.setText(user.getStorename());

        if (user.getAccountype().equals("STAFF")) {
            holder.accountype.setText("Staff");
            holder.storeListAdd.setText(user.getStorename());
        } else if (user.getAccountype().equals("Rider")) {
            holder.accountype.setVisibility(View.GONE);
            holder.storeListAdd.setText("Delivery Guy");
        } else if (user.getAccountype().equals("Store Owner")) {
            holder.accountype.setVisibility(View.VISIBLE);
            holder.accountype.setText(user.getFullname());
            holder.accountname.setText(user.getStorename());
            holder.storeListAdd.setText("Shop Owner");
        } else {
            holder.accountype.setText("Customer");
            holder.storeListAdd.setText("");
        }

        if(user.getAccountype().equals("Store Owner")){
            holder.ratings.setVisibility(View.VISIBLE);
        }else{
            holder.ratings.setVisibility(View.INVISIBLE);
        }
        if(user.getActivation().equals("1")){
            holder.status.setText("Account Active");
        }else{
            holder.status.setText("Account Inactive");
        }
        if(user.getActivation().equals("0")){
            holder.activate.setVisibility(View.VISIBLE);
            holder.deactivate.setVisibility(View.INVISIBLE);
        }else{
            holder.activate.setVisibility(View.INVISIBLE);
            holder.deactivate.setVisibility(View.VISIBLE);
        }
        //String orderid = (String) holder.orderid.getText();

        holder.ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,activity_storeratings.class);
                intent.putExtra("storeSelect",user.getStorename());
                context.startActivity(intent);
            }
        });
        holder.accountname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,signupstaff.class);
                intent.putExtra("storeSelect",user.getStorename());
                intent.putExtra("fullname",user.getFullname());
                intent.putExtra("username",user.getUsername());
                intent.putExtra("email",user.getEmail());
                intent.putExtra("password",user.getPassword());
                intent.putExtra("phone",user.getPhone());
                intent.putExtra("address",user.getAddress());
                intent.putExtra("long",user.getDestlong());
                intent.putExtra("lat",user.getDestlat());
                intent.putExtra("hasdata","1");
                context.startActivity(intent);
            }
        });

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
                        Toast.makeText(view.getContext(), "Account is now active.",Toast.LENGTH_SHORT).show();
//                    //OR
//                    String YouEditTextValue = edittext.getText().toString();

                        String username = "storelocator2023@gmail.com";
                        String password = "ceqcpxxmxadyvzod";

                        sendEmail(username,
                                password,
                                user.getEmail(),
                                "Message from Your Friendly Milktea App",
                                "Hello " + user.getFullname() + ", You have account has been activated. Feel free to use the account anytime! =)");
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
                alert.setMessage("Your Deactivating This Account");
                alert.setTitle("Account Update");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("users").child(user.getUsername());
                        //reference.setValue("sample");
                        reference.child("activation").setValue("0");
                        Toast.makeText(view.getContext(), "Account is now inactive.",Toast.LENGTH_SHORT).show();
//                    //OR
//                    String YouEditTextValue = edittext.getText().toString();

                        String username = "storelocator2023@gmail.com";
                        String password = "ceqcpxxmxadyvzod";

                        sendEmail(username,
                                password,
                                user.getEmail(),
                                "Message from Your Friendly Milktea App",
                                "Hello " + user.getFullname() + ", You have account has been deactivated. Please contact administrator for details.");

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
        TextView accountname,accountype,status,storeListAdd;
        Button activate,deactivate,ratings;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            accountname = itemView.findViewById(R.id.AccountName);
            accountype = itemView.findViewById(R.id.Accountype);
            status = itemView.findViewById(R.id.status);
            activate = itemView.findViewById(R.id.activate);
            deactivate = itemView.findViewById(R.id.deactivate);
            ratings = itemView.findViewById(R.id.ratings);
            storeListAdd = itemView.findViewById(R.id.storeListAdd);
        }
    }



    private void sendEmail(final String Sender,final String Password,final String Receiver,final String Title,final String Message)
    {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(Sender,Password);
                    sender.sendMail(Title, "<b>"+Message+"</b>", Sender, Receiver);
//                    makeAlert(Receiver);

                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }
//    private void makeAlert(String email){
//        this.runOnUiThread(new Runnable() {
//            public void run() {
//                System.out.println("Mail Sent to "+email+".");
//            }
//        });
//    }
}
