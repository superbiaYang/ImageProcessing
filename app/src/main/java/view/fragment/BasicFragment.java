package view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import processor.Basic;
import superbiayang.imageprocessing.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BasicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BasicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener = null;

    public BasicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BasicFragment.
     */
    public static BasicFragment newInstance() {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        int[] buttonId = {R.id.basic_red_button, R.id.basic_green_button, R.id.basic_blue_button};
        boolean isColor = mListener.isColor();
        for (int id : buttonId) {
            Button button = (Button) view.findViewById(id);
            if (isColor) {
                button.setOnClickListener(this);
            } else {
                button.setEnabled(false);
            }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basic_red_button:
                mListener.separateChannel(Basic.MASK_RED);
                break;
            case R.id.basic_green_button:
                mListener.separateChannel(Basic.MASK_GREEN);
                break;
            case R.id.basic_blue_button:
                mListener.separateChannel(Basic.MASK_BLUE);
                break;
            default:
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
        void separateChannel(int channelMask);

        boolean isColor();
    }
}