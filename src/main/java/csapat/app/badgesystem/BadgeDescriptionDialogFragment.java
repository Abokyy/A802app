package csapat.app.badgesystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import csapat.app.R;

public class BadgeDescriptionDialogFragment extends DialogFragment {


    private ImageView badgeImage;
    private TextView badgeName;
    private TextView badgeDescription;

    public  BadgeDescriptionDialogFragment (){}

    public BadgeDescriptionDialogFragment (Badge badge) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout., container, false);

        // Do all the stuff to initialize your custom view

        return v;
    }


}
