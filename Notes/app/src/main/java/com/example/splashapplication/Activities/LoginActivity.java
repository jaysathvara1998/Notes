package com.example.splashapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.splashapplication.Halper.SessionManager;
import com.example.splashapplication.Model.User;

public class LoginActivity extends AppCompatActivity {
    Button signIn;
    TextView signUp,tvEmail,tvPassword,tvForgot;
    EditText email,password;
    String stEmail,stPassword;
    DBAdapter sqLiteHelper;
    SessionManager sessionManager;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager=new SessionManager(getApplicationContext());
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvPassword=(TextView)findViewById(R.id.tvPassword);
        tvForgot=(TextView)findViewById(R.id.tvForgot);
        email=(EditText)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPassword);
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
//        checkbox = (AppCompatCheckBox) findViewById(R.id.checkbox);
//        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                }else {
//                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }
//            }
//        });
        sqLiteHelper = new DBAdapter(this);
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

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(i);
//                finish();
            }
        });

        signIn = (Button)findViewById(R.id.btnSignIn);
        signUp = (TextView)findViewById(R.id.txtSignUp);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = email.getText().toString();
                stPassword= password.getText().toString();
                if(validation()){
                    User currentUser = sqLiteHelper.Authenticate(new User(null,null,stEmail,stPassword));
                    if(!sqLiteHelper.isEmailExists(stEmail)){
                        Toast.makeText(getApplicationContext(), "Please registered with this email", Toast.LENGTH_LONG).show();
                    }else {
                        if(currentUser != null){
                            Toast.makeText(LoginActivity.this, "Successfully Logged in!", Toast.LENGTH_LONG).show();
                            sessionManager.createLoginSession(stEmail);
                            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);// New activity
                startActivity(intent);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
        });

        Drawable[] drawablesEmail = email.getCompoundDrawables();
        drawablesEmail[0].setAlpha(128);

        Drawable[] drawablesPassword = password.getCompoundDrawables();
        drawablesPassword[0].setAlpha(128);
    }

    private boolean validation() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        if(stEmail.isEmpty()){
            email.setError("Email field is required");
        }else if (!stEmail.matches(emailPattern)){
            email.setError("Please enter valid email");
        }else if(stPassword.isEmpty()){
            password.setError("Password field is required");
        }else if (stPassword.length()<8 && !stPassword.matches(passwordPattern)){
            password.setError("Enter valid password");
        }else {
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
