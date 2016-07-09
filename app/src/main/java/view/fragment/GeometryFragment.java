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
import android.widget.EditText;
import android.widget.RadioGroup;

import superbiayang.imageprocessing.R;
import view.component.CircularSeekBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeometryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeometryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeometryFragment extends Fragment implements
        View.OnClickListener,
        CircularSeekBar.OnCircularSeekBarChangeListener,
        TextWatcher {
    private OnFragmentInteractionListener mListener = null;
    private CircularSeekBar seekBar = null;

    public GeometryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GeometryFragment.
     */
    public static GeometryFragment newInstance() {
        GeometryFragment fragment = new GeometryFragment();
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
        View view = inflater.inflate(R.layout.fragment_geometry, container, false);
        seekBar = (CircularSeekBar) view.findViewById(R.id.circle_seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        EditText editText = (EditText) view.findViewById(R.id.circle_editText);
        editText.addTextChangedListener(this);

        int[] viewId = {
                R.id.resize_button
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
            case R.id.resize_button:
                resize();
        }
    }

    private void resize() {
        int width = (int) getValue(R.id.geo_width_editText, 0);
        int height = (int) getValue(R.id.geo_height_editText, 0);
        RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.geometry_radioGroup);
        mListener.resize(width, height, radioGroup.getCheckedRadioButtonId());
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

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
        EditText editText = (EditText) getView().findViewById(R.id.circle_editText);
        editText.setText(String.valueOf(progress));
        RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.geometry_radioGroup);
        mListener.rotate(progress, radioGroup.getCheckedRadioButtonId());
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {

    }

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
        value = value % 360;
        seekBar.setProgress(value);
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
        void rotate(int degree, int choice);

        void resize(int width, int height, int choice);
    }
}