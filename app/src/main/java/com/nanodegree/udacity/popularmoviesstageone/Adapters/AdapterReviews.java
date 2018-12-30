package com.nanodegree.udacity.popularmoviesstageone.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanodegree.udacity.popularmoviesstageone.Models.Reviews;
import com.nanodegree.udacity.popularmoviesstageone.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.MovieReviewViewHolder> {

    private Context context;
    private List<Reviews> reviewList;

    public AdapterReviews(Context context, List<Reviews> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_reviews_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieReviewViewHolder holder, int position) {
        final Reviews review = reviewList.get(position);
        holder.bindReview(reviewList.get(position));

        holder.itemView.setOnClickListener(v -> {
            boolean expanded = review.isExpanded();
            review.setExpanded(!expanded);
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author)
        TextView author;
        @BindView(R.id.tv_content)
        TextView content;
        @BindView(R.id.genre_item)
        LinearLayout genreItem;

        MovieReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindReview(Reviews review) {
            boolean expanded = review.isExpanded();
            genreItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }
    }
}
