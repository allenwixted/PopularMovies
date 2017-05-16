package allenwixted.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    static String TAG = "MainActivity";
    final static String MOVIE_BASE_URL = "http://api.themoviedb.org/3/";
    final static String SIZE = "w185";
    final static String PARAM_QUERY = "q";
    final static String API_KEY = "0e1ce68db3048ca25c9f60f2832a9b88";
    static String sortBy = "/movie/top_rated";
    final static String testURL = "https://api.themoviedb.org/3/movie/550?api_key=0e1ce68db3048ca25c9f60f2832a9b88";
    String jsonResult = "";

    private TextView errorTextView;
    private ProgressBar progressBar;

    private static final int NUM_LIST_ITEMS = 100;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;

    private Toast mToast;

    public ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorTextView = (TextView) findViewById(R.id.error_message_display);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(movieArrayList.size(), this);
        recyclerView.setAdapter(movieAdapter);

        URL url = buildUrl(true);
        new AsyncTask().execute(url);

    }

    public ArrayList<Movie> getMovieArrayList() {
        return movieArrayList;
    }

    private void showJSONDataView(){
        errorTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessageView(){
        errorTextView.setVisibility(View.VISIBLE);
    }

    public static URL buildUrl(Boolean sorter) {

        if(sorter){
            sortBy = "top_rated";
        } else {
            sortBy = "popular";
        }

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(sortBy)
                .appendQueryParameter("api_key", API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.i(TAG, url.toString());
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mToast.makeText(this, "#" + String.valueOf(clickedItemIndex), Toast.LENGTH_SHORT).show();
    }

    public class AsyncTask extends android.os.AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            try{
                //jsonResult = getResponseFromHttpUrl(buildUrl(true));
                jsonResult = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if(s != null && !s.equals("")){
                showJSONDataView();
                Log.i(TAG, s);
                parseJSON(s);
                movieAdapter.setMovieData(movieArrayList);
                movieAdapter.notifyDataSetChanged();
                recyclerView.invalidate();
            } else {
                showErrorMessageView();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    public void parseJSON(String results){
        try {
            JSONObject main = new JSONObject(results);
            JSONArray array;
            array = main.getJSONArray("results");
            for(int i = 0; i < array.length(); i++){
                JSONObject object = (JSONObject) array.get(i);
                Movie jsonMovie = new Movie(object);
                movieArrayList.add(jsonMovie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
