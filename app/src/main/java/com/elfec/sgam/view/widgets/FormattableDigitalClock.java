package com.elfec.sgam.view.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.DigitalClock;

import org.joda.time.DateTime;

import java.text.ParseException;

/**
 * Clase {@link DigitalClock} pero que es formateable y no está deprecado
 */
@SuppressWarnings("deprecation")
public class FormattableDigitalClock extends DigitalClock {
    DateTime mDate = DateTime.now();
    private boolean mIsFormatting;
    private java.text.DateFormat mDefaultFormat = DateFormat.getTimeFormat(getContext());
    private String mFormat = "HH:mm";

    public FormattableDigitalClock(Context context) {
        super(context);
    }

    public FormattableDigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTextChanged(@Nullable CharSequence text, int start, int lenghtBefore, int lenghtAfter){
        if(!mIsFormatting && text!=null && !text.equals("")){
            mIsFormatting = true;
            try {
                mDate.withMillis(mDefaultFormat.parse(text.toString()).getTime());
                setText(mDate.toString(mFormat));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mIsFormatting = false;
        }
    }

    public String getmFormat() {
        return mFormat;
    }

    public void setmFormat(String mFormat) {
        this.mFormat = mFormat;
    }

}
