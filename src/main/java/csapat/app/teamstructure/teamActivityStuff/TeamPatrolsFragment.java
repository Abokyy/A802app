package csapat.app.teamstructure.teamActivityStuff;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import csapat.app.R;
import csapat.app.teamstructure.patrolActivityStuff.PatrolDetailActivity;
import csapat.app.teamstructure.StructureItemAdapter;
import csapat.app.teamstructure.model.Team;


public class TeamPatrolsFragment extends Fragment implements StructureItemAdapter.OnViewItemSelectedListener {

    public static final String EXTRA_TEAM_NAME = "extra.team_name";

    private RecyclerView recyclerView;
    private StructureItemAdapter structureItemAdapter;
    private Team team;
    private List<String> patrols;


    public TeamPatrolsFragment(Team team) {
        this.team = team;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_team_patrols, container, false);

        patrols = new ArrayList<>();



        structureItemAdapter = new StructureItemAdapter(this);
        initRecyclerView(root);
        return root;
    }

    private void initRecyclerView(View view) {

        assert team != null;
        try {
            patrols.addAll(team.getPatrols());
        } catch (NullPointerException e) {
            getActivity().finish();
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Nem találtunk őrsöket a rajban! :(", Toast.LENGTH_LONG);
            toast.show();
        }
        Collections.sort(patrols);
        for(String var: patrols) {
            structureItemAdapter.addStructureItem(var);
        }
        recyclerView = view.findViewById(R.id.structureView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        recyclerView.setAdapter(structureItemAdapter);
    }




    @Override
    public void onViewItemSelected(String item) {
        Intent showNextLevel = new Intent();
        showNextLevel.setClass(getActivity(), PatrolDetailActivity.class);
        showNextLevel.putExtra(PatrolDetailActivity.EXTRA_PATROL_NAME, item);
        startActivity(showNextLevel);
    }
}
