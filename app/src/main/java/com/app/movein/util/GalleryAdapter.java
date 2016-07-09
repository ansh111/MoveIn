package com.app.movein.util;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.movein.R;
import com.squareup.picasso.Picasso;

public class GalleryAdapter extends BaseAdapter
{
  private ArrayList<CustomGallery> data = new ArrayList();
  private LayoutInflater infalter;
  private boolean isActionMultiplePick;
  private Context mContext;

  public GalleryAdapter(Context paramContext)
  {
    this.infalter = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    this.mContext = paramContext;
  }

  public void addAll(ArrayList<CustomGallery> paramArrayList)
  {
    try
    {
      this.data.clear();
      this.data.addAll(paramArrayList);
      notifyDataSetChanged();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void changeSelection(View paramView, int paramInt)
  {
    if (((CustomGallery)this.data.get(paramInt)).isSeleted);
    for (((CustomGallery)this.data.get(paramInt)).isSeleted = false; ; ((CustomGallery)this.data.get(paramInt)).isSeleted = true)
    {
      ((ViewHolder)paramView.getTag()).imgQueueMultiSelected.setSelected(((CustomGallery)this.data.get(paramInt)).isSeleted);
      return;
    }
  }

  public void clear()
  {
    this.data.clear();
    notifyDataSetChanged();
  }

  public int getCount()
  {
    return this.data.size();
  }

  public CustomGallery getItem(int paramInt)
  {
    return (CustomGallery)this.data.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public ArrayList<CustomGallery> getSelected()
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < this.data.size(); i++)
      if (((CustomGallery)this.data.get(i)).isSeleted)
        localArrayList.add(this.data.get(i));
    return localArrayList;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ViewHolder localViewHolder = null;
    if (paramView == null)
    {
      paramView = this.infalter.inflate(R.layout.gallery_item, null);
      localViewHolder = new ViewHolder();
      localViewHolder.imgQueue = ((ImageView)paramView.findViewById(R.id.imgQueue));
      localViewHolder.imgQueueMultiSelected = ((ImageView)paramView.findViewById(R.id.imgQueueMultiSelected));
      if (this.isActionMultiplePick)
        localViewHolder.imgQueueMultiSelected.setVisibility(0);
    }
    while (true)
    {
      paramView.setTag(localViewHolder);
      localViewHolder.imgQueue.setTag(Integer.valueOf(paramInt));
      try
      {
        Picasso.with(this.mContext).load(new File(((CustomGallery)this.data.get(paramInt)).sdcardPath)).resize(250, 250).centerCrop().into(localViewHolder.imgQueue);
        if (this.isActionMultiplePick)
          localViewHolder.imgQueueMultiSelected.setSelected(((CustomGallery)this.data.get(paramInt)).isSeleted);
        return paramView;
        /*localViewHolder.imgQueueMultiSelected.setVisibility(View.GONE);
        continue;
        localViewHolder = (ViewHolder)paramView.getTag();*/
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
   /* return paramView;*/
  }

  public boolean isAllSelected()
  {
    boolean bool = true;
    for (int i = 0; ; i++)
      if (i < this.data.size())
      {
        if (!((CustomGallery)this.data.get(i)).isSeleted)
          bool = false;
      }
      else
        return bool;
  }

  public boolean isAnySelected()
  {
    for (int i = 0; ; i++)
    {
      int j = this.data.size();
      boolean bool = false;
      if (i < j)
      {
        if (((CustomGallery)this.data.get(i)).isSeleted)
          bool = true;
      }
      else
        return bool;
    }
  }

  public void selectAll(boolean paramBoolean)
  {
    for (int i = 0; i < this.data.size(); i++)
      ((CustomGallery)this.data.get(i)).isSeleted = paramBoolean;
    notifyDataSetChanged();
  }

  public void setMultiplePick(boolean paramBoolean)
  {
    this.isActionMultiplePick = paramBoolean;
  }

  public class ViewHolder
  {
    ImageView imgQueue;
    ImageView imgQueueMultiSelected;

    public ViewHolder()
    {
    }
  }
}

