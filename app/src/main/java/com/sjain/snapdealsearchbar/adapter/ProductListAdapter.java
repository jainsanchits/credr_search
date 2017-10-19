package com.sjain.snapdealsearchbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjain.snapdealsearchbar.MainActivity;
import com.sjain.snapdealsearchbar.R;
import com.sjain.snapdealsearchbar.model.ProductDescription;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sjain on 18/10/17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    ArrayList<ProductDescription> mProductDescription;
    private LayoutInflater mInflater;
    Context mContext;

    public ProductListAdapter(Context mContext, ArrayList<ProductDescription> mProductDescription) {

        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mProductDescription = mProductDescription;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_product_description, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductDescription current = mProductDescription.get(position);
        holder.tvProductPrice.setText(MainActivity.RUPEE_WITH_SPACE + " " + current.getPrice());
        holder.tvProductTitle.setText(current.getTitle());
        Picasso.with(mContext).load(current.getImageLink()).into(holder.ivProductImage);

    }

    @Override
    public int getItemCount() {
        return mProductDescription.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_product_image)
        ImageView ivProductImage;
        @BindView(R.id.tv_product_price)
        TextView tvProductPrice;
        @BindView(R.id.tv_product_title)
        TextView tvProductTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
