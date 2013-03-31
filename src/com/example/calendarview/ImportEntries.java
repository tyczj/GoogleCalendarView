package com.example.calendarview;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.text.format.DateUtils;
import android.text.format.Time;

public class ImportEntries extends AsyncTask<Context, Void, Void> {
	
	Context context;

	@Override
	protected Void doInBackground(Context... arg0) {
		context = arg0[0];
		importEntries();
		return null;
	}
	
//	private Long[] checkTimes(long id){
//		Cursor cur = null;
// 		Long[] times = new Long[2];
//		java.util.Calendar c = java.util.Calendar.getInstance();
//	 	long now = c.getTimeInMillis();
//			String[] projection = {Instances.BEGIN,Instances.END};
//			Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
//			ContentUris.appendId(builder,now - DateUtils.YEAR_IN_MILLIS);
//			ContentUris.appendId(builder,now + DateUtils.YEAR_IN_MILLIS);
//			cur = context.getContentResolver().query(builder.build(),projection,Instances.EVENT_ID + "=" + id
//					,null,null);
//			if(cur != null && cur.moveToFirst()){
////	 			do{
//	 				long start = Long.parseLong(cur.getString(0));
//	 				long end = cur.getLong(1);
////	 				long newStart = start - (Long.parseLong(reminders) * (60*1000));
//	 				long current = System.currentTimeMillis();
////	 				if(start > current){
//	 					times[0] = start;
//	 					times[1] = end;
////	 					break;
////	 				}
////	 			}while(cur.moveToNext());
//			}
//		
//		cur.close();
//		return times;
//	}
	
	private void importEntries(){	
//		Long[] times = new Long[2];
//		if(add){
//			long time = System.currentTimeMillis();
			String[] eventHolder = {Events._ID,Events.CALENDAR_ID,Events.TITLE,Events.EVENT_LOCATION,Events.DESCRIPTION,Events.DTSTART,Events.DTEND};
			Cursor c = context.getContentResolver().query(Events.CONTENT_URI,eventHolder,null,null,null);
			if(c != null && c.moveToFirst()){
				do{
					long eventID = c.getLong(0);
		        	String calID = c.getString(1);
		            String dbEvent = c.getString(2);
		            String dblocation = c.getString(3);
		            String dbdesc = c.getString(4);
//		            times = checkTimes(eventID);
		            Cursor e = context.getContentResolver().query(CalendarProvider.CONTENT_URI,new String[] {CalendarProvider.CALENDAR_ID},
		            		CalendarProvider.EVENT_ID+"=?", new String[] {String.valueOf(eventID)}, null);
		            if(e == null || !e.moveToFirst()){
		            	ContentValues values = new ContentValues();
		    			Calendar cal = Calendar.getInstance();
		    			TimeZone tz = TimeZone.getDefault();
		    			cal.setTimeZone(tz);
		    			long offset2 = tz.getRawOffset();
		    			long offset3 = (tz.getOffset(c.getLong(5))/1000) % 60;
		    			long offset = 3600000*4;
		    			cal.setTimeInMillis(c.getLong(5));
		    			
		    			long offset4 = TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(c.getLong(5)));
		    			
		    			int hours = cal.get(Calendar.HOUR_OF_DAY);
		    			int min = cal.get(Calendar.MINUTE);
		    			int startDay = Time.getJulianDay(c.getLong(5),offset4);
		    			int endDay = Time.getJulianDay(c.getLong(6), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(c.getLong(6))));
		    			int startMin = (cal.get(Calendar.HOUR_OF_DAY)*60)+cal.get(Calendar.MINUTE);
		    			cal = Calendar.getInstance();
		    			cal.setTimeInMillis(c.getLong(6));
		    			int endMin = (cal.get(Calendar.HOUR_OF_DAY)*60)+cal.get(Calendar.MINUTE);
		    			values.put(CalendarProvider.DESCRIPTION, dbdesc);
		    			values.put(CalendarProvider.END, c.getLong(6));
		    			values.put(CalendarProvider.START, c.getLong(5));
		    			values.put(CalendarProvider.EVENT, dbEvent);
		    			values.put(CalendarProvider.EVENT_ID, eventID);
		    			values.put(CalendarProvider.LOCATION, dblocation);
		    			values.put(CalendarProvider.CALENDAR_ID, calID);
		    			values.put(CalendarProvider.START_DAY, startDay);
		    			values.put(CalendarProvider.END_DAY, endDay);
		    			values.put(CalendarProvider.START_TIME, startMin);
		    			values.put(CalendarProvider.END_TIME, endMin);
		            	context.getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
		            }
		            e.close();
				}while(c.moveToNext());
	    		
			}
			c.close();
//		}else{
//			String[] eventHolder = {Events._ID,Events.CALENDAR_ID,Events.TITLE,Events.EVENT_LOCATION,Events.DESCRIPTION,Events.DTSTART,Events.DTEND};
//			Cursor c = getContentResolver().query(Events.CONTENT_URI,eventHolder,null,null,null);
//			int value = c.getCount();
//			int count = 0;
//			if(c != null && c.moveToFirst()){
//				do{
//					count++;
//                	long eventID = c.getLong(0);
//                	String calID = c.getString(1);
//                    String dbEvent = c.getString(2);
//                    String dblocation = c.getString(3);
//                    String dbdesc = c.getString(4);
//                    String reminders = getReminders(eventID);
//                    if(reminders == null){
//                    	reminders = "0";
//                    }
//                    times = checkTimes(eventID,reminders);
//                    
//                    if(times[0] != null && times[1] != null){
//                    	long alert = Long.parseLong(reminders) * (60*1000);
//        	            long trueStart = times[0] - alert;
//                    	Cursor e = getContentResolver().query(CalendarProvider.CONTENT_URI, new String[] {CalendarProvider.ID},
//        	            		CalendarProvider.EVENT_ID+"=?",new String[] {String.valueOf(eventID)},null);
//                    	if(e != null && e.moveToFirst()){
//                    		ContentValues values = new ContentValues();
//        	    			values.put(CalendarProvider.DESCRIPTION, dbdesc);
//        	    			values.put(CalendarProvider.END, times[1]);
//        	    			values.put(CalendarProvider.START, times[0]);
//        	    			values.put(CalendarProvider.EVENT, dbEvent);
//        	    			values.put(CalendarProvider.EVENT_ID, eventID);
//        	    			values.put(CalendarProvider.LOCATION, dblocation);
//        	    			values.put(CalendarProvider.CALENDAR_ID, calID);
//        	    			values.put(CalendarProvider.REMINDERS, reminders);
//        	    			values.put(CalendarProvider.START_WITH_REMINDER, trueStart);
//        	    			getContentResolver().update(CalendarProvider.CONTENT_URI, values,
//        	    					CalendarProvider.EVENT_ID+"=?", new String[] {String.valueOf(eventID)});
//                    	}else{
//                    		ContentValues values = new ContentValues();
//                    		values.put(CalendarProvider.BLINK, "Default");
//        	    			values.put(CalendarProvider.COLOR, "Default");
//        	    			values.put(CalendarProvider.DESCRIPTION, dbdesc);
//        	    			values.put(CalendarProvider.END, times[1]);
//        	    			values.put(CalendarProvider.START, times[0]);
//        	    			values.put(CalendarProvider.EVENT, dbEvent);
//        	    			values.put(CalendarProvider.EVENT_ID, eventID);
//        	    			values.put(CalendarProvider.ICON, "Default");
//        	    			values.put(CalendarProvider.VIBRATE, "Normal");
//        	    			values.put(CalendarProvider.LOCATION, dblocation);
//        	    			values.put(CalendarProvider.CALENDAR_ID, calID);
//        	    			values.put(CalendarProvider.REMINDERS, reminders);
//        	    			values.put(CalendarProvider.START_WITH_REMINDER, trueStart);
//        	            	getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
//                    	}
//                    	e.close();
//                    }
//				}while(c.moveToNext());
//			}
//			c.close();
//		}
		
	}

}
