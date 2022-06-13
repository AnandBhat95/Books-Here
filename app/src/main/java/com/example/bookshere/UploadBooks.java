package com.example.bookshere;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadBooks extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


//    public static String Database_Path;
    String Storage_Path = "All_Image_Uploads/";
    //static String Database_Path = "All_Image_Uploads_Database";
     public static String Database_Path = "All_Image_Uploads_Database/";
    String[] bankNames={"MCA","MBA","EEE","IS","CS","Civil","Mechanical","Biotech"};

    String[] yearNames={"1st","2nd","3rd","4th"};
    Button BSelectImage,UploadButton;
    ImageView IVPreviewImage;
    EditText book_name,pub_year,price,author,contact_no;
    Spinner course_year,course_name;
    int SELECT_PICTURE = 200;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        }
        setContentView(R.layout.activity_upload_books);
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        progressDialog = new ProgressDialog(UploadBooks.this);
        book_name=findViewById(R.id.book_name );
        pub_year=findViewById(R.id.pub_year);
        price=findViewById(R.id.price);
        author=findViewById(R.id.author);
        contact_no=findViewById(R.id.contact_no);
        course_name  = (Spinner) findViewById(R.id.course_spinner);
        course_year = (Spinner) findViewById(R.id.year_spinner);
        course_name.setOnItemSelectedListener(this);
        course_year.setOnItemSelectedListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        UploadButton = (Button)findViewById(R.id.upload);

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter bb = new ArrayAdapter(this,android.R.layout.simple_spinner_item,yearNames);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        course_name.setAdapter(aa);
        course_year.setAdapter(bb);
        BSelectImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                imageChooser();
            }
        });

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Book_Name = book_name.getText().toString().trim();
                String Book_Pub = pub_year.getText().toString().trim();
                String Book_Price = price.getText().toString().trim();
                String Book_Author = author.getText().toString().trim();
                String Book_Contact = contact_no.getText().toString().trim();
                String C_Name=course_name.getContext().toString().trim();
                String C_Year=course_year.getContext().toString().trim();

                if(Book_Name.isEmpty())
                {
                    book_name.setError("Type Book Name");
                    book_name.requestFocus();
                    return;
                }
                if(android.text.TextUtils.isDigitsOnly(Book_Name)==true){
                    book_name.setError("Type valid Book Name");
                    book_name.requestFocus();
                    return;
                }
                if(Book_Pub.isEmpty())
                {
                    pub_year.setError("Type Published Year");
                    pub_year.requestFocus();
                    return;
                }
                if(Integer.parseInt(Book_Pub)>2023 || Integer.parseInt(Book_Pub)<2005)
                {
                    pub_year.setError("Type valid Published Year");
                    pub_year.requestFocus();
                    return;
                }
                if(Book_Price.isEmpty())
                {
                    price.setError("Type Book Price");
                    price.requestFocus();
                    return;
                }
                if(Integer.parseInt(Book_Price)>2000 || Integer.parseInt(Book_Price)<0)
                {
                    price.setError("Type valid Price");
                    price.requestFocus();
                    return;
                }
                if (IVPreviewImage.getDrawable()==null){
                    Toast.makeText(UploadBooks.this, "Upload image", Toast.LENGTH_LONG).show();
                    IVPreviewImage.requestFocus();
                    return;
                }

                if(Book_Author.isEmpty())
                {
                    author.setError("Type Author name or xerox if notes");
                    author.requestFocus();
                    return;
                }
                if(android.text.TextUtils.isDigitsOnly(Book_Author)==true){
                    author.setError("Type valid Book author");
                    author.requestFocus();
                    return;
                }
                if (Book_Author.length()<3){
                    author.setError("Type valid Book author");
                    author.requestFocus();
                    return;
                }
                if(Book_Contact.isEmpty())
                {
                    contact_no.setError("type you mobile number");
                    contact_no.requestFocus();
                    return;
                }
                if(Book_Contact.length()!=10){
                    contact_no.setError("Valid number to contact");
                    contact_no.requestFocus();
                    return;
                }

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }


        });
    }
//    private void imageChooser(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(UploadBooks.this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.alert_dilog_profile_picture, null);
//        dialogView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        builder.setCancelable(true);
//        builder.setView(dialogView);
//
//        ImageView imageViewADPPCamera = dialogView.findViewById(R.id.imageViewADPPCamera);
//        ImageView imageViewADPPGallery = dialogView.findViewById(R.id.imageViewADPPGallery);
//
//        final AlertDialog alertDialogProfilePicture = builder.create();
//        alertDialogProfilePicture.show();
//
//        imageViewADPPCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkAndRequestPermissions()) {
//                    takePictureFromCamera();
//                    alertDialogProfilePicture.dismiss();
//                }
//            }
//        });
//
//        imageViewADPPGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                takePictureFromGallery();
//                alertDialogProfilePicture.dismiss();
//            }
//        });
//    }
//
//    private void takePictureFromGallery(){
//        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(pickPhoto, 1);
//    }
//
//    private void takePictureFromCamera(){
//        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(takePicture.resolveActivity(getPackageManager()) != null){
//            startActivityForResult(takePicture, 2);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode)
//        {
//            case 1:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImageUri = data.getData();
//                    FilePathUri=data.getData();
//                    System.out.println("Path"+FilePathUri);
//                    IVPreviewImage.setImageURI(selectedImageUri);
//                }
//                break;
//            case 2:
//                if(resultCode == RESULT_OK){
//                    Bundle bundle = data.getExtras();
//                    FilePathUri=data.g
//                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//                    IVPreviewImage.setImageBitmap(imageBitmap);
//                }
//                break;
//        }
//    }
//    private boolean checkAndRequestPermissions(){
//        if(Build.VERSION.SDK_INT >= 23){
//            int cameraPermission = ActivityCompat.checkSelfPermission(UploadBooks.this, Manifest.permission.CAMERA);
//            if(cameraPermission == PackageManager.PERMISSION_DENIED){
//                ActivityCompat.requestPermissions(UploadBooks.this, new String[]{Manifest.permission.CAMERA}, 20);
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            takePictureFromCamera();
//        }
//        else
//            Toast.makeText(UploadBooks.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
//    }
    
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getWidth()*0.8), true);
                // Setting up bitmap selected image into ImageView.
                IVPreviewImage.setImageBitmap(resized);

                // After selecting image change choose button above text.
                BSelectImage.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Making it for you ...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

                                    Uri dlUri = uri;
                                    // Hiding the progressDialog after done uploading.
                                    progressDialog.dismiss();

                                    // Showing toast message after done uploading.
                                    Toast.makeText(getApplicationContext(), "Details Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(UploadBooks.this, BuySellScreen.class);
                                    startActivity(intent);
                                    // Getting image name from EditText and store into string variable.
                                    String TempImageName = book_name.getText().toString().trim();
                                    String book_year = pub_year.getText().toString().trim();
                                    String book_price = price.getText().toString().trim();
                                    String book_author = author.getText().toString().trim();
                                    String book_contact = contact_no.getText().toString().trim();
                                    String course = course_name.getSelectedItem().toString().trim();
                                    String crs_yr = course_year.getSelectedItem().toString().trim();
                                    String u_user = sharedpreferences.getString("UserName",null);
                                    // Getting image upload ID.
                                    String ImageUploadId = databaseReference.push().getKey();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName,dlUri.toString(),book_year,book_price,course,crs_yr,u_user,ImageUploadId,book_author,book_contact);





                                    // Adding image upload id s child element into databaseReference.
                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo); }
                            });






//                            @SuppressWarnings("VisibleForTests")



                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            Toast.makeText(UploadBooks.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Making it for you...");

                        }
                    });
        }
        else {

            Toast.makeText(UploadBooks.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.GRAY);
        ((TextView) adapterView.getChildAt(0)).setTextSize(15);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}