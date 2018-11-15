package br.ufpa.smartufpa.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.EditElementActivity;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.osm.ElementParser;

/**
 * Fragment to show details about an specific place selected by the user.
 * Must follow the POI model.
 *
 * @author kaeuchoa
 */
public class PlaceDetailsFragment extends Fragment {


    public static final String FRAGMENT_TAG = PlaceDetailsFragment.class.getName();

    public static final String ARG_ELEMENT = "element";

    private static final String TAG = PlaceDetailsFragment.class.getSimpleName();

    // VIEWS
    private TextView txtWebsite;
    private TextView txtOperationHours;
    private TextView txtDescription;
    private Button btnEdit;

    private ElementParser elementParser;

    private int colorBlack;

    private Element element;

    public PlaceDetailsFragment() {}

    public static PlaceDetailsFragment newInstance(Element element) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ELEMENT, element);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        elementParser = ElementParser.INSTANCE;
        if (getArguments() != null) {
            element = getArguments().getParcelable(ARG_ELEMENT);
        }

        colorBlack = getActivity().getResources().getColor(android.R.color.black);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_place_details, container, false);
        bindViews(view);


        final String website = elementParser.getWebsite(element);
        final HashMap<String, String> operationHours = elementParser.getOpeningHours(element);
        final String description = elementParser.getDescription(element);

        setupWebsiteText(website);
        setupOperationHoursText(operationHours, txtOperationHours);
        setupDescriptionText(description, txtDescription);
        setupBtnEdit(element);

        return view;
    }

    private void setupBtnEdit(final Element element) {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getContext(), EditElementActivity.class);
                intent.putExtra(ARG_ELEMENT,element);
                startActivityForResult(intent,Constants.RequestCode.EDIT_ELEMENT);
            }
        });
    }

    private void bindViews(View view) {
        txtWebsite = view.findViewById(R.id.txtWebsite);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtOperationHours = view.findViewById(R.id.txtOperationHours);
        btnEdit = view.findViewById(R.id.btnEdit);
    }

    private void setupDescriptionText(String description, TextView txtDescription) {
        if (description != null) {
            txtDescription.setText(description);
            txtDescription.setTextColor(colorBlack);
        }
    }

    private void setupOperationHoursText(HashMap<String, String> operationHours, TextView txtOperationHours) {
        //TODO parse de horas
        if (operationHours != null) {
            txtOperationHours.setText(
                    String.format(getString(R.string.operation_hours_template),
                            operationHours.get(Constants.OpeningHours.OPENING_DAY),
                            operationHours.get(Constants.OpeningHours.CLOSING_DAY),
                            operationHours.get(Constants.OpeningHours.OPENING_HOUR),
                            operationHours.get(Constants.OpeningHours.CLOSING_HOUR)
                    ));
            txtOperationHours.setTextColor(colorBlack);
        }
    }

    private void setupWebsiteText(String website) {
        if (website != null) {
            txtWebsite.setText(website);
            txtWebsite.setTextColor(colorBlack);
        }
    }


}
