package br.ufpa.smartufpa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.MainActivity;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.utils.ElementParser;
import br.ufpa.smartufpa.utils.UIHelper;

/**
 * Fragment to show details about an specific place selected by the user.
 * Must follow the POI model.
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

    private ElementParser elementParser;


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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_place_details, container, false);
        txtWebsite = view.findViewById(R.id.txtWebsite);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtOperationHours = view.findViewById(R.id.txtOperationHours);

        final MainActivity parentActivity = (MainActivity) getActivity();

        if (parentActivity != null) {
            // Back Button
            parentActivity.bottomSheetController.collapse();
            btnBack = parentActivity.findViewById(R.id.imgBtnBack);
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.showToastShort(parentActivity,"clique");
                }
            });

            // Title
            final TextView txtBsheetTitle = parentActivity.findViewById(R.id.txt_bsheet_title);
            txtBsheetTitle.setText(elementParser.getName(pointOfInterest));

            // Subtitle
            final TextView txtBsheetSubtitle = parentActivity.findViewById(R.id.txt_bsheet_subtitle);
            final String localName = elementParser.getLocalName(pointOfInterest);
            if (localName != null){
                txtBsheetSubtitle.setText(localName);
            }else{
                txtBsheetSubtitle.setVisibility(View.INVISIBLE);
            }

            // ExtraInfo
            final TextView txtExtraInfo = parentActivity.findViewById(R.id.txt_bsheet_extra_info);
            txtExtraInfo.setVisibility(View.GONE);

        }

        final String website = elementParser.getWebsite(pointOfInterest);
        if(website!= null)
            txtWebsite.setText(website);

        final String operationHours = elementParser.getOperationHours(pointOfInterest);
        if(operationHours != null)
            txtOperationHours.setText(operationHours);

        final String description = elementParser.getDescription(pointOfInterest);
        if(description != null)
            txtDescription.setText(description);

        return view;
    }



}
