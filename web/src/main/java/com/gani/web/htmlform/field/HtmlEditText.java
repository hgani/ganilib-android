package com.gani.web.htmlform.field;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.gani.lib.ui.view.GEditText;
import com.gani.web.R;
import com.gani.web.htmlform.HtmlFieldValidator;
import com.gani.web.htmlform.HtmlForm;
import com.gani.web.htmlform.HtmlFormValidatable;

import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//public class HTMLEditText extends EditText implements HtmlFormValidatable {
public class HtmlEditText extends GEditText implements HtmlFormValidatable {
  private final Element mField;

//  private static final String TAG = HTMLEditText.class.getName();

  private static final String ATTR_PLACEHOLDER = "placeholder";
  private static final String ATTR_NAME = "name";

  private static final String DATETIME_PICKER_TYPE = "datetime_picker";
  private static final String DATE_PICKER_TYPE = "date_picker";
  private static final String TIME_PICKER_TYPE = "time_picker";

  private Validator validator;

//  public HTMLEditText(Context context, Element field, RelativeLayout.LayoutParams params) {
//    super(context);
//
//    this.mField = field;
//    this.validator = new Validator(field);
//    setLayoutParams(params);
//    setDefaultListeners();
//  }

  public HtmlEditText(Context context, HtmlForm form, Element field) {
    super(context);

    this.mField = field;
    this.validator = new Validator(form, field);

    size(ViewGroup.LayoutParams.MATCH_PARENT, null);

    setDefaultListeners();
  }


  ///// Validation /////

  @Override
  public boolean validate() {
    return validator.run(getText().toString(), this);
  }

  @Override
  public boolean isValid() {
    return validator.isValid();
  }

  private class Validator extends HtmlFieldValidator implements TextWatcher {
    private static final String DATE_FORMAT = "\\d{4}-\\d{2}-\\d{2}";
    private static final String TIME_FORMAT = "\\d{2}:\\d{2}";
    private static final String DATETIME_FORMAT = DATE_FORMAT + "\\s" + TIME_FORMAT;

    Validator(HtmlForm form, Element field) {
      super(form, field);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      validate();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      // Nothing to do
    }

    @Override
    public void afterTextChanged(Editable editable) {
      // Nothing to do
    }

    @Override
    protected void validateSpecific(String value) {
      if (mField.classNames().contains(DATE_PICKER_TYPE)) {
        validateDateTimeFormat(value, DATE_FORMAT);
      } else if (mField.classNames().contains(TIME_PICKER_TYPE)) {
        validateDateTimeFormat(value, TIME_FORMAT);
      } else if (mField.classNames().contains(DATETIME_PICKER_TYPE)) {
        validateDateTimeFormat(value, DATETIME_FORMAT);
      }
    }

    private void validateDateTimeFormat(String value, String dateFormat) {
      Pattern r = Pattern.compile("^" + dateFormat + "$");
      Matcher m = r.matcher(value);

      if (!m.find()) {
        addErrorMessage("Invalid");
      }
    }
  }

  /////


  ///// Formatting /////

  private void setDefaultListeners() {
//    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.WRAP_CONTENT
//    );
//    params.setMargins(0, 0, 0, 25);
//
//    setLayoutParams(params);
    setPadding(20, 20, 20, 20);
    setBackgroundResource(R.drawable.edit_text_state);
    setTag(mField.attr(ATTR_NAME));
    setHint(mField.attr(ATTR_PLACEHOLDER));
    setText(mField.val());

    if (mField.classNames().contains(DATE_PICKER_TYPE)) {
      setOnTouchListener(new DatePickerOnTouchEvent());
    } else if (mField.classNames().contains(TIME_PICKER_TYPE)) {
      setOnTouchListener(new TimePickerOnTouchEvent());
    } else if (mField.classNames().contains(DATETIME_PICKER_TYPE)) {
      setOnTouchListener(new DateTimePickerOnTouchEvent());
    }

    addTextChangedListener(validator);
  }

  private DatePickerDialog createDatePickerDialog(
      Calendar calendar,
      DatePickerDialog.OnDateSetListener onDateSetListener) {
    return new DatePickerDialog(
        getContext(),
        onDateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    );
  }

  private TimePickerDialog createTimePickerDialog(
      Calendar calendar,
      TimePickerDialog.OnTimeSetListener onTimeSetListener) {
    return new TimePickerDialog(
        getContext(),
        onTimeSetListener,
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    );
  }

  private class DatePickerOnTouchEvent implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
        final HtmlEditText htmlEditText = (HtmlEditText) view;
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = createDatePickerDialog(
            calendar,
            new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker view, int year, int month, int day) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(year, month, day);
                htmlEditText.setText(format.format(calendar.getTime()));
              }
            }
        );

        datePicker.show();
      }

      return false;
    }
  }

  private class TimePickerOnTouchEvent implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
        final HtmlEditText htmlEditText = (HtmlEditText) view;
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePicker = createTimePickerDialog(
            calendar,
            new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker view, int hour, int minute) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, hour, minute);
                htmlEditText.setText(format.format(calendar.getTime()));
              }
            }
        );

        timePicker.show();
      }

      return false;
    }
  }

  private class DateTimePickerOnTouchEvent implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
        final HtmlEditText htmlEditText = (HtmlEditText) view;
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = createDatePickerDialog(
            calendar,
            new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
              }
            }
        );

        datePicker.setOnDismissListener(new DatePickerDialog.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
            TimePickerDialog timePicker = createTimePickerDialog(
                calendar,
                new TimePickerDialog.OnTimeSetListener() {
                  @Override
                  public void onTimeSet(TimePicker view, int hour, int minute) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        hour,
                        minute
                    );
                    htmlEditText.setText(format.format(calendar.getTime()));
                  }
                }
            );

            timePicker.show();
          }
        });

        datePicker.show();

      }

      return false;
    }
  }

  /////
}
