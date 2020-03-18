package csapat.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import csapat.app.auth.LoginActivity;
import csapat.app.supportFiles.SaveSharedPreference;
import csapat.app.teamstructure.model.AppUser;


public class CurrentUserProfileFragment extends Fragment {


    private ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_current_user_profile, container, false);

        Button logoutBtn = root.findViewById(R.id.btn_logout);
        FloatingActionButton fab = root.findViewById(R.id.profile_editing_floating_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent;
                editProfileIntent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(editProfileIntent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivityIntent;
                SaveSharedPreference.logOutUser(getActivity());
                FirebaseAuth.getInstance().signOut();
                //baseCompat.signOut();
                nextActivityIntent = new Intent(getActivity(), LoginActivity.class);
                nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(nextActivityIntent);
                getActivity().finish();
            }
        });

        imageView = root.findViewById(R.id.current_profile_picture);

        readData(root, getActivity());

        return root;
    }


    public void getProfilePicture(final Context context) {


    }

    @SuppressLint("SetTextI18n")
    private void initProfileView(View view, final Context context) {

        TextView memberAtPatrol = view.findViewById(R.id.current_profile_patrol_member_at);
        TextView profileFullname = view.findViewById(R.id.current_profile_FullName);
        //TextView rank = findViewById(R.id.profile_rank);
        TextView patrolLeaderAt = view.findViewById(R.id.current_profile_patrol_leader_at);
        TextView troopLeaderAt = view.findViewById(R.id.current_profile_troop_leader_at);
        View patrolLeaderAtLayout = view.findViewById(R.id.current_profile_patrol_leader_at_layout);
        View troopLeaderAtLayout = view.findViewById(R.id.current_profile_troop_leader_at_layout);

        AppUser user = BaseCompat.appUser;

        profileFullname.setText(user.getFullName());

        int userRank = user.getRank();


        memberAtPatrol.setText("Tagja vagy a " + user.getPatrol() + " őrsnek.");
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
                patrolLeaderAt.setText("Vezetője vagy a " + user.getPatrolLeaderAt() + " őrsnek.");
                //rank.setText(R.string.patrolLeader);
                break;

            case 4:
                patrolLeaderAt.setText("Vezetője vagy a " + user.getPatrolLeaderAt() + " őrsnek.");
                troopLeaderAt.setText("Parancsnoka vagy a " + user.getTroopLeaderAt() + " rajnak.");
                //rank.setText(R.string.troop_leader);
                break;

            case 5:
                //rank.setText(R.string.team_leader);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + user.getRank());
        }


        if (BaseCompat.appUser.getProfile_picture() != null) {

            BaseCompat.storageReference.child(BaseCompat.appUser.getProfile_picture()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(context)
                            .load(uri)
                            .into(imageView);

                    //hideProgressDialog();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    }

    private void readData(final View view, final Context context) {

        //baseCompat.showProgressDialog(getActivity());

        BaseCompat.db.collection("users")
                .whereEqualTo("username", BaseCompat.appUser.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                initProfileView(view, context);
                            }
                        } else {
                            //TODO handle unsuccessful query
                        }

                        //baseCompat.hideProgressDialog();
                    }
                });

    }


}
