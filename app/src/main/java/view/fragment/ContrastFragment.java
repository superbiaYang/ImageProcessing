package view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import superbiayang.imageprocessing.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContrastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContrastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContrastFragment extends Fragment implements
        View.OnClickListener {
    private OnFragmentInteractionListener mListener = null;

    public ContrastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContrastFragment.
     */
    public static ContrastFragment newInstance() {
        ContrastFragment fragment = new ContrastFragment();
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
        View view = inflater.inflate(R.layout.fragment_contrast, container, false);
        int[] viewId = {
                R.id.contrast_button,
                R.id.linear_radioButton,
                R.id.logarithm_radioButton,
                R.id.power_radioButton
        };
        for (int id : viewId) {
            View v = (View) view.findViewById(id);
            v.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.linear_radioButton:
                enableLinearInput();
                break;
            case R.id.logarithm_radioButton:
                enableLogInput();
                break;
            case R.id.power_radioButton:
                enablePowInput();
                break;
            case R.id.contrast_button:
                process();
                break;
        }
    }

    private double getValue(int id, double defaultValue) {
        EditText editText = (EditText) getView().findViewById(id);
        if (editText == null) {
            return defaultValue;
        }
        double v = defaultValue;
        String text = editText.getText().toString();
        if (text.compareTo("") == 0) {
            editText.setText(String.valueOf(defaultValue));
        } else {
            v = Double.parseDouble(text);
        }
        return v;
    }

    private void process() {
        RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.contrast_radioGroup);
        EditText editText;
        String text;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.linear_radioButton:
                double k = getValue(R.id.linear_k_editText, 1);
                double b = getValue(R.id.linear_b_editText, 0);
                mListener.linearContrast(k, b);
                break;
            case R.id.logarithm_radioButton:
                double log_a = getValue(R.id.log_a_editText, 1);
                double log_b = getValue(R.id.log_b_editText, 1);
                double log_c = getValue(R.id.log_c_editText, 1);
                mListener.logContrast(log_a, log_b, log_c);
                break;
            case R.id.power_radioButton:
                double pow_a = getValue(R.id.pow_a_editText, 1);
                double pow_b = getValue(R.id.pow_b_editText, 1);
                double pow_c = getValue(R.id.pow_c_editText, 1);
                mListener.powContrast(pow_a, pow_b, pow_c);
                break;
        }
    }

    private void disableInput() {
        int[] editTextId = {
                R.id.linear_k_editText,
                R.id.linear_b_editText,
                R.id.log_a_editText,
                R.id.log_b_editText,
                R.id.log_c_editText,
                R.id.pow_a_editText,
                R.id.pow_b_editText,
                R.id.pow_c_editText
        };
        for (int id : editTextId) {
            EditText editText = (EditText) getView().findViewById(id);
            editText.setEnabled(false);
        }
    }

    private void enableLinearInput() {
        disableInput();
        int[] editTextId = {
                R.id.linear_k_editText,
                R.id.linear_b_editText
        };
        for (int id : editTextId) {
            EditText editText = (EditText) getView().findViewById(id);
            editText.setEnabled(true);
        }
    }

    private void enableLogInput() {
        disableInput();
        int[] editTextId = {
                R.id.log_a_editText,
                R.id.log_b_editText,
                R.id.log_c_editText
        };
        for (int id : editTextId) {
            EditText editText = (EditText) getView().findViewById(id);
            editText.setEnabled(true);
        }
    }

    private void enablePowInput() {
        disableInput();
        int[] editTextId = {
                R.id.pow_a_editText,
                R.id.pow_b_editText,
                R.id.pow_c_editText
        };
        for (int id : editTextId) {
            EditText editText = (EditText) getView().findViewById(id);
            editText.setEnabled(true);
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
        void linearContrast(double k, double b);

        void logContrast(double a, double b, double c);

        void powContrast(double a, double b, double c);
    }
}