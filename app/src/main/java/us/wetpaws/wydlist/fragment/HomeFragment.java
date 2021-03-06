package us.wetpaws.wydlist.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.activity.CommentActivity;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.adapter.SimpleDividerItemDecoration;
import us.wetpaws.wydlist.model.BucketList;
import us.wetpaws.wydlist.model.User;
import us.wetpaws.wydlist.viewholder.FeedViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final int FEED_ITEM_CALL = 50;
    private RecyclerView.Adapter<FeedViewHolder> mListAdapter;
    DatabaseReference mainFeedReference;
    FirebaseUser firebaseUser;
    Query mainQuery;
    User user;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView wydRecyclerView = (RecyclerView) rootView.findViewById(R.id.user_wydlist_recyclerview);
        NativeExpressAdView nativeExpressAdView = (NativeExpressAdView) rootView.findViewById(R.id.nativeExpressAdView);
        AdRequest adRequest = new AdRequest.Builder().build();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseUtil.getUser();
        mainFeedReference = FirebaseUtil.getMainListRef();
        mainQuery = mainFeedReference.limitToLast(FEED_ITEM_CALL);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        mListAdapter = new FirebaseRecyclerAdapter<BucketList, FeedViewHolder>(
                BucketList.class,
                R.layout.item_feed_wyd_list,
                FeedViewHolder.class,
                mainQuery
        ) {
            @Override
            protected void populateViewHolder(FeedViewHolder viewHolder, BucketList model, int position) {
                final String feedPostKey = ((FirebaseRecyclerAdapter) mListAdapter).getRef(position).getKey();

                viewHolder.setBucketTitle(model.getTitle());
                viewHolder.setBackgroundImage(model.getImageurl());
                viewHolder.setBucketDateTimestamp(DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());
                viewHolder.setPostClickListener(new FeedViewHolder.PostClickListener() {
                    @Override
                    public void showComments() {
                        Intent intent = new Intent(HomeFragment.this.getActivity(), CommentActivity.class);
                        intent.putExtra(CommentActivity.POST_KEY_EXTRA, feedPostKey);
                        HomeFragment.this.startActivity(intent);
                    }
                });
            }
        };

        wydRecyclerView.setLayoutManager(linearLayoutManager);
        wydRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        wydRecyclerView.setAdapter(mListAdapter);

        nativeExpressAdView.loadAd(adRequest);

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
