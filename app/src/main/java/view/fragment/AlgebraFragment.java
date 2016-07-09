package view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import superbiayang.imageprocessing.PicInfo;
import superbiayang.imageprocessing.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlgebraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlgebraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlgebraFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private PicInfo pic;

    public AlgebraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlgebraFragment.
     */
    public static AlgebraFragment newInstance() {
        AlgebraFragment fragment = new AlgebraFragment();
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
        View view = inflater.inflate(R.layout.fragment_algebra, container, false);
        int[] buttonId = {
                R.id.algebra_open_button,
                R.id.algebra_add_button,
                R.id.algebra_sub_button,
                R.id.algebra_multi_button
        };
        for (int id : buttonId) {
            Button button = (Button) view.findViewById(id);
            if (id != R.id.algebra_open_button) {
                button.setEnabled(false);
            }
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
        if (id == R.id.algebra_open_button) {
            mListener.openAlgebraBitmap();
        } else if (id == R.id.algebra_add_button) {
            mListener.add(pic);
        } else if (id == R.id.algebra_sub_button) {
            mListener.sub(pic);
        } else if (id == R.id.algebra_multi_button) {
            mListener.multi(pic);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        ImageView imageView = (ImageView) getView().findViewById(R.id.algebra_imageView);
        imageView.setImageBitmap(bitmap);
        pic = new PicInfo(bitmap);
        pic.setPicType(PicInfo.PicType.COLOR);
        int[] buttonId = {
                R.id.algebra_add_button,
                R.id.algebra_sub_button,
                R.id.algebra_multi_button
        };
        for (int id : buttonId) {
            Button button = (Button) getView().findViewById(id);
            button.setEnabled(true);
        }
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
        void openAlgebraBitmap();

        void add(PicInfo pic);

        void sub(PicInfo pic);

        void multi(PicInfo pic);
    }
}