package com.munifahsan.skdskb.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.munifahsan.skdskb.Models.KategoriModel;
import com.munifahsan.skdskb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KategoriAdapter extends FirestoreRecyclerAdapter<KategoriModel, KategoriAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public KategoriAdapter(@NonNull FirestoreRecyclerOptions<KategoriModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull KategoriModel model) {
        Glide.with(holder.itemView)
                .load(model.getnImageUrl())
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.mImage);
        holder.mTitle.setText(model.getnTitle());
        holder.mTitle.setTextSize(model.getnFont());
        holder.id = model.getId();
        holder.collection = model.getnCollection();
        holder.kategori = model.getnKategori();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView_itemKategori)
        ImageView mImage;
        @BindView(R.id.textView_title_itemKategori)
        TextView mTitle;
        @BindView(R.id.itemKategori_lay)
        ConstraintLayout mItem;

        String id;
        String collection;
        String kategori;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(id, position, kategori, collection);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id, int position, String kategori, String collection);
    }
}
