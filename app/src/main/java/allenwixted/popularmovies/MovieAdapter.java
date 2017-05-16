package allenwixted.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by allenwixted on 12/05/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private int mNumberItems;
    private ArrayList<Movie> moviesArrayList = new ArrayList<Movie>();
    private final ItemClickListener mOnClickListener;

    public void setMovieData(ArrayList<Movie> movieArrayList) {
        this.moviesArrayList = movieArrayList;
        Log.i("ADAPTER ", movieArrayList.toString());
        Log.i("ADAPTER MOVIE 1 ", movieArrayList.get(0).getTitle());
        this.notifyDataSetChanged();
    }

    public interface ItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdapter(int numberOfItems, ItemClickListener listener) {
        mOnClickListener = listener;
        mNumberItems = numberOfItems;
    }


    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listItemMovieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            listItemMovieTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            itemView.setOnClickListener(this);
        }

        public void bind(int listIndex){
            //listItemMovieTitle.setText(String.valueOf(listIndex));
            listItemMovieTitle.setText(moviesArrayList.get(listIndex).getTitle());
            Log.i(TAG, moviesArrayList.get(listIndex).getTitle());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
