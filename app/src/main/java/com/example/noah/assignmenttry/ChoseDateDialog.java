package com.example.noah.assignmenttry;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;

public class ChoseDateDialog extends DialogFragment {

    /* Instance Field */
    private CalendarView calendarView;

    private int currentDay;
    private int currentMonth;
    private int currentYear;
    private Calendar date = Calendar.getInstance();

    public ChoseDateDialog() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.chose_date, container);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.chose_date, null);

        // Set the handle of the component
        calendarView = view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currentDay = dayOfMonth;
                currentMonth = month + 1;
                currentYear = year;
            }
        });

        builder.setView(view).setNegativeButton("Set Date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mAddImageDate != null) {
                    if (currentDay != 0) {
                        mAddImageDate.onDateChosen(currentMonth, currentDay, currentYear);
                    }else {
                        // Now
                        mAddImageDate.onDateChosen(date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE), date.get(Calendar.YEAR));
                    }
                }
            }
        });
        return builder.create();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * The CallBack for "GridFragment"
     * TODO Need to decide the exact words
     */
    // CallBack
    public interface addImageDate {
        void onDateChosen(int month, int day, int year);
    }

    private addImageDate mAddImageDate;

    public void setOnAddImageDate(addImageDate mAddImageDate) {
        this.mAddImageDate = mAddImageDate;
    }
}
