package com.example.splashapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView tvEmail;
    EditText email;
    Button submit;
    String stEmail;
    DBAdapter dbAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        dbAdapter = new DBAdapter(getApplicationContext());
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        email = (EditText)findViewById(R.id.etEmail);
        submit = (Button)findViewById(R.id.btnSubmit);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (email.getText().length() == 0)
                {
                    tvEmail.setText("");
                }
                else {
                    tvEmail.setText("Email");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvEmail.setText("Email");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (email.getText().length() == 0)
                {
                    tvEmail.setText("");
                }
                else {
                    tvEmail.setText("Email");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = email.getText().toString();
                if(validation()){
                    if(dbAdapter.isEmailExists(stEmail)){
                        Intent i = new Intent(getApplicationContext(),ChangePasswordActivity.class);
                        i.putExtra("email",stEmail);
                        startActivity(i);
                    }else {
                        Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validation() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(stEmail.isEmpty()){
            email.setError("Email field is required");
        }else if (!stEmail.matches(emailPattern)){
            email.setError("Please enter valid email");
        }else {
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
