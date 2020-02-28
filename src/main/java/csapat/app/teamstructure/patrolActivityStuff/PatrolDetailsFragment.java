package csapat.app.teamstructure.patrolActivityStuff;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csapat.app.R;
import csapat.app.teamstructure.model.Patrol;


public class PatrolDetailsFragment extends Fragment {


    private Patrol patrol;

    public PatrolDetailsFragment() {
        // Required empty public constructor
    }

    public PatrolDetailsFragment(Patrol patrol) {
        this.patrol = patrol;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_team_details, container, false);

        TextView leader = root.findViewById(R.id.unit_leader_name);
        TextView name = root.findViewById(R.id.unit_name);
        name.setText(patrol.getName() + " őrs");
        leader.setText("Őrsvezető: " + patrol.getLeader());
        return root;
    }

}
