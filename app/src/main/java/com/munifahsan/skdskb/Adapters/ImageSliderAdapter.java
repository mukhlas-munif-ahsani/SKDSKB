package com.munifahsan.skdskb.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.munifahsan.skdskb.Models.ImageSliderModel;
import com.munifahsan.skdskb.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.Holder> {

    private List<ImageSliderModel> mData;

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ImageSliderAdapter() {
        this.mData = new ArrayList<>();
    }

    public void addCardItem(ImageSliderModel item) {
        mData.add(item);
    }

    @Override
    public ImageSliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(ImageSliderAdapter.Holder viewHolder, int position) {
        Glide.with(viewHolder.itemView)
                .load(mData.get(position).getnImage())
                .transform(new BlurTransformation())
                .fitCenter()
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    class Holder extends SliderViewAdapter.ViewHolder implements View.OnClickListener{
        ImageView imageViewBackground;

        CardView frameLayout_promo;
        String id;
        public Holder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            frameLayout_promo = itemView.findViewById(R.id.frameLayout_promo);

            frameLayout_promo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //int position = getAdapterPosition();
            onItemClickListener.onItemClick(id, 0);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id, int position);
    }
}
