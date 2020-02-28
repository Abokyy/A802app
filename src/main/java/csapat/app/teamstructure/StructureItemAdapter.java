package csapat.app.teamstructure;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

import csapat.app.R;

public class StructureItemAdapter extends RecyclerView.Adapter<StructureItemAdapter.StructureItemViewHolder> {

    private final List<String> viewitems;

    private OnViewItemSelectedListener listener;

    @NonNull
    @Override
    public StructureItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_structure_card, parent, false);
        return new StructureItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StructureItemViewHolder holder, int position) {
        String item = viewitems.get(position);
        holder.nameTextView.setText(viewitems.get(position));
        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return viewitems.size();
    }

    public void addStructureItem(String newItem) {
        viewitems.add(newItem);
        notifyItemInserted(viewitems.size()-1);
    }

    public interface OnViewItemSelectedListener {
        void onViewItemSelected(String item);
    }

    public StructureItemAdapter(OnViewItemSelectedListener listener) {
        this.listener = listener;
        viewitems = new ArrayList<>();
    }

    class StructureItemViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;


        String item;

        StructureItemViewHolder(final View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tvItemName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onViewItemSelected(item);
                    }
                }
            });
        }
    }

}
