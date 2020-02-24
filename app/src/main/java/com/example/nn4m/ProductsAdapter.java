package com.example.nn4m;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private static final String TAG = "ProductsAdapter";

    private Context mContext;
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView productThumbnail;

        public MyViewHolder(View view) {
            super(view);
            Log.d(TAG, "Setting view");
            name = (TextView) view.findViewById(R.id.product_name);
            price = (TextView) view.findViewById(R.id.price);
            productThumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            productThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog nagDialog = new Dialog(mContext,android.R.style.Theme_Black_NoTitleBar);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    nagDialog.setCancelable(false);
                    nagDialog.setContentView(R.layout.preview_image);
                    Button btnClose = nagDialog.findViewById(R.id.btnIvClose);
                    ImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);
                    ivPreview.setBackground(productThumbnail.getDrawable());
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {

                            nagDialog.dismiss();
                        }
                    });
                    nagDialog.show();
                }
            });

        }
    }


    public ProductsAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_products_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        Picasso.get().load(product.getImage()).into(holder.productThumbnail);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}