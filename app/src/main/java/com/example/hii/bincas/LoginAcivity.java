package com.example.hii.bincas;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginAcivity extends AppCompatActivity {

    //data
    private EditText editTextMobile;
    private Button log;
    private TextView forgot;
    private FirebaseAuth mAuth;

    private String TAG="LoginACT";
    private TextView reg;
    private String mobile;

    //firebase
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acivity);

        checkInternet();
        Firebase.setAndroidContext(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        editTextMobile=findViewById(R.id.etemid);
        log=findViewById(R.id.lbid);

        reg=findViewById(R.id.rgid);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginAcivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        updateUI(user);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ValueEventListener valueeventlist=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Intent intent = new Intent(LoginAcivity.this, verifyotpActivity.class);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                }
                else
                    Toast.makeText(LoginAcivity.this,"Register First to log in",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginAcivity.this,"Register First to log in",Toast.LENGTH_LONG).show();
            }
        };


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(android.R.id.content);
                mobile = editTextMobile.getText().toString();
                if (mobile.isEmpty() || mobile.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile number");
                    editTextMobile.requestFocus();
                    return;
                }
                com.google.firebase.database.Query q = myRef.
                        orderByChild("contno").
                        equalTo(mobile);

                q.addListenerForSingleValueEvent(valueeventlist);

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if(user!=null)
        {
            String no=user.getPhoneNumber();
            Intent i = new Intent(LoginAcivity.this,HomeActivity.class);
            i.putExtra("mobile",no);
            startActivity(i);
        }
    }

   public void checkInternet(){
       int f=0;
       ConnectivityManager cmo = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
       NetworkInfo  nifo = cmo.getActiveNetworkInfo();

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
        finishAffinity();
        finish();
    }
}
