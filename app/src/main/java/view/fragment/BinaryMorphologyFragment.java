package view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import superbiayang.imageprocessing.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BinaryMorphologyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BinaryMorphologyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BinaryMorphologyFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private int[] mSkeleton;

    public BinaryMorphologyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BinaryMorphologyFragment.
     */
    public static BinaryMorphologyFragment newInstance() {
        BinaryMorphologyFragment fragment = new BinaryMorphologyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_binary_morphology, container, false);
        int[] buttonId = {
                R.id.distance_button,
                R.id.skeleton_button,
                R.id.reconstruct_button
        };
        for (int id : buttonId) {
            Button button = (Button) view.findViewById(id);
            button.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSkeleton = null;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * In order to support API 21
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSkeleton = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.distance_button:
                mListener.transformDistance();
                break;
            case R.id.skeleton_button:
                mSkeleton = mListener.skeleton();
                if (mSkeleton != null) {
                    getView().findViewById(R.id.reconstruct_button).setEnabled(true);
                }
                break;
            case R.id.reconstruct_button:
                mListener.reconstruct(mSkeleton);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void transformDistance();

        int[] skeleton();

        void reconstruct(int[] skeleton);
    }
}