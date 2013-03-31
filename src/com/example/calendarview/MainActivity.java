package com.example.calendarview;

import com.example.calendarview.CalendarController.EventHandler;
import com.example.calendarview.CalendarController.EventInfo;
import com.example.calendarview.CalendarController.EventType;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity implements EventHandler{
	
	private CalendarController mController;
	Fragment monthFrag;
	Fragment dayFrag;
	private EventInfo event;
	private boolean dayView;
	private long time;
	private long eventID;
	boolean eventView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new ImportEntries().execute(this);
		mController = CalendarController.getInstance(this);
		setContentView(R.layout.cal_layout);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
				
		monthFrag = new MonthByWeekFragment(System.currentTimeMillis(), false);
        ft.replace(R.id.cal_frame, monthFrag).commit();
        mController.registerEventHandler(R.id.cal_frame, (EventHandler) monthFrag);
        
        mController.registerFirstEventHandler(0, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public long getSupportedEventTypes() {
		return EventType.GO_TO | EventType.VIEW_EVENT | EventType.UPDATE_TITLE;
	}

	@Override
	public void handleEvent(EventInfo event) {
		if (event.eventType == EventType.GO_TO) {
			this.event = event;
			dayView = true;
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				dayFrag = new DayFragment(event.startTime.toMillis(true),1);
				ft.replace(R.id.cal_frame, dayFrag).addToBackStack(null).commit();
		}if(event.eventType == EventType.VIEW_EVENT){
			//TODO do something when an event is clicked
					dayView = false;
					eventView = true;
					this.event = event;
//					FragmentTransaction ft = getFragmentManager().beginTransaction();
//					edit = new EditEvent(event.id);
//					ft.replace(R.id.cal_frame, edit).addToBackStack(null).commit();

			
		}
		
	}

	@Override
	public void eventsChanged() {
	
	}

}
