package pl.wat.nutpromobile.fragments.more;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.ble.DevicesAdapter;

public class MoreElementsAdapter extends RecyclerView.Adapter<MoreElementsAdapter.MyViewHolder> {

    private final List<String> moreElements;
    private final ClickListener clickListener;

    public MoreElementsAdapter(List<String> moreElements, ClickListener clickListener) {
        this.moreElements = moreElements;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public MoreElementsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MoreElementsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.more_elements_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!moreElements.isEmpty()) {
            holder.elementName.setText(moreElements.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return moreElements.isEmpty() ? 1 : moreElements.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView elementName;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            elementName = (TextView) itemView.findViewById(R.id.elementName);
            itemView.setOnClickListener(v -> clickListener.onClick(itemView, getAdapterPosition()));
        }
    }

    public String getElement(Integer pos) {
        return moreElements.get(pos);
    }

    public interface ClickListener{
        void onClick(View v, Integer pos);
    }
}
