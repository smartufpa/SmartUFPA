package br.ufpa.smartufpa.fragments;

import android.app.TimePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

import org.json.JSONObject;

import java.util.Calendar;

import br.ufpa.smartufpa.interfaces.OnCreatePlaceListener;

public abstract class NewPlaceFragment extends Fragment implements OnCreatePlaceListener {


    protected void showTimePickerDialog(final TextInputEditText input) {
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        input.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }

    public interface onNewPlaceListener {
        void getFormJSON(JSONObject jsonObject);
    }

}
