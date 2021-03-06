package us.wetpaws.wydlist.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.adapter.GlideUtil;
import us.wetpaws.wydlist.adapter.SimpleDividerItemDecoration;
import us.wetpaws.wydlist.model.BucketList;
import us.wetpaws.wydlist.model.User;
import us.wetpaws.wydlist.viewholder.PrivateViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyListFragment extends Fragment {

    String photoResolutionSizeString = "s300-c";
    private RecyclerView.Adapter<PrivateViewHolder> mListAdapter;
    FirebaseRecyclerAdapter<BucketList, PrivateViewHolder> mUserAdapter;
    FirebaseRecyclerAdapter<Boolean, PrivateViewHolder> mAdapter;
    DatabaseReference userFeedReference;
    DatabaseReference mainFeedReference;
    private OnFragmentInteractionListener mListener;
    FirebaseUser mFirebaseUser;
    ImageView profileImageView;
    TextView profileDisplayName;

    public MyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyListFragment newInstance(String param1, String param2) {
        MyListFragment fragment = new MyListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_list, container, false);
        RecyclerView wydRecyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recyclerview_list);
        profileImageView = (ImageView) rootView.findViewById(R.id.profile_user_photo);
        profileDisplayName = (TextView) rootView.findViewById(R.id.profile_display_name);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = FirebaseUtil.getUser();

        String photoQualityEnlargeRes = String.valueOf(firebaseUser.getPhotoUrl());
        String[] splitString = photoQualityEnlargeRes.split("s96-c");
        String firstString = splitString[0];
        String secondString = splitString[1];
        String newImageString = firstString + photoResolutionSizeString + secondString;

        GlideUtil.loadImage(newImageString, profileImageView);
        profileDisplayName.setText(String.format("%s Wants To Do:", user.getUserDisplayName()));

        userFeedReference = FirebaseUtil.getUserListRef();
        Query privateQuery = userFeedReference.child(user.getUserid());
        mainFeedReference = FirebaseUtil.getMainListRef();

        mAdapter = new FirebaseRecyclerAdapter<Boolean, PrivateViewHolder>(
                Boolean.class,
                R.layout.item_personal_wyd_list,
                PrivateViewHolder.class,
                userFeedReference.child(user.getUserid())
        ) {
            @Override
            protected void populateViewHolder(final PrivateViewHolder viewHolder, Boolean model, int position) {
                String feedKey = this.getRef(position).getKey();

                mainFeedReference.child(feedKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BucketList bucketList = dataSnapshot.getValue(BucketList.class);
                        viewHolder.setPersonal_bucketlist_title(bucketList.getTitle());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mUserAdapter = new FirebaseRecyclerAdapter<BucketList, PrivateViewHolder>(
                BucketList.class,
                R.layout.item_personal_wyd_list,
                PrivateViewHolder.class,
                userFeedReference.child(user.getUserid())
        ) {
            @Override
            protected void populateViewHolder(final PrivateViewHolder viewHolder, BucketList model, int position) {
                String feedKey = this.getRef(position).getKey();

                mainFeedReference.child(feedKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BucketList bucketList = dataSnapshot.getValue(BucketList.class);
//                        TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.profile_bucket_title);
//                        assert bucketList != null;
//                        textView.setText(bucketList.getTitle());
                        viewHolder.setPersonal_bucketlist_title(bucketList.getTitle());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        // Produced an error.
//        userFeedReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                BucketList bucketList = dataSnapshot.getValue(BucketList.class);
//                Log.i("title", bucketList.getTitle().toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        mListAdapter = new RecyclerView.Adapter<PrivateViewHolder>() {
            @Override
            public PrivateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.i("title", "One");
                return null;
            }

            @Override
            public void onBindViewHolder(PrivateViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

//        mListAdapter = new FirebaseRecyclerAdapter<BucketList, PrivateViewHolder>(
//                BucketList.class,
//                R.layout.item_personal_wyd_list,
//                PrivateViewHolder.class,
//                userFeedReference
//        ) {
//            @Override
//            protected void populateViewHolder(PrivateViewHolder viewHolder, BucketList model, int position) {
//                String feedPostKey = ((FirebaseRecyclerAdapter) mListAdapter).getRef(position).getKey();
//
//                viewHolder.setPersonal_bucketlist_title(model.getTitle());
//            }
//        };

        wydRecyclerView.setLayoutManager(linearLayoutManager);
        wydRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        wydRecyclerView.setAdapter(mAdapter);

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
