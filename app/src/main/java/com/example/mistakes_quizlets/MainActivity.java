package com.example.mistakes_quizlets;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.stetho.common.LogUtil;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    TextView creatNewAccount;
    TextView forgetPass;

    EditText inputEmail, inputPassword;
    Button btnLogin;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\+[a-z]+";

    public static dbHelper DB;

    ProgressDialog progressDialog;

    LottieAnimationView robot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());


        creatNewAccount = findViewById(R.id.createNewAccount);

        forgetPass = findViewById(R.id.forgetPassword);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);


        progressDialog = new ProgressDialog(this);
        DB = new dbHelper(this);


        robot = findViewById(R.id.lottie);
        btnLogin = findViewById(R.id.btnLogin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        creatNewAccount.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   startActivity(new Intent(MainActivity.this, Register.class));
                                               }
                                           }

        );


        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    robot.setSpeed(1);
                    robot.loop(true);
                    robot.playAnimation();
                } else {
                    robot.cancelAnimation();
                }


            }
        });

        inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    robot.setSpeed(1);
                    robot.loop(true);
                    robot.playAnimation();
                } else {
                    robot.cancelAnimation();
                }
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              startActivity(new Intent(MainActivity.this, ForgetPassword.class));

                                          }
                                      }

        );


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DB.loadCategories()){

                }else {
                    DB.insertCat("GK", 20);
                    DB.insertCat("HISTORY", 30);
                    DB.insertCat("SCIENCE", 10);
                    DB.insertCat("ENGLISH", 25);
                    DB.insertCat("MATHS", 15);
                    boolean b = DB.loadCategories();
                    LogUtil.e("loadCategories "+b);
                }

                if (DB.loadTestData()){

                }else{
                    DB.insertTest(10,20,1);
                    DB.insertTest(5,15,1);
                    DB.insertTest(10,30,1);
                    DB.insertTest(50,5,2);
                    boolean b = DB.loadTestData();
                    LogUtil.e("loadTestData "+b);
                }


//
//                DB.insertQuestion("1+1=?","1","2","3","4",1,1,2);
//                DB.insertQuestion("1+2=?","1","2","3","4",1,1,2);
//                DB.insertQuestion("1+3=?","1","2","3","4",1,1,2);

//


//
                DB.loadQuestionsData();

//                String email = inputEmail.getText().toString();
//                String password = inputPassword.getText().toString();
                String email = "azaleacool@icloud.com";
                String password = "111111";


                if (!emailValidator(email)) {
                    inputEmail.setError("Enter Correct Email");
                } else if (password.isEmpty() || password.length() < 6) {
                    inputPassword.setError("Enter Proper Password");
                } else {
                    progressDialog.setMessage("Please Wait While Login...");
                    progressDialog.setTitle("Login");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Boolean checkuserpass = DB.checkusernamepassword(email, password);
                    if (checkuserpass) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Log in is successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
//
//    private void initOcr() {
//        String datapath = getFilesDir() + "/tesseract/";
//        checkFile(new File(datapath + "tessdata/"), "chi_sim");
//    }
//
//    /**
//     * @param dir
//     * @param language chi_sim eng
//     */
//    private void checkFile(File dir, String language) {
//        //如果目前不存在则创建方面，然后在判断训练数据文件是否存在
//        if (!dir.exists() && dir.mkdirs()) {
//            copyFiles(lag);
//        }
//        if (dir.exists()) {
//            String datafilepath = datapath + "/tessdata/" + language + ".traineddata";
//            File datafile = new File(datafilepath);
//            if (!datafile.exists()) {
//                copyFiles(lag);
//            }
//        }
//    }
//
//    /**
//     把训练数据放到手机内存
//     * @param language "chi_sim" ,"eng"
//     */
//    private void copyFiles(String language) {
//        try {
//            String filepath = datapath + "/tessdata/" + language + ".traineddata";
//            AssetManager assetManager = getAssets();
//            InputStream instream = assetManager.open("tessdata/" + language + ".traineddata");
//            OutputStream outstream = new FileOutputStream(filepath);
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = instream.read(buffer)) != -1) {
//                outstream.write(buffer, 0, read);
//            }
//            outstream.flush();
//            outstream.close();
//            instream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//


}

