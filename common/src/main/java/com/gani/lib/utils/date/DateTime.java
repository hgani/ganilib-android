package com.gani.lib.utils.date;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTime {
  private Calendar calendar;

  private DateTime(Calendar calendar) {
    this.calendar = calendar;
  }

  public DateTime moveDays(int count) {
    calendar.add(Calendar.DAY_OF_MONTH, count);
    return this;
  }

  // Use this for compatibility with server
  public long inSeconds() {
    return calendar.getTimeInMillis() / 1000;
  }

  public DateTime beginningOfToday() {
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND,0);
    return this;
  }

  private DateTime toMillis(long millis) {
    calendar.setTimeInMillis(millis);
    return this;
  }

  public static DateTime now() {
    // See http://stackoverflow.com/questions/6905288/getting-current-datetime-using-calendar-getinstance-vs-new-gregoriancalendar
    return new DateTime(GregorianCalendar.getInstance());
  }

  public static DateTime fromMillis(long millis) {
    return now().toMillis(millis);
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public int getYear() {
    return calendar.get(Calendar.YEAR);
  }

  public int getHour() { return calendar.get(Calendar.HOUR_OF_DAY); }

  public String getShortDayOfWeek() {
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
  }
}
