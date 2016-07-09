package com.app.movein.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.movein.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {
    private ArrayList<CustomGallery> data = new ArrayList();
    private LayoutInflater infalter;
    private boolean isActionMultiplePick;
    private Context mContext;

    public GalleryAdapter(Context paramContext) {
        this.infalter = ((LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        this.mContext = paramContext;
    }

    public void addAll(ArrayList<CustomGallery> paramArrayList) {
        try {
            this.data.clear();
            this.data.addAll(paramArrayList);
            notifyDataSetChanged();
            return;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public void changeSelection(View paramView, int paramInt) {
        if (this.data.get(paramInt).isSeleted) ;
        for (this.data.get(paramInt).isSeleted = false; ; this.data.get(paramInt).isSeleted = true) {
            ((ViewHolder) paramView.getTag()).imgQueueMultiSelected.setSelected(this.data.get(paramInt).isSeleted);
            return;
        }
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.data.size();
    }

    public CustomGallery getItem(int paramInt) {
        return this.data.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public ArrayList<CustomGallery> getSelected() {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < this.data.size(); i++)
            if (this.data.get(i).isSeleted)
                localArrayList.add(this.data.get(i));
        return localArrayList;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ViewHolder localViewHolder = null;
        if (paramView == null) {
            paramView = this.infalter.inflate(R.layout.gallery_item, null);
            localViewHolder = new ViewHolder();
            localViewHolder.imgQueue = ((ImageView) paramView.findViewById(R.id.imgQueue));
            localViewHolder.imgQueueMultiSelected = ((ImageView) paramView.findViewById(R.id.imgQueueMultiSelected));
            if (this.isActionMultiplePick)
                localViewHolder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
        }
        while (true) {
            paramView.setTag(localViewHolder);
            localViewHolder.imgQueue.setTag(Integer.valueOf(paramInt));
            try {
                Picasso.with(this.mContext).load(new File(this.data.get(paramInt).sdcardPath)).resize(250, 250).centerCrop().into(localViewHolder.imgQueue);
                if (this.isActionMultiplePick)
                    localViewHolder.imgQueueMultiSelected.setSelected(this.data.get(paramInt).isSeleted);
                return paramView;
        /*localViewHolder.imgQueueMultiSelected.setVisibility(View.GONE);
        continue;
        localViewHolder = (ViewHolder)paramView.getTag();*/
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
   /* return paramView;*/
    }

    public boolean isAllSelected() {
        boolean bool = true;
        for (int i = 0; ; i++)
            if (i < this.data.size()) {
                if (!this.data.get(i).isSeleted)
                    bool = false;
            } else
                return bool;
    }

    public boolean isAnySelected() {
        for (int i = 0; ; i++) {
            int j = this.data.size();
            boolean bool = false;
            if (i < j) {
                if (this.data.get(i).isSeleted)
                    bool = true;
            } else
                return bool;
        }
    }

    public void selectAll(boolean paramBoolean) {
        for (int i = 0; i < this.data.size(); i++)
            this.data.get(i).isSeleted = paramBoolean;
        notifyDataSetChanged();
    }

    public void setMultiplePick(boolean paramBoolean) {
        this.isActionMultiplePick = paramBoolean;
    }

    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgQueueMultiSelected;

        public ViewHolder() {
        }
    }
}

