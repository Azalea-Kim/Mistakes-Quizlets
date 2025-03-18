package com.example.mistakes_quizlets;

import static androidx.core.content.FileProvider.getUriForFile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.imageview.ShapeableImageView;


/***************************************************************************************
 *    Title: Baidu OCR API
 *    Availability:https://cloud.baidu.com/product/ocr
 *
 ***************************************************************************************/


/***************************************************************************************
 * I have utilized the way Reggie got access to the baidu ocr api, which is by
 *  sending HTTP requests to the HTTP server and receive response messages
 *
 *
 *    Title: CharacterRecognition
 *    Author: Reggie 1996
 *    Date: Jan 31, 2022
 *    Availability: https://github.com/reggie1996/CharacterRecognition
 *
 ***************************************************************************************/




public class TextRecognitionActivity extends AppCompatActivity implements MainContract.View{


    private Context mContext;
    private MainPresenter mPresenter;

    private Button inputImageBtn;
    private Button recogBtn;
    private ShapeableImageView ImageIv;
    private EditText recogTextEt;

    ActivityResultLauncher<String> cropImage;

//    public static final String TESS_DATA = "/tessdata";
    private TextView textView;
//    private TessBaseAPI tessBaseAPI;
    private Uri outputFileDir;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString()+"/Tess";

//    private TextRecognizer textRecognizer;

    //TAG
    private static final String TAG = "MAIN_TAG";

    private Uri imageUri = null;

//    private static final int CAMERA_REQUEST_CODE = 100;
//    private static final int STORAGE_REQUEST_CODE = 101;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;



    private String[] cameraPermissions;
    private String[] storagePermissions;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);

//        mContext = this;
//        mPresenter = new MainPresenter(this);

        inputImageBtn = findViewById(R.id.inputImageBtn);
        recogBtn = findViewById(R.id.recogBtn);
        ImageIv = findViewById(R.id.shapeableImageView);
        recogTextEt = findViewById(R.id.recognizedTextEt);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent  = new Intent(TextRecognitionActivity.this.getApplicationContext(), UcropperActivity.class);
            intent.putExtra("SendImageData" ,result.toString());

            startActivityForResult(intent, 100);
        });

//        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);



        inputImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();

            }
        });

        recogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri == null){
                    Toast.makeText(TextRecognitionActivity.this,"Pick image first",Toast.LENGTH_SHORT).show();


                }else{

                            mContext = TextRecognitionActivity.this;
                            mPresenter = new MainPresenter(TextRecognitionActivity.this);

                    baiduHttp();
//                    sourceUri = imageUri.toString();
//                    recogTextFromImg();
//                    ocrActivity();

                }
            }
        });

    }

//    private void recogTextFromImg() {
//
//        Log.d(TAG,"recognizeTextFromImage");
//
//        progressDialog.setMessage("Recognizing text...");
//        progressDialog.show();
//
//        try{
//            InputImage inputImage = InputImage.fromFilePath(this,imageUri);
//
//            Task<Text> textTaskResult = textRecognizer.process(inputImage)
//                    .addOnSuccessListener(new OnSuccessListener<Text>() {
//                        @Override
//                        public void onSuccess(Text text) {
//                            progressDialog.dismiss();
//
//                            String recognizedText = text.getText();
//                            Log.d(TAG, "onSuccess"+ recognizedText);
//
//                            recogTextEt.setText(recognizedText);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Log.d(TAG, "onFailure"+ e);
//                            Toast.makeText(TextRecognitionActivity.this,"Failed preparing text due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
//                        }
//                    })
//
//
//                    ;
//
//        } catch (IOException e) {
//
//            progressDialog.dismiss();
//            Log.d(TAG, "recogTextfromImg",e);
//            Toast.makeText(this,"Failed preparing image due to"+e.getMessage(), Toast.LENGTH_SHORT).show();
////            e.printStackTrace();
//        }
//
//    }

    private void showInputDialog() {
        PopupMenu popupMenu = new PopupMenu(this, inputImageBtn);

        popupMenu.getMenu().add(Menu.NONE, 1,1,"CAMERA");

        popupMenu.getMenu().add(Menu.NONE, 2,2,"GALLERY");

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id ==1){

                    Log.d(TAG, "OnMenuClick Camera clicked");
                    if(checkCameraPermission()){
                        pickImageCamera();
                    }else{
                        requestCameraPer();
                    }
                }else if (id == 2){

                    Log.d(TAG, "OnMenuClick Gallery clicked");
                    if(checkStoragePermission()){
                        pickImageGall();
                    }else{
                        requestStoragePer();
                    }
                }
                return true;
            }
        });

    }
    private void pickImageGall() {

        Log.d(TAG, "pickImageGall");
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

//        galleryActL.launch(intent);
        cropImage.launch("image/*");

    }

    private void pickImageCamera(){
        Log.d(TAG, "pickImageCamera: ");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        cameraActL.launch(intent);


    }




    private ActivityResultLauncher<Intent> galleryActL = registerForActivityResult(

            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent  = new Intent(TextRecognitionActivity.this.getApplicationContext(), UcropperActivity.class);
                        //image picked
                        Intent data = result.getData();
                        Log.d(TAG, "onActivityResult: "+imageUri);
                        imageUri = data.getData();
                        ImageIv.setImageURI(imageUri);
                    }else{
                        Log.d(TAG, "onActivityResult: cancelled");
                        Toast.makeText(TextRecognitionActivity.this, "Cancelled.",Toast.LENGTH_SHORT).show();


                    }

                }
            }
    );




    private ActivityResultLauncher<Intent> cameraActL = registerForActivityResult(

            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){

                        Intent intent  = new Intent(TextRecognitionActivity.this.getApplicationContext(), UcropperActivity.class);
                        intent.putExtra("SendImageData" ,imageUri.toString());

                        startActivityForResult(intent, 100);
//                        Log.d(TAG, "onActivityResult: "+imageUri);
//                        ImageIv.setImageURI(imageUri);
                    }else{
                        Log.d(TAG, "onActivityResult: cancelled");
                        Toast.makeText(TextRecognitionActivity.this, "Cancelled.",Toast.LENGTH_SHORT).show();


                    }

                }
            }
    );

    private boolean checkStoragePermission(){

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePer(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean camResult = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean stoResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return camResult && stoResult;


    }

    private void requestCameraPer(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted&&storageAccepted){
                        pickImageCamera();

                    }
                    else{
                        Toast.makeText(this,"Camera and Storage Permissions are required",Toast.LENGTH_SHORT).show();

                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{

                if(grantResults.length>0){

                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted){
                        pickImageGall();
                    }
                    else{
                        Toast.makeText(this,"Storage Permission is required",Toast.LENGTH_SHORT).show();

                    }
                }

            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: 1");
        if(requestCode == 100 && resultCode ==101){
            Log.d(TAG, "onActivityResult: 2");
            String result = data.getStringExtra("CROP");
            imageUri = data.getData();
            if(result!=null){
                imageUri = Uri.parse(result);
                Log.d(TAG, "onActivityResult: 3");

            }ImageIv.setImageURI(imageUri);
//            ocrActivity();
        }
    }


    private void baiduHttp(){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 7;
            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(),options);
            mPresenter.getRecognitionResultByImage(bitmap);

    }

//    private void prepareTessData(){
//        try{
//            File dir = new File(DATA_PATH + TESS_DATA);
//            if(!dir.exists()){
//                dir.mkdir();
//            }
//
//            String fileList[] = getAssets().list("");
//
//            for (String fileName : fileList){
//                String pathToDataFile = DATA_PATH+TESS_DATA+"/"+fileName;
//                if(!(new File(pathToDataFile)).exists()){
//                    InputStream in  = getAssets().open(fileName);
//                    OutputStream out = new FileOutputStream(pathToDataFile);
//                    byte[] buff = new byte[1024];
//                    int len;
//                    while((len = in.read(buff))>0){
//                        out.write(buff,0,len);
//                    }
//                    in.close();
//                    out.close();
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void startOCR(Uri imageUri){
//        try{
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 7;
//            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(),options);
//            String result = this.getText(bitmap);
//            recogTextEt.setText(result);
//        }catch (Exception e){
//            Log.e(TAG, e.getMessage());
//        }
//    }
//
//    private String getText(Bitmap bitmap){
//        try{
//            tessBaseAPI  = new TessBaseAPI();
//
//        }catch (Exception e){
//            Log.e(TAG, e.getMessage());
//        }
//
//        tessBaseAPI.init(DATA_PATH,"eng");
//        tessBaseAPI.setImage(bitmap);
//        String retStr = "No result";
//        try{
//            retStr = tessBaseAPI.getUTF8Text();
//
//        }catch (Exception e){
//            Log.e(TAG,e.getMessage());
//
//        }
//        tessBaseAPI.end();
//        return retStr;
//    }
//
//    private void ocrActivity(){
//        prepareTessData();
//        startOCR(imageUri);
//
//    }




    @Override
    public void updateUI(String s) {
        recogTextEt.setText(s);
    }
}