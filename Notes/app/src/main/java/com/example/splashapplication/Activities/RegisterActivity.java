package com.example.splashapplication.Activities;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.splashapplication.Adapter.DBAdapter;
import com.example.splashapplication.R;
import com.example.splashapplication.Model.User;

public class RegisterActivity extends AppCompatActivity {
    TextView signIn,tvName,tvPassword,tvEmail,tvConfPassword;
    Button signUp;
    String stEmail,stName,stPassword,stConfPassword;
    EditText email,password,name,confPassword;
    //SQLiteHelper sqLiteHelper;
    DBAdapter db;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DBAdapter(this);
        name=(EditText)findViewById(R.id.etUser);
        email=(EditText)findViewById(R.id.etEmail);
        tvName=(TextView)findViewById(R.id.tvName);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvPassword=(TextView)findViewById(R.id.tvPassword);
        tvConfPassword=(TextView)findViewById(R.id.tvConfPassword);
        password=(EditText)findViewById(R.id.etPassword);
        confPassword=(EditText)findViewById(R.id.etConfPassword);
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
        //sqLiteHelper = new SQLiteHelper(this);
        signUp = (Button)findViewById(R.id.btnSignUp);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (name.getText().length() == 0)
                {
                    tvName.setText("");
                }
                else {
                    tvName.setText("Name");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvName.setText("Name");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (name.getText().length() == 0)
                {
                    tvName.setText("");
                }
                else {
                    tvName.setText("Name");
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (password.getText().length() == 0)
                {
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
                if (password.getText().length() == 0)
                {
                    tvPassword.setText("");
                }
                else {
                    tvPassword.setText("Password");
                }
            }
        });

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
        confPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (confPassword.getText().length() == 0)
                {
                    tvConfPassword.setText("");
                }
                else {
                    tvConfPassword.setText("Confirm Password");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvConfPassword.setText("Confirm Password");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (confPassword.getText().length() == 0)
                {
                    tvConfPassword.setText("");
                }
                else {
                    tvConfPassword.setText("Confirm Password");
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = email.getText().toString();
                stName = name.getText().toString();
                stPassword = password.getText().toString();
                stConfPassword = confPassword.getText().toString();
                if(validate()){
                    if(!db.isEmailExists(stEmail)){
                        db.addUser(new User(null,stName,stEmail,stPassword));
                        Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(RegisterActivity.this, "Email already exist", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        signIn = (TextView)findViewById(R.id.txtSignIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validate() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        if (name.getText().toString().trim().length()==0){
            name.setError("Name field is required");
        }else if(stEmail.isEmpty()){
            email.setError("Email field is required");
        }else if (!stEmail.matches(emailPattern)){
            email.setError("Please enter valid email");
        }else if(stPassword.isEmpty()){
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
    @Override
    public void onBackPressed() {
        finish();
    }
}