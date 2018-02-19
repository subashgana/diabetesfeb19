package com.example.subashb.androiddiabeticfounderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import Util.GifImageView;

/**
 * Created by subash.b on 6/24/2017.
 */

public class SelectImageActivity extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;

    Button selectbtn;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    EditText etfirstname,etlastname,editemailaddress,editphoneno,editage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectimage);
        etfirstname = (EditText)findViewById(R.id.etfirstname);
        etlastname = (EditText)findViewById(R.id.etlastname);
        editemailaddress = (EditText)findViewById(R.id.editemailaddress);
        editphoneno = (EditText)findViewById(R.id.editphoneno);
        editage = (EditText)findViewById(R.id.editage);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        selectbtn = (Button)findViewById(R.id.selectbutton);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etfirstname.getText().toString() .length() > 0  && etlastname.getText().toString() .length() > 0  && editemailaddress.getText().toString()  .length() > 0  && editphoneno.getText().toString() .length() > 0  && editage.getText().toString() .length() > 0   ) {

                    if(isValidEmail(editemailaddress.getText().toString()) && isValidPhoneNumber(editphoneno.getText().toString())) {

                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    radioSexButton = (RadioButton) findViewById(selectedId);


                    SharedPreferences pref = getApplicationContext().getSharedPreferences("PROJECT", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name", etfirstname.getText().toString());
                    editor.putInt("idName", 12);
                    editor.commit();
             /*   Toast.makeText(SelectImageActivity.this,
                        radioSexButton.getText(), Toast.LENGTH_SHORT).show();*/
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"Please enter proper phone number and email address",Toast.LENGTH_LONG).show();
                    }

                }
                else{

                    Toast.makeText(SelectImageActivity.this,"Please enter all the fields",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){



                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = null;
                        try {
                            //imageStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap bitmap = decodeUri(selectedImage);

                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("name", etfirstname.getText().toString());
                            intent.putExtra("BitmapImage", bitmap);
                            startActivity(intent);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                }
        }
    }



    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() <=10 ) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

}

