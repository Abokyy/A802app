package csapat.app.forLeadersFlow;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import csapat.app.R;
import csapat.app.badgesystem.BadgesActivity;
import csapat.app.badgesystem.TaskListActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForLeadersFragment extends Fragment {


    public ForLeadersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_for_leaders, container, false);

        Button nextMeetingAttBtn = root.findViewById(R.id.nextMeetingAttBtn);
        Button projectorBtn = root.findViewById(R.id.projectorBtn);
        Button ach = root.findViewById(R.id.home_orderBtn);
        final Button submittedTasks = root.findViewById(R.id.allSubmittedTaskBtn);

        submittedTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submittedTasksIntent = new Intent(getActivity(), TaskListActivity.class);
                submittedTasksIntent.putExtra("taskListingMode", 2);
                startActivity(submittedTasksIntent);
            }
        });

        ach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = new Intent(getActivity(), BadgesActivity.class);
                startActivity(temp);
            }
        });

        projectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectorWhereaboutsActivity.class);
                startActivity(intent);
            }
        });

        nextMeetingAttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NextMeetingAttendanceActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

}
