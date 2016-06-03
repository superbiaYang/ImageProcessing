package view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import superbiayang.imageprocessing.R;
import view.component.BinarySeekBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BinaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BinaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BinaryFragment extends Fragment implements
        BinarySeekBar.OnRangeSeekBarChangeListener,
        View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private BinarySeekBar seekBar = null;
    private EditText minText = null;
    private EditText maxText = null;

    public BinaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BinaryFragment.
     */
    public static BinaryFragment newInstance() {
        BinaryFragment fragment = new BinaryFragment();
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
        View view = inflater.inflate(R.layout.fragment_binary, container, false);
        seekBar = (BinarySeekBar) view.findViewById(R.id.binary_seekbar);
        seekBar.setOnBinarySeekBarChangeListener(this);
        Button otsu = (Button) view.findViewById(R.id.binary_otsu_button);
        otsu.setOnClickListener(this);
        minText = (EditText) view.findViewById(R.id.binary_min_editText);
        minText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    return;
                }
                int value = Integer.parseInt(s.toString());
                seekBar.setSelectedMinValue(value);

                mListener.onBinaryThresholdUpdate(value, Integer.parseInt(maxText.getText().toString()));
            }
        });
        maxText = (EditText) view.findViewById(R.id.binary_max_editText);
        maxText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    return;
                }
                int value = Integer.parseInt(s.toString());
                seekBar.setSelectedMaxValue(value);
                mListener.onBinaryThresholdUpdate(Integer.parseInt(minText.getText().toString()), value);
            }
        });
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
    public void onRangeSeekBarValuesChanged(BinarySeekBar bar, int minValue, int maxValue) {
        if (minText.getText().toString().compareTo(String.valueOf(minValue)) != 0) {
            minText.setText(String.valueOf(minValue));
        }
        if (maxText.getText().toString().compareTo(String.valueOf(maxValue)) != 0) {
            maxText.setText(String.valueOf(maxValue));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.binary_otsu_button) {
            int value = mListener.getOtusThreshold();
            minText.setText("0");
            maxText.setText(String.valueOf(value));
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
        void onBinaryThresholdUpdate(int min, int max);

        int getOtusThreshold();
    }
}