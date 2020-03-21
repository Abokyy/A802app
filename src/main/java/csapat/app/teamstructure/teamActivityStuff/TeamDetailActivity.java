package csapat.app.teamstructure.teamActivityStuff;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.teamstructure.ViewPagerAdapter;
import csapat.app.teamstructure.model.Team;

public class TeamDetailActivity extends BaseCompat {

    public static final String EXTRA_TEAM_NAME = "extra.team_name";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Team team;
    private String teamname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_structure);

        teamname = getIntent().getStringExtra(EXTRA_TEAM_NAME);
        readData();
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFrag(new TeamPatrolsFragment(team));
        adapter.addFrag(new TeamDetailsFragment(team));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Őrsök");
        tabLayout.getTabAt(1).setText("Részletek");
    }



    private void readData() {


        //showProgressDialog();
        DocumentReference documentReference = db.collection("teams").document(teamname);

        documentReference
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                team = documentSnapshot.toObject(Team.class);
                initView();
                //hideProgressDialog();
            }
        });

    }

}
