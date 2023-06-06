package br.com.application.moviestmdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    //Chave da API (v3 auth)
    //da0e4838c057baf77b75e5338ced2bb3
    public static final String URL_BASE = "https://api.themoviedb.org/3/";
    @GET("movie/popular")
    Call<GetPopularMovies> GetAPIPopularMovies(@Query("language") String language, @Query("api_key") String apiKey);
    @GET("search/movie")
    Call<GetPopularMovies> GetAPISearchMovie(@Query("query") String query, @Query("include_adult") boolean includeAdult, @Query("language") String language, @Query("page") int page, @Query("api_key") String apiKey);
    @GET("genre/movie/list")
    Call<Genres> GetAPIGeneros(@Query("language") String language, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/credits")
    Call<Credits> GetCredits(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}")
    Call<Details> GetDetails(@Path("movie_id") Integer movieId, @Query("language") String language, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/release_dates")
    Call<ReleaseDates> GetReleaseDates(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);

}
