package csapat.app;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import csapat.app.teamstructure.StructureItemAdapter;
import csapat.app.teamstructure.model.Patrol;

public class NextMeetingAttendanceActivity extends BaseCompat implements StructureItemAdapter.OnViewItemSelectedListener {

    private static final String TAG = "NextMeetingAttendanceAct";
    private RecyclerView recyclerView;
    private StructureItemAdapter structureItemAdapter;
    private List<String> attendees;
    private Patrol patrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_meeting_attendance);

        attendees = new ArrayList<>();
        structureItemAdapter = new StructureItemAdapter(this);

        readData();
    }

    private void readData() {

        db.collection("patrols").document(appUser.getPatrolLeaderAt())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        patrol = documentSnapshot.toObject(Patrol.class);
                        initRecyclerview();
                    }
                });
    }

    private void initRecyclerview(){
        recyclerView = findViewById(R.id.meetingAttendanceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(String s : patrol.getNextMeetingAttendance()) {
            structureItemAdapter.addStructureItem(s);
        }

        recyclerView.setAdapter(structureItemAdapter);
    }

    @Override
    public void onViewItemSelected(String item) {

    }
}
