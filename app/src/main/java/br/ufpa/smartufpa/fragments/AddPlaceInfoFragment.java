package br.ufpa.smartufpa.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.PlaceCategory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AddPlaceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlaceInfoFragment extends Fragment {

    // TODO: check this https://stuff.mit.edu/afs/sipb/project/android/docs/training/improving-layouts/loading-ondemand.html
    // https://stackoverflow.com/questions/25884448/dynamically-loading-xml-file-to-linearlayout

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_CATEGORY = "category";

    public static final String FRAGMENT_TAG = AddPlaceInfoFragment.class.getName();

    private double latitude;
    private double longitude;
    private PlaceCategory category;

    private TextView txtCategoryTitle;
    private ImageView imgCategoryIcon;
    private Toolbar tbAddPlace;
    private LinearLayout layoutAddPlaceInfo;
    private Button btnConfirm;
    private Button btnCancel;

    public AddPlaceInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latitude  Parameter 1.
     * @param longitude Parameter 2.
     * @return A new instance of fragment AddPlaceInfoFragment.
     */

    public static AddPlaceInfoFragment newInstance(double latitude, double longitude, PlaceCategory category) {
        AddPlaceInfoFragment fragment = new AddPlaceInfoFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putParcelable(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
            category = getArguments().getParcelable(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_place_info, container, false);

        txtCategoryTitle = view.findViewById(R.id.txt_category);
        imgCategoryIcon = view.findViewById(R.id.img_add_info_icon);
//        btnConfirm = view.findViewById(R.id.btn_add_place_confirm);
//        btnCancel = view.findViewById(R.id.btn_add_place_cancel);
        tbAddPlace = getActivity().findViewById(R.id.tb_add_place);
        layoutAddPlaceInfo = view.findViewById(R.id.layout_add_place_info);
        layoutAddPlaceInfo.setPadding(16, 16, 16, 16);

//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (tbAddPlace != null)
            tbAddPlace.setSubtitle(R.string.add_place_subtitle_details);
        if (txtCategoryTitle != null) {
            txtCategoryTitle.setText(category.getName());
            imgCategoryIcon.setImageDrawable(category.getIcon());
        }

    }

    private void loadBuildingView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_add_building, null);
        layoutAddPlaceInfo.addView(view);
        final FragmentActivity activity = getActivity();

    }

    private void chamarTimePickerDialog() {
        String formattedHour = "";

    }


}
