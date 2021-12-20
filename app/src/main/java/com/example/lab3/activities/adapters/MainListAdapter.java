package com.example.lab3.activities.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.R;
import com.example.lab3.fragments.MainListFragment;
import com.example.lab3.model.Train;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ElementsViewHolder> {
    private final ArrayList<Train> elements;
    private final LayoutInflater inflater;
    private ItemSelected onItemSelected;
    private ElementLongClick onItemLongClick;
    private final Context context;
    private int contextualSelectedId;
    private View view;

    public MainListAdapter(Context context, @NonNull ArrayList<Train> elements) {
        Log.d("D0006", "MainListAdapter");
        Log.d("D0006", String.valueOf(elements));
        this.context = context;
        this.elements = elements;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ElementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("D0006", "MainListAdapter.onCreateViewHolder()");
        View view = inflater.inflate(R.layout.recycler_list_item, parent, false);
        ElementsViewHolder vh = new ElementsViewHolder(view);
        vh.setOnItemClick(elementId -> {
            if(this.onItemSelected != null){
                Log.d("D0010", "vh.setOnItemClick(elementId -> {}) : " + elementId);
                onItemSelected.OnItemSelected(elementId);
            }
        });
        vh.setItemLongClick(((elementId, view1) -> {
            if(onItemLongClick != null){
                contextualSelectedId = elementId;
                this.view = view1;
                onItemLongClick.onElementLongClick(elementId, view1);
            }
        }));
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ElementsViewHolder holder, int position) {
        Log.d("D003", String.valueOf(position));
        holder.setElementId(elements.get(position).getId());
        holder.getCaption().setText(elements.get(position).getDestination());
        holder.getDetails().setText(elements.get(position).getTrainNumber());
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public long getItemId(int position) {
        return elements.get(position).hashCode();
    }

    public void dropData() {
        elements.clear();
        this.notifyDataSetChanged();
    }

    public void addData(@NonNull ArrayList<Train> trains) {
        elements.addAll(trains);
        this.notifyItemRangeChanged(0, trains.size());
    }

    public ItemSelected getOnItemSelected() {
        return onItemSelected;
    }

    public void setOnItemSelected(ItemSelected onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    public void setOnItemLongClick(ElementLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public int getContextualSelectedId() {
        return contextualSelectedId;
    }

    public View getContextSelectedView() {
        return view;
    }

    public static class ElementsViewHolder extends RecyclerView.ViewHolder {
        public final int ELEMENT_VH_TAG = 42;
        private final TextView caption;
        private final TextView details;
        private int elementId;
        private ElementItemClick itemClick;
        private ElementLongClick itemLongClick;



        public ElementsViewHolder(@NonNull View itemView) {
            super(itemView);
            caption = itemView.findViewById(R.id.trainDescription);
            details = itemView.findViewById(R.id.trainName);
            itemView.setOnClickListener(v -> {
                if (itemClick != null){
                    itemClick.onElementItemClick(this.elementId);
                }
            });
            itemView.setOnLongClickListener(v ->{
                if(itemLongClick != null){
                    itemLongClick.onElementLongClick(this.elementId, v);
                }
                return false;
            });

            itemView.setTag(R.id.element_vh_tag, this.elementId);
        }

        public TextView getDetails() {
            return details;
        }

        public TextView getCaption() {
            return caption;
        }

        public int getElementId() {
            return elementId;
        }

        public void setElementId(int elementId) {
            this.elementId = elementId;
        }

        public void setOnItemClick(ElementItemClick itemClick) {
            this.itemClick = itemClick;
        }

        public void setItemLongClick(ElementLongClick itemLongClick) {
            this.itemLongClick = itemLongClick;
        }


        public interface ElementItemClick {
            void onElementItemClick(int elementId);
        }

        public interface ElementLongClick {
            void onElementLongClick(int elementId, View view);
        }
    }

    public interface ItemSelected{
        void OnItemSelected(int elementId);
    }

    public interface ElementLongClick {
        void onElementLongClick(int elementId, View view);
    }


}
