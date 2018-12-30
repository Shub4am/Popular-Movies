package com.nanodegree.udacity.popularmoviesstageone;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nanodegree.udacity.popularmoviesstageone.Adapters.AdapterCast;
import com.nanodegree.udacity.popularmoviesstageone.Adapters.AdapterReviews;
import com.nanodegree.udacity.popularmoviesstageone.Adapters.AdapterTrailer;
import com.nanodegree.udacity.popularmoviesstageone.Database.Database;
import com.nanodegree.udacity.popularmoviesstageone.Interface.Interface;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieCast;
import com.nanodegree.udacity.popularmoviesstageone.Models.Movie;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieCredits;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieReviews;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieTrailer;
import com.nanodegree.udacity.popularmoviesstageone.Models.Reviews;
import com.nanodegree.udacity.popularmoviesstageone.Models.Trailer;
import com.nanodegree.udacity.popularmoviesstageone.Network.APIClient;
import com.nanodegree.udacity.popularmoviesstageone.Utils.AppExecutors;
import com.nanodegree.udacity.popularmoviesstageone.Utils.MovieUtils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.nanodegree.udacity.popularmoviesstageone.Utils.Constants.FAV_MOVIE_KEY;
import static com.nanodegree.udacity.popularmoviesstageone.Utils.Constants.SELECTED_MOVIE_TO_SEE_DETAILS;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private static Retrofit retrofit;
    private static String API_KEY;
    public List<Trailer> trailers;
    public List<Reviews> reviews;
    public List<MovieCast> cast;
    private boolean isFavorite;
    private int movieId;
    private Movie movie;

    @BindView(R.id.iv_details_moviePoster)
    ImageView moviePoster;

    @BindView(R.id.tv_details_MovieTitle)
    TextView movieTitle;

    @BindView(R.id.tv_details_plot)
    TextView moviePlot;

    @BindView(R.id.tv_details_releaseDate)
    TextView movieReleaseDate;

    @BindView(R.id.tv_details_voteAverage)
    TextView movieVoteAverage;

    @BindView(R.id.rv_trailer)
    public RecyclerView rvTrailer;

    @BindView(R.id.rv_reviews)
    public RecyclerView rvReviews;

    @BindView(R.id.rv_cast)
    public RecyclerView rvCast;

    @BindView(R.id.tv_cast_not_available)
    TextView castNotAvailable;

    @BindView(R.id.tv_trailers_not_available)
    TextView trailersNotAvailable;

    @BindView(R.id.tv_reviews_not_available)
    TextView reviewsNotAvailable;

    @BindView(R.id.iv_fav_btn)
    ImageView favBtn;

    private DetailsActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        API_KEY = getResources().getString(R.string.API_KEY);

        ButterKnife.bind(this);
        Interface movieService = APIClient.getRetrofitInstance().create(Interface.class);
        viewModel = ViewModelProviders.of(this).get(DetailsActivityViewModel.class);

        favBtn.setOnClickListener(v -> onFavButtonClicked());

        if (getIntent() != null) {
            if (getIntent().hasExtra(SELECTED_MOVIE_TO_SEE_DETAILS)) {
                movie = getIntent().getParcelableExtra(SELECTED_MOVIE_TO_SEE_DETAILS);
                movieId = movie.getMovieId();
                AppExecutors.getExecutorInstance().getDiskIO().execute(() -> {
                    isFavorite = viewModel.isFavorite(movieId);
                    if (isFavorite) {
                        movie = Database.getInstance(this).movieDao().getMovie(movieId);
                        runOnUiThread(() -> favBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_selected)));
                    } else {
                        runOnUiThread(() -> favBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_unselected)));
                    }
                });
            }
        }

        getSelectedMovieDetails(movieService);

    }

    private void getSelectedMovieDetails(Interface client) {
        if (movieId != 0) {
            Call<Movie> detailResultsCall = client.getMovieDetails(movieId, API_KEY);
            detailResultsCall.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                    if (response.body() == null) {
                        return;
                    }

                    movieTitle.setText(response.body().getTitle());
                    if (response.body().getOverview() != null && !response.body().getOverview().isEmpty()) {
                        moviePlot.setText(response.body().getOverview());
                    } else {
                        moviePlot.setText(getResources().getString(R.string.plotNotAvailable));
                    }
                    movieReleaseDate.setText(new StringBuilder(getString(R.string.Release_Date_Title)).append(response.body().getReleaseDate()));
                    movieVoteAverage.setText(new StringBuilder(getString(R.string.Rating_Title)).append(response.body().getVoteAverage()));
                   // movieVoteAverage.setText();

                    Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
                    builder.downloader(new OkHttp3Downloader(getApplicationContext()));
                    builder.build().load(getResources().getString(R.string.IMAGE_BASE_URL) + response.body().getBackdropPath())
                            .placeholder((R.drawable.loading_image))
                            .error(R.drawable.ic_launcher_background)
                            .into(moviePoster);
                }

                @Override
                public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                    if (movie != null) {
                        Log.d(TAG, "Movie already set by favorites");
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            getMovieTrailers(movieId);
            getMovieReviews(movieId);
            getMovieCast(movieId);
        }
    }

    private void onFavButtonClicked() {
        AppExecutors.getExecutorInstance().getDiskIO().execute(() -> {
            boolean isFavorite = viewModel.isFavorite(movieId);
            if (isFavorite) {
                viewModel.removeMovieFromFavorites(movie);
                runOnUiThread(() -> {
                    favBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_unselected));
                    Toast.makeText(this, getResources().getString(R.string.Favorite_Removed), Toast.LENGTH_SHORT).show();
                });
            } else {
                viewModel.addMovieToFavorites(movie);
                runOnUiThread(() -> {
                    Toast.makeText(this, getResources().getString(R.string.Favorite_Added), Toast.LENGTH_SHORT).show();
                    favBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_selected));
                });
            }
            viewModel.updateFavoriteMovie(movieId, !isFavorite);
           // selectFavoritesMovieScreen();
            finish();
        });
    }

    private void selectFavoritesMovieScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(FAV_MOVIE_KEY, true);
        startActivity(intent);
    }

    private void getMovieCast(Integer id) {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = APIClient.getRetrofitInstance();
            }
            Interface movieService = retrofit.create(Interface.class);
            Call<MovieCredits> call = movieService.getMovieCredits(id, API_KEY);
            call.enqueue(new Callback<MovieCredits>() {
                @Override
                public void onResponse(@NonNull Call<MovieCredits> call, @NonNull Response<MovieCredits> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        cast = response.body().getCast();
                        if (cast != null && !cast.isEmpty()) {
                            rvCast.setVisibility(View.VISIBLE);
                            castNotAvailable.setVisibility(View.GONE);
                            generateCreditsList(cast);
                        } else {
                            rvCast.setVisibility(View.GONE);
                            castNotAvailable.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieCredits> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, R.string.Error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, getResources().getString(R.string.Network_Unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void getMovieReviews(Integer id) {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = APIClient.getRetrofitInstance();
            }
            Interface movieService = retrofit.create(Interface.class);
            Call<MovieReviews> call = movieService.getMovieReviews(id, API_KEY, 1);
            call.enqueue(new Callback<MovieReviews>() {
                @Override
                public void onResponse(@NonNull Call<MovieReviews> call, @NonNull Response<MovieReviews> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        reviews = response.body().getReviewList();
                        if (reviews != null && !reviews.isEmpty()) {
                            rvReviews.setVisibility(View.VISIBLE);
                            reviewsNotAvailable.setVisibility(View.GONE);
                            generateReviewList(reviews);
                        } else {
                            rvReviews.setVisibility(View.GONE);
                            reviewsNotAvailable.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieReviews> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, getResources().getString(R.string.Error), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, getResources().getString(R.string.Network_Unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void getMovieTrailers(Integer id) {
        if (MovieUtils.getInstance().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = APIClient.getRetrofitInstance();
            }
            Interface movieService = retrofit.create(Interface.class);
            Call<MovieTrailer> call = movieService.getMovieTrailers(id, API_KEY);
            call.enqueue(new Callback<MovieTrailer>() {
                @Override
                public void onResponse(@NonNull Call<MovieTrailer> call, @NonNull Response<MovieTrailer> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        trailers = response.body().getTrailers();
                        if (trailers != null && !trailers.isEmpty()) {
                            rvTrailer.setVisibility(View.VISIBLE);
                            trailersNotAvailable.setVisibility(View.GONE);
                            generateTrailerList(trailers);
                        } else {
                            rvTrailer.setVisibility(View.GONE);
                            trailersNotAvailable.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieTrailer> call, @NonNull Throwable t) {
                    Toast.makeText(DetailsActivity.this, R.string.Error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, getResources().getString(R.string.Network_Unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void generateCreditsList(List<MovieCast> cast) {
        AdapterCast adapter = new AdapterCast(this, cast);
        initCastAdapter(adapter);
    }

    private void generateTrailerList(final List<Trailer> trailers) {
        AdapterTrailer adapter = new AdapterTrailer(this, trailers);
        initTrailersAdapter(adapter);
    }

    private void generateReviewList(final List<Reviews> reviews) {
        AdapterReviews adapter = new AdapterReviews(this, reviews);
        initReviewsAdapter(adapter);
    }

    private void initCastAdapter(AdapterCast adapter) {
        rvCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCast.setAdapter(adapter);
    }

    private void initTrailersAdapter(AdapterTrailer adapter) {
        rvTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrailer.setAdapter(adapter);
    }

    private void initReviewsAdapter(AdapterReviews adapter) {
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(adapter);
    }
}
