package br.ufpa.smartufpa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.dialogs.EditDialog;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.ElementParser;

/**
 * Fragment to show details about an specific place selected by the user.
 * Must follow the POI model.
 *
 * @author kaeuchoa
 */
public class PlaceDetailsFragment extends Fragment {


    public static final String FRAGMENT_TAG = PlaceDetailsFragment.class.getName();

    private static final String ARG_POINT_OF_INTEREST = "point_of_interest";
    private static final String TAG = PlaceDetailsFragment.class.getSimpleName();

    // VIEWS
    private TextView txtWebsite;
    private TextView txtOperationHours;
    private TextView txtDescription;
    private ImageButton btnBack;
    private Button btnEdit;

    private ElementParser elementParser;

    private int colorBlack;


    // TODO (POSTPONED): LOAD IMAGE OF PLACE AND IMPLEMENT RATING FUNCTIONS

    private Element pointOfInterest;

    public PlaceDetailsFragment() {
        // Required empty public constructor
    }

    public static PlaceDetailsFragment newInstance(Element element) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POINT_OF_INTEREST, element);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        elementParser = ElementParser.INSTANCE;
        if (getArguments() != null) {
            pointOfInterest = getArguments().getParcelable(ARG_POINT_OF_INTEREST);
        }

        colorBlack = getActivity().getResources().getColor(android.R.color.black);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_place_details, container, false);
        bindViews(view);


        final String website = elementParser.getWebsite(pointOfInterest);
        final HashMap<String, String> operationHours = elementParser.getOpeningHours(pointOfInterest);
        final String description = elementParser.getDescription(pointOfInterest);

        setupWebsiteText(website);
        setupOperationHoursText(operationHours, txtOperationHours);
        setupDescriptionText(description, txtDescription);
        setupBtnEdit(pointOfInterest);

        return view;
    }

    private void setupBtnEdit(final Element pointOfInterest) {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog editDialog = new EditDialog(view.getContext(), (ViewGroup) view.getRootView(), getResources());
                editDialog.open(pointOfInterest);
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
