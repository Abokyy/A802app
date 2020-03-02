package csapat.app.newsfeed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.supportFiles.SaveSharedPreference;

public class NewsFragment extends Fragment implements NewsAdapter.OnViewItemSelectedListener {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsInstance> news;
    private FirebaseFirestore db;
    BaseCompat baseCompat;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        news = new ArrayList<>();
        baseCompat = new BaseCompat();
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        FloatingActionButton fab = root.findViewById(R.id.news_feed_floating_button);

        if(SaveSharedPreference.getAppUser(getActivity()).getRank() == 1 ||SaveSharedPreference.getAppUser(getActivity()).getUsername().equals("802guest")) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setBehavior(null); //should disable default animations
            p.setAnchorId(View.NO_ID); //should let you set visibility
            fab.setLayoutParams(p);
            fab.setVisibility(View.GONE); // View.INVISIBLE might also be worth trying
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewActivityIntent = new Intent(getActivity(), AddNewNewActivity.class);
                startActivity(addNewActivityIntent);
                //getActivity().finish();
            }
        });

        readData(root);
        return root;
    }

    private void readData(final View view) {
        //baseCompat.showProgressDialog(getActivity().getBaseContext());

        db.collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot documentSnapshot : task.getResult()) {
                                news.add(documentSnapshot.toObject(NewsInstance.class));
                            }
                        } else {
                            //TODO
                        }
                        initRecyclerView(view);
                        //baseCompat.hideProgressDialog();
                    }
                });



    }

    private void initRecyclerView(View view) {

        recyclerView = view.findViewById(R.id.news_feed_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsAdapter = new NewsAdapter(this, news);
        recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void onViewItemSelected(String item) {
        Intent showNewDetail = new Intent();
        showNewDetail.setClass(getActivity(), NewDetailActivity.class);
        showNewDetail.putExtra(NewDetailActivity.EXTRA_NEW_TITLE, item);
        startActivity(showNewDetail);
    }
}