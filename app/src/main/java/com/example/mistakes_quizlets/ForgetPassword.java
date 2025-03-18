package com.example.mistakes_quizlets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {

    TextView alreadyHaveAccount;
    EditText inputEmail, inputPassword, confirmPassword;
    Button btnReset;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\+[a-z]+";
    dbHelper DB;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        alreadyHaveAccount = findViewById(R.id.createNewAccount);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        btnReset = findViewById(R.id.btnReset);

        progressDialog = new ProgressDialog(this);
        DB = new dbHelper(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPassword.this,MainActivity.class));

            }
                                              }

        );

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirm = confirmPassword.getText().toString();

                if(!emailValidator(email)){
                    inputEmail.setError("Enter Correct Email");
                }else if(password.isEmpty() || password.length()<6){
                    inputPassword.setError("Enter Proper Password");
                }else if(!password.equals(confirm)){
                    confirmPassword.setError("password not match");
                    confirmPassword.requestFocus();
                }else{
                    progressDialog.setMessage("Please Wait While Reset...");
                    progressDialog.setTitle("Reset");
                    progressDialog.setCanceledOnTouchOutside(false);
//                    progressDialog.show();

                    Boolean checkuser = DB.checkemail(email);
                    if(checkuser==false){
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPassword.this, "You haven't registered yet, please sign up first!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                    }
                    else{
                        boolean reset = DB.resetData(email,password);
                        if(reset==true){
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPassword.this, "Reset successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(ForgetPassword.this, "Reset failed", Toast.LENGTH_SHORT).show();
                        }

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
}