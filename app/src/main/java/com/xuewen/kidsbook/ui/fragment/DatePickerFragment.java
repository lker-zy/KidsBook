package com.xuewen.kidsbook.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by lker_zy on 16-5-27.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public interface OnDateSetListener {
        void onDateSet(int year, int month, int day);
    }

    private OnDateSetListener onDateSetListener;

    public void setOnDateSetListener(OnDateSetListener listener) {
        onDateSetListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(getActivity());
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (onDateSetListener != null) {
            onDateSetListener.onDateSet(year, monthOfYear, dayOfMonth);
        }
    }
}
