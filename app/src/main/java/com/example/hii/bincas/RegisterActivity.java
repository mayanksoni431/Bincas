package com.example.hii.bincas;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    static private Button sb;
    //firebase instances
    private UserTb ut;
    private FirebaseAuth mAuth;
    FirebaseDatabase database ;
    DatabaseReference myRef;

    //data
    private EditText id,vtype,vnum,contno,street,area,city;
    private String addr;
    private EditText name;
    public static String regbtnstr=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regbtnstr=getIntent().getStringExtra("value");

        //data ele to
        name=findViewById(R.id.rnameid);
        id=findViewById(R.id.rsemailid);
        vtype=findViewById(R.id.rvtype);
        vnum=findViewById(R.id.rvid);
        contno =findViewById(R.id.runoid);
        street=findViewById(R.id.rsstreetid);
        area=findViewById(R.id.rsareaid);
        city=findViewById(R.id.rscityid);
        //addr

        sb=findViewById(R.id.rsbid);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //checking connection
        checkInternet();

        //alertforregistration
        showalertdialog();


        //time id
        final Random rn= new Random();
        //firebase
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
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
                    createAccount();
                }

                }
        });
    }

    private void showalertdialog() {
        AlertDialog.Builder alertdiag = new AlertDialog.Builder(this);
        alertdiag.setMessage("If you want to register service center.Please enter null in vehicle no.");
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
            Toast.makeText(this,"Please fill the mandatory fields",Toast.LENGTH_LONG).show();
        }
        else
            f=true;
        return(f);

    }


    private void createAccount() {
            //checking
        com.google.firebase.database.Query q = myRef.
                orderByChild("contno").
                equalTo(ut.getContno());

        q.addListenerForSingleValueEvent(valueeventlist);

    }

    ValueEventListener valueeventlist=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
                Toast.makeText(RegisterActivity.this,"Number Already Exist",Toast.LENGTH_LONG).show();
            else
                createnewAccount();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            createnewAccount();
        }
    };

    private void createnewAccount() {
        checkInternet();
        //adding online
        myRef.child(ut.getContno()).setValue(ut)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,"Failed to register.Make sure you are connected.",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //register or updated
                    Toast.makeText(RegisterActivity.this,"Registered successfully",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                });
    }

    public void checkInternet(){
        int f=0;
        ConnectivityManager cmo = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
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
            View parlay = findViewById(android.R.id.content);
            Snackbar.make(parlay, "Please check internet connection.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(regbtnstr!=null)
        {
            finishAffinity();
            finish();
            Intent i =new Intent(RegisterActivity.this,LoginAcivity.class);
            startActivity(i);
        }
        super.onBackPressed();
    }

}
