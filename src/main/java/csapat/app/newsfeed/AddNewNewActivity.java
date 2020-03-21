package csapat.app.newsfeed;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import 	java.util.UUID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.supportFiles.SaveSharedPreference;


public class AddNewNewActivity extends BaseCompat {

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private String imagePath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_new);

        final EditText newTitle = findViewById(R.id.new_new_title);
        final EditText newDescription = findViewById(R.id.new_new_descreption);
        Button submitBtn = findViewById(R.id.submit_new_new_btn);
        Button imageChooserBtn = findViewById(R.id.choose_image_button);
        imageView = findViewById(R.id.imageViewForUpload);


        imageChooserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                Map<String, Object> data = new HashMap<>();
                data.put("author", SaveSharedPreference.getAppUser(AddNewNewActivity.this).getFullName());
                data.put("title", newTitle.getText().toString());
                data.put("description", newDescription.getText().toString());
                data.put("imagePath", imagePath);


                db.collection("news")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error adding document", e);
                            }
                        });

                Toast toast = Toast.makeText(getApplicationContext(), "Hír közzétéve", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            showProgressDialog();
            String rndUUID = "images_for_news/" + UUID.randomUUID().toString();
            imagePath = rndUUID;
            StorageReference ref = storageReference.child(rndUUID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            hideProgressDialog();
                            Toast.makeText(AddNewNewActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                            Toast.makeText(AddNewNewActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }
    }
}
