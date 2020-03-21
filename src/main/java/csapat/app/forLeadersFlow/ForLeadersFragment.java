package csapat.app.forLeadersFlow;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Objects;

import csapat.app.R;
import csapat.app.badgesystem.BadgesActivity;
import csapat.app.badgesystem.TaskListActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForLeadersFragment extends Fragment {


    private RewardedAd rewardedAd;

    public ForLeadersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_for_leaders, container, false);

        rewardedAd = new RewardedAd(Objects.requireNonNull(getActivity()), "ca-app-pub-4133828845327420/7420873330"); //TODO replace with real rewardAd id


        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Toast.makeText(getActivity(), "Ad loaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                Toast.makeText(getActivity(), "Ad failed to load", Toast.LENGTH_LONG).show();

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        Button nextMeetingAttBtn = root.findViewById(R.id.nextMeetingAttBtn);
        Button projectorBtn = root.findViewById(R.id.projectorBtn);
        Button homeOrder = root.findViewById(R.id.home_orderBtn);
        final Button submittedTasks = root.findViewById(R.id.allSubmittedTaskBtn);

        submittedTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submittedTasksIntent = new Intent(getActivity(), TaskListActivity.class);
                submittedTasksIntent.putExtra("taskListingMode", 2);
                startActivity(submittedTasksIntent);
            }
        });

        homeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Hamarosan", Toast.LENGTH_LONG).show();
                if (rewardedAd.isLoaded()) {
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display.
                        }
                    };
                    rewardedAd.show(getActivity(), adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
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
