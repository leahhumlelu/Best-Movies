package com.future.bestmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.future.bestmovies.data.Cast;
import com.future.bestmovies.utils.ImageUtils;
import com.squareup.picasso.Picasso;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private final Context mContext;
    private Cast[] mCast;

    //No click listener needed yet

    public CastAdapter(Context context, Cast[] cast) {
        mContext = context;
        mCast = cast;
    }

    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cast_list_item, parent, false);
        view.setFocusable(false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastAdapter.CastViewHolder holder, int position) {
        String description;

        if (!TextUtils.isEmpty(mCast[position].getProfilePath())) {
            Picasso.with(mContext)
                    .load(ImageUtils.buildImageUrlWithImageType(
                            mContext,
                            mCast[position].getProfilePath(),
                            ImageUtils.POSTER))
                    .into(holder.actorProfileImageView);
            description = mCast[position].getActorName();
        } else {
            holder.actorProfileImageView.setImageResource(R.drawable.default_poster);
            description = mContext.getString(R.string.no_profile_picture);
        }

        holder.actorProfileImageView.setContentDescription(description);
        holder.actorNameTextView.setText(mCast[position].getActorName());
    }

    @Override
    public int getItemCount() { return mCast.length; }

    void swapCast(Cast[] newCast) {
        mCast = newCast;
        notifyDataSetChanged();
    }

    class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView actorProfileImageView;
        TextView actorNameTextView;

        CastViewHolder(View itemView) {
            super(itemView);
            actorProfileImageView = itemView.findViewById(R.id.actor_profile_iv);
            actorNameTextView = itemView.findViewById(R.id.actor_name_tv);
        }
    }
}