package com.munifahsan.skdskb.Adapters;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munifahsan.skdskb.HomePage.model.MateriModel;
import com.munifahsan.skdskb.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifAdapter extends FirestoreRecyclerAdapter<MateriModel, NotifAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public OnItemClickListener onItemClickListener;
    FirebaseUser mCurrentUser;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NotifAdapter(@NonNull FirestoreRecyclerOptions<MateriModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull MateriModel model) {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        if (model.getnSeen().contains(mCurrentUser.getUid())) {
            holder.mBg.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif, parent, false);
        return new Holder(view);
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

        @BindView(R.id.notifBg)
        RelativeLayout mBg;

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
            onItemClickListener.onItemClick(id, position, jenis, isPremium);
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
        void onItemClick(String id, int position, String jenis, boolean isPremium);
    }
}
