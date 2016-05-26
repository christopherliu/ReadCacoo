package net.christopherliu.readcacoo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.christopherliu.cacooapi.types.Diagram;

/**
 * Created by Christopher Liu on 5/23/2016.
 * Adapter for getting images of Diagrams.
 */
public class DiagramAdapter extends BaseAdapter {
    private Context mContext;
    private Diagram[] diagrams = new Diagram[0];

    public DiagramAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return diagrams.length;
    }

    public Diagram getItem(int position) {
        return diagrams[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(diagrams[position].image);
        return imageView;
    }

    /**
     * Updates Diagrams in this list of images.
     *
     * @param diagrams
     */
    public void setDiagrams(Diagram[] diagrams) {
        this.diagrams = diagrams;
        this.notifyDataSetChanged();
    }

}