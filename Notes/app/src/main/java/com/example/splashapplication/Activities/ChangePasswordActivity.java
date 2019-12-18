package com.example.splashapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.R;

public class ChangePasswordActivity extends AppCompatActivity {

    String email,stPassword,stConfPassword;
    EditText password,confPassword;
    TextView tvPassword,tvConfPassword;
    Button update;
    DBAdapter dbAdapter;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        dbAdapter = new DBAdapter(getApplicationContext());
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
        password = (EditText)findViewById(R.id.etPassword);
        confPassword = (EditText)findViewById(R.id.etConfPassword);
        tvPassword = (TextView)findViewById(R.id.tvPassword);
        tvConfPassword = (TextView)findViewById(R.id.tvConfPassword);
        update = (Button)findViewById(R.id.btnUpdate);

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            password.setSelection(password.getText().length());
                            return true;
                        }else{
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            password.setSelection(password.getText().length());
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        confPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (confPassword.getRight() - confPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(confPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                            confPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            confPassword.setSelection(confPassword.getText().length());
                            return true;
                        }else{
                            confPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            confPassword.setSelection(confPassword.getText().length());
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (password.getText().length() == 0) {
                    tvPassword.setText("");
                }
                else {
                    tvPassword.setText("Password");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPassword.setText("Password");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().length() == 0) {
                    tvPassword.setText("");
                }
                else {
                    tvPassword.setText("Password");
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stPassword = password.getText().toString();
                stConfPassword = confPassword.getText().toString();
                if(validate()){
                    if (dbAdapter.updatePassword(email,stPassword)){
                        Toast.makeText(ChangePasswordActivity.this, "Password Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
    }
    private boolean validate() {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        if(stPassword.isEmpty()){
            password.setError("Password field is required");
        }else if (stPassword.length()<8 && !stPassword.matches(passwordPattern)){
            password.setError("Password is too small");
        }else if (!stConfPassword.equals(stPassword)){
            confPassword.setError("Password not match");
        }else {
            return true;
        }
        return false;
    }
}