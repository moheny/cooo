package com.example.guycohen.coacher;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;

import static com.example.guycohen.coacher.R.array.group_from_event;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    //the date we set from the Dialog
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    //Calendar Account Name
    private static final String MY_ACCOUNT_NAME = "Home Up" ;
    //SharedPref save/read
    private SharedPreferences pref;
    //boolean for first time load
    private boolean isCalendarHasBeenCreated;
    //Calendar View
    private MaterialCalendarView calendar;
    //Buttons
    private Button btnLoadCal,btnGroup;
    //Arraylist to images from group choice
    private ArrayList<Integer> groupImages;
    //TextViews
    private TextView tvGroupSelected;
    private static TextView tvCalendarDate;
    //Calendar data to send
    static int year, month, day;
    public AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initEvents();
        pref = getSharedPreferences(Constants.MEMORY_FILE_ON_DEVICE, Context.MODE_PRIVATE);

        //Check if the user created a calendar, if not we going to create one
        //and save the Calendar ID To memory
        checkIfWeCreatedCalendar();
        //working method to add to calender
        anotherOne(getCalendarIId(),year,month,day);



    }


    private void initEvents() {
        //We load the calendar to chose a date from
        btnLoadCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleCalendarDialogFragment().show(getSupportFragmentManager(), "test-simple-calendar");

            }
        });

        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setTitle("group choose");
                builder.setItems(group_from_event, MainActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                dialog = builder.create();
                dialog.show();
            }
        });


    }

    private void setCalendar() {

        calendar.setFirstDayOfWeek(1);
        calendar.setOnDateChangedListener(new OnDateChangedListener() {
            @Override
            public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                int calDay = calendarDay.getDay();
                int calMonth = calendarDay.getMonth();
                int calYear = calendarDay.getYear();

            }
        });

    }
   // @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  //  private void setCalendar() {

//        calendar.setShowWeekNumber(true);
//        calendar.setFirstDayOfWeek(1);
//        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
//
//
//        //The background color for the selected week.
//        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
//        //sets the color for the dates of an unfocused month.
//        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
//        //sets the color for the separator line between weeks.
//        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
//        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
//        calendar.setSelectedDateVerticalBar(R.color.darkgreen);
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
//
//
//            }
//        });
//
   // }

    private void initLayout() {
        //TextViews
        tvGroupSelected = (TextView) findViewById(R.id.tvGroupSelected);
        tvCalendarDate = (TextView) findViewById(R.id.tvCalendarDate);
        //Buttons
        btnLoadCal = (Button) findViewById(R.id.btnLoadCal);
        btnGroup = (Button) findViewById(R.id.btnGroup);
        //ArrayList
        groupImages = new ArrayList<Integer>();
        groupImages.add(R.mipmap.ic_launcher);
        groupImages.add(R.mipmap.ic_launcher);
        groupImages.add(R.mipmap.ic_launcher);

    }


    public  void anotherOne(final long theIdOfCalendar, final int year, final int month, final int day){
        // must be off the UI thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long calID = theIdOfCalendar;
                long startMillis = 0;
                long endMillis = 0;
                java.util.Calendar beginTime = java.util.Calendar.getInstance();
                beginTime.set(year, month, day);
                startMillis = beginTime.getTimeInMillis();
                java.util.Calendar endTime = java.util.Calendar.getInstance();
                endTime.set(year, month, day);
                endMillis = endTime.getTimeInMillis();

                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, endMillis);
                values.put(CalendarContract.Events.ALL_DAY,true);
                values.put(CalendarContract.Events.TITLE, "success");
                values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
                values.put(CalendarContract.Events.CALENDAR_ID, calID);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
                long eventID = Long.parseLong(uri.getLastPathSegment());
//
// ... do something with event ID
//
//
            }
        });
        thread.start();
    }

    private void moreCalendar() {

        //Calendar beginTime = Calendar.getInstance();
        java.util.Calendar beginTime = java.util.Calendar.getInstance();
        beginTime.set(2015,9,1,11,11,11);
      //  beginTime.set(ScheduleActivity.Year, ScheduleActivity.Month, ScheduleActivity.DayOfMonth, hourOfDay, minute, 0);
        java.util.Calendar endTime = java.util.Calendar.getInstance();

        endTime.set(2015, 9, 1, 11, 40, 0);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                //.putExtra(CalendarContract.Events._ID, 5)
                //.putExtra("calendar_id", 5)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Run Time")
                .putExtra(CalendarContract.Events.DESCRIPTION, "[Step 1")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

    private void addCalendarEventTest() {

        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 5);

        event.put(CalendarContract.Events.TITLE, "titleTest");
        event.put(CalendarContract.Events.DESCRIPTION, "DescriptionTest");
        event.put(CalendarContract.Events.EVENT_LOCATION, "LocationTest");

        event.put(CalendarContract.Events.DTSTART, "10:00");
        event.put(CalendarContract.Events.DTEND, "11:00");
        event.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true

        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

        Uri baseUri;
        if (Build.VERSION.SDK_INT >= 8) {
            baseUri = Uri.parse("content://com.android.calendar/events");
        } else {
            baseUri = Uri.parse("content://calendar/events");
        }

        this.getContentResolver().insert(baseUri, event);

    }

    /**
     * Get the id of Calendar and save to memory
     */

    private long getCalendarIId (){
        String idOfCalendar = "Home Up";

        CalendarProvider provider = new CalendarProvider(this);
        List<me.everything.providers.android.calendar.Calendar> calendars = provider.getCalendars().getList();
        //let's run on calendars
        for (Calendar cal : calendars) {
            Log.d("allCalNames", cal.accountName);
            if (cal.accountName.compareTo(idOfCalendar) == 0 ){
                Log.d("theIdIs", String.valueOf(cal.id));

            return cal.id;
            }
        }
        return 0;
    }
    //Todo: delete it , an rewrite the method
    private void getCalendarId() {
        String idOfCalendar = "Home Up";

        CalendarProvider provider = new CalendarProvider(this);
        List<me.everything.providers.android.calendar.Calendar> calendars = provider.getCalendars().getList();
        //let's run on calendars
        for (Calendar cal : calendars) {
            Log.d("allCalNames", cal.accountName);
           if (cal.accountName.compareTo(idOfCalendar) == 0 ){
               Log.d("theIdIs", String.valueOf(cal.id));

               SharedPreferences.Editor editor = pref.edit();
               editor.putString(Constants.ID_OF_CALENDAR, String.valueOf(cal.id));
               editor.commit();
           }
        }
 }


    private void checkIfWeCreatedCalendar(){
        //let's check if we created an calendar already
        isCalendarHasBeenCreated = pref.getBoolean(Constants.IS_CALENDAR_HAS_BEEN_CREATED, false);
        if (isCalendarHasBeenCreated == false){
            //we didn't create a calendar, let's create one
            createNewLocalCalendar();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private  void createNewLocalCalendar(){
        ContentValues values = new ContentValues();
        //Name of the calendar -> i've created a Constant
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                MY_ACCOUNT_NAME);
        //We must keep it local calendar
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);

        values.put(
                CalendarContract.Calendars.NAME,
                "Home Up");
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "Home Up");
        //Color of the calendar // TODO: change the color
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                0xffff0000);

        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        /*
        values.put(
                CalendarContract.Calendars.OWNER_ACCOUNT,
                "some.account@googlemail.com");
                */
     //Timezone, even tho we probably going to use the default
        values.put(
                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
                "Asia/Jerusalem");
        //we want to sync this calendar and store events
        values.put(
                CalendarContract.Calendars.SYNC_EVENTS,
                1);

        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "Home Up");
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri =
                getContentResolver().insert(builder.build(), values);

        pref = getSharedPreferences(Constants.MEMORY_FILE_ON_DEVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_CALENDAR_HAS_BEEN_CREATED, true);
        editor.commit();

        //Let's save the calendar Id To Memory
        getCalendarId();
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showAllCalendarsInDevice(){
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                // …
            } while (calCursor.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dialog
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        String[] fromResources = getResources().getStringArray(R.array.group_from_event);
        tvGroupSelected.setText(fromResources[which]);

    }

    public static class SimpleCalendarDialogFragment extends DialogFragment implements OnDateChangedListener {

        private TextView textView;
        private Button btnCloseCal;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_basic, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            textView = (TextView) view.findViewById(R.id.textView);
            btnCloseCal = (Button) view.findViewById(R.id.btnCloseCal);
            btnCloseCal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvCalendarDate.setText("" + year + "-" + month + "-" + day);
                    getDialog().dismiss();
                }
            });


            MaterialCalendarView widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);

            widget.setOnDateChangedListener(this);
        }

        @Override
        public void onDateChanged(@NonNull MaterialCalendarView widget, CalendarDay date) {
            textView.setText(FORMATTER.format(date.getDate()));

             year = date.getYear();
             month = date.getMonth();
             day = date.getMonth();

            Log.d("allDates", "year:" + year + "month:" + month + "day" + day);
        }

    }

}
