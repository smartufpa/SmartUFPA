package br.ufpa.smartufpa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.SelectCategoryActivity;

public class AddBuildingFragment extends NewPlaceFragment {

    public static final String TAG = AddBuildingFragment.class.getName();

    // Views
    private TextInputEditText edtxtName;
    private TextInputEditText edtxtDescription;
    private TextInputEditText edtxtWebsite;
    private TextInputEditText edtxtOpeningTime;
    private Spinner spinner;
    private TextInputEditText edtxtClosingTime;
    private Button btnConfirm;
    private Button btnCancel;

    // Keys for JSON Object
    private final String keyName = "name";
    private final String keyDescription = "description";
    private final String keyWebsite = "website";
    private final String keyOpeningTime = "opening_time";
    private final String keyClosingTime = "closing_time";
    private final String keyAdministrativeRole = "administrative_role";

    private double latitude;
    private double longitude;

    private onNewPlaceListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_building, container, false);
        edtxtName = view.findViewById(R.id.input_bld_name);
        edtxtDescription = view.findViewById(R.id.input_bld_description);
        edtxtWebsite = view.findViewById(R.id.input_bld_website);
        edtxtOpeningTime = view.findViewById(R.id.input_bld_openingtime);
        edtxtClosingTime = view.findViewById(R.id.input_bld_closingtime);
        spinner = view.findViewById(R.id.spin_bld_administrative_role);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.spinner_administrative_role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnConfirm = view.findViewById(R.id.btn_bld_confirm);
        btnCancel = view.findViewById(R.id.btn_bld_cancel);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_bld_confirm:
                        updateJson();
                        break;
                    case R.id.btn_bld_cancel:
                       getActivity().onBackPressed();
                        break;
                }
            }
        };

        btnConfirm.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);

        edtxtOpeningTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showTimePickerDialog(edtxtOpeningTime);
            }
        });

        edtxtClosingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showTimePickerDialog(edtxtClosingTime);
            }
        });



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        latitude = bundle != null ? bundle.getDouble(SelectCategoryActivity.KEY_LATITUDE) : 0;
        longitude = bundle != null ? bundle.getDouble(SelectCategoryActivity.KEY_LONGITUDE) : 0;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof onNewPlaceListener){
            listener = (onNewPlaceListener) context;
        }else{
            throw new ClassCastException();
        }
    }

    @Override
    public void updateJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(keyName, edtxtName.getText().toString());
            json.put(SelectCategoryActivity.KEY_LATITUDE, latitude);
            json.put(SelectCategoryActivity.KEY_LONGITUDE, longitude);
            json.put(keyDescription, edtxtDescription.getText().toString());
            json.put(keyWebsite, edtxtWebsite.getText().toString());
            json.put(keyOpeningTime, edtxtOpeningTime.getText().toString());
            json.put(keyClosingTime, edtxtClosingTime.getText().toString());

            if (spinner.getSelectedItemId() == 0)
                json.put(keyAdministrativeRole,"");
            else
                json.put(keyAdministrativeRole, spinner.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.getFormJSON(json);
    }


}
