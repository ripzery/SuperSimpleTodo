package com.onemorebit.supersimpletodo.Interfaces;

import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.Models.Item;
import java.util.List;

/**
 * Created by Euro on 1/2/16 AD.
 */
public interface ItemAdapterInterface {
    void setListItems(List<Item> listItems);
    void removeItem(int index);
    void setTodoInteractionListener(TodoInteractionListener listener);
    void addItem(Item item);
}
