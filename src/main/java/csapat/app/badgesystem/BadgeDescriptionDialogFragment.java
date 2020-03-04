package csapat.app.badgesystem;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private Badge badge;

    public  BadgeDescriptionDialogFragment (){}

    public static BadgeDescriptionDialogFragment newInstance(Badge passedBadge) {
        BadgeDescriptionDialogFragment fragment = new BadgeDescriptionDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("badge", passedBadge);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badge = (Badge) getArguments().getSerializable("badge");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.badge_description_dialog_fragment, container, false);

        badgeDescription = v.findViewById(R.id.badgeDescription);
        badgeImage = v.findViewById(R.id.detailedBadgeImage);
        badgeName = v.findViewById(R.id.detailedBadgeName);

        badgeName.setText(badge.getName());
        badgeDescription.setText(badge.getBadgeDescription());

        storageReference.child(badge.getBadgeImageSrc()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
