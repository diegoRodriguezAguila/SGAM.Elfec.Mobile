package com.elfec.sgam.view.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.elfec.sgam.view.adapter.widget.TextWatcherAdapter;

/**
 * To change clear icon, set
 *
 * <pre>
 * android:drawableRight="@drawable/custom_icon"
 * </pre>
 */
public class ClearableEditText extends AppCompatEditText implements OnTouchListener, OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

    public interface Listener {
        void didClearText();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable mDrawableClear;
    private Listener listener;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;
    private int[] stateEnabled = new int[]{android.R.attr.state_enabled};
    private int[] stateActivated = new int[]{android.R.attr.state_activated};
    private boolean mIsDrawablePressed;
    private boolean mIsDrawableVisible;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mIsDrawableVisible) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - mDrawableClear.getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDrawableClear.setState(stateActivated);
                    mIsDrawablePressed = true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mDrawableClear.setState(stateEnabled);
                    if(mIsDrawablePressed) {
                        mIsDrawablePressed = false;
                        setText("");
                        if (listener != null) {
                            listener.didClearText();
                        }
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int left = (v.getRight() - mDrawableClear.getIntrinsicWidth());
                    Rect rect = new Rect(left, v.getTop(), v.getRight(),
                            v.getBottom());
                    if (!rect.contains((int) event.getX(),
                            v.getTop() + (int) event.getY())) {
                        mDrawableClear.setState(stateEnabled);
                        mIsDrawablePressed = false;
                    }
                }
                return true;
            }
        }
        return l != null && l.onTouch(v, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    public boolean isNotEmpty(CharSequence str){
        return str!=null && str.length()>0 && !str.toString().isEmpty();
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            setClearIconVisible(isNotEmpty(text));
        }
    }

    private void init() {
        mDrawableClear = getCompoundDrawables()[2];
        if (mDrawableClear == null) {
            mDrawableClear = ContextCompat.getDrawable(getContext(), android.R.drawable.presence_offline);
        }
        mDrawableClear.setBounds(0, 0, mDrawableClear.getIntrinsicWidth(), mDrawableClear.getIntrinsicHeight());
        mIsDrawableVisible = true;
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    protected void setClearIconVisible(boolean visible) {
        if (visible != mIsDrawableVisible) {
            mIsDrawableVisible = visible;
            mDrawableClear.setAlpha(visible ? 255 : 0);
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], mDrawableClear, getCompoundDrawables()[3]);
        }
    }
}
