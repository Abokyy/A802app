package csapat.app.teamstructure;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.teamstructure.teamActivityStuff.TeamDetailActivity;

public class TeamStructureFragment extends Fragment implements StructureItemAdapter.OnViewItemSelectedListener{

    public static final String EXTRA_TEAM_NAME = "extra.team_name";
    private static final String TAG = "TeamStructureAct";
    private RecyclerView recyclerView;
    private StructureItemAdapter structureItemAdapter;
    private List<String> troops;
    private FirebaseFirestore db;
    private BaseCompat baseCompat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        View root = inflater.inflate(R.layout.fragment_team_structure, container, false);

        troops = new ArrayList<>();

        readData(root);

        return root;
    }

    private void readData(final View view) {
        //baseCompat.showProgressDialog();
        db.collection("teams")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                troops.add(document.getString("name"));
                            }
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                        initRecyclerView(view);
                    }
                });
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.structureView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        structureItemAdapter = new StructureItemAdapter(this);

        Collections.sort(troops);

        for(String s : troops)
            structureItemAdapter.addStructureItem(s);

        recyclerView.setAdapter(structureItemAdapter);
    }

    @Override
    public void onViewItemSelected(String item) {
        Intent showNextLevel = new Intent();
        showNextLevel.setClass(getActivity(), TeamDetailActivity.class);
        showNextLevel.putExtra(TeamDetailActivity.EXTRA_TEAM_NAME, item);
        startActivity(showNextLevel);


    }
}