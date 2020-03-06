package csapat.app.badgesystem;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import csapat.app.BaseCompat;
import csapat.app.R;

public class BadgesActivity extends BaseCompat implements BadgeAdapter.OnBadgeViewItemSelectedListener {

    private RecyclerView recyclerView;
    private BadgesCardAdapter badgesCardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Badge> allBadge;
    private static final String TAG = "badgesactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        showProgressDialog();


        allBadge = new ArrayList<>();

        readData();
    }

    private void readData() {


        db.collection("badges")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            allBadge.add(doc.toObject(Badge.class));
                        }
                        initRecyclerView();
                    }
                });
    }

    private void initRecyclerView() {

        badgesCardAdapter = new BadgesCardAdapter(allBadge, BadgesActivity.this, BadgesActivity.this, this);
        Log.d(TAG, "initrecyclerview");
        layoutManager = new LinearLayoutManager(BadgesActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView = findViewById(R.id.badge_list_View);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(badgesCardAdapter);
        hideProgressDialog();
    }

    public void showBadgeDescrDialog(Badge badge) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);

        DialogFragment dialogFragment = BadgeDescriptionDialogFragment.newInstance(badge);
        dialogFragment.show(ft, TAG);
    }

    @Override
    public void onBadgeItemSelected(Badge badge) {
        showBadgeDescrDialog(badge);
    }
}
