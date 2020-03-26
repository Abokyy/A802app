package csapat.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

public class EditProfileActivity extends BaseCompat {


    private final int PICK_IMAGE_REQUEST = 71;
    private final int PERMISSION_CODE = 1000;
    private Uri filePath;
    private String imagePath;
    private ImageView imageView;
    private ProgressBar progressBar;
    private boolean choosedNewImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button changeProfilePic = findViewById(R.id.change_profile_pic_btn);

        imageView = findViewById(R.id.edit_new_profile_picture);
        progressBar = findViewById(R.id.editCurrentUserProgressBar);

        initTextViews();


        FloatingActionButton fab = findViewById(R.id.profile_saving_floating_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                finish();
            }
        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        chooseImage();
                    }
                } else {
                    //system OS is < Marshmallow
                    chooseImage();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initTextViews() {

        TextView profileFullName = findViewById(R.id.edit_profile_FullName);
        TextView memberAtPatrol = findViewById(R.id.edit_profile_patrol_member_at);
        TextView patrolLeaderAt = findViewById(R.id.edit_profile_patrol_leader_at);
        TextView troopLeaderAt = findViewById(R.id.edit_profile_troop_leader_at);
        View patrolLeaderAtLayout = findViewById(R.id.edit_profile_patrol_leader_at_layout);
        View troopLeaderAtLayout = findViewById(R.id.edit_profile_troop_leader_at_layout);

        profileFullName.setText(appUser.getFullName());
        int userRank = appUser.getRank();

        memberAtPatrol.setText("Tagja vagy a " + appUser.getPatrol() + " őrsnek.");
        switch (userRank) {
            case 1:
                patrolLeaderAtLayout.setVisibility(View.INVISIBLE);
                troopLeaderAtLayout.setVisibility(View.INVISIBLE);
                //rank.setText(R.string.member);
                break;

            case 2:
                patrolLeaderAtLayout.setVisibility(View.INVISIBLE);
                troopLeaderAtLayout.setVisibility(View.INVISIBLE);
                //rank.setText(R.string.sub_leader);

            case 3:
                troopLeaderAtLayout.setVisibility(View.INVISIBLE);
                patrolLeaderAt.setText("Vezetője vagy a " + appUser.getPatrolLeaderAt() + " őrsnek.");
                //rank.setText(R.string.patrolLeader);
                break;

            case 4:
                patrolLeaderAt.setText("Vezetője vagy a " + appUser.getPatrolLeaderAt() + " őrsnek.");
                troopLeaderAt.setText("Parancsnoka vagy a " + appUser.getTroopLeaderAt() + " rajnak.");
                //rank.setText(R.string.troop_leader);
                break;

            case 5:
                //rank.setText(R.string.team_leader);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + userRank);
        }

        if (!BaseCompat.appUser.getProfile_picture().equals("default") && !choosedNewImage) {

            BaseCompat.storageReference.child(BaseCompat.appUser.getProfile_picture()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(EditProfileActivity.this)
                            .load(uri)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imageView);

                    //hideProgressDialog();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.profilepictureicon);
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(EditProfileActivity.this, "Engedély elutasítva", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                choosedNewImage = true;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            showProgressDialog(EditProfileActivity.this);
            storageReference.child(appUser.getProfile_picture()).delete();
            String rndUUID = "profile_pictures/" + UUID.randomUUID().toString();
            imagePath = rndUUID;
            StorageReference ref = storageReference.child(rndUUID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            hideProgressDialog();
                            Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                            Toast.makeText(EditProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


            //db.collection("users").document(appUser.getUserID()).update("profile_picture", imagePath);

            Map<String, Object> data = new HashMap<>();

            data.put("profile_picture", imagePath);

            BaseCompat.appUser.setProfile_picture(imagePath);

            db.collection("users").document(appUser.getUserID()).set(data, SetOptions.merge());
        }

    }


}

