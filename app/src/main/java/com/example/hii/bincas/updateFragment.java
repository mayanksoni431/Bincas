package com.example.hii.bincas;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class updateFragment extends Fragment {
    Context context;
    private ViewGroup container;

    //user firebase
    FirebaseDatabase database ;
    DatabaseReference myRef;
    private UserTb ut;

    //form vars
    private EditText id,vtype,vnum,contno,street,area,city;
    private String addr;
    private EditText name;
    private Button sb;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context=getContext();
        this.container=container;
        checkInternet();
        return inflater.inflate(R.layout.update_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        name=container.findViewById(R.id.ufnameid);
        id=container.findViewById(R.id.ufemailid);
        vtype=container.findViewById(R.id.ufvtype);
        vnum=container.findViewById(R.id.ufvid);
        contno =container.findViewById(R.id.ufunoid);
        contno.setText(HomeActivity.sender);
        contno.setEnabled(false);
        street=container.findViewById(R.id.ufstreetid);
        area=container.findViewById(R.id.ufareaid);
        city=container.findViewById(R.id.ufcityid);
        sb=container.findViewById(R.id.ufbid);

        showalertdialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        final Random rn= new Random();
        //firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()) {
                    int tiid = rn.nextInt();
                    String timid = ""+tiid;
                    addr = street.getText().toString() + "/" + area.getText().toString() + "/" + city.getText().toString();

                    ut = new UserTb();
                    ut.setTimmid(timid);
                    ut.setId(id.getText().toString());
                    ut.setName(name.getText().toString());
                    ut.setContno(contno.getText().toString());
                    ut.setAddr(addr);
                    ut.setVtype(vtype.getText().toString());
                    ut.setVno(vnum.getText().toString());
                    createnewAccount();
                }

            }
        });
    }

    public void checkInternet(){
        int f=0;
        ConnectivityManager cmo = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo nifo = cmo.getActiveNetworkInfo();

        if(nifo!=null &&nifo.isConnectedOrConnecting()){
            NetworkInfo mobile =cmo.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(mobile!=null && mobile.isConnectedOrConnecting())
                f=1;
        }
        else
            f=0;

        if(f==0)
        {
            View parlay = container.findViewById(android.R.id.content);
            Snackbar.make(parlay, "Please check internet connection.", Snackbar.LENGTH_LONG).show();
        }
    }


    private void showalertdialog() {
        AlertDialog.Builder alertdiag = new AlertDialog.Builder(context);
        alertdiag.setMessage("You can't change phone number.");
        alertdiag.setTitle("Note");
        alertdiag.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertdiag.create().show();
    }

    private boolean check() {
        boolean f=false;
        if(contno.getText().equals("") || name.getText().equals("")||id.getText().equals("") || vtype.getText().equals("")
                || vnum.getText().equals(""))
        {
            Toast.makeText(context,"Please fill the mandatory fields",Toast.LENGTH_LONG).show();
        }
        else
            f=true;
        return(f);

    }

    private void createnewAccount() {
        checkInternet();
        //adding online
        myRef.child(ut.getContno()).setValue(ut)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Failed to update.Make sure you are connected."+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //register or updated
                        Toast.makeText(context,"Updated",Toast.LENGTH_LONG).show();
                        cleatAllTextviews();
                    }
                });
    }

    private void cleatAllTextviews() {
        name.setText("");
        id.setText("");
        vtype.setText("");
        vnum.setText("");
        street.setText("");
        area.setText("");
        city.setText("");
        sb.setBackgroundResource(R.color.decfrasendbtn);
    }

}
