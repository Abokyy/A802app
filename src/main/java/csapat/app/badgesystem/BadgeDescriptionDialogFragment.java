package csapat.app.badgesystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

import csapat.app.R;

import static csapat.app.BaseCompat.storageReference;

public class BadgeDescriptionDialogFragment extends DialogFragment {


    private ImageView badgeImage;
    private TextView badgeName;
    private TextView badgeDescription;
    private TextView badgePoints;
    private Badge badge;
    private boolean unlocked;
    private Button unclockBtn;

    public  BadgeDescriptionDialogFragment (){}

    public static BadgeDescriptionDialogFragment newInstance(Badge passedBadge, boolean unlocked) {
        BadgeDescriptionDialogFragment fragment = new BadgeDescriptionDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("badge", passedBadge);
        args.putBoolean("unlocked", unlocked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badge = (Badge) getArguments().getSerializable("badge");
        unlocked = getArguments().getBoolean("unlocked");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_badge_description_dialog, container, false);

        badgeDescription = v.findViewById(R.id.badgeDescription);
        badgeImage = v.findViewById(R.id.detailedBadgeImage);
        badgeName = v.findViewById(R.id.detailedBadgeName);
        badgePoints = v.findViewById(R.id.badgePointNumber);
        unclockBtn = v.findViewById(R.id.unlockBtn);
        String imageToLoad = "";

        if(unlocked) {
            unclockBtn.setVisibility(View.GONE);
            imageToLoad = badge.getUnlockedImgSrc();
        } else {
            imageToLoad = badge.getBadgeImageSrc();
        }

        unclockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendTaskActivity.class);
                intent.putExtra("badgeID", badge.getBadgeID());
                intent.putExtra("badgeLevel", badge.getLevel());
                startActivity(intent);
            }
        });


        badgePoints.setText(Integer.toString(badge.getPoints()));

        badgeName.setText(badge.getName());
        badgeDescription.setText(badge.getBadgeDescription());

        storageReference.child(imageToLoad).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(uri)
                        .into(badgeImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



        return v;
    }


}
