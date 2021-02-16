package com.munifahsan.skdskb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.Holder> {

    AppCompatActivity appCompatActivity;
    List<SkuDetails> skuDetailsList;
    BillingClient billingClient;

    ProductClickListener productClickListener;

    public void setProductClickListener(ProductClickListener productClickListener) {
        this.productClickListener = productClickListener;
    }

    public MyProductAdapter(AppCompatActivity appCompatActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.appCompatActivity = appCompatActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(appCompatActivity.getBaseContext())
        .inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter.Holder holder, int position) {

        String title = skuDetailsList.get(position).getTitle();
        holder.mTitle.setText(title.replace("(SKD SKB Indonesia)", ""));
        holder.mHarga.setText(skuDetailsList.get(position).getPrice());
        holder.mDesc.setText(skuDetailsList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.harga)
        TextView mHarga;
        @BindView(R.id.desc)
        TextView mDesc;
        @BindView(R.id.pilih)
        Button mPilih;

        public Holder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mPilih.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            productClickListener.onClick(view, getAdapterPosition());
        }
    }
}
