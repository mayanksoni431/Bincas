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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class requestFragment extends Fragment {
    private int i;
    private ViewGroup cont;
    private Context context;
    private ArrayList<UserTb> userTbs;

    private ListView listView;

    //firebase
    FirebaseDatabase database;
    DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getContext();
        this.cont = container;
        checkInternet();
        return inflater.inflate(R.layout.haservice_fragment, container, false);
    }

    @Override
    public void onStart() {
        //firebase
        super.onStart();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        userTbs = new ArrayList<UserTb>();

        listView = cont.findViewById(R.id.listcontainerid);
        //get data create adapter and set to adapter
        com.google.firebase.database.Query q = myRef.
                orderByChild("vno").
                equalTo("null");
        q.addListenerForSingleValueEvent(valueeventlist);

        //set adapter

    }

    ValueEventListener valueeventlist = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userTbs.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserTb userTb = snapshot.getValue(UserTb.class);
                    userTbs.add(userTb);
                }

                Myadapter myadapter = new Myadapter(context,userTbs);
                listView.setAdapter(myadapter);
            }
            else
                Toast.makeText(getActivity(), "No Service center available", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeActivity.receiver=userTbs.get(position).getName();
                HomeActivity.replaceFragmentwith(10);
            }
        });
    }

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

    class Myadapter extends ArrayAdapter<UserTb> {
        ArrayList<UserTb> userTbs;
        public Myadapter(Context context, ArrayList<UserTb> userTbs ) {
            super(context,R.layout.listitemsview_row,userTbs);
            this.userTbs=userTbs;
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.listitemsview_row,parent,false);

            TextView t1,t2,t3,t4,t5;
            t1=row.findViewById(R.id.textView2);
            t2=row.findViewById(R.id.textView3);
            t3=row.findViewById(R.id.textView4);
            t4=row.findViewById(R.id.textView5);
            t5=row.findViewById(R.id.textView6);

            UserTb obj =userTbs.get(position);
            t1.setText(obj.getName());
            t2.setText(obj.getId());
            t3.setText(obj.getContno());
            t4.setText(obj.getVtype());
            t5.setText(obj.getAddr());
            return row;
        }

    }


}
