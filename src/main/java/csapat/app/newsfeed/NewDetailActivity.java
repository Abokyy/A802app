package csapat.app.newsfeed;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import csapat.app.BaseCompat;
import csapat.app.R;

public class NewDetailActivity extends BaseCompat {


    public static final String EXTRA_NEW_TITLE = "extra.new_title";

    private String newsTitle;
    private NewsInstance newsInstance;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
        //showProgressDialog();


        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        newsTitle = getIntent().getStringExtra(EXTRA_NEW_TITLE);imageView = findViewById(R.id.new_detail_photo);



        readData(this);

    }

    private void readData(final Context context) {




        showProgressDialog();
        db.collection("news")
                .whereEqualTo("title", newsTitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                newsInstance = document.toObject(NewsInstance.class);
                            }







                        } else {
                            //TODO
                        }
                        initNewsDetailView(context);
                    }
                });

    }

    private void initNewsDetailView(final Context context) {

        TextView newDescription = findViewById(R.id.new_description);
        newDescription.setText(newsInstance.getDescription());


        storageReference.child(newsInstance.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(context)
                        .load(uri)
                        .into(imageView);

                hideProgressDialog();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }
}
