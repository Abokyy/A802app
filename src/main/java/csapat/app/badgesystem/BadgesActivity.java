package csapat.app.badgesystem;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import csapat.app.BaseCompat;
import csapat.app.R;

public class BadgesActivity extends BaseCompat implements BadgeAdapter.OnBadgeViewItemSelectedListener {

    private RecyclerView personalRecyclerView;
    private RecyclerView patrolRecyclerView;
    private BadgesCardAdapter personalBadgesCardAdapter;
    private BadgesCardAdapter patrolBadgesCardAdapter;
    private RecyclerView.LayoutManager personallayoutManager;
    private RecyclerView.LayoutManager patrollayoutManager;
    private List<Badge> allpersonalBadge;
    private List<Badge> allPatrolBadges;
    private List<Long> unlockedPersonalBadges;
    private List<Long> unlockedPatrolBadges;
    private static final String TAG = "badgesactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        showProgressDialog();


        allpersonalBadge = new ArrayList<>();
        unlockedPersonalBadges = new ArrayList<>();
        allPatrolBadges = new ArrayList<>();
        unlockedPatrolBadges = new ArrayList<>();

        readData();
    }

    private void readData() {


        db.collection("badges")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Badge badge = doc.toObject(Badge.class);
                            switch (badge.getLevel()) {
                                case 1:
                                    allpersonalBadge.add(badge);
                                    break;
                                case 2:
                                    allPatrolBadges.add(badge);
                                    break;
                            }
                        }

                        db.collection("users").document(appUser.getUserID())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        unlockedPersonalBadges = (List<Long>) documentSnapshot.get("achievedBadges");
                                        db.collection("patrols").document(appUser.getPatrol())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        unlockedPatrolBadges = (List<Long>) documentSnapshot.get("achievedBadges");
                                                        initRecyclerView();
                                                    }
                                                });
                                        //hideProgressDialog();
                                    }
                                });

                    }
                });
    }

    private void initRecyclerView() {

        personalBadgesCardAdapter = new BadgesCardAdapter(allpersonalBadge, unlockedPersonalBadges, BadgesActivity.this, BadgesActivity.this, this);
        patrolBadgesCardAdapter = new BadgesCardAdapter(allPatrolBadges, unlockedPatrolBadges, BadgesActivity.this, BadgesActivity.this, this);
        Log.d(TAG, "initrecyclerview");
        personallayoutManager = new LinearLayoutManager(BadgesActivity.this, LinearLayoutManager.VERTICAL, false);
        patrollayoutManager = new LinearLayoutManager(BadgesActivity.this, LinearLayoutManager.VERTICAL, false);

        personalRecyclerView = findViewById(R.id.personal_badge_list_View);
        personalRecyclerView.setLayoutManager(personallayoutManager);
        personalRecyclerView.setAdapter(personalBadgesCardAdapter);

        patrolRecyclerView = findViewById(R.id.patrol_badge_list_View);
        patrolRecyclerView.setLayoutManager(patrollayoutManager);
        patrolRecyclerView.setAdapter(patrolBadgesCardAdapter);

        personalRecyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        hideProgressDialog();
                        personalRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    public void showBadgeDescrDialog(Badge badge) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);

        boolean unlocked = false;

        if (unlockedPersonalBadges.contains((long) badge.getBadgeID()) || unlockedPatrolBadges.contains((long) badge.getBadgeID()))
            unlocked = true;

        DialogFragment dialogFragment = BadgeDescriptionDialogFragment.newInstance(badge, unlocked);
        dialogFragment.show(ft, TAG);
    }

    @Override
    public void onBadgeItemSelected(Badge badge) {
        showBadgeDescrDialog(badge);
    }
}
