package csapat.app.teamstructure.patrolActivityStuff;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import csapat.app.R;
import csapat.app.teamstructure.UserProfileActivity;
import csapat.app.teamstructure.model.Patrol;


public class PatrolDetailsFragment extends Fragment {


    private Patrol patrol;
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();

        Button leader = root.findViewById(R.id.unit_leader_name);
        TextView name = root.findViewById(R.id.unit_name);
        try {
            name.setText(patrol.getName() + " őrs");
            leader.setText("Őrsvezető: " + patrol.getLeader());
            leader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("users")
                            .whereEqualTo("fullName", patrol.getLeader())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            String username;
                                            username = document.getString("username");
                                            Intent showNextLevel = new Intent();
                                            showNextLevel.setClass(getActivity(), UserProfileActivity.class);
                                            showNextLevel.putExtra(UserProfileActivity.EXTRA_USER_NAME_TO_CHECK, username);
                                            startActivity(showNextLevel);
                                        }
                                    } else {

                                        Toast.makeText(getActivity(), "Nem találtunk felhasználót", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
            });
        } catch (Exception e) {
            Log.d("baj", "baj");
            Toast.makeText(getActivity(), "Nem találtunk felhasználót", Toast.LENGTH_LONG).show();

        }

        return root;
    }

}
