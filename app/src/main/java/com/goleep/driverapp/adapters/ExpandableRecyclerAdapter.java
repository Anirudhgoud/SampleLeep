package com.goleep.driverapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 20/02/18.
 */

public abstract class ExpandableRecyclerAdapter<T extends BaseListItem>
        extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {
    protected Context mContext;
    protected List<T> allItems = new ArrayList<>();
    protected List<T> visibleItems = new ArrayList<>();
    private List<Integer> indexList = new ArrayList<>();
    private SparseIntArray expandMap = new SparseIntArray();
    protected SparseIntArray doPositionMapAllItems = new SparseIntArray();
    private static final int ARROW_ROTATION_DURATION = 150;
    public ExpandableRecyclerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return visibleItems == null ? 0 : visibleItems.size();
    }

    protected View inflate(int resourceID, ViewGroup viewGroup) {
        return LayoutInflater.from(mContext).inflate(resourceID, viewGroup, false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public class HeaderViewHolder extends ViewHolder {
        ImageView arrow;

        public HeaderViewHolder(View view) {
            super(view);
            view.setOnClickListener(v -> toggleExpandedItems(getLayoutPosition(),false));
        }

        public HeaderViewHolder(View view, final ImageView arrow, final View.OnClickListener clickListener) {
            super(view);
            this.arrow = arrow;
            view.setOnClickListener(v -> {
                if(clickListener != null)
                    clickListener.onClick(v);
                handleClick();
            });
        }

        protected void handleClick() {
            if (toggleExpandedItems(getLayoutPosition(), true)) {
                openArrow(arrow);
            } else {
                closeArrow(arrow);
            }
            updateDoPositionMap();
        }

        public void bind(int position) {

        }
    }

    private void updateDoPositionMap() {
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getItemType() == AppConstants.TYPE_HEADER) {
                doPositionMapAllItems.put(((DeliveryOrderEntity) allItems.get(i)).getId(), i);
            }
        }
    }

    public boolean toggleExpandedItems(int position, boolean notify) {
        if (isExpanded(position)) {
            collapseItems(position, notify);
            return false;
        } else {
            expandItems(position, notify);
            collapseAllExcept(position);
            return true;
        }
    }

    public void expandItems(int position, boolean notify) {
        int count = 0;
        int index = indexList.get(position);
        int insert = position;

        for (int i=index+1; i<allItems.size() && !isHeader(allItems.get(i)); i++) {
            insert++;
            count++;
            visibleItems.add(insert, allItems.get(i));
            indexList.add(insert, i);
        }

        notifyItemRangeInserted(position + 1, count);

        int allItemsPosition = indexList.get(position);
        expandMap.put(allItemsPosition, 1);

        if (notify) {
            notifyItemChanged(position);
        }
    }

    public void collapseItems(int position, boolean notify) {
        int count = 0;
        int index = indexList.get(position);

        for (int i = index + 1; i < allItems.size() && !isHeader(allItems.get(i)); i++) {
            count++;
            visibleItems.remove(position + 1);
            indexList.remove(position + 1);
        }

        notifyItemRangeRemoved(position + 1, count);

        int allItemsPosition = indexList.get(position);
        expandMap.delete(allItemsPosition);

        if (notify) {
            notifyItemChanged(position);
        }
    }

    protected boolean isExpanded(int position) {
        int allItemsPosition = indexList.get(position);
        return expandMap.get(allItemsPosition, -1) >= 0;
    }

    @Override
    public int getItemViewType(int position) {
        return visibleItems.get(position).getItemType();
    }

    public void setItems(List<T> items) {
        allItems = items;
        List<T> visibleItems = new ArrayList<>();
        expandMap.clear();
        indexList.clear();

        for (int i=0; i<items.size(); i++) {
            if (items.get(i).getItemType() == AppConstants.TYPE_HEADER ||
                    items.get(i).getItemType() == AppConstants.TYPE_ORDERS_HEADER ||
                    items.get(i).getItemType() == AppConstants.TYPE_CASH_SALES_ITEM ||
                    items.get(i).getItemType() == AppConstants.TYPE_SALES_INFO) {
                indexList.add(i);
                if(items.get(i).getItemType() == AppConstants.TYPE_HEADER){
                    doPositionMapAllItems.put(((DeliveryOrderEntity) items.get(i)).getId(), i);
                }
                visibleItems.add(items.get(i));
            }
        }

        this.visibleItems = visibleItems;
        notifyDataSetChanged();
    }


    public void collapseAllExcept(int position) {
        for (int i=visibleItems.size()-1; i>=0; i--) {
            if (i != position && getItemViewType(i) == AppConstants.TYPE_HEADER) {
                if (isExpanded(i)) {
                    collapseItems(i, true);
                }
            }
        }
    }

    public static void openArrow(View view) {
        view.animate().setDuration(ARROW_ROTATION_DURATION).rotation(180);

    }

    public static void closeArrow(View view) {
        view.animate().setDuration(ARROW_ROTATION_DURATION).rotation(0);
    }

    private boolean isHeader(BaseListItem baseListItem) {
        return baseListItem.getItemType() == AppConstants.TYPE_HEADER ||
                baseListItem.getItemType() == AppConstants.TYPE_ORDERS_HEADER ||
                baseListItem.getItemType() == AppConstants.TYPE_CASH_SALES_ITEM ||
                baseListItem.getItemType() == AppConstants.TYPE_SALES_INFO;
    }
}
