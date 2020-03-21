package csapat.app.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nonnull;

import csapat.app.R;


public class MonthCardAdapter extends RecyclerView.Adapter<MonthCardAdapter.MonthCardViewHolder> implements EventCardAdapter.OnEventCardViewItemSelectedListener {

    private List<String> months;
    private List<Integer> years;
    private List<Event> events;
    private RecyclerView.RecycledViewPool viewPool;
    private RecyclerView recyclerViewInsideMonthCard;
    private EventCardAdapter eventCardAdapter;
    private Context context;

    private OnMonthCardViewItemSelectedListener listener;

    @Nonnull
    @Override
    public MonthCardViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month_card, parent, false);

        return new MonthCardViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@Nonnull MonthCardViewHolder holder, int position) {
        String item = months.get(position);

        List<Event> eventsInMonthInYear = new ArrayList<>();
        int monthNum = transformMonthFromStringToint(months.get(position));
        int yearNum = years.get(position);
        for (Event e : events) {
            if (e.getStartmonth() == monthNum && e.getYear() == yearNum) {
                eventsInMonthInYear.add(e);
            }
        }

        //Collections.sort(eventsInMonthInYear, new EventComparator());
        //TODO a months listát is updatelni amikor a datasetet is updateljük
        holder.monthName.setText(String.format("%d %s", eventsInMonthInYear.get(0).getYear(), months.get(position)));


        eventCardAdapter = new EventCardAdapter(eventsInMonthInYear, context);
        holder.recyclerView.setAdapter(eventCardAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    @Override
    public void onViewItemSelected(String item) {

    }

    public interface OnMonthCardViewItemSelectedListener {
        void onViewItemSelected(String item);
    }

    public void addMonthCardItem(Event newEvent) {
        events.add(newEvent);
        Collections.sort(this.events, new EventComparator());
        notifyItemInserted(events.indexOf(newEvent));
        eventCardAdapter.addEventItem(newEvent);
    }

    public void notifyItemAdded(){
        int s = events.size();
        notifyItemInserted(events.size() - 1);
        eventCardAdapter.notifyItemAdded();
    }

    MonthCardAdapter(OnMonthCardViewItemSelectedListener listener) {
        this.listener = listener;
        months = new ArrayList<>();
        years = new ArrayList<>();
        viewPool = new RecyclerView.RecycledViewPool();

        events = null;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(List<Event> changedEvents) {
        events.clear();
        events.addAll(changedEvents);

        List<Integer> monthNums = new ArrayList<>();
        List<Integer> yearNums = new ArrayList<>();

        monthNums.add(events.get(0).getStartmonth());
        yearNums.add(events.get(0).getYear());


        for (Event e : events) {

            if (yearNums.get(yearNums.size() - 1) != e.getYear()) {
                monthNums.add(e.getStartmonth());
                yearNums.add(e.getYear());
            } else if (monthNums.get(monthNums.size() - 1) != e.getStartmonth()) {
                monthNums.add(e.getStartmonth());
                yearNums.add(e.getYear());
            }

        }

        years = yearNums;
        months = transformIntsToUniqueMonths(monthNums);

        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MonthCardAdapter(List<Event> events, Context context) {
        //months = new ArrayList<>();
        this.events = new ArrayList<>();
        this.events.addAll(events);

        Collections.sort(this.events, new EventComparator());

        List<Integer> monthNums = new ArrayList<>();
        List<Integer> yearNums = new ArrayList<>();

        monthNums.add(events.get(0).getStartmonth());
        yearNums.add(events.get(0).getYear());


        for (Event e : events) {

            if (yearNums.get(yearNums.size() - 1) != e.getYear()) {
                monthNums.add(e.getStartmonth());
                yearNums.add(e.getYear());
            } else if (monthNums.get(monthNums.size() - 1) != e.getStartmonth()) {
                monthNums.add(e.getStartmonth());
                yearNums.add(e.getYear());
            }

        }

        years = yearNums;
        months = transformIntsToUniqueMonths(monthNums);
        this.context = context;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    private List<Event> getEventsInMonth(int monthNum) {
        List<Event> eventsInMonth = new ArrayList<>();

        for (Event e : events) {
            if (e.getStartmonth() == monthNum) eventsInMonth.add(e);
        }

        return eventsInMonth;
    }

    private int transformMonthFromStringToint(String month) {
        switch (month) {
            case ("Január"):
                return 0;
            case ("Február"):
                return 1;
            case ("Március"):
                return 2;
            case ("Április"):
                return 3;
            case ("Május"):
                return 4;
            case ("Június"):
                return 5;
            case ("Július"):
                return 6;
            case ("Augusztus"):
                return 7;
            case ("Szeptember"):
                return 8;
            case ("Október"):
                return 9;
            case ("November"):
                return 10;
            case ("December"):
                return 11;
            default:
                return 12;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<String> transformIntsToUniqueMonths(List<Integer> monthInts) {

        List<String> monthsToDisplay = new ArrayList<>();

        for (Integer i : monthInts) {
            switch (i) {
                case 0:
                    monthsToDisplay.add("Január");
                    break;
                case 1:
                    monthsToDisplay.add("Február");
                    break;
                case 2:
                    monthsToDisplay.add("Március");
                    break;
                case 3:
                    monthsToDisplay.add("Április");
                    break;
                case 4:
                    monthsToDisplay.add("Május");
                    break;
                case 5:
                    monthsToDisplay.add("Június");
                    break;
                case 6:
                    monthsToDisplay.add("Július");
                    break;
                case 7:
                    monthsToDisplay.add("Augusztus");
                    break;
                case 8:
                    monthsToDisplay.add("Szeptember");
                    break;
                case 9:
                    monthsToDisplay.add("Október");
                    break;
                case 10:
                    monthsToDisplay.add("November");
                    break;
                case 11:
                    monthsToDisplay.add("December");
                    break;
                default:
                    monthsToDisplay.add("none");
                    break;
            }
        }

        return monthsToDisplay;
    }


    public class MonthCardViewHolder extends RecyclerView.ViewHolder {

        TextView monthName;

        RecyclerView recyclerView;

        private LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        MonthCardViewHolder(final View itemView) {
            super(itemView);

            monthName = itemView.findViewById(R.id.tvMonthCard);
            recyclerView = itemView.findViewById(R.id.monthRecycler);
            recyclerView.setLayoutManager(layoutManager);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    static class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            return o1.compareTo(o2);
        }
    }

}
