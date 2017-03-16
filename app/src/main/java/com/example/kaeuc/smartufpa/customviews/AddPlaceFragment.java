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

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.utils.InputParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPlaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// TODO: Consertar o tamanho do fragment


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
    private TextInputEditText edtNickname;
    private TextInputEditText edtOther;
    
    
    private OnFragmentInteractionListener mListener;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latitude marker latitude.
     * @param longitude marker longitude.
     * @return A new instance of fragment AddPlaceFragment.
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
        edtNickname = (TextInputEditText) view.findViewById(R.id.edt_nickname);
        edtDescription = (TextInputEditText) view.findViewById(R.id.edt_description);
        edtOther = (TextInputEditText) view.findViewById(R.id.edt_other);



        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) checkNameField();
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
                            // TODO: Enviar formulário
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
                if(i == spinnerDefaultMarkers.getCount()-1)
                    edtOther.setEnabled(true);
                else {
                    edtOther.setText("");
                    edtOther.setEnabled(false);
                    edtOther.clearFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean checkNameField(){
        try {
            InputParser.validateInput(edtName, 80, 4);
        } catch (InputParser.EmptyInputException e) {
            edtName.setError(getString(R.string.error_fragment_no_name));
            return false;
        } catch (InputParser.ExtenseInputException e) {
            edtName.setError(getString(R.string.error_fragment_name_too_long));
            return false;
        } catch (InputParser.ShortInputException e) {
            edtName.setError(getString(R.string.error_fragment_name_too_short));
            return false;
        }

        return true;
    }

    private void parseForm(){
        if(checkNameField()){
            String parsedString = InputParser.parseInputString(edtName.getText().toString());
        }

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
