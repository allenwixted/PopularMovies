package allenwixted.popularmovies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allenwixted on 12/05/2017.
 */

public class Movie {

    private String title;
    private String overview;
    private String release;
    private double voteAverage;
    private String poster;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease() {
        return release;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getPoster() {
        return poster;
    }

    public Movie(JSONObject movie){
        try {
            this.title = movie.getString("title");
            this.overview = movie.getString("overview");
            this.release = movie.getString("release_date");
            this.voteAverage = movie.getDouble("vote_average");
            this.poster = movie.getString("poster_path");
            //Log.i("MOVIE ", title + "\n" + overview + "\n" + release + "\n" + String.valueOf(voteAverage) + "\n" + poster);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
