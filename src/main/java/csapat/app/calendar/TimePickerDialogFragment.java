package csapat.app.calendar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private OnTimeSelectedListener onTimeSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(!(context instanceof OnTimeSelectedListener)) {
            throw new RuntimeException("The activity does not implement the" +
                    "OnDateSelectedListener interface");
        }

        onTimeSelectedListener = (OnTimeSelectedListener) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        TimePickerDialog dialog;
        dialog = new TimePickerDialog(getActivity(), this, hour,minutes, true);

        return dialog;
    }


    public interface OnTimeSelectedListener {
        void onTimeSelected(int hourOfDay, int minute);
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        onTimeSelectedListener.onTimeSelected(hourOfDay, minute);
    }
}
