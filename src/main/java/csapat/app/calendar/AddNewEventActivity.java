package csapat.app.calendar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import csapat.app.BaseCompat;
import csapat.app.R;
import csapat.app.supportFiles.SaveSharedPreference;

import java.util.HashMap;
import java.util.Map;


public class AddNewEventActivity extends BaseCompat {


    private static final String TAG = "ADD_NEW_EVENT_ACT";

    private Event newEvent;
    private TextView newEventYear;
    private TextView newEventStartMonth;
    private TextView newEventStartDay;
    private TextView newEventStartHour;
    private TextView newEventStartMinute;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private boolean switchState = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        //Button startdatePickerButton = findViewById(R.id.btn_pick_start_date);
        Button submitButton = findViewById(R.id.submitBtn);
        /*newEventYear = findViewById(R.id.start_new_event_year);
        newEventStartMonth = findViewById(R.id.start_new_event_month);
        newEventStartDay = findViewById(R.id.start_new_event_day);
        newEventStartHour = findViewById(R.id.event_start_hour);
        newEventStartMinute = findViewById(R.id.event_start_minute);*/

        Switch multipleDaySwitch = findViewById(R.id.multiple_day_switch);
        startDatePicker = findViewById(R.id.startDatePicker);
        startTimePicker = findViewById(R.id.startTimePicker);
        endTimePicker = findViewById(R.id.endTimePicker);
        endDatePicker = findViewById(R.id.endDatePicker);

        multipleDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    switchState = true;
                    endDatePicker.setVisibility(View.VISIBLE);
                    endTimePicker.setVisibility(View.VISIBLE);
                } else {
                    switchState = false;
                    endDatePicker.setVisibility(View.GONE);
                    endTimePicker.setVisibility(View.GONE);
                }
            }
        });

        startDatePicker.setMinDate(System.currentTimeMillis() - 1000);

        endDatePicker.setMinDate(System.currentTimeMillis() - 1000);

        submitButton.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (!switchState) {
                    uploadEvent();
                } else if (checkValidEventRange()) {
                    uploadEvent();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid date range", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        /*startdatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialogFragment().show(getSupportFragmentManager(), "DATE_TAG");
            }
        });*/

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void uploadEvent() {
        String creator = SaveSharedPreference.getAppUser(AddNewEventActivity.this).getFullName();
        TextView eventTitle = findViewById(R.id.event_title_input);

        newEvent = new Event(creator, startDatePicker.getYear(), startDatePicker.getMonth(),
                startDatePicker.getDayOfMonth(), startTimePicker.getHour(), startTimePicker.getMinute(),
                eventTitle.getText().toString(), endDatePicker.getMonth(), endDatePicker.getDayOfMonth(),
                endTimePicker.getHour(), endTimePicker.getMinute());

        if (!switchState) {
            newEvent.setAllEnd(-1);
        }
        Map<String, Object> data = new HashMap<>();

        data.put("creator", newEvent.getCreator());
        data.put("year", newEvent.getYear());
        data.put("startmonth", newEvent.getStartmonth());
        data.put("startday", newEvent.getStartday());
        data.put("starthour", newEvent.getStarthour());
        data.put("startminute", newEvent.getStartminute());
        data.put("endmonth", newEvent.getEndmonth());
        data.put("endday", newEvent.getEndday());
        data.put("endhour", newEvent.getEndhour());
        data.put("endminute", newEvent.getEndminute());
        data.put("title", newEvent.getTitle());


        showProgressDialog();

        db.collection("events")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        hideProgressDialog();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                    }
                });

        Toast toast = Toast.makeText(getApplicationContext(), "Esemény közzétéve", Toast.LENGTH_LONG);
        toast.show();
        //Intent eventListIntent = new Intent(AddNewEventActivity.this, EventListActivity.class);
        //startActivity(eventListIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkValidEventRange() {

        if (startDatePicker.getYear() > endDatePicker.getYear()) return false;
        else {
            if (startDatePicker.getMonth() > endDatePicker.getMonth()) return false;
            else {
                if (startDatePicker.getDayOfMonth() > endDatePicker.getDayOfMonth()) return false;
                else {
                    if (startDatePicker.getDayOfMonth() < endDatePicker.getDayOfMonth())
                        return true;
                    else {
                        if (startTimePicker.getHour() > endTimePicker.getHour()) return false;
                        else {
                            return startTimePicker.getMinute() <= endTimePicker.getMinute();
                        }
                    }
                }
            }
        }
    }


}
