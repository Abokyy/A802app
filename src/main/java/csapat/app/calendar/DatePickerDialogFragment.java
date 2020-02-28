package csapat.app.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private OnDateSelectedListener onDateSelectedListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof OnDateSelectedListener)) {
            throw new RuntimeException("The activity does not implement the" +
                    "OnDateSelectedListener interface");
        }

        onDateSelectedListener = (OnDateSelectedListener) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(),this,
                year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        onDateSelectedListener.onDateSelected(year, month, day);
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }
}
