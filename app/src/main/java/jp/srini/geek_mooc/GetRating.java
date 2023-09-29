package jp.srini.geek_mooc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Srini.geek_mooc.R;

import java.util.ArrayList;

import Backend.BasicInfo_helper;
import Backend.ratings;


public class GetRating extends AppCompatActivity {

    final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    final int GOOGLE_PAY_REQUEST_CODE = 22;

    TextView vTitle, vName, vNo, vEmail, vBtnText;
    EditText vComments;
    CheckBox c1,c2,c3,c4,c5;
    RelativeLayout vDonateBtn, vSubmitBtn;
    ProgressBar vProgressBar;
    int rating;
    Intent get;
    DatabaseReference reference;
    BasicInfo_helper authorDetials;
    String UPIid, authorKey;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_rating);
        get = getIntent();

        vTitle = findViewById(R.id.title);
        vName = findViewById(R.id.name);
        vNo = findViewById(R.id.no);
        vEmail = findViewById(R.id.email);
        vBtnText = findViewById(R.id.BtnText);
        vComments = findViewById(R.id.comments);
        vDonateBtn = findViewById(R.id.donate);
        vSubmitBtn = findViewById(R.id.submitRatingBtn);
        vProgressBar = findViewById(R.id.Progressbarrating);

        vTitle.setText(get.getStringExtra("ccName"));
        vName.setText(get.getStringExtra("ccAuthor"));
        authorKey = get.getStringExtra("ccPath").split("/")[2];
        reference = FirebaseDatabase.getInstance().getReference("users").child(authorKey);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                authorDetials = snapshot.getValue(BasicInfo_helper.class);
                setValues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!c1.isChecked()){
                    c2.setChecked(false);
                    c3.setChecked(false);
                    c4.setChecked(false);
                    c5.setChecked(false);
                    rating = 0;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                }else{
                    rating = 1;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                }
            }
        });

        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(c2.isChecked()) {
                    c1.setChecked(true);
                    rating =2;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                }else{
                    rating = 1;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                    c3.setChecked(false);
                    c4.setChecked(false);
                    c5.setChecked(false);
                }
            }
        });

        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(c3.isChecked()) {
                    c1.setChecked(true);
                    c2.setChecked(true);
                    rating =3;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                }else{
                    rating = 2;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                    c4.setChecked(false);
                    c5.setChecked(false);
                }
            }
        });

        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(c4.isChecked()) {
                    c1.setChecked(true);
                    c2.setChecked(true);
                    c3.setChecked(true);
                    rating =4;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                }
                else{
                    rating = 3;
                    Log.d("UT_Rating", "onCheckedChanged: "+rating);
                    c5.setChecked(false);
                }
            }
        });

        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(true);
                c4.setChecked(true);
                rating = 5;
                Log.d("UT_Rating", "onCheckedChanged: "+rating);
            }
        });

        vSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference(get.getStringExtra("ccPath"));
                reference.child("ratingsTotal").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("ratingTotal")) {
                            reference.child("ratingsTotal").setValue(snapshot.getValue(Integer.class) + rating);
                        }else{
                            reference.child("ratingTotal").setValue(rating);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                reference.child("commentsRatings").child(FirebaseAuth.getInstance().getUid())
                        .setValue(new ratings(vComments.getText().toString(),"","",rating))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(GetRating.this, "Thanks for your ratings", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                }else{
                                    Toast.makeText(GetRating.this, "Your ratings not added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        if ((RESULT_OK == resultCode) || (resultCode == 11)) {
            if (data != null) {
                String trxt = data.getStringExtra("response");
                Log.e("UPI", "onActivityResult: " + trxt);
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add(trxt);
                upiPaymentDataOperation(dataList);
            } else {
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        } else {
            //when user simply back without payment
            Log.e("UPI", "onActivityResult: " + "Return data is null");
            ArrayList<String> dataList = new ArrayList<>();
            dataList.add("nothing");
            upiPaymentDataOperation(dataList);
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(GetRating.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(getApplicationContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(getApplicationContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(getApplicationContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(getApplicationContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public void setValues(){
        vNo.setText(authorDetials.getMobile());
        vEmail.setText(authorDetials.getEmail());
        UPIid = authorDetials.getUpi();
        vDonateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UPIid.isEmpty()){
                    Toast.makeText(GetRating.this, "Author UPI id not available", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("UT_upi", "onClick: "+UPIid);
                    Uri upiUri = new Uri.Builder()
                            .scheme("upi")
                            .authority("pay")
                            .appendQueryParameter("pa",UPIid)
                            .appendQueryParameter("pn",get.getStringExtra("ccAuthor"))
                            .appendQueryParameter("tn","Thanks for your course "+get.getStringExtra("ccName"))
                            .appendQueryParameter("am","100")
                            .appendQueryParameter("cu","INR")
                            .build();
                    Intent googlePay = new Intent(Intent.ACTION_VIEW);
                    googlePay.setData(upiUri);
                    googlePay.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                    startActivityForResult(googlePay,GOOGLE_PAY_REQUEST_CODE);
                }
            }
        });

    }
}