package csapat.app.badgesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import csapat.app.R;

public class BadgesCardAdapter extends RecyclerView.Adapter<BadgesCardAdapter.BadgeCardViewHolder> implements BadgeAdapter.OnBadgeViewItemSelectedListener {

    private List<Badge> badgeList;
    private RecyclerView.RecycledViewPool viewPool;
    private BadgeAdapter badgeAdapter;
    private Context context;

    @NonNull
    @Override
    public BadgeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge_card, parent, false);
        return new BadgeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeCardViewHolder holder, int position) {
        List<Badge> badgeTriple = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            badgeTriple.add(badgeList.get(i));
        }

        badgeAdapter = new BadgeAdapter(badgeTriple, context);
        holder.recyclerView.setAdapter(badgeAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    @Override
    public void onViewItemSelected(String item) {

    }

    public BadgesCardAdapter(List<Badge> badges, Context context) {
        this.badgeList = badges;
        this.context = context;
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
