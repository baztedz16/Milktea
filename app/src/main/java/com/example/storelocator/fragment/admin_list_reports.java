package com.example.storelocator.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storelocator.R;
import com.example.storelocator.adapter_rider_delivery;
import com.example.storelocator.helper_order_rider;
import com.example.storelocator.helper_review;
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
import com.google.android.gms.common.util.Strings;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class admin_list_reports extends Fragment {
    ArrayList<helper_order_rider> list;
    adapter_rider_delivery myAdapter;

    TextView rdate,totalTxt;
    EditText date1,date2;
    DatePickerDialog.OnDateSetListener startdate,enddate;
    BarChart barchartReport;
    PieChart pichartreport,pichartreport2;

    Button salesbtn,genreport;
    Spinner storelist;
    Query query1;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adminreport, container, false);
        date1 = view.findViewById(R.id.date1);
        date2 = view.findViewById(R.id.date2);
        barchartReport= view.findViewById(R.id.barchartReport);
        pichartreport = view.findViewById(R.id.pichartreport);
        pichartreport2 = view.findViewById(R.id.pichartreport2);
        salesbtn = view.findViewById(R.id.salesbtn);
        rdate = view.findViewById(R.id.rdate);
        genreport =view.findViewById(R.id.genreport);
        storelist= view.findViewById(R.id.storelist);
        totalTxt = view.findViewById(R.id.totalTxt);
        list = new ArrayList<>();
        myAdapter = new adapter_rider_delivery(view.getContext(),list);

        //defaultview();
        pieview();
        pieview2();
        liststore();
        genreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://dentalclinicmng.000webhostapp.com/firebase_milktea/index.php");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        date1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
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

        date2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
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
                if(date1.getText().toString().equals("") || date2.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Error:  Please Select Date Properly",Toast.LENGTH_SHORT).show();
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        Date strDate = sdf.parse(date1.getText().toString());
                        Date edDate = sdf.parse(date2.getText().toString());
                        if (edDate.after(strDate)) {
                            defaultview(date1.getText().toString(),date2.getText().toString());
                        }else{
                            Toast.makeText(getContext(),"Error:  End date is Greater Than Start Date",Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Log.i("Error Date: " , ""+e);
                    }
                }

            }
        });
//        barchartReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                defaultview(date1.getText().toString(),date2.getText().toString());
//            }
//        });
        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        if(accountype.equals("Rider")){
            pichartreport.setVisibility(View.VISIBLE);
        }else{
            pichartreport.setVisibility(View.VISIBLE);
        }
        // Inflate the layout for this fragment
        return view;



    }
    public void defaultview(String date1, String date2){
        query1=reference.child("orders").orderByChild("status").equalTo("5");

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    double salesTotal = 0.0;
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
                            if(storelist.getSelectedItem().toString().equals("All")){
                                if(dateformdb.after(startdate) && dateformdb.before(enddate)){
                                    if(map.containsKey(orders.getDate_order())){
                                        Double valueNew = Double.parseDouble(map.get(orders.getDate_order()))+ Double.parseDouble( orders.getOrder_total());
                                        map.put(orders.getDate_order(),String.valueOf(valueNew));

                                    }else{
                                        map.put(orders.getDate_order(),orders.getOrder_total());
                                    }
                                    salesTotal = salesTotal + Double.parseDouble(orders.getOrder_total());
                                    //Log.i("Get",orders.getOrder_id()+" @"+orders.getDate_order());
                                }else{
                                    //Log.i("Pass",orders.getOrder_id()+" @"+orders.getDate_order());
                                }
                            }else{
                                if(orders.getStore().equals(storelist.getSelectedItem().toString())){
                                    if(dateformdb.after(startdate) && dateformdb.before(enddate)){
                                        if(map.containsKey(orders.getDate_order())){
                                            Double valueNew = Double.parseDouble(map.get(orders.getDate_order()))+ Double.parseDouble( orders.getOrder_total());
                                            map.put(orders.getDate_order(),String.valueOf(valueNew));

                                        }else{
                                            map.put(orders.getDate_order(),orders.getOrder_total());
                                        }
                                        salesTotal = salesTotal + Double.parseDouble(orders.getOrder_total());
                                        //Log.i("Get",orders.getOrder_id()+" @"+orders.getDate_order());
                                    }else{
                                        //Log.i("Pass",orders.getOrder_id()+" @"+orders.getDate_order());
                                    }
                                }

                            }
                            totalTxt.setText("Total Sales: "+String.valueOf(salesTotal));
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
    public void liststore(){
        reference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();
                areas.clear();
                areas.add("All");
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    if(areaSnapshot.child("accountype").getValue(String.class).equals("Store Owner")){
                        String areaName = areaSnapshot.child("storename").getValue(String.class);
                        areas.add(areaName);
                    }

                }

                Spinner areaSpinner =storelist;
//                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, areas);
//                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                areaSpinner.setAdapter(areasAdapter);
                if (getContext()!=null) {
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areasAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void pieview(){
        query1=reference.child("users").orderByChild("activation").equalTo("1");

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    Log.i("R","4");
                    Map<String, Double> map = new TreeMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_user user = snapshot.getValue(helper_user.class);

                        String accounttype = user.getAccountype();

                        if(!user.getAccountype().equals("STAFF")){
                            if (user.getAccountype().equals("User")) {
                                accounttype = "Customers";
                            } else if (user.getAccountype().equals("Store Owner")) {
                                accounttype = "Shop Owners";
                            } else if (user.getAccountype().equals("Rider")) {
                                accounttype = "Delivery Guys";
                            }else{
                                Log.i("UserTPS:",accounttype.toString());
                                accounttype="Admin";
                            }
                            if(map.containsKey(accounttype) && !accounttype.equals("Admin")){
                                Double valueNew = map.get(accounttype) + 1;
                                map.put(accounttype,valueNew);
                            }else if (!map.containsKey(accounttype) && !accounttype.equals("Admin")){
                                map.put(accounttype,1.0);
                            }
                        }
//                        if(user.getAccountype().equals("STAFF")){
//
//                        }
                        Log.i("Get",map.toString());

                    }


                    ArrayList<PieEntry> AccountCnt = new ArrayList<>();

                    for (Map.Entry<String,Double> entry : map.entrySet()) {

                        AccountCnt.add(new PieEntry(entry.getValue().floatValue(),entry.getKey()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(AccountCnt, "User Account");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(16f);

                    PieData pieData = new PieData(pieDataSet);

                    pichartreport.setData(pieData);
                    pichartreport.getDescription().setEnabled(false);
                    pichartreport.setCenterText("Users");
                    pichartreport.animate();

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
    public void pieview2(){
        query1=reference.child("reviews");

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    Log.i("R","4");
                    Map<String, Double> map = new TreeMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_review review = snapshot.getValue(helper_review.class);

                        String accounttype;

                        accounttype = review.getStore();

                        if(review.getRatingtype().equals("Store")){
                            try {
                                if(map.containsKey(accounttype)){
                                    Double valueNew = map.get(accounttype) + Double.parseDouble(review.getRating_count());
                                    map.put(accounttype,valueNew);
                                }else{
                                    map.put(accounttype,1.0);
                                }
                            }catch (Exception e){
                                Log.i("Get",e.toString());
                            }
                            Log.i("Get",map.toString() + review.getStore());
                        }

                    }


                    ArrayList<PieEntry> AccountCnt = new ArrayList<>();

                    for (Map.Entry<String,Double> entry : map.entrySet()) {

                        AccountCnt.add(new PieEntry(entry.getValue().floatValue(),entry.getKey()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(AccountCnt, "Reviews");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(16f);

                    PieData pieData = new PieData(pieDataSet);

                    pichartreport2.setData(pieData);
                    pichartreport2.getDescription().setEnabled(false);
                    pichartreport2.setCenterText("Reviews");
                    pichartreport2.animate();

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
}