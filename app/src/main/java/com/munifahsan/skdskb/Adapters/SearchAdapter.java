package com.munifahsan.skdskb.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.munifahsan.skdskb.Models.MateriListModel;
import com.munifahsan.skdskb.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> implements Filterable {

    private List<MateriListModel> mModelList;
    private List<MateriListModel> mModelListFull;
    private OnListItemCliked onListItemCliked;

    public void setOnListItemCliked(OnListItemCliked onListItemCliked) {
        this.onListItemCliked = onListItemCliked;
    }

    public void setListModels(List<MateriListModel> mModelList) {
        this.mModelList = mModelList;
        mModelListFull = new ArrayList<>(mModelList);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.id = mModelList.get(position).getId();
        holder.jenis = mModelList.get(position).getnJenis();
        holder.isPremium = mModelList.get(position).isPremium();

        Glide.with(holder.itemView)
                .load(mModelList.get(position).getnThumbnailImageUrl())
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.mImage);
        holder.mTitle.setText(mModelList.get(position).getnTitle());
        holder.mJenis.setText(mModelList.get(position).getnJenis());

        Glide.with(holder.itemView)
                .load(mModelList.get(position).getnJenisImage())
                .fitCenter()
                .into(holder.mImageJenis);

        holder.mDate.setText(getTimeDate(mModelList.get(position).getnUploadTime()));

        if (mModelList.get(position).isPremium()) {
            holder.mPremium.setVisibility(View.VISIBLE);
        }

        if (!mModelList.get(position).isPremium()) {
            holder.mPremium.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mModelList == null) {
            return 0;
        } else {
            return mModelList.size();
        }
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
        String jenis;
        boolean isPremium;

        public Holder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onListItemCliked.onItemCliked(id, position, jenis, isPremium);
        }

    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<MateriListModel> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(mModelListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (MateriListModel item : mModelListFull) {
                    if (item.getnTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mModelList.clear();
            mModelList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static String getTimeDate(Date timestamp) {
        try {
            //Date netDate = (timestamp);
            SimpleDateFormat sfd = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return sfd.format(timestamp);
        } catch (Exception e) {
            return "date";
        }
    }

    public interface OnListItemCliked {
        void onItemCliked(String id, int position, String jenis, boolean isPremium);
    }
}
