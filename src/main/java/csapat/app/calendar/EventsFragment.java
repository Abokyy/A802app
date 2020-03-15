package csapat.app.calendar;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import csapat.app.NavMainActivity;
import csapat.app.R;
import csapat.app.supportFiles.SaveSharedPreference;

public class EventsFragment extends Fragment implements MonthCardAdapter.OnMonthCardViewItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private MonthCardAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Event> allEvents;
    private FirebaseFirestore db;
    private Button showPrevEventsBtn;
    private SwipeRefreshLayout swipeContainer;


    private static final String TAG = "eventsfragment";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);

        allEvents = new ArrayList<>();
        FloatingActionButton fab = root.findViewById(R.id.floating_action_button);
        showPrevEventsBtn = root.findViewById(R.id.show_prev_events_btn);
        swipeContainer = root.findViewById(R.id.swipeContainer);
        db = FirebaseFirestore.getInstance();

        if(SaveSharedPreference.getAppUser(getActivity()).getRank() == 1 || SaveSharedPreference.getAppUser(getActivity()).getUsername().equals("802guest")) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setBehavior(null); //should disable default animations
            p.setAnchorId(View.NO_ID); //should let you set visibility
            fab.setLayoutParams(p);
            fab.setVisibility(View.GONE); // View.INVISIBLE might also be worth trying
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewActivityIntent = new Intent(getActivity(), AddNewEventActivity.class);
                startActivity(addNewActivityIntent);
                //getActivity().finish();
            }
        });

        swipeContainer.setOnRefreshListener(this);


        showPrevEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.update(allEvents);
                //showNotification();
                showPrevEventsBtn.setVisibility(View.GONE);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        readData(root);
        //initRecyclerView(root);
        return root;
    }

    private void readData(final View view) {

        db.collection("events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        allEvents.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("startday") != null) {
                                allEvents.add(doc.toObject(Event.class));
                                //allEvents.add(doc.toObject(Event.class));
                                //adapter.addMonthCardItem(doc.toObject(Event.class));
                            }
                        }
                        //Log.d(TAG, "Current cites in CA: " + cities);
                        initRecyclerView(view);

                    }
                });

        //showProgressDialog();
        /*db.collection("allEvents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Event ev = document.toObject(Event.class);
                                allEvents.add(ev);
                            }
                        } else {
                            //Log.d(TAG, "Cached get failed: ", task.getException());
                        }

                        initRecyclerView(view);
                        //hideProgressDialog();
                    }
                });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initRecyclerView(View view) {

        Collections.sort(this.allEvents, new MonthCardAdapter.EventComparator());

        Calendar c = Calendar.getInstance();
        List<Event> upcomingEvents = new ArrayList<>();
        Event today = new Event (c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        for (Event e: allEvents) {
            if(e.compareTo(today) > 0) {
                upcomingEvents.add(e);
            }
        }

        adapter = new MonthCardAdapter(allEvents, getActivity());
        adapter.update(upcomingEvents);
        layoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);

        recyclerView = view.findViewById(R.id.event_list_View);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);






    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                adapter.update(allEvents);
                swipeContainer.setRefreshing(false);
            }
        }, 2000);
    }


    private void showNotification() {
        Notification notification = new NotificationCompat.Builder(getActivity(), NavMainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Title")
                .setContentText("Text")
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, notification);
    }







    @Override
    public void onViewItemSelected(String item) {
    }
}