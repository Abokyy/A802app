package csapat.app.teamstructure;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.teamstructure.model.AppUser;

public class UserProfileActivity extends BaseCompat {


    public static final String EXTRA_USER_NAME_TO_CHECK = "extra.user_name";
    private String usernameOfProfile;
    //private FirebaseFirestore db;
    private AppUser user;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        usernameOfProfile = getIntent().getStringExtra(EXTRA_USER_NAME_TO_CHECK);

        readData();
    }

    @SuppressLint("SetTextI18n")
    private void initProfileView() {

        TextView memberAtPatrol = findViewById(R.id.profile_patrol_member_at);
        TextView profileFullname = findViewById(R.id.profile_FullName);
        //TextView rank = findViewById(R.id.profile_rank);
        TextView patrolLeaderAt = findViewById(R.id.profile_patrol_leader_at);
        TextView troopLeaderAt = findViewById(R.id.profile_troop_leader_at);
        View patrolLeaderAtLayout = findViewById(R.id.profile_patrol_leader_at_layout);
        View troopLeaderAtLayout = findViewById(R.id.profile_troop_leader_at_layout);
        progressBar = findViewById(R.id.UserProfileProgressBar);
        final ImageView imageView = findViewById(R.id.profile_picture);

        profileFullname.setText(user.getFullName());

        int userRank = user.getRank();

        memberAtPatrol.setText("Tagja a " + user.getPatrol() + " őrsnek.");
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
                patrolLeaderAt.setText("Vezetője a " + user.getPatrolLeaderAt() + " őrsnek.");
                //rank.setText(R.string.patrolLeader);
                break;

            case 4:
                patrolLeaderAt.setText("Vezetője a " + user.getPatrolLeaderAt() + " őrsnek.");
                troopLeaderAt.setText("Parancsnoka a " + user.getTroopLeaderAt() + " rajnak.");
                //rank.setText(R.string.troop_leader);
                break;

            case 5:
                //rank.setText(R.string.team_leader);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + user.getRank());
        }


        if (user.getProfile_picture() != null) {

            storageReference.child(user.getProfile_picture()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(UserProfileActivity.this)
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

    private void readData() {

        db.collection("users")
                .whereEqualTo("username", usernameOfProfile)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = document.toObject(AppUser.class);
                                initProfileView();
                            }
                        } else {
                            //TODO handle unsuccessful query
                        }
                    }
                });

    }

    /*private void readLeadUnits(final TextView patrolLeaderAt, final TextView troopLeaderAt, int userRank) {


        db.collection("patrols")
                .whereEqualTo("leader", user.getFullName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                troopLeaderAt.setText("Őrsvezető a " + document.getString("name") + " őrsben.");
                            }
                        }
                    }
                });


        if (userRank > 3) {
            db.collection("teams")
                    .whereEqualTo("leader", user.getFullName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    patrolLeaderAt.setText("Rajparancsnok a " + document.getString("name") + " rajban.");
                                    initProfileView();
                                }
                            }
                        }
                    });
        } else initProfileView();


    }*/

}
