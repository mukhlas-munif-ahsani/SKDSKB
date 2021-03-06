package com.munifahsan.skdskb.HomePage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.munifahsan.skdskb.HomePage.model.MateriModel;
import com.munifahsan.skdskb.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListTwoAdapter extends FirestoreRecyclerAdapter<MateriModel, ListTwoAdapter.Holder> {

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ListTwoAdapter(@NonNull FirestoreRecyclerOptions<MateriModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListTwoAdapter.Holder holder, int position, @NonNull MateriModel model) {
        holder.id = model.getId();
        holder.jenis = model.getnJenis();
        holder.isPremium = model.isPremium();

        Glide.with(holder.itemView)
                .load(model.getnThumbnailImageUrl())
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.mImage);
        holder.mTitle.setText(model.getnTitle());
        holder.mJenis.setText(model.getnJenis());

        Glide.with(holder.itemView)
                .load(model.getnJenisImage())
                .fitCenter()
                .into(holder.mImageJenis);

        holder.mDate.setText(getTimeDate(model.getnUploadTime()));

        if (model.isPremium()) {
            holder.mPremium.setVisibility(View.VISIBLE);
        }

        if (!model.isPremium()) {
            holder.mPremium.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public ListTwoAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi, parent, false);
        return new ListTwoAdapter.Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView_image_itemMateri)
        ImageView mImage;
        @BindView(R.id.textView_title_itemMateri)
        TextView mTitle;
        @BindView(R.id.textView_jenis_itemMateri)
        TextView mJenis;
        @BindView(R.id.textView_date_itemMateri)
        TextView mDate;
        @BindView(R.id.rel_premium_itemMateri)
        RelativeLayout mPremium;
        @BindView(R.id.imageView_jenisImage_itemMateri)
        ImageView mImageJenis;
        @BindView(R.id.cons_itemMateri)
        ConstraintLayout mItem;

        String id;
        boolean isPremium;
        String jenis;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(id, position,  jenis, isPremium);
        }
    }

    public static String getTimeDate(Date timestamp) {
        try {
            //Date netDate = (timestamp);
            SimpleDateFormat sfd = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return sfd.format(timestamp);
        } catch (Exception e) {
            return "date";
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id, int position,  String jenis, boolean isPremium);
    }
}
