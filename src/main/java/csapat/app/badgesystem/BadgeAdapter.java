package csapat.app.badgesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import csapat.app.R;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>{

    //private final List<String> badgeNames;
    private final List<Badge> badges;
    private Context context;
    private OnBadgeViewItemSelectedListener listener;

    public BadgeAdapter (List<Badge> badges, Context context) {
        this.badges = badges;
        this.context = context;
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        String item = badges.get(position).getName();
        holder.badgeName.setText(item);
    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public interface OnBadgeViewItemSelectedListener {
        void onViewItemSelected(String item);
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {

        ImageView badgeImage;
        TextView badgeName;

        public BadgeViewHolder(@NonNull final View itemView) {
            super(itemView);

            badgeImage = itemView.findViewById(R.id.badgeImage);
            badgeName = itemView.findViewById(R.id.badgeName);
        }
    }
}
