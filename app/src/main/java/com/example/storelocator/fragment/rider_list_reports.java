package com.example.storelocator.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storelocator.R;
import com.example.storelocator.adapter_rider_delivery;
import com.example.storelocator.adapter_rider_payables;
import com.example.storelocator.helper_order_rider;
import com.example.storelocator.helper_payables;
import com.example.storelocator.helper_user;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


public class rider_list_reports extends Fragment {
    ArrayList<helper_order_rider> list;
    adapter_rider_payables myAdapter;

    TextView rdate,totalpayables,status;
    EditText date1,date2,reportdate;
    DatePickerDialog.OnDateSetListener startdate,enddate,payabledate;
    BarChart barchartReport;
    PieChart pichartreport;
    Button salesbtn,payablesbtn,exportcsv1;
    RecyclerView listpayables;
    Query query1;


    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riderreport, container, false);
        date1 = view.findViewById(R.id.date1);
        date2 = view.findViewById(R.id.date2);
        reportdate = view.findViewById(R.id.reportdate);
        barchartReport= view.findViewById(R.id.barchartReport);
        salesbtn = view.findViewById(R.id.salesbtn);
        rdate = view.findViewById(R.id.rdate);
        listpayables = view.findViewById(R.id.listpayables);
        totalpayables = view.findViewById(R.id.totaltxt);
        status = view.findViewById(R.id.status);
        payablesbtn = view.findViewById(R.id.payablesbtn);


        listpayables.setHasFixedSize(true);
        listpayables.setLayoutManager(new LinearLayoutManager(view.getContext()));



        list = new ArrayList<>();
        myAdapter = new adapter_rider_payables(view.getContext(),list);
        listpayables.setAdapter(myAdapter);
        //defaultview();


        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startdate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        startdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String startdate = (month+1) + "/" +day+ "/" + year;
                date1.setText(startdate);
            }
        };

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        enddate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        enddate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String startdate = (month+1) + "/" +day+ "/" + year;
                date2.setText(startdate);
            }
        };

        salesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultview(date1.getText().toString(),date2.getText().toString());
            }
        });
//        barchartReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                defaultview(date1.getText().toString(),date2.getText().toString());
//            }
//        });
        payabledate= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String val = "";
                if(day <10){
                    val = "0"+String.valueOf(day);
                }else{
                    val =String.valueOf(day);
                }
                String startdate = (month+1) + "/" +val+ "/" + year;
//                String pattern = "MM-dd-yyyy";
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                reportdate.setText(startdate);
            }
        };
        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String ridername = preferences.getString("username","");
        reportdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        payabledate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        reportdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("musthappen","4");
                listpayables(reportdate.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        payablesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                if(!status.getText().equals("Under review") && !status.getText().equals("Approved")){
                    final EditText edittext = new EditText(getContext());
                    alert.setMessage("Enter Your Reference Number!");
                    alert.setTitle("Payment Process");

                    alert.setView(edittext);

                    alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //What ever you want to do with the value
                            Editable YouEditTextValue = edittext.getText();

                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("payables").child(ridername+reportdate.getText().toString().replace("/",""));
                            //reference.setValue("sample");
                            reference.child("date_topay").setValue(reportdate.getText().toString());
                            reference.child("reference_no").setValue(edittext.getText().toString());
                            reference.child("status").setValue("Under review");
                            reference.child("rider").setValue(ridername);
                            reference.child("amount").setValue(totalpayables.getText().toString());
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
                }else{
                }
            }
        });


        // Inflate the layout for this fragment
        return view;



    }
    public void defaultview(String date1, String date2){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String ridername = preferences.getString("username","");
        query1=reference.child("orders").orderByChild("status").equalTo("5");

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    Map<String, String> map = new TreeMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_order_rider orders = snapshot.getValue(helper_order_rider.class);
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        Date dateformdb,startdate,enddate;


                        try {
                            dateformdb = format.parse(orders.getDate_order());
                            startdate = format.parse(date1);
                            enddate = format.parse(date2);
                            if(dateformdb.after(startdate) && dateformdb.before(enddate)){
                                if(map.containsKey(orders.getDate_order()) && orders.getRider().equals(ridername)){
                                    Double valueNew = Double.parseDouble(map.get(orders.getDate_order()))+ Double.parseDouble( orders.getOrder_total());
                                    map.put(orders.getDate_order(),String.valueOf(valueNew));
                                }else{
                                    map.put(orders.getDate_order(),orders.getOrder_total());
                                }
                                //Log.i("Get",orders.getOrder_id()+" @"+orders.getDate_order());
                            }else{
                                //Log.i("Pass",orders.getOrder_id()+" @"+orders.getDate_order());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }


                    ArrayList<BarEntry> DailySales = new ArrayList<>();

                    for (Map.Entry<String,String> entry : map.entrySet()) {
                            String value = entry.getKey().replace("/","");
                            DailySales.add(new BarEntry(Integer.parseInt(value.substring(0,4)),Float.parseFloat(entry.getValue())));
                            Log.i("List Data:",""+Integer.parseInt(value.substring(0,4).toString()));
                    }
                    BarDataSet barDataSet = new BarDataSet(DailySales, "Date");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);

                    BarData barData = new BarData(barDataSet);

                    barchartReport.setFitBars(true);
                    barchartReport.setData(barData);
                    barchartReport.getDescription().setText("Daily Sales Report");
                    barchartReport.animateY(2000);
                    rdate.setText(map.entrySet().toString());
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void listpayables(String date){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");

        if(accountype.equals("Rider")){
            query1=reference.child("orders").orderByChild("status").equalTo("5");
        }else{
            query1=reference.child("orders");
        }

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalpayablesdata = 0.0;
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("payables","4"+date);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_order_rider orders = snapshot.getValue(helper_order_rider.class);

                        if(accountype.equals("STAFF") && orders.getDate_order().equals(date)){
                            if(orders.getStore().equals(staffstore)){
                                if(orders.getStore().equals(staffstore) ){
                                    list.add(orders);
                                    Log.i("R","1");
                                }
                            }
                        }else{
                            if(orders.getRider().equals(rider) && orders.getDate_order().equals(date)){

                                Log.i("2DATA",rider+":"+snapshot.child("rider").getValue().toString());
                                Log.i("2DATA",rider+":"+snapshot.child("order_id").getValue().toString());
                                list.add(orders);
                                totalpayablesdata = totalpayablesdata+Double.parseDouble(orders.getOrder_total().toString());
                            }
                        }
                    }
                    totalpayables.setText(String.valueOf(totalpayablesdata));
                    getpayablesdetails(date,rider);
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("payables","6");
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getpayablesdetails(String refdate,String ridername){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");


        try{
            query1=reference.child("payables");
            query1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.i("R",editTextname.getText().toString());
                    if (dataSnapshot.exists()) {
                        Log.i("R","4");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helper_payables payables = snapshot.getValue(helper_payables.class);
                            if(payables.getRider().equals(ridername) && payables.getDate_topay().equals(refdate)){
                                status.setText(payables.getStatus());
//                            Log.i("2DATA",rider+":"+snapshot.child("rider").getValue().toString());
//                            list.add(orders);
                            }else{
                                status.setText("No Pyament Made. !!!");
                            }
                        }
                    }else{

                        Log.i("R","6");
                        status.setText("No Pyament Made. !!!");
                        //Log.i("R",searchtext);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(Exception e){
            System.out.println(e);
        }

    }
}