package csapat.app.newsfeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import csapat.app.newsfeed.NewsInstance;
import csapat.app.R;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder> {

    private final List<NewsInstance> news;

    private OnViewItemSelectedListener listener;

    @NonNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_card, parent, false);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsItemViewHolder holder, int position) {
        String item = news.get(position).getTitle();
        holder.newAuthor.setText(news.get(position).getAuthor());
        holder.newTitle.setText(news.get(position).getTitle());
        holder.item = item;

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public interface OnViewItemSelectedListener {
        void onViewItemSelected(String item);
    }

    public NewsAdapter(OnViewItemSelectedListener listener, List<NewsInstance> news) {
        this.listener = listener;
        this.news = news;
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder {

        TextView newTitle;
        TextView newAuthor;

        String item;

        NewsItemViewHolder(final View itemView) {
            super(itemView);

            newTitle = itemView.findViewById(R.id.tvNewTitle);
            newAuthor = itemView.findViewById(R.id.newsAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onViewItemSelected(item);
                    }
                }
            });
        }

    }

}
