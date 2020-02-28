package csapat.app.teamstructure.patrolActivityStuff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.ArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.teamstructure.StructureItemAdapter;
import csapat.app.teamstructure.UserProfileActivity;
import csapat.app.teamstructure.ViewPagerAdapter;
import csapat.app.teamstructure.model.Patrol;
import csapat.app.teamstructure.teamActivityStuff.TeamDetailsFragment;
import csapat.app.teamstructure.teamActivityStuff.TeamPatrolsFragment;

public class PatrolDetailActivity extends BaseCompat  {

    public static final String EXTRA_PATROL_NAME = "extra.patrol_name";
    private String patrolName;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Patrol patrol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_structure);



        patrolName = getIntent().getStringExtra(EXTRA_PATROL_NAME);


        readData();

    }

    private void initView() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFrag(new PatrolMembersFragment(patrol));
        adapter.addFrag(new PatrolDetailsFragment(patrol));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Tagok");
        tabLayout.getTabAt(1).setText("Részletek");
    }



    private void readData() {


        DocumentReference documentReference = db.collection("patrols").document(patrolName);

        documentReference
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                patrol = documentSnapshot.toObject(Patrol.class);
                initView();


            }
        });
    }



}
