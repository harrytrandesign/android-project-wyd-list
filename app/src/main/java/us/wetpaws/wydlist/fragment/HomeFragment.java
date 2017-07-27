package us.wetpaws.wydlist.fragment;

import android.content.Context;
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
import com.google.firebase.database.ServerValue;

import us.wetpaws.wydlist.R;
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

    private RecyclerView.Adapter<FeedViewHolder> mListAdapter;
    DatabaseReference mainFeedReference;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = FirebaseUtil.getUser();
        BucketList bucketList = new BucketList(user, "-AKGJADSF34290854059", "Vegas Strip.", "https://exp.cdn-hotels.com/hotels/1000000/150000/140600/140596/140596_275_z.jpg", ServerValue.TIMESTAMP);

        mainFeedReference = FirebaseUtil.getMainListRef();

        mainFeedReference.push().setValue(bucketList);

        Query mainQuery = mainFeedReference;

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
                String feedPostKey = ((FirebaseRecyclerAdapter) mListAdapter).getRef(position).getKey();

                viewHolder.setBucketTitle(model.getTitle());
                viewHolder.setBackgroundImage(model.getImageurl());
                viewHolder.setBucketDateTimestamp(DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());
            }
        };

        wydRecyclerView.setLayoutManager(linearLayoutManager);
        wydRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        wydRecyclerView.setAdapter(mListAdapter);

        nativeExpressAdView.loadAd(adRequest);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
