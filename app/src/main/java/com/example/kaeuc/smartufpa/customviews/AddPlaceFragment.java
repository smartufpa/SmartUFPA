package com.example.kaeuc.smartufpa.customviews;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.models.PlaceFactory;
import com.example.kaeuc.smartufpa.models.overpass.Tags;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.InputParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPlaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */




public class AddPlaceFragment extends DialogFragment {
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    public static final String FRAGMENT_TAG = "AddPlaceDialog";
    private static final String TAG = AddPlaceFragment.class.getSimpleName();

    private double latitude;
    private double longitude;

    //Views
    private Spinner spinnerDefaultMarkers;
    private Button btnConfirm;
    private Button btnCancel;

    private TextInputEditText edtName;
    private TextInputEditText edtDescription;
    private TextInputEditText edtShortName;
    private TextInputEditText edtLocalName;
    private TextInputEditText edtOther;


    private OnFragmentInteractionListener mListener;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latitude Latitude do ponto sinalizado pelo marcador.
     * @param longitude Longitude do ponto sinalizado pelo marcador.
     * @return Uma nova instância de AddPlaceFragment.
     */

    public static AddPlaceFragment newInstance(double latitude, double longitude) {
        AddPlaceFragment fragment = new AddPlaceFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
        }
        setCancelable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NO_FRAME,android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_place, container, false);
        btnCancel = (Button) view.findViewById(R.id.btn_fragment_cancel);
        btnConfirm = (Button) view.findViewById(R.id.btn_fragment_confirm);
        edtName = (TextInputEditText) view.findViewById(R.id.edt_name);
        edtShortName = (TextInputEditText) view.findViewById(R.id.edt_short_name);
        edtLocalName = (TextInputEditText) view.findViewById(R.id.edt_local_name);
        edtDescription = (TextInputEditText) view.findViewById(R.id.edt_description);
        edtOther = (TextInputEditText) view.findViewById(R.id.edt_other);

        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) checkField(edtName);
            }
        });

        edtOther.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) checkField(edtOther);
            }
        });


        final Button.OnClickListener btnsClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_fragment_cancel:
                        dismiss();
                        break;
                    case R.id.btn_fragment_confirm:
                        parseForm();
                        break;
                }
            }
        };
        btnConfirm.setOnClickListener(btnsClickListener);
        btnCancel.setOnClickListener(btnsClickListener);

        spinnerSetup(view);

        // Inflate the layout for this fragment
        return view;
    }


    //Implementar caso necessite de comunicação com a activity
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private void spinnerSetup(View view){
        spinnerDefaultMarkers = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.default_places,R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinnerDefaultMarkers.setAdapter(arrayAdapter);
        spinnerDefaultMarkers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == spinnerDefaultMarkers.getCount()-1) {
                    edtOther.setEnabled(true);
                    edtOther.setError(getString(R.string.error_fragment_create_other));
                }else {
                    edtOther.setText("");
                    edtOther.setEnabled(false);
                    edtOther.setError(null);
                    edtOther.clearFocus();
                }

                if(i == 0) { // É o item default do spinner
                    ((TextView) view).setError("");
                    ((TextView) view).setText(getString(R.string.error_fragment_select_valid_option));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private boolean checkField(TextInputEditText inputField){
        try {
            InputParser.validateInput(inputField, 80, 3);
        } catch (InputParser.EmptyInputException e) {
            inputField.setError(getString(R.string.error_fragment_no_input));
            return false;
        } catch (InputParser.ExtenseInputException e) {
            inputField.setError(getString(R.string.error_fragment_input_too_long));
            return false;
        } catch (InputParser.ShortInputException e) {
            inputField.setError(getString(R.string.error_fragment_input_too_short));
            return false;
        }

        return true;
    }

    private void parseForm(){
        Tags tags = new Tags();
        if(checkField(edtName)){
            tags.setName(InputParser.parseInputString(edtName.getText().toString()));
        }else{
            Log.e(TAG,"Name field must be filled");
            return;
        }
        if(edtOther.isEnabled() && checkField(edtOther))
            tags.setAmenity(InputParser.parseInputString(edtOther.getText().toString()));
        else{
            Log.e(TAG,"Other field must be filled");
            return;
        }
        if(!edtDescription.getText().toString().isEmpty())
            tags.setDescription(InputParser.parseInputString(edtDescription.getText().toString()));
        if(!edtShortName.getText().toString().isEmpty())
            tags.setShortName(InputParser.parseInputString(edtShortName.getText().toString()));
        if(!edtLocalName.getText().toString().isEmpty())
            tags.setLocName(InputParser.parseInputString(edtLocalName.getText().toString()));
        switch (spinnerDefaultMarkers.getSelectedItemPosition()){
            case 1: // Auditórios
                tags.setAmenity(Constants.TAG_EXHIBITION_CENTRE);
                break;
            case 2: // Banheiros
                tags.setAmenity(Constants.TAG_TOILETS);
                break;
            case 3: // Bibliotecas
                tags.setAmenity(Constants.TAG_LIBRARY);
                break;
            case 4: // Restaurantes
                tags.setAmenity(Constants.TAG_RESTAURANT);
                break;
            case 5: // Xerox
                tags.setAmenity(Constants.TAG_COPYSHOP);
                break;

        }


        PlaceFactory placeFactory = new PlaceFactory();
        Place place = placeFactory.getPlace(1234565654, latitude, longitude, tags);
        // TODO: resolver ID, gerar o gson e enviar para o servidor

    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.i(TAG, "onDestroyView()");
    }


    @Override
    public void onDetach(){
        super.onDetach();
        Log.i(TAG, "onDetach()");
    }


    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        Log.i(TAG, "onDismiss()");
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "onStart()");
    }


    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG, "onStop()");
    }
}
