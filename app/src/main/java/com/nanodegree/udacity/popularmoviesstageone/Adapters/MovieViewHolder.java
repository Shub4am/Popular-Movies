package com.nanodegree.udacity.popularmoviesstageone.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nanodegree.udacity.popularmoviesstageone.Models.Movie;
import com.nanodegree.udacity.popularmoviesstageone.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Context mContext;

    @BindView(R.id.ivMovieImage)
    ImageView coverImage;

    private RecyclerViewOnClickListener clickListener;

    MovieViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        this.clickListener = listener;
    }

    void bindMovie(Movie movie) {
        StringBuilder releaseText = new StringBuilder().append(mContext.getResources().getString(R.string.Release_Date_Title));
        releaseText.append(movie.getReleaseDate());

        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.downloader(new OkHttp3Downloader(mContext));
        builder.build().load(mContext.getResources().getString(R.string.IMAGE_BASE_URL) + movie.getPosterPath())
                .placeholder((R.drawable.loading_image))
                .error(R.drawable.ic_launcher_background)
                .into(coverImage);
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getAdapterPosition());
    }
}
