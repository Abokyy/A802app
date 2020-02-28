package csapat.app.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import csapat.app.R;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.EventCardViewHolder> {

    private final List<String> eventTitles;
    private final List<Event> events;
    private Context context;
    private OnEventCardViewItemSelectedListener listener;

    public EventCardAdapter(List<Event> events, Context context) {
        this.events = events;
        eventTitles = new ArrayList<>();

        for(Event e : events) {
            eventTitles.add(e.getTitle());
        }

        this.context = context;

    }

    @Nonnull
    @Override
    public EventCardViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_card, parent, false);
        return new EventCardViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@Nonnull EventCardViewHolder holder, int position) {
        String item = events.get(position).getTitle();
        holder.eventTitle.setText(item);
        String startday = String.valueOf(events.get(position).getStartday());

        if (events.get(position).getEndday() != -1) {
            String endday = String.valueOf(events.get(position).getEndday());

            holder.dayNumber.setText(startday + "-" + endday);
        } else {
            holder.dayNumber.setText(startday);
        }

        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return eventTitles.size();
    }

    public void addEventItem(Event newEvent) {
        events.add(newEvent);
        Collections.sort(this.events, new EventCardAdapter.EventComparator());
        notifyItemInserted(events.indexOf(newEvent));
    }

    public void notifyItemAdded() {
        notifyItemInserted(events.size()-1);
    }

    public void update() {

    }

    public interface OnEventCardViewItemSelectedListener {
        void onViewItemSelected(String item);
    }

    EventCardAdapter (OnEventCardViewItemSelectedListener listener) {
        this.listener = listener;
        eventTitles = new ArrayList<>();
        events = new ArrayList<>();
    }




   public class EventCardViewHolder extends  RecyclerView.ViewHolder {

        TextView dayNumber;
        TextView eventTitle;

        String item;

        EventCardViewHolder (final View itemView) {
            super(itemView);

            dayNumber = itemView.findViewById(R.id.tvEventDate);
            eventTitle = itemView.findViewById(R.id.event_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

        }

    }

    class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            return o1.compareTo(o2);
        }
    }
}
