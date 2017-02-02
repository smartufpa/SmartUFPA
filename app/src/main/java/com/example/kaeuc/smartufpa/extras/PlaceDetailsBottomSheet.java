package com.example.kaeuc.smartufpa.extras;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.example.kaeuc.smartufpa.MapActivity;
import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.classes.Place;

/**
 * Created by kaeuc on 17/11/2016.
 * Fragment para mostrar o detalhes sobre o local selecionado
 */

public class PlaceDetailsBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    // Método que permite que o fragment receba parâmetros
    public static PlaceDetailsBottomSheet newInstance(Place place) {
        Bundle args = new Bundle();
        PlaceDetailsBottomSheet fragment = new PlaceDetailsBottomSheet();
        args.putSerializable("place",place);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View contentView = View.inflate(getContext(), R.layout.details_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final CoordinatorLayout.Behavior behavior = params.getBehavior();
        bottomSheetBehavior = (BottomSheetBehavior) behavior;

        final Place targetPlace = (Place) getArguments().getSerializable("place");
        // Views
        TextView placeName = (TextView) contentView.findViewById(R.id.place_name);
        placeName.setText(targetPlace.getName());

        TextView placeDescription = (TextView) contentView.findViewById(R.id.place_description);
        placeDescription.setText(targetPlace.getDescription());

        FloatingActionButton goToFAB = (FloatingActionButton) contentView.findViewById(R.id.fab);
        goToFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapActivity)getActivity()).traceRoute(targetPlace);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });


        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) behavior).setPeekHeight(800);
        }
    }

}
