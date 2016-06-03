package view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import superbiayang.imageprocessing.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment implements Button.OnClickListener {
    private OnFragmentInteractionListener mListener;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FilterFragment.
     */
    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        int[] buttonId = {
                R.id.mean_blur_button,
                R.id.median_blur_button,
                R.id.gaussian_blur_button,
                R.id.sobel_button,
                R.id.prewitt_button,
                R.id.roberts_button
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
        if (id == R.id.mean_blur_button) {
            EditText editText = (EditText) getView().findViewById(R.id.mean_blur_editText);
            int size = Integer.parseInt(editText.getText().toString());
            mListener.meanBlur(size);
        } else if (id == R.id.median_blur_button) {
            EditText editText = (EditText) getView().findViewById(R.id.median_blur_editText);
            int size = Integer.parseInt(editText.getText().toString());
            if (size % 2 == 1) {
                mListener.medianBlur(size);
            } else {
                //TODO: show error
            }
        } else if (id == R.id.gaussian_blur_button) {
            EditText editText = (EditText) getView().findViewById(R.id.gaussian_blur_size_editText);
            int size = Integer.parseInt(editText.getText().toString());
            editText = (EditText) getView().findViewById(R.id.gaussian_blur_sigma_editText);
            double sigma = Double.parseDouble(editText.getText().toString());
            if (size % 2 == 1) {
                mListener.gaussianBlur(size, sigma);
            } else {
                //TODO: show error
            }
        } else if (id == R.id.sobel_button) {
            mListener.sobel();
        } else if (id == R.id.prewitt_button) {
            mListener.prewitt();
        } else if (id == R.id.roberts_button) {
            mListener.roberts();
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
        void meanBlur(int size);

        void medianBlur(int size);

        void gaussianBlur(int size, double sigma);

        void sobel();

        void prewitt();

        void roberts();
    }
}
