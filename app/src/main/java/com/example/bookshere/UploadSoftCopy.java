package com.example.bookshere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class UploadSoftCopy extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String Storage_Path = "All_Pdf_Uploads/";
    //static String Database_Path = "All_Image_Uploads_Database";
    public static String Database_Path1 = "All_Pdf_Uploads_Database/";
    String[] bankNames={"MCA","MBA","EEE","IS","CS","Civil","Mechanical","Biotech"};
    TextView pdfName;
    String[] yearNames={"1st","2nd","3rd","4th"};
    Button BSelectPdf,UploadButtonPdf;
    EditText pdf_name,contact_no_pdf;
    int SELECT_PDF = 200;
    Spinner course_year1,course_name1;
    public Uri FilePathUri1;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        setContentView(R.layout.activity_upload_soft_copy);
        BSelectPdf=findViewById(R.id.BSelectPdf);
        UploadButtonPdf=findViewById(R.id.uploadPdf);
        pdf_name=findViewById(R.id.pdf_name);
        pdfName=findViewById(R.id.pdfDetails);
        contact_no_pdf=findViewById(R.id.contact_no_pdf);
        course_name1  = (Spinner) findViewById(R.id.course_spinner1);
        course_year1 = (Spinner) findViewById(R.id.year_spinner1);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path1);

       course_name1.setOnItemSelectedListener(this);
       course_year1.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter bb = new ArrayAdapter(this,android.R.layout.simple_spinner_item,yearNames);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        course_name1.setAdapter(aa);
        course_year1.setAdapter(bb);
        BSelectPdf.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                pdfChooser();
            }
        });

        UploadButtonPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Pdf_Name = pdf_name.getText().toString().trim();
                String contact_no = contact_no_pdf.getText().toString().trim();

                if(Pdf_Name.isEmpty())
                {
                    pdf_name.setError("Type Book Name");
                    pdf_name.requestFocus();
                    return;
                }
                if(android.text.TextUtils.isDigitsOnly(Pdf_Name)==true){
                    pdf_name.setError("Type valid Book Name");
                    pdf_name.requestFocus();
                    return;
                }
                if(contact_no.isEmpty())
                {
                    contact_no_pdf.setError("type you mobile number");
                    contact_no_pdf.requestFocus();
                    return;
                }
                if(contact_no.length()!=10){
                    contact_no_pdf.setError("Valid number to contact");
                    contact_no_pdf.requestFocus();
                    return;
                }

                UploadPDFFileToFirebaseStorage();

            }

        });


    }



    private void pdfChooser() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri1 = data.getData();
            BSelectPdf.setText("Pdf selected");
            pdfName.setVisibility(View.VISIBLE);
            pdfName.setText("Name: "+ FilePathUri1);
            Toast.makeText(this,""+ FilePathUri1,Toast.LENGTH_LONG).show();

        }

    }
    private void UploadPDFFileToFirebaseStorage() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        final StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(FilePathUri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                                String TempPdfName = pdf_name.getText().toString().trim();
                                String Contact=contact_no_pdf.getText().toString().trim();
                                String Course= course_name1.getSelectedItem().toString().trim();
                                String Course_Year= course_year1.getSelectedItem().toString().trim();
                                String pdfid=databaseReference.push().getKey();
                                String u_user1 = sharedpreferences.getString("UserName",null);
                                fileinfomodel obj=new fileinfomodel(TempPdfName,Contact,Course,Course_Year,u_user1,uri.toString(),pdfid.toString());
                                databaseReference.child(pdfid).setValue(obj);

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(UploadSoftCopy.this, BuySellScreen.class);
                                startActivity(intent);

                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });
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