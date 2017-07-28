package us.wetpaws.wydlist.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.model.Comment;
import us.wetpaws.wydlist.model.User;
import us.wetpaws.wydlist.viewholder.CommentViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {

    static final String POST_REF_PARAM = "post_ref_param";
    NativeExpressAdView nativeCommentExpressAdView;
    EditText discussionEdittext;
    ImageView wydlist_image;
    ImageView discussionSubmitButton;
    String mPostRef;
    DatabaseReference discussionRef;
    RecyclerView.Adapter<CommentViewHolder> discussionAdapter;

    private OnFragmentInteractionListener mListener;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance(String postRef) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(POST_REF_PARAM, postRef);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPostRef = getArguments().getString(POST_REF_PARAM);
        } else {
            throw new RuntimeException("You must specify a post reference.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        final RecyclerView mCommentsView = (RecyclerView) rootView.findViewById(R.id.discussion_recycler_view);
        wydlist_image = (ImageView) rootView.findViewById(R.id.wydlist_image_view);
        discussionEdittext = (EditText) rootView.findViewById(R.id.user_comment_input);
        discussionSubmitButton = (ImageView) rootView.findViewById(R.id.comment_submit_button);
        nativeCommentExpressAdView = (NativeExpressAdView) rootView.findViewById(R.id.comment_native_express_adview);

        AdRequest adRequest = new AdRequest.Builder().build();

        discussionRef = FirebaseUtil.getCommentRef();

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager.setReverseLayout(false);

        discussionAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(
                Comment.class,
                R.layout.item_comment_single,
                CommentViewHolder.class,
                discussionRef.child(mPostRef)
        ) {
            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, Comment model, int position) {
                User user = model.getUser();
                viewHolder.setProfileIcon(model.getUser().getUserPhoto());
                viewHolder.setAuthor(model.getUser().getUserDisplayName());
                viewHolder.setCommentText(model.getCommentText());
                viewHolder.setCommentTimestamp(DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());
            }
        };

        discussionSubmitButton.setEnabled(false);
        discussionSubmitButton.setColorFilter(R.color.disabled_grey, PorterDuff.Mode.SRC_ATOP);

        discussionEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    discussionSubmitButton.setEnabled(true);
                    discussionSubmitButton.setColorFilter(null);
                } else {
                    discussionSubmitButton.setEnabled(false);
                    discussionSubmitButton.setColorFilter(R.color.disabled_grey, PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        discussionSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Editable discussionTextEntered = discussionEdittext.getText();
                discussionEdittext.setText("");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                
                if (user == null) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }

                User commentAuthor = FirebaseUtil.getUser();

                Comment commentText = new Comment(commentAuthor, discussionTextEntered.toString(), ServerValue.TIMESTAMP);

                discussionRef.child(mPostRef).push().setValue(commentText, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            discussionEdittext.setText(discussionTextEntered);
                        }
                    }
                });

                mCommentsView.smoothScrollToPosition(discussionAdapter.getItemCount());

            }
        });

        mCommentsView.setLayoutManager(mLinearLayoutManager);
        mCommentsView.setAdapter(discussionAdapter);

        nativeCommentExpressAdView.loadAd(adRequest);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressd(Uri uri) {
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
