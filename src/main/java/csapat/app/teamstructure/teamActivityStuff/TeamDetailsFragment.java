package csapat.app.teamstructure.teamActivityStuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csapat.app.R;
import csapat.app.teamstructure.model.Team;


public class TeamDetailsFragment extends Fragment {

    private Team team;

    public TeamDetailsFragment(Team team) {
        this.team = team;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_team_details, container, false);

        TextView leader = root.findViewById(R.id.unit_leader_name);
        TextView name = root.findViewById(R.id.unit_name);

        name.setText(team.getName() + " raj");
        leader.setText("Parancsnok: " + team.getLeader());

        return root;
    }
}
