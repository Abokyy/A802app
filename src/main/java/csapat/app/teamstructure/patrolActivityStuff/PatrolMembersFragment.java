package csapat.app.teamstructure.patrolActivityStuff;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import csapat.app.R;
import csapat.app.teamstructure.StructureItemAdapter;
import csapat.app.teamstructure.UserProfileActivity;
import csapat.app.teamstructure.model.Patrol;


public class PatrolMembersFragment extends Fragment implements StructureItemAdapter.OnViewItemSelectedListener {


    private RecyclerView recyclerView;
    private StructureItemAdapter structureItemAdapter;
    private List<String> members;
    private Patrol patrol;
    private FirebaseFirestore db;

    public PatrolMembersFragment() {
        // Required empty public constructor
    }

    public PatrolMembersFragment(Patrol patrol) {
        this.patrol = patrol;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_patrol_members, container, false);

        db = FirebaseFirestore.getInstance();
        members = new ArrayList<>();
        structureItemAdapter = new StructureItemAdapter(this);
        initRecyclerView(root);
        return root;
    }

    private void initRecyclerView(View view) {

        assert patrol != null;
        try {
            members.addAll(patrol.getMembers());
        } catch (NullPointerException e) {
            getActivity().finish();
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Nem találtunk tagokat az őrsben! :(", Toast.LENGTH_LONG);
            toast.show();
        }
        Collections.sort(members);
        for (String var : members) {
            structureItemAdapter.addStructureItem(var);
        }

        recyclerView = view.findViewById(R.id.structureView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        recyclerView.setAdapter(structureItemAdapter);
    }

    @Override
    public void onViewItemSelected(String item) {


        db.collection("users")
                .whereEqualTo("fullName", item)
                .whereEqualTo("patrol", patrol.getName())
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
                            Toast.makeText(getActivity(), "Nem találtunk regisztrált felhasználót! :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}
