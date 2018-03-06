package packi.day.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog

import com.samsistemas.calendarview.widget.CalendarView

import org.joda.time.MonthDay

import java.util.Calendar
import java.util.Date
import java.util.Locale

import packi.day.R
import packi.day.ui.ActivityMain

class CustomDateSearchBuilder(private val activity: ActivityMain) :
        AlertDialog(activity, R.style.MyAlertDialogStyle),
        DialogInterface.OnDismissListener,
        CalendarView.OnDateSelectedListener {

    private val handler = Handler()

    private val runnable = {
        if (isShowing) {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_calendar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        calendarView?.setFirstDayOfWeek(Calendar.MONDAY)
        calendarView?.setIsOverflowDateVisible(true)
        calendarView?.setDateAsSelected(Date(System.currentTimeMillis()))
        calendarView?.refreshCalendar(Calendar.getInstance(Locale.getDefault()))
        calendarView?.setNextButtonColor(R.color.accent)
        calendarView?.setBackButtonColor(R.color.accent)
        calendarView?.setOnDateSelectedListener(this)

        setView(calendarView)
        setOnDismissListener(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        handler.removeCallbacks(runnable)
    }

    override fun onDateSelected(date: Date) {
        activity.showFocus(MonthDay.fromDateFields(date))
        handler.postDelayed(runnable, 200)
    }

}
