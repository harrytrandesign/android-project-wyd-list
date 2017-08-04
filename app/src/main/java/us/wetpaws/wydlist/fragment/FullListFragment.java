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

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.activity.CommentActivity;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.model.BucketList;
import us.wetpaws.wydlist.model.User;
import us.wetpaws.wydlist.viewholder.FeedViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FullListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FullListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter<FeedViewHolder> mFullListAdapter;
    FirebaseUser firebaseUser;
    RecyclerView full_list_recyclerview;
    DatabaseReference fullFeedReference;
    NativeExpressAdView full_list_native_express_adview;
    LinearLayoutManager linearLayoutManager;
    AdRequest adRequest;
    User user;
    View rootView;

    public FullListFragment() {
        // Required empty public constructor
    }

    public static FullListFragment newInstance(String param1, String param2) {
        FullListFragment fragment = new FullListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_full_list, container, false);
        full_list_recyclerview = (RecyclerView) rootView.findViewById(R.id.user_wydlist_full_list_recyclerview);
        full_list_native_express_adview = (NativeExpressAdView) rootView.findViewById(R.id.nativeExpressAdView_public_full_list);
        adRequest = new AdRequest.Builder().build();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseUtil.getUser();
        fullFeedReference = FirebaseUtil.getMainListRef();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        mFullListAdapter = new FirebaseRecyclerAdapter<BucketList, FeedViewHolder>(
                BucketList.class,
                R.layout.item_feed_wyd_list,
                FeedViewHolder.class,
                fullFeedReference
        ) {
            @Override
            protected void populateViewHolder(FeedViewHolder viewHolder, BucketList model, int position) {
                final String feedPostKey = ((FirebaseRecyclerAdapter) mFullListAdapter).getRef(position).getKey();

                viewHolder.setBucketTitle(model.getTitle());
                viewHolder.setBackgroundImage(model.getImageurl());
                viewHolder.setBucketDateTimestamp(DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());
                viewHolder.setPostClickListener(new FeedViewHolder.PostClickListener() {
                    @Override
                    public void showComments() {
                        Intent intent = new Intent(FullListFragment.this.getActivity(), CommentActivity.class);
                        intent.putExtra(CommentActivity.POST_KEY_EXTRA, feedPostKey);
                        FullListFragment.this.startActivity(intent);
                    }
                });
            }
        };

        full_list_recyclerview.setLayoutManager(linearLayoutManager);
        full_list_recyclerview.setAdapter(mFullListAdapter);

        full_list_native_express_adview.loadAd(adRequest);

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
