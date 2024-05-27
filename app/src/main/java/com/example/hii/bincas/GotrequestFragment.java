package com.example.hii.bincas;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GotrequestFragment extends Fragment {
    //for list
    ListView listView;
    ArrayList<RequestFormat> reqarr;

    ViewGroup container;
    Context context;
    int i;

    //firebase vars
    FirebaseDatabase database ;
    DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        checkInternet();
        this.context=getActivity();
        this.container=container;
        return inflater.inflate(R.layout.gotrequest_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = container.findViewById(R.id.gotrequestlv);
        reqarr = new ArrayList<RequestFormat>();
        i=HomeActivity.i;
        database = FirebaseDatabase.getInstance();

        if(i==20){
            myRef = database.getReference("ServiceRequest");
        }
        else if(i==30){
            myRef = database.getReference("TestDrive");
        }
        //get data and set in liet view
    }

    @Override
    public void onResume() {
        super.onResume();

        //quety for data
        com.google.firebase.database.Query q = myRef.
                orderByChild("receiver").
                equalTo(HomeActivity.sender);
        q.addListenerForSingleValueEvent(valueeventlist);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertdiag = new AlertDialog.Builder(context);
                alertdiag.setTitle("Alert");
                alertdiag.setMessage("Do you want to accept request");
                alertdiag.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //null
                    }
                });

                alertdiag.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //code to delete account
                        RequestFormat srf = reqarr.get(position);
                        srf.setStatus("Proceed");
                        reqarr.get(position).setStatus("Proceed");
                        myRef.child(srf.getId()).setValue(srf)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //register or updated
                                        Toast.makeText(getActivity(),"Accepted",Toast.LENGTH_LONG).show();
                                        Toast.makeText(getActivity(),"Please Reload the page to see changes",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

                alertdiag.create().show();

            }
        });
    }

    ValueEventListener valueeventlist = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            reqarr.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestFormat req = snapshot.getValue(RequestFormat.class);
                    reqarr.add(req);
                }

                Myadapter myadapter = new Myadapter(context,reqarr);
                listView.setAdapter(myadapter);
            }
            else
                Toast.makeText(getActivity(), "No Records available", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };



    public void checkInternet() {
        int f = 0;
        ConnectivityManager cmo = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo nifo = cmo.getActiveNetworkInfo();

        if (nifo != null && nifo.isConnectedOrConnecting()) {
            NetworkInfo mobile = cmo.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobile != null && mobile.isConnectedOrConnecting())
                f = 1;
        } else
            f = 0;

        if (f == 0) {
            Toast.makeText(getActivity(), "Please check internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    class Myadapter extends ArrayAdapter<RequestFormat> {
        ArrayList<RequestFormat> reqarr;
        public Myadapter(Context context, ArrayList<RequestFormat> reqarr ) {
            super(context,R.layout.gorequestilist_row,reqarr);
            this.reqarr=reqarr;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.gorequestilist_row,parent,false);

            TextView t1,t2,t3;
            t1=row.findViewById(R.id.textView10);
            t2=row.findViewById(R.id.textView11);
            t3=row.findViewById(R.id.textView12);

            RequestFormat obj =reqarr.get(position);
            t1.setText(obj.getSender());
            t2.setText(obj.getTxt());
            t3.setText(obj.getStatus());
            return row;
        }

    }

}
