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
    private BadgesActivity act;
    private static final String TAG = "badgescardadapter";

    @NonNull
    @Override
    public BadgeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge_card, parent, false);
        return new BadgeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeCardViewHolder holder, int position) {
        List<Badge> badgeTriple = new ArrayList<>();

        //int x

        for (int i = 0; i < 3; i++) {
            if (position * 3 + i < badgeList.size())
                badgeTriple.add(badgeList.get(position * 3 + i));
            else act.hideProgressDialog();
        }

        badgeAdapter = new BadgeAdapter(badgeTriple, unlockedBadges, context, listener);
        holder.recyclerView.setAdapter(badgeAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);
    }


    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) badgeList.size() / 3);
    }

    @Override
    public void onBadgeItemSelected(Badge badge) {


        if(act != null)
            act.showBadgeDescrDialog(badge);

    }

    public BadgesCardAdapter(List<Badge> badges, List<Long> unlockedBadges, Context context, BadgesActivity act, BadgeAdapter.OnBadgeViewItemSelectedListener listener) {
        this.badgeList = badges;
        this.unlockedBadges = unlockedBadges;
        this.act = act;
        this.context = context;
        this.listener = listener;
        viewPool = new RecyclerView.RecycledViewPool();
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
