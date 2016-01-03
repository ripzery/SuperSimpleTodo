package com.onemorebit.supersimpletodo.Adapters;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.Interfaces.ItemAdapterInterface;
import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.Contextor;
import com.onemorebit.supersimpletodo.Utils.Logger;
import com.onemorebit.supersimpletodo.databinding.ItemBinder;
import java.util.List;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements ItemAdapterInterface {
    private int tabNumber = BaseTodoFragment.ONE;
    private List<Item> listItems;
    private TodoInteractionListener todoInteractionListener;

    public RecyclerAdapter(List<Item> listItem, int tabNumber) {
        this.listItems = listItem;
        this.tabNumber = tabNumber;
    }

    public List<Item> getListItems() {
        return listItems;
    }

    @Override public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(inflate);
    }

    @Override public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(listItems.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override public int getItemCount() {
        return listItems.size();
    }

    @Override public void setListItems(List<Item> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    @Override public void removeItem(int index) {
        listItems.remove(index);
        notifyItemRemoved(index);
    }

    @Override public void addItem(Item item) {
        int position = listItems.size();
        listItems.add(position, item);
        notifyItemInserted(position);
    }

    public void setTodoInteractionListener(TodoInteractionListener listener) {
        this.todoInteractionListener = listener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemBinder binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.setTabNumber(tabNumber);
            binding.cbItemChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkItem(isChecked);
                }
            });

            binding.rippleItem.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    binding.cbItemChecked.performClick();
                }
            });
        }

        private void checkItem(boolean isChecked) {
            try {
                //binding.rippleItem.setRippleColor(ContextCompat.getColor(Contextor.getInstance().getContext(), !isChecked ?  R.color.colorPrimary : R.color.colorRipple));

                if (todoInteractionListener != null) {
                    listItems.get(getAdapterPosition()).setChecked(isChecked);
                    todoInteractionListener.onCheckedChangeListener(isChecked, binding.tvItemDesc);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Logger.i(ItemViewHolder.class, "onCheckedChanged_87: ");
                e.printStackTrace();
            }
        }

        public void bind(final Item item) {
            binding.setTodo(item);
        }

        public ItemBinder getBinding() {
            return binding;
        }
    }
}
