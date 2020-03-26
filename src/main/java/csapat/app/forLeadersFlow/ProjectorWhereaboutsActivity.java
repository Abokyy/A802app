package csapat.app.forLeadersFlow;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.forLeadersFlow.ProjectorWhereabouts;

public class ProjectorWhereaboutsActivity extends BaseCompat {


    TextView projectorWhereabouts;
    Button iHaveTheProjectorBtn;
    DatabaseReference projWhereInDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projector_whereabouts);

        projectorWhereabouts = findViewById(R.id.projectorWhereaboutsTextView);
        iHaveTheProjectorBtn = findViewById(R.id.iHaveTheProjectorBtn);
        Button projAtCsotthon = findViewById(R.id.projAtCsotthon);
        Button projAtKozhaz = findViewById(R.id.projAtKozhaz);

        projAtCsotthon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProjectorWhereabouts("Csotthon");
            }
        });

        projAtKozhaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProjectorWhereabouts("Közház");
            }
        });

        iHaveTheProjectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProjectorWhereabouts(appUser.getFullName());
            }
        });

        realtimeDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ProjectorWhereabouts projWhere = dataSnapshot.getValue(ProjectorWhereabouts.class);
                assert projWhere != null;
                projectorWhereabouts.setText(projWhere.getLocation());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ProjectorWhereabouts projWhere = dataSnapshot.getValue(ProjectorWhereabouts.class);
                assert projWhere != null;
                projectorWhereabouts.setText(projWhere.getLocation());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void setProjectorWhereabouts(String where) {
        realtimeDB.child("projectorWhereabouts").child("location").setValue(where);
    }


}

