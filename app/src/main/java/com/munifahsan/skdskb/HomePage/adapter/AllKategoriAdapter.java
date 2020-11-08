package com.munifahsan.skdskb.HomePage.adapter;

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
import com.munifahsan.skdskb.HomePage.model.KategoriModel;
import com.munifahsan.skdskb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllKategoriAdapter extends FirestoreRecyclerAdapter<KategoriModel, AllKategoriAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public AllKategoriAdapter(@NonNull FirestoreRecyclerOptions<KategoriModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllKategoriAdapter.Holder holder, int position, @NonNull KategoriModel model) {
        Glide.with(holder.itemView)
                .load(model.getnImageUrl())
                .fitCenter()
                .into(holder.mImage);
        holder.mTitle.setText(model.getnTitle());
        holder.mTitle.setTextSize(model.getnFont());
        holder.id = model.getId();
        holder.kategori = model.getnKategori();
    }

    @NonNull
    @Override
    public AllKategoriAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_kategori, parent, false);
        return new AllKategoriAdapter.Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.imageView_itemKategori)
        ImageView mImage;
        @BindView(R.id.textView_title_itemKategori)
        TextView mTitle;
        @BindView(R.id.itemKategori_lay)
        ConstraintLayout mItem;

        String id;
        String kategori;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(id, position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id, int position);
    }
}
