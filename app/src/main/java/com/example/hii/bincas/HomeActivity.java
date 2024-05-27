package com.example.hii.bincas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //context
    public static Context context;
    //moblileno
    public static String sender="bd";
    //name
    public static String receiver;
    //multivalue
    public static int i;
    static FragmentManager fm;
    static FragmentTransaction ftrn;

    //headerhomepanel
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String str =getIntent().getStringExtra("mobile");
        if(str!=null)
            sender=str;

        context=HomeActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        textView=(TextView)navigationView.getHeaderView(0).findViewById(R.id.hausernamepanelupdatedid);
        textView.setText(sender);

        navigationView.setNavigationItemSelectedListener(this);
        //initializing
        fm = getSupportFragmentManager();

        addFragment();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.hasvrpopid) {
            i=20;
            replaceFragmentwith(20);
            return true;
        }
        if (id == R.id.hatestdrivepopid) {
            i=30;
            replaceFragmentwith(30);
            return true;
        }
        if (id == R.id.halogoutpopid) {
            goInLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.hasercivebtnid) {
            // Handle the camera action
            i=1;
            replaceFragmentwith(i);
        } else if (id == R.id.hatestdbtn) {
            i=2;
            replaceFragmentwith(i);
        } else if (id == R.id.haenquirybtn) {
            i=3;
            replaceFragmentwith(i);
        } else if (id == R.id.hafeedbackbtn) {
            i=4;
            replaceFragmentwith(i);
        } else if (id == R.id.haupdatebtn) {
            i=5;
            replaceFragmentwith(i);
        } else if (id == R.id.hadeletebtn) {
            i=6;
            replaceFragmentwith(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void replaceFragmentwith(int i) {
        ftrn=fm.beginTransaction();
        if(i==1){
            requestFragment invfra =new requestFragment();
            ftrn.replace(R.id.hafragmentcontainer,invfra);
        }
        else if(i==2){
            requestFragment invfra =new requestFragment();
            ftrn.replace(R.id.hafragmentcontainer,invfra);
        }
        else if(i==3){
            requestFragment invfra =new requestFragment();
            ftrn.replace(R.id.hafragmentcontainer,invfra);
        }
        else if(i==4){
            requestFragment invfra =new requestFragment();
            ftrn.replace(R.id.hafragmentcontainer,invfra);
        }
        else if(i==5){
            //update by reinter info
            updateFragment updf = new updateFragment();
            ftrn.replace(R.id.hafragmentcontainer,updf);
        }
        else if(i==6){
            showalertdialog();
        }
        else if(i==10){
            description_fragment desf = new description_fragment();
            ftrn.replace(R.id.hafragmentcontainer,desf);
        }
        else if(i==20){
            GotrequestFragment desf = new GotrequestFragment();
            ftrn.replace(R.id.hafragmentcontainer,desf);
        }
        else if(i==30){
            GotrequestFragment desf = new GotrequestFragment();
            ftrn.replace(R.id.hafragmentcontainer,desf);
        }
        ftrn.commit();
    }


    public static void addFragment(){
        ftrn=fm.beginTransaction();
        welcomefragment wfrag = new welcomefragment();
        ftrn.add(R.id.hafragmentcontainer,wfrag);
        ftrn.commit();
    }

    public  static void showalertdialog() {
        AlertDialog.Builder alertdiag = new AlertDialog.Builder(context);
        alertdiag.setMessage("Do you want to delete account.");
        alertdiag.setTitle("Alert");
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
                FirebaseAuth.getInstance().signOut();
                FirebaseDatabase.getInstance().getReference("Users").child(sender).removeValue();
                Toast.makeText(context,"Account Deleted",Toast.LENGTH_LONG).show();
                Intent i = new Intent(context,RegisterActivity.class);
                i.putExtra("value","yes");
                context.startActivity(i);
            }
        });

        alertdiag.create().show();
    }


    public void goInLogin() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(HomeActivity.this,LoginAcivity.class);
        startActivity(i);
    }


}
