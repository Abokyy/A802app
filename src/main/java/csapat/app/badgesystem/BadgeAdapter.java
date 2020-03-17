package csapat.app.badgesystem;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Arrays;

import csapat.app.BaseCompat;
import csapat.app.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static csapat.app.BaseCompat.storageReference;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {

    //private final List<String> badgeNames;
    private final List<Badge> badges;
    private final List<Long> unlocked;
    private Context context;
    private OnBadgeViewItemSelectedListener listener;

    public BadgeAdapter(List<Badge> badges, List<Long> unlocked, Context context, OnBadgeViewItemSelectedListener listener) {
        this.listener = listener;
        this.unlocked = unlocked;
        this.badges = badges;
        this.context = context;
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge, parent, false);
        view.getLayoutParams().width = parent.getMeasuredWidth() / 3;

        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BadgeViewHolder holder, int position) {
        holder.badge = badges.get(position);
        String item = badges.get(position).getName();
        holder.badgeName.setText(item);

        String imageToLoad;

        if (unlocked.contains((long) holder.badge.getBadgeID())) {
            imageToLoad = holder.badge.getUnlockedImgSrc();
        } else {
            imageToLoad = holder.badge.getBadgeImageSrc();
        }

        storageReference.child(imageToLoad).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(context)
                        .load(uri)
                        .into(holder.badgeImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public interface OnBadgeViewItemSelectedListener {
        void onBadgeItemSelected(Badge badge);
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {

        ImageView badgeImage;
        TextView badgeName;
        Badge badge;

        public BadgeViewHolder(@NonNull final View itemView) {
            super(itemView);

            badgeImage = itemView.findViewById(R.id.badgeImage);
            badgeName = itemView.findViewById(R.id.badgeName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onBadgeItemSelected(badge);
                }
            });
        }
    }
}
