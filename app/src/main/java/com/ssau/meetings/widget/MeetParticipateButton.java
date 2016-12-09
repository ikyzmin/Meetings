package com.ssau.meetings.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.ssau.meetings.R;


public class MeetParticipateButton extends AppCompatButton {

    private boolean isParticipated;
    private int[] STATE_PARTICIPATED = {R.attr.state_participated};

    public MeetParticipateButton(Context context) {
        this(context, null);
    }

    public MeetParticipateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeetParticipateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setParticipated(boolean isParticipated) {
        this.isParticipated = isParticipated;
        if (isParticipated) {
            setText(R.string.meet_participating);
        }else{
            setText(R.string.meet_not_interested);
        }
            drawableStateChanged();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (isParticipated) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, STATE_PARTICIPATED);
            invalidate();
            return drawableState;
        }else{
            invalidate();
            return super.onCreateDrawableState(extraSpace);
        }

    }
}
