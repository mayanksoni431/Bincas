package com.example.hii.bincas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Random;

public class description_fragment extends Fragment {
    public int i;

    public static String rec;
    private ViewGroup container;
    private EditText edt;
    private TextView txtv;
    private Context context;
    //firebase variables
    FirebaseDatabase database ;
    DatabaseReference myRef;

    //listview items
    ListView prelist;
    ArrayList<RequestFormat> reqarr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        checkInternet();
        this.context=getContext();
        this.container=container;
        return inflater.inflate(R.layout.decription_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //firebase
        edt= container.findViewById(R.id.desfraedittxt);
        txtv=container.findViewById(R.id.desfratxtbtn);
        prelist=container.findViewById(R.id.descfralistview);

        //manu btn
        i=HomeActivity.i;

        database = FirebaseDatabase.getInstance();
        if(i==1){
            edt.setHint("What is the problem ?");
            myRef = database.getReference("ServiceRequest");
        }
        else if(i==2){
            edt.setHint("*Optional");
            myRef = database.getReference("TestDrive");
        }
        else if(i==3){
            edt.setHint("What is your query.");
            myRef = database.getReference("Enquiry");
        }
        else if(i==4){
            edt.setHint("Give ratings");
            myRef = database.getReference("Feedback");
        }
        //intitializing reqarr
        reqarr=new ArrayList<RequestFormat>();

    }

    @Override
    public void onResume() {
        super.onResume();
        txtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestFormat srf =new RequestFormat();
                Random rnd =new Random();
                int i = rnd.nextInt();
                String uni=""+i;

                srf.setId(uni);
                srf.setSender(HomeActivity.sender);
                srf.setReceiver(HomeActivity.receiver);
                srf.setTxt(edt.getText().toString());
                srf.setStatus("REQUEST_SENT");

                myRef.child(srf.getId()).setValue(srf)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),"Failed to sent"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //register or updated
                                txtv.setBackgroundResource(R.color.decfrasendbtn);
                                edt.setText("");
                                txtv.setEnabled(false);
                                Toast.makeText(getActivity(),"Sent",Toast.LENGTH_LONG).show();
                            }
                        });


            }
        });

        //getting old data and setting adapter acoordingly
        com.google.firebase.database.Query q = myRef.
                orderByChild("sender").
                equalTo(HomeActivity.sender);
        q.addListenerForSingleValueEvent(valueeventlist);


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
                prelist.setAdapter(myadapter);
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
            super(context,R.layout.listitemsview2_row,reqarr);
            this.reqarr=reqarr;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.listitemsview2_row,parent,false);

            TextView t1,t2,t3,t4,t5;
            t1=row.findViewById(R.id.textView7);
            t2=row.findViewById(R.id.textView8);
            t3=row.findViewById(R.id.textView9);

            RequestFormat obj =reqarr.get(position);
            t1.setText(obj.getReceiver());
            t2.setText(obj.getTxt());
            t3.setText(obj.getStatus());
            return row;
        }

    }
}
