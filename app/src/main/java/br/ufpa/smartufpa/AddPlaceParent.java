package br.ufpa.smartufpa;

import android.app.TimePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

import br.ufpa.smartufpa.activities.AddBuildingActivity;

public class AddPlaceParent extends AppCompatActivity {

    protected static final String ARG_LATITUDE = "latitude";
    protected static final String ARG_LONGITUDE = "longitude";

    protected double latitude;
    protected double longitude;

    protected JSONObject json;

    public AddPlaceParent() {
        this.json = new JSONObject();
    }


    public JSONObject getJson() {
        return json;
    }

    protected void showTimePickerDialog(final TextInputEditText input) {
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(AddPlaceParent.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        input.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }
}
