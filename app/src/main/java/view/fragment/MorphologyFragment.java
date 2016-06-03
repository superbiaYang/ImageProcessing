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
 * {@link MorphologyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MorphologyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MorphologyFragment extends Fragment implements Button.OnClickListener {
    private OnFragmentInteractionListener mListener;

    public MorphologyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MorphologyFragment.
     */
    public static MorphologyFragment newInstance() {
        MorphologyFragment fragment = new MorphologyFragment();
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
        View view = inflater.inflate(R.layout.fragment_morphology, container, false);
        int[] buttonId = {
                R.id.morphology_erode_button,
                R.id.morphology_dilate_button,
                R.id.morphology_open_button,
                R.id.morphology_close_button
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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.morphology_erode_button) {
            mListener.erode();
        } else if (id == R.id.morphology_dilate_button) {
            mListener.dilate();
        } else if (id == R.id.morphology_open_button) {
            mListener.open();
        } else if (id == R.id.morphology_close_button) {
            mListener.close();
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
        void erode();

        void dilate();

        void open();

        void close();
    }
}
