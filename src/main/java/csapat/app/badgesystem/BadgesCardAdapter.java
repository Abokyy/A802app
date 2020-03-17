package csapat.app.badgesystem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import csapat.app.R;

public class BadgesCardAdapter extends RecyclerView.Adapter<BadgesCardAdapter.BadgeCardViewHolder> implements BadgeAdapter.OnBadgeViewItemSelectedListener {

    private List<Badge> badgeList;
    private List<Long> unlockedBadges;
    private RecyclerView.RecycledViewPool viewPool;
    private BadgeAdapter badgeAdapter;
    private BadgeAdapter.OnBadgeViewItemSelectedListener listener;
    private Context context;
    private Activity act;
    private static final String TAG = "badgescardadapter";

    @NonNull
    @Override
    public BadgeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge_card, parent, false);
        Log.d(TAG, "oncreateviewholder");
        return new BadgeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeCardViewHolder holder, int position) {
        List<Badge> badgeTriple = new ArrayList<>();

        //int x

        for (int i = 0; i < 3; i++) {
            if (position * 3 + i < badgeList.size())
                badgeTriple.add(badgeList.get(position * 3 + i));
        }

        badgeAdapter = new BadgeAdapter(badgeTriple, unlockedBadges, context, listener);
        holder.recyclerView.setAdapter(badgeAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);


        /*switch (position % 3) {
            case 0:
                for (int i = 0; i < 3; i++)
                    badgeTriple.add(badgeList.get(position + i));
                break;
            case 1:
                    badgeTriple.add(badgeList.get(position));
                break;
            case 2: for (int i = 0; i < 2; i++)
                badgeTriple.add(badgeList.get(position + i));
                break;
        }*/


    }


    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) badgeList.size() / 3);
    }

    @Override
    public void onBadgeItemSelected(Badge badge) {

        Log.d(TAG, "item selected");

        if(act instanceof BadgesActivity)
            ((BadgesActivity) act).showBadgeDescrDialog(badge);

    }

    public BadgesCardAdapter(List<Badge> badges, List<Long> unlockedBadges, Context context, Activity act, BadgeAdapter.OnBadgeViewItemSelectedListener listener) {
        this.badgeList = badges;
        this.unlockedBadges = unlockedBadges;
        this.act = act;
        this.context = context;
        this.listener = listener;
        viewPool = new RecyclerView.RecycledViewPool();
        Log.d(TAG, "BadgesCardAdapter constructor");
    }

    public class BadgeCardViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        BadgeCardViewHolder(final View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.badgeRV);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
