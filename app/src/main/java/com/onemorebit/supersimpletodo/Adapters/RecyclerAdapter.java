package com.onemorebit.supersimpletodo.Adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.Interfaces.ItemAdapterInterface;
import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.Logger;
import com.onemorebit.supersimpletodo.databinding.ItemBinder;
import java.util.List;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements ItemAdapterInterface {
    private int tabNumber = BaseTodoFragment.ONE; //Default tab number is ONE
    private List<Item> listItems;
    private TodoInteractionListener todoInteractionListener;

    /* Initialize listItems and tabNumber */
    public RecyclerAdapter(List<Item> listItem, int tabNumber) {
        this.listItems = listItem;
        this.tabNumber = tabNumber;
    }

    public List<Item> getListItems() {
        return listItems;
    }

    /* create view holder */
    @Override public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(inflate);
    }

    /* bind viewholder */
    @Override public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(listItems.get(position));

        /* required this to immediately binding */
        holder.getBinding().executePendingBindings();
    }

    /* get count of list items*/
    @Override public int getItemCount() {
        return listItems.size();
    }

    /* set item list */
    @Override public void setListItems(List<Item> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    /* remove item at index */
    @Override public void removeItem(int index) {
        listItems.remove(index);
        notifyItemRemoved(index);
    }

    /* add item at the last position */
    @Override public void addItem(Item item) {
        int position = listItems.size();
        listItems.add(position, item);
        notifyItemInserted(position);
    }

    /* insert item in a position */
    public void insertItem(int position, Item item) {
        Logger.i(RecyclerAdapter.class, "insertItem_68: " + position);
        listItems.add(position, item);
        notifyItemInserted(position);
    }

    /* Bind listener to recycler adapter */
    public void setTodoInteractionListener(TodoInteractionListener listener) {
        this.todoInteractionListener = listener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemBinder binding;

        public ItemViewHolder(View itemView) {
            super(itemView);

            /* in data binding ItemBinder object is a whole of viewHolder so we only keep this one */
            /* then we able to access all view */
            binding = DataBindingUtil.bind(itemView);
            binding.setTabNumber(tabNumber);

            /* set checked listener when itemView is first initialized */
            binding.cbItemChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkItem(isChecked);
                }
            });

            /* set onclick in rootLayout to toggle checkbox */
            binding.rippleItem.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    binding.cbItemChecked.setChecked(!binding.cbItemChecked.isChecked());
                }
            });

            /* set onlongclick in rootLayout to edit task */
            binding.rippleItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    onLongClickItem(getAdapterPosition(), listItems.get(getAdapterPosition()));
                    return true;
                }
            });
        }

        /**
         * Update listItems whenever checkbox is toggle and invoke listener
         *
         * @param isChecked - is checkbox checked?
         */
        private void checkItem(boolean isChecked) {
            try {

                /* if bind listener */
                if (todoInteractionListener != null) {
                    /*  */
                    listItems.get(getAdapterPosition()).setChecked(isChecked);
                    todoInteractionListener.onCheckedChange(isChecked, binding.tvItemDesc);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Logger.i(ItemViewHolder.class, "onCheckedChanged_87: ");
                e.printStackTrace();
            }
        }

        private void onLongClickItem(int index, Item item){
            /* if bind listener */
            if (todoInteractionListener != null) {
                todoInteractionListener.onItemLongClick(index, item);
            }
        }

        /* Bind to do object to view */
        public void bind(final Item item) {
            binding.setTodo(item);
        }

        public ItemBinder getBinding() {
            return binding;
        }
    }
}
