ExtendedCalendarView
====================

This project is meant for people who want to display a calendar view and show that there are events on certain days.
What I did was pull out the calendar view from Google's Calendar application found here 

https://github.com/android/platform_packages_apps_calendar.

Implementation is not the easiest though as there is a lot to it but I hope to filter out stuff that is not needed to try to make it simpler

A quick run though of how to use it:

in your activity before you set the content view you need to create a Calendar Controller

mController = CalendarController.getInstance(this);
