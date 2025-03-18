package com.example.mistakes_quizlets;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;

public class Register extends AppCompatActivity {

    TextView alreadyHaveAccount;
    EditText inputEmail, inputPassword, confirmPassword;
    Button btnRegister;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\+[a-z]+";
    dbHelper DB;
    ProgressDialog progressDialog;


    LottieAnimationView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        alreadyHaveAccount = findViewById(R.id.createNewAccount);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        btnRegister = findViewById(R.id.btnRegister);

        loading = findViewById(R.id.lottie);

        loading.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(this);
        DB = new dbHelper(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      startActivity(new Intent(Register.this, MainActivity.class));

                                                  }
                                              }

        );


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirm = confirmPassword.getText().toString();

                if (!emailValidator(email)) {
                    inputEmail.setError("Enter Correct Email");
                } else if (password.isEmpty() || password.length() < 6) {
                    inputPassword.setError("Enter Proper Password");
                } else if (!password.equals(confirm)) {
                    confirmPassword.setError("password not match");
                    confirmPassword.requestFocus();
                } else {
                    progressDialog.setMessage("Please Wait While Registration...");
                    progressDialog.setTitle("Registration");
                    progressDialog.setCanceledOnTouchOutside(false);

                    loading.setVisibility(View.VISIBLE);
                    loading.setSpeed(1);
                    loading.playAnimation();
//                    progressDialog.show();

                    Boolean checkuser = DB.checkemail(email);
                    if (checkuser == false) {
                        Boolean insert = DB.insertData(email, password);
                        if (insert == true) {
                            progressDialog.dismiss();
                            loading.cancelAnimation();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            loading.cancelAnimation();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        loading.cancelAnimation();
                        loading.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean emailValidator(String email) {

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }
//BD:E5:C6:1B:C9:AD:61:4C:0D:50:6C:3F:3B:16:F7:E9

}

