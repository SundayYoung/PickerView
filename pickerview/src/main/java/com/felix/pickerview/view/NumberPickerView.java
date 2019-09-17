package com.felix.pickerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.widget.ScrollerCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.felix.pickerview.R;

import java.util.List;

public class NumberPickerView extends View {
    private static final int DEFAULT_TEXT_COLOR_NORMAL = -13421773;
    private static final int DEFAULT_TEXT_COLOR_SELECTED = -695533;
    private static final int DEFAULT_TEXT_SIZE_NORMAL_SP = 14;
    private static final int DEFAULT_TEXT_SIZE_SELECTED_SP = 16;
    private static final int DEFAULT_TEXT_SIZE_HINT_SP = 14;
    private static final int DEFAULT_MARGIN_START_OF_HINT_DP = 8;
    private static final int DEFAULT_MARGIN_END_OF_HINT_DP = 8;
    private static final int DEFAULT_DIVIDER_COLOR = -695533;
    private static final int DEFAULT_DIVIDER_HEIGHT = 2;
    private static final int DEFAULT_DIVIDER_MARGIN_HORIZONTAL = 0;
    private static final int DEFAULT_SHOW_COUNT = 3;
    private static final int DEFAULT_ITEM_PADDING_DP_H = 5;
    private static final int DEFAULT_ITEM_PADDING_DP_V = 2;
    private static final int HANDLER_WHAT_REFRESH = 1;
    private static final int HANDLER_WHAT_LISTENER_VALUE_CHANGED = 2;
    private static final int HANDLER_WHAT_REQUEST_LAYOUT = 3;
    private static final int HANDLER_INTERVAL_REFRESH = 32;
    private static final int DEFAULT_INTERVAL_REVISE_DURATION = 300;
    private static final int DEFAULT_MIN_SCROLL_BY_INDEX_DURATION = 300;
    private static final int DEFAULT_MAX_SCROLL_BY_INDEX_DURATION = 600;
    private static final String TEXT_ELLIPSIZE_START = "start";
    private static final String TEXT_ELLIPSIZE_MIDDLE = "middle";
    private static final String TEXT_ELLIPSIZE_END = "end";
    private static final boolean DEFAULT_SHOW_DIVIDER = true;
    private static final boolean DEFAULT_WRAP_SELECTOR_WHEEL = true;
    private static final boolean DEFAULT_CURRENT_ITEM_INDEX_EFFECT = false;
    private static final boolean DEFAULT_RESPOND_CHANGE_ON_DETACH = false;
    private static final boolean DEFAULT_RESPOND_CHANGE_IN_MAIN_THREAD = true;
    private static final int SUB_START = 0;
    private static final int SUB_END = 8;
    private int mTextColorNormal = -13421773;
    private int mTextColorSelected = -695533;
    private int mTextColorHint = -695533;
    private int mTextSizeNormal = 0;
    private int mTextSizeSelected = 0;
    private int mTextSizeHint = 0;
    private int mWidthOfHintText = 0;
    private int mWidthOfAlterHint = 0;
    private int mMarginStartOfHint = 0;
    private int mMarginEndOfHint = 0;
    private int mItemPaddingVertical = 0;
    private int mItemPaddingHorizontal = 0;
    private int mDividerColor = -695533;
    private int mDividerHeight = 2;
    private int mDividerMarginL = 0;
    private int mDividerMarginR = 0;
    private int mShowCount = 3;
    private int mDividerIndex0 = 0;
    private int mDividerIndex1 = 0;
    private int mMinShowIndex = -1;
    private int mMaxShowIndex = -1;
    private int mMinValue = 0;
    private int mMaxValue = 0;
    private int mMaxWidthOfDisplayedValues = 0;
    private int mMaxHeightOfDisplayedValues = 0;
    private int mMaxWidthOfAlterArrayWithMeasureHint = 0;
    private int mMaxWidthOfAlterArrayWithoutMeasureHint = 0;
    private int mPrevPickedIndex = 0;
    private int mMiniVelocityFling = 150;
    private int mScaledTouchSlop = 8;
    private String mHintText;
    private String mTextEllipsize;
    private String mEmptyItemHint;
    private String mAlterHint;
    private int mTextColorLeve2 = -13421773;
    private float mFriction = 1.0F;
    private float mTextSizeNormalCenterYOffset = 0.0F;
    private float mTextSizeSelectedCenterYOffset = 0.0F;
    private float mTextSizeHintCenterYOffset = 0.0F;
    private boolean mShowDivider = true;
    private boolean mWrapSelectorWheel = true;
    private boolean mCurrentItemIndexEffect = false;
    private boolean mHasInit = false;
    private boolean mWrapSelectorWheelCheck = true;
    private boolean mPendingWrapToLinear = false;
    private boolean mRespondChangeOnDetach = false;
    private boolean mRespondChangeInMainThread = true;
    private ScrollerCompat mScroller;
    private VelocityTracker mVelocityTracker;
    private Paint mPaintDivider = new Paint();
    private TextPaint mPaintText = new TextPaint();
    private Paint mPaintHint = new Paint();
    private String[] mDisplayedValues;
    private CharSequence[] mAlterTextArrayWithMeasureHint;
    private CharSequence[] mAlterTextArrayWithoutMeasureHint;
    private HandlerThread mHandlerThread;
    private Handler mHandlerInNewThread;
    private Handler mHandlerInMainThread;
    private NumberPickerView.OnValueChangeListenerRelativeToRaw mOnValueChangeListenerRaw;
    private NumberPickerView.OnValueChangeListener mOnValueChangeListener;
    private NumberPickerView.OnScrollListener mOnScrollListener;
    private NumberPickerView.OnValueChangeListenerInScrolling mOnValueChangeListenerInScrolling;
    private int mScrollState = 0;
    private int mInScrollingPickedOldValue;
    private int mInScrollingPickedNewValue;
    private int mNotWrapLimitYTop;
    private int mNotWrapLimitYBottom;
    private float downYGlobal = 0.0F;
    private float downY = 0.0F;
    private float currY = 0.0F;
    private boolean mFlagMayPress = false;
    private int mViewWidth;
    private int mViewHeight;
    private int mItemHeight;
    private float dividerY0;
    private float dividerY1;
    private float mViewCenterX;
    private int mCurrDrawFirstItemIndex = 0;
    private int mCurrDrawFirstItemY = 0;
    private int mCurrDrawGlobalY = 0;
    private int mSpecModeW = 0;
    private int mSpecModeH = 0;

    public NumberPickerView(Context context) {
        super(context);
        this.init(context);
    }

    public NumberPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initAttr(context, attrs);
        this.init(context);
    }

    public NumberPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initAttr(context, attrs);
        this.init(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerView);
            int n = a.getIndexCount();

            for(int i = 0; i < n; ++i) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.NumberPickerView_npv_ShowCount) {
                    this.mShowCount = a.getInt(attr, 3);
                } else if (attr == R.styleable.NumberPickerView_npv_DividerColor) {
                    this.mDividerColor = a.getColor(attr, -695533);
                } else if (attr == R.styleable.NumberPickerView_npv_textColorLevel2) {
                    this.mTextColorLeve2 = a.getColor(attr, -13421773);
                } else if (attr == R.styleable.NumberPickerView_npv_DividerHeight) {
                    this.mDividerHeight = a.getDimensionPixelSize(attr, 2);
                } else if (attr == R.styleable.NumberPickerView_npv_DividerMarginLeft) {
                    this.mDividerMarginL = a.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.NumberPickerView_npv_DividerMarginRight) {
                    this.mDividerMarginR = a.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.NumberPickerView_npv_TextArray) {
                    this.mDisplayedValues = this.convertCharSequenceArrayToStringArray(a.getTextArray(attr));
                } else if (attr == R.styleable.NumberPickerView_npv_TextColorNormal) {
                    this.mTextColorNormal = a.getColor(attr, -13421773);
                } else if (attr == R.styleable.NumberPickerView_npv_TextColorSelected) {
                    this.mTextColorSelected = a.getColor(attr, -695533);
                } else if (attr == R.styleable.NumberPickerView_npv_TextColorHint) {
                    this.mTextColorHint = a.getColor(attr, -695533);
                } else if (attr == R.styleable.NumberPickerView_npv_TextSizeNormal) {
                    this.mTextSizeNormal = a.getDimensionPixelSize(attr, this.sp2px(context, 14.0F));
                } else if (attr == R.styleable.NumberPickerView_npv_TextSizeSelected) {
                    this.mTextSizeSelected = a.getDimensionPixelSize(attr, this.sp2px(context, 16.0F));
                } else if (attr == R.styleable.NumberPickerView_npv_TextSizeHint) {
                    this.mTextSizeHint = a.getDimensionPixelSize(attr, this.sp2px(context, 14.0F));
                } else if (attr == R.styleable.NumberPickerView_npv_MinValue) {
                    this.mMinShowIndex = a.getInteger(attr, 0);
                } else if (attr == R.styleable.NumberPickerView_npv_MaxValue) {
                    this.mMaxShowIndex = a.getInteger(attr, 0);
                } else if (attr == R.styleable.NumberPickerView_npv_WrapSelectorWheel) {
                    this.mWrapSelectorWheel = a.getBoolean(attr, true);
                } else if (attr == R.styleable.NumberPickerView_npv_ShowDivider) {
                    this.mShowDivider = a.getBoolean(attr, true);
                } else if (attr == R.styleable.NumberPickerView_npv_HintText) {
                    this.mHintText = a.getString(attr);
                } else if (attr == R.styleable.NumberPickerView_npv_AlternativeHint) {
                    this.mAlterHint = a.getString(attr);
                } else if (attr == R.styleable.NumberPickerView_npv_EmptyItemHint) {
                    this.mEmptyItemHint = a.getString(attr);
                } else if (attr == R.styleable.NumberPickerView_npv_MarginStartOfHint) {
                    this.mMarginStartOfHint = a.getDimensionPixelSize(attr, this.dp2px(context, 8.0F));
                } else if (attr == R.styleable.NumberPickerView_npv_MarginEndOfHint) {
                    this.mMarginEndOfHint = a.getDimensionPixelSize(attr, this.dp2px(context, 8.0F));
                } else if (attr == R.styleable.NumberPickerView_npv_ItemPaddingVertical) {
                    this.mItemPaddingVertical = a.getDimensionPixelSize(attr, this.dp2px(context, 2.0F));
                } else if (attr == R.styleable.NumberPickerView_npv_ItemPaddingHorizontal) {
                    this.mItemPaddingHorizontal = a.getDimensionPixelSize(attr, this.dp2px(context, 5.0F));
                } else if (attr == R.styleable.NumberPickerView_npv_AlternativeTextArrayWithMeasureHint) {
                    this.mAlterTextArrayWithMeasureHint = a.getTextArray(attr);
                } else if (attr == R.styleable.NumberPickerView_npv_AlternativeTextArrayWithoutMeasureHint) {
                    this.mAlterTextArrayWithoutMeasureHint = a.getTextArray(attr);
                } else if (attr == R.styleable.NumberPickerView_npv_RespondChangeOnDetached) {
                    this.mRespondChangeOnDetach = a.getBoolean(attr, false);
                } else if (attr == R.styleable.NumberPickerView_npv_RespondChangeInMainThread) {
                    this.mRespondChangeInMainThread = a.getBoolean(attr, true);
                } else if (attr == R.styleable.NumberPickerView_npv_TextEllipsize) {
                    this.mTextEllipsize = a.getString(attr);
                }
            }

            a.recycle();
        }
    }

    private void init(Context context) {
        this.mScroller = ScrollerCompat.create(context);
        this.mMiniVelocityFling = ViewConfiguration.get(this.getContext()).getScaledMinimumFlingVelocity();
        this.mScaledTouchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
        if (this.mTextSizeNormal == 0) {
            this.mTextSizeNormal = this.sp2px(context, 14.0F);
        }

        if (this.mTextSizeSelected == 0) {
            this.mTextSizeSelected = this.sp2px(context, 16.0F);
        }

        if (this.mTextSizeHint == 0) {
            this.mTextSizeHint = this.sp2px(context, 14.0F);
        }

        if (this.mMarginStartOfHint == 0) {
            this.mMarginStartOfHint = this.dp2px(context, 8.0F);
        }

        if (this.mMarginEndOfHint == 0) {
            this.mMarginEndOfHint = this.dp2px(context, 8.0F);
        }

        this.mPaintDivider.setColor(this.mDividerColor);
        this.mPaintDivider.setAntiAlias(true);
        this.mPaintDivider.setStyle(Paint.Style.STROKE);
        this.mPaintDivider.setStrokeWidth((float)this.mDividerHeight);
        this.mPaintText.setColor(this.mTextColorNormal);
        this.mPaintText.setAntiAlias(true);
        this.mPaintText.setTextAlign(Paint.Align.CENTER);
        this.mPaintHint.setColor(this.mTextColorHint);
        this.mPaintHint.setAntiAlias(true);
        this.mPaintHint.setTextAlign(Paint.Align.CENTER);
        this.mPaintHint.setTextSize((float)this.mTextSizeHint);
        if (this.mShowCount % 2 == 0) {
            ++this.mShowCount;
        }

        if (this.mMinShowIndex == -1 || this.mMaxShowIndex == -1) {
            this.updateValueForInit();
        }

        this.initHandler();
    }

    private void initHandler() {
        this.mHandlerThread = new HandlerThread("HandlerThread-For-Refreshing");
        this.mHandlerThread.start();
        this.mHandlerInNewThread = new Handler(this.mHandlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 1:
                        if (!NumberPickerView.this.mScroller.isFinished()) {
                            if (NumberPickerView.this.mScrollState == 0) {
                                NumberPickerView.this.onScrollStateChange(1);
                            }

                            NumberPickerView.this.mHandlerInNewThread.sendMessageDelayed(NumberPickerView.this.getMsg(1, 0, 0, msg.obj), 32L);
                        } else {
                            int duration = 0;
                            int willPickIndex;
                            if (NumberPickerView.this.mCurrDrawFirstItemY != 0) {
                                if (NumberPickerView.this.mScrollState == 0) {
                                    NumberPickerView.this.onScrollStateChange(1);
                                }

                                if (NumberPickerView.this.mCurrDrawFirstItemY < -NumberPickerView.this.mItemHeight / 2) {
                                    duration = (int)(300.0F * (float)(NumberPickerView.this.mItemHeight + NumberPickerView.this.mCurrDrawFirstItemY) / (float)NumberPickerView.this.mItemHeight);
                                    NumberPickerView.this.mScroller.startScroll(0, NumberPickerView.this.mCurrDrawGlobalY, 0, NumberPickerView.this.mItemHeight + NumberPickerView.this.mCurrDrawFirstItemY, duration);
                                    willPickIndex = NumberPickerView.this.getWillPickIndexByGlobalY(NumberPickerView.this.mCurrDrawGlobalY + NumberPickerView.this.mItemHeight + NumberPickerView.this.mCurrDrawFirstItemY);
                                } else {
                                    duration = (int)(300.0F * (float)(-NumberPickerView.this.mCurrDrawFirstItemY) / (float)NumberPickerView.this.mItemHeight);
                                    NumberPickerView.this.mScroller.startScroll(0, NumberPickerView.this.mCurrDrawGlobalY, 0, NumberPickerView.this.mCurrDrawFirstItemY, duration);
                                    willPickIndex = NumberPickerView.this.getWillPickIndexByGlobalY(NumberPickerView.this.mCurrDrawGlobalY + NumberPickerView.this.mCurrDrawFirstItemY);
                                }

                                NumberPickerView.this.postInvalidate();
                            } else {
                                NumberPickerView.this.onScrollStateChange(0);
                                willPickIndex = NumberPickerView.this.getWillPickIndexByGlobalY(NumberPickerView.this.mCurrDrawGlobalY);
                            }

                            Message changeMsg = NumberPickerView.this.getMsg(2, NumberPickerView.this.mPrevPickedIndex, willPickIndex, msg.obj);
                            if (NumberPickerView.this.mRespondChangeInMainThread) {
                                NumberPickerView.this.mHandlerInMainThread.sendMessageDelayed(changeMsg, (long)(duration * 2));
                            } else {
                                NumberPickerView.this.mHandlerInNewThread.sendMessageDelayed(changeMsg, (long)(duration * 2));
                            }
                        }
                        break;
                    case 2:
                        NumberPickerView.this.respondPickedValueChanged(msg.arg1, msg.arg2, msg.obj);
                }

            }
        };
        this.mHandlerInMainThread = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                    case 2:
                        NumberPickerView.this.respondPickedValueChanged(msg.arg1, msg.arg2, msg.obj);
                        break;
                    case 3:
                        NumberPickerView.this.requestLayout();
                }

            }
        };
    }

    private void respondPickedValueChangedInScrolling(int oldVal, int newVal) {
        this.mOnValueChangeListenerInScrolling.onValueChangeInScrolling(this, oldVal, newVal);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.updateMaxWHOfDisplayedValues(false);
        this.setMeasuredDimension(this.measureWidth(widthMeasureSpec), this.measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mViewWidth = w;
        this.mViewHeight = h;
        this.mItemHeight = this.mViewHeight / this.mShowCount;
        this.mViewCenterX = (float)(this.mViewWidth + this.getPaddingLeft() - this.getPaddingRight()) / 2.0F;
        int defaultValue = 0;
        if (this.getOneRecycleSize() > 1) {
            if (this.mHasInit) {
                defaultValue = this.getValue() - this.mMinValue;
            } else if (this.mCurrentItemIndexEffect) {
                defaultValue = this.mCurrDrawFirstItemIndex + (this.mShowCount - 1) / 2;
            } else {
                defaultValue = 0;
            }
        }

        this.correctPositionByDefaultValue(defaultValue, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
        this.updateFontAttr();
        this.updateNotWrapYLimit();
        this.updateDividerAttr();
        this.mHasInit = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mHandlerThread == null || !this.mHandlerThread.isAlive()) {
            this.initHandler();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHandlerThread.quit();
        if (this.mItemHeight != 0) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                this.mCurrDrawGlobalY = this.mScroller.getCurrY();
                this.calculateFirstItemParameterByGlobalY();
                if (this.mCurrDrawFirstItemY != 0) {
                    if (this.mCurrDrawFirstItemY < -this.mItemHeight / 2) {
                        this.mCurrDrawGlobalY = this.mCurrDrawGlobalY + this.mItemHeight + this.mCurrDrawFirstItemY;
                    } else {
                        this.mCurrDrawGlobalY += this.mCurrDrawFirstItemY;
                    }

                    this.calculateFirstItemParameterByGlobalY();
                }

                this.onScrollStateChange(0);
            }

            int currPickedIndex = this.getWillPickIndexByGlobalY(this.mCurrDrawGlobalY);
            if (currPickedIndex != this.mPrevPickedIndex && this.mRespondChangeOnDetach) {
                try {
                    if (this.mOnValueChangeListener != null) {
                        this.mOnValueChangeListener.onValueChange(this, this.mPrevPickedIndex + this.mMinValue, currPickedIndex + this.mMinValue);
                    }

                    if (this.mOnValueChangeListenerRaw != null) {
                        this.mOnValueChangeListenerRaw.onValueChangeRelativeToRaw(this, this.mPrevPickedIndex, currPickedIndex, this.mDisplayedValues);
                    }
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }

            this.mPrevPickedIndex = currPickedIndex;
        }
    }

    public int getOneRecycleSize() {
        return this.mMaxShowIndex - this.mMinShowIndex + 1;
    }

    public int getRawContentSize() {
        return this.mDisplayedValues != null ? this.mDisplayedValues.length : 0;
    }

    public void setDisplayedValuesAndPickedIndex(String[] newDisplayedValues, int pickedIndex, boolean needRefresh) {
        this.stopScrolling();
        if (newDisplayedValues == null) {
            throw new IllegalArgumentException("newDisplayedValues should not be null.");
        } else if (pickedIndex < 0) {
            throw new IllegalArgumentException("pickedIndex should not be negative, now pickedIndex is " + pickedIndex);
        } else {
            this.updateContent(newDisplayedValues);
            this.updateMaxWHOfDisplayedValues(true);
            this.updateNotWrapYLimit();
            this.updateValue();
            this.mPrevPickedIndex = pickedIndex + this.mMinShowIndex;
            this.correctPositionByDefaultValue(pickedIndex, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
            if (needRefresh) {
                this.mHandlerInNewThread.sendMessageDelayed(this.getMsg(1), 0L);
                this.postInvalidate();
            }

        }
    }

    public void setDisplayedValues(String[] newDisplayedValues, boolean needRefresh) {
        this.setDisplayedValuesAndPickedIndex(newDisplayedValues, 0, needRefresh);
    }

    public void setDisplayedValusArray(List<String> list) {
        String[] arra = (String[])((String[])list.toArray(new String[list.size()]));
        this.setDisplayedValues(arra);
    }

    public void setDisplayedValues(String[] newDisplayedValues) {
        this.stopRefreshing();
        this.stopScrolling();
        if (newDisplayedValues == null) {
            throw new IllegalArgumentException("newDisplayedValues should not be null.");
        } else {
            this.updateContent(newDisplayedValues);
            this.updateMaxWHOfDisplayedValues(true);
            this.mPrevPickedIndex = 0 + this.mMinShowIndex;
            this.correctPositionByDefaultValue(0, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
            this.postInvalidate();
            this.mHandlerInMainThread.sendEmptyMessage(3);
        }
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        if (this.mWrapSelectorWheel != wrapSelectorWheel) {
            if (!wrapSelectorWheel) {
                if (this.mScrollState == 0) {
                    this.internalSetWrapToLinear();
                } else {
                    this.mPendingWrapToLinear = true;
                }
            } else {
                this.mWrapSelectorWheel = wrapSelectorWheel;
                this.updateWrapStateByContent();
                this.postInvalidate();
            }
        }

    }

    public void smoothScrollToValue(int toValue) {
        this.smoothScrollToValue(this.getValue(), toValue, true);
    }

    public void smoothScrollToValue(int toValue, boolean needRespond) {
        this.smoothScrollToValue(this.getValue(), toValue, needRespond);
    }

    public void smoothScrollToValue(int fromValue, int toValue) {
        this.smoothScrollToValue(fromValue, toValue, true);
    }

    public void smoothScrollToValue(int fromValue, int toValue, boolean needRespond) {
        fromValue = this.refineValueByLimit(fromValue, this.mMinValue, this.mMaxValue, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
        toValue = this.refineValueByLimit(toValue, this.mMinValue, this.mMaxValue, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
        int deltaIndex;
        if (this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck) {
            deltaIndex = toValue - fromValue;
            int halfOneRecycleSize = this.getOneRecycleSize() / 2;
            if (deltaIndex < -halfOneRecycleSize || halfOneRecycleSize < deltaIndex) {
                deltaIndex = deltaIndex > 0 ? deltaIndex - this.getOneRecycleSize() : deltaIndex + this.getOneRecycleSize();
            }
        } else {
            deltaIndex = toValue - fromValue;
        }

        this.setValue(fromValue);
        if (fromValue != toValue) {
            this.scrollByIndexSmoothly(deltaIndex, needRespond);
        }
    }

    public void refreshByNewDisplayedValues(String[] display) {
        int minValue = this.getMinValue();
        int oldMaxValue = this.getMaxValue();
        int oldSpan = oldMaxValue - minValue + 1;
        int newMaxValue = display.length - 1;
        int newSpan = newMaxValue - minValue + 1;
        if (newSpan > oldSpan) {
            this.setDisplayedValues(display);
            this.setMaxValue(newMaxValue);
        } else {
            this.setMaxValue(newMaxValue);
            this.setDisplayedValues(display);
        }

    }

    private void respondPickedValueChanged(int oldVal, int newVal, Object respondChange) {
        this.onScrollStateChange(0);
        if (oldVal != newVal && (respondChange == null || !(respondChange instanceof Boolean) || (Boolean)respondChange)) {
            if (this.mOnValueChangeListener != null) {
                this.mOnValueChangeListener.onValueChange(this, oldVal + this.mMinValue, newVal + this.mMinValue);
            }

            if (this.mOnValueChangeListenerRaw != null) {
                this.mOnValueChangeListenerRaw.onValueChangeRelativeToRaw(this, oldVal, newVal, this.mDisplayedValues);
            }
        }

        this.mPrevPickedIndex = newVal;
        if (this.mPendingWrapToLinear) {
            this.mPendingWrapToLinear = false;
            this.internalSetWrapToLinear();
        }

    }

    private void scrollByIndexSmoothly(int deltaIndex) {
        this.scrollByIndexSmoothly(deltaIndex, true);
    }

    private void scrollByIndexSmoothly(int deltaIndex, boolean needRespond) {
        int duration;
        if (!this.mWrapSelectorWheel || !this.mWrapSelectorWheelCheck) {
            duration = this.getPickedIndexRelativeToRaw();
            if (duration + deltaIndex > this.mMaxShowIndex) {
                deltaIndex = this.mMaxShowIndex - duration;
            } else if (duration + deltaIndex < this.mMinShowIndex) {
                deltaIndex = this.mMinShowIndex - duration;
            }
        }

        int dy;
        if (this.mCurrDrawFirstItemY < -this.mItemHeight / 2) {
            dy = this.mItemHeight + this.mCurrDrawFirstItemY;
            duration = (int)(300.0F * (float)(this.mItemHeight + this.mCurrDrawFirstItemY) / (float)this.mItemHeight);
            if (deltaIndex < 0) {
                duration = -duration - deltaIndex * 300;
            } else {
                duration += deltaIndex * 300;
            }
        } else {
            dy = this.mCurrDrawFirstItemY;
            duration = (int)(300.0F * (float)(-this.mCurrDrawFirstItemY) / (float)this.mItemHeight);
            if (deltaIndex < 0) {
                duration -= deltaIndex * 300;
            } else {
                duration += deltaIndex * 300;
            }
        }

        dy += deltaIndex * this.mItemHeight;
        if (duration < 300) {
            duration = 300;
        }

        if (duration > 600) {
            duration = 600;
        }

        this.mScroller.startScroll(0, this.mCurrDrawGlobalY, 0, dy, duration);
        if (needRespond) {
            this.mHandlerInNewThread.sendMessageDelayed(this.getMsg(1), (long)(duration / 4));
        } else {
            this.mHandlerInNewThread.sendMessageDelayed(this.getMsg(1, 0, 0, new Boolean(needRespond)), (long)(duration / 4));
        }

        this.postInvalidate();
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
        this.mMinShowIndex = 0;
        this.updateNotWrapYLimit();
    }

    public void setMaxValue(int maxValue) {
        if (this.mDisplayedValues == null) {
            throw new NullPointerException("mDisplayedValues should not be null");
        } else {
            this.mMaxValue = maxValue;
            this.mMaxShowIndex = this.mMaxValue - this.mMinValue + this.mMinShowIndex;
            this.setMinAndMaxShowIndex(this.mMinShowIndex, this.mMaxShowIndex);
            this.updateNotWrapYLimit();
        }
    }

    public void setValue(int value) {
        if (value < this.mMinValue) {
            throw new IllegalArgumentException("should not set a value less than mMinValue, value is " + value);
        } else if (value > this.mMaxValue) {
            throw new IllegalArgumentException("should not set a value greater than mMaxValue, value is " + value);
        } else {
            this.setPickedIndexRelativeToRaw(value - this.mMinValue);
        }
    }

    public int getValue() {
        return this.getPickedIndexRelativeToRaw() + this.mMinValue;
    }

    public String getContentByCurrValue() {
        return this.mDisplayedValues[this.getValue() - this.mMinValue];
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    public boolean getWrapSelectorWheelAbsolutely() {
        return this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck;
    }

    public void setHintText(String hintText) {
        if (!this.isStringEqual(this.mHintText, hintText)) {
            this.mHintText = hintText;
            this.mTextSizeHintCenterYOffset = this.getTextCenterYOffset(this.mPaintHint.getFontMetrics());
            this.mWidthOfHintText = this.getTextWidth(this.mHintText, this.mPaintHint);
            this.mHandlerInMainThread.sendEmptyMessage(3);
        }
    }

    public void setPickedIndexRelativeToMin(int pickedIndexToMin) {
        if (0 <= pickedIndexToMin && pickedIndexToMin < this.getOneRecycleSize()) {
            this.mPrevPickedIndex = pickedIndexToMin + this.mMinShowIndex;
            this.correctPositionByDefaultValue(pickedIndexToMin, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
            this.postInvalidate();
        }

    }

    public void setNormalTextColor(int normalTextColor) {
        if (this.mTextColorNormal != normalTextColor) {
            this.mTextColorNormal = normalTextColor;
            this.postInvalidate();
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        if (this.mTextColorSelected != selectedTextColor) {
            this.mTextColorSelected = selectedTextColor;
            this.postInvalidate();
        }
    }

    public void setHintTextColor(int hintTextColor) {
        if (this.mTextColorHint != hintTextColor) {
            this.mTextColorHint = hintTextColor;
            this.mPaintHint.setColor(this.mTextColorHint);
            this.postInvalidate();
        }
    }

    public void setDividerColor(int dividerColor) {
        if (this.mDividerColor != dividerColor) {
            this.mDividerColor = dividerColor;
            this.mPaintDivider.setColor(this.mDividerColor);
            this.postInvalidate();
        }
    }

    public void setPickedIndexRelativeToRaw(int pickedIndexToRaw) {
        if (this.mMinShowIndex > -1 && this.mMinShowIndex <= pickedIndexToRaw && pickedIndexToRaw <= this.mMaxShowIndex) {
            this.mPrevPickedIndex = pickedIndexToRaw;
            this.correctPositionByDefaultValue(pickedIndexToRaw - this.mMinShowIndex, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
            this.postInvalidate();
        }

    }

    public int getPickedIndexRelativeToRaw() {
        int willPickIndex;
        if (this.mCurrDrawFirstItemY != 0) {
            if (this.mCurrDrawFirstItemY < -this.mItemHeight / 2) {
                willPickIndex = this.getWillPickIndexByGlobalY(this.mCurrDrawGlobalY + this.mItemHeight + this.mCurrDrawFirstItemY);
            } else {
                willPickIndex = this.getWillPickIndexByGlobalY(this.mCurrDrawGlobalY + this.mCurrDrawFirstItemY);
            }
        } else {
            willPickIndex = this.getWillPickIndexByGlobalY(this.mCurrDrawGlobalY);
        }

        return willPickIndex;
    }

    public void setMinAndMaxShowIndex(int minShowIndex, int maxShowIndex) {
        this.setMinAndMaxShowIndex(minShowIndex, maxShowIndex, true);
    }

    public void setMinAndMaxShowIndex(int minShowIndex, int maxShowIndex, boolean needRefresh) {
        if (minShowIndex > maxShowIndex) {
            ;
        }

        if (this.mDisplayedValues == null) {
            throw new IllegalArgumentException("mDisplayedValues should not be null, you need to set mDisplayedValues first.");
        } else {
            this.mMinShowIndex = minShowIndex;
            this.mMaxShowIndex = maxShowIndex;
            if (needRefresh) {
                this.mPrevPickedIndex = 0 + this.mMinShowIndex;
                this.correctPositionByDefaultValue(0, this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
                this.postInvalidate();
            }

        }
    }

    public void setFriction(float friction) {
        if (friction <= 0.0F) {
            throw new IllegalArgumentException("you should set a a positive float friction, now friction is " + friction);
        } else {
            ViewConfiguration.get(this.getContext());
            this.mFriction = ViewConfiguration.getScrollFriction() / friction;
        }
    }

    private void onScrollStateChange(int scrollState) {
        if (this.mScrollState != scrollState) {
            this.mScrollState = scrollState;
            if (this.mOnScrollListener != null) {
                this.mOnScrollListener.onScrollStateChange(this, scrollState);
            }

        }
    }

    public void setOnScrollListener(NumberPickerView.OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    public void setOnValueChangedListener(NumberPickerView.OnValueChangeListener listener) {
        this.mOnValueChangeListener = listener;
    }

    public void setOnValueChangedListenerRelativeToRaw(NumberPickerView.OnValueChangeListenerRelativeToRaw listener) {
        this.mOnValueChangeListenerRaw = listener;
    }

    public void setOnValueChangeListenerInScrolling(NumberPickerView.OnValueChangeListenerInScrolling listener) {
        this.mOnValueChangeListenerInScrolling = listener;
    }

    public void setContentTextTypeface(Typeface typeface) {
        this.mPaintText.setTypeface(typeface);
    }

    public void setHintTextTypeface(Typeface typeface) {
        this.mPaintHint.setTypeface(typeface);
    }

    private int getWillPickIndexByGlobalY(int globalY) {
        if (this.mItemHeight == 0) {
            return 0;
        } else {
            int willPickIndex = globalY / this.mItemHeight + this.mShowCount / 2;
            int index = this.getIndexByRawIndex(willPickIndex, this.getOneRecycleSize(), this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
            if (0 <= index && index < this.getOneRecycleSize()) {
                return index + this.mMinShowIndex;
            } else {
                throw new IllegalArgumentException("getWillPickIndexByGlobalY illegal index : " + index + " getOneRecycleSize() : " + this.getOneRecycleSize() + " mWrapSelectorWheel : " + this.mWrapSelectorWheel);
            }
        }
    }

    private int getIndexByRawIndex(int index, int size, boolean wrap) {
        if (size <= 0) {
            return 0;
        } else if (wrap) {
            index %= size;
            if (index < 0) {
                index += size;
            }

            return index;
        } else {
            return index;
        }
    }

    private void internalSetWrapToLinear() {
        int rawIndex = this.getPickedIndexRelativeToRaw();
        this.correctPositionByDefaultValue(rawIndex - this.mMinShowIndex, false);
        this.mWrapSelectorWheel = false;
        this.postInvalidate();
    }

    private void updateDividerAttr() {
        this.mDividerIndex0 = this.mShowCount / 2;
        this.mDividerIndex1 = this.mDividerIndex0 + 1;
        this.dividerY0 = (float)(this.mDividerIndex0 * this.mViewHeight / this.mShowCount);
        this.dividerY1 = (float)(this.mDividerIndex1 * this.mViewHeight / this.mShowCount);
        if (this.mDividerMarginL < 0) {
            this.mDividerMarginL = 0;
        }

        if (this.mDividerMarginR < 0) {
            this.mDividerMarginR = 0;
        }

        if (this.mDividerMarginL + this.mDividerMarginR != 0) {
            if (this.getPaddingLeft() + this.mDividerMarginL >= this.mViewWidth - this.getPaddingRight() - this.mDividerMarginR) {
                int surplusMargin = this.getPaddingLeft() + this.mDividerMarginL + this.getPaddingRight() + this.mDividerMarginR - this.mViewWidth;
                this.mDividerMarginL = (int)((float)this.mDividerMarginL - (float)surplusMargin * (float)this.mDividerMarginL / (float)(this.mDividerMarginL + this.mDividerMarginR));
                this.mDividerMarginR = (int)((float)this.mDividerMarginR - (float)surplusMargin * (float)this.mDividerMarginR / (float)(this.mDividerMarginL + this.mDividerMarginR));
            }

        }
    }

    private void updateFontAttr() {
        if (this.mTextSizeNormal > this.mItemHeight) {
            this.mTextSizeNormal = this.mItemHeight;
        }

        if (this.mTextSizeSelected > this.mItemHeight) {
            this.mTextSizeSelected = this.mItemHeight;
        }

        if (this.mPaintHint == null) {
            throw new IllegalArgumentException("mPaintHint should not be null.");
        } else {
            this.mPaintHint.setTextSize((float)this.mTextSizeHint);
            this.mTextSizeHintCenterYOffset = this.getTextCenterYOffset(this.mPaintHint.getFontMetrics());
            this.mWidthOfHintText = this.getTextWidth(this.mHintText, this.mPaintHint);
            if (this.mPaintText == null) {
                throw new IllegalArgumentException("mPaintText should not be null.");
            } else {
                this.mPaintText.setTextSize((float)this.mTextSizeSelected);
                this.mTextSizeSelectedCenterYOffset = this.getTextCenterYOffset(this.mPaintText.getFontMetrics());
                this.mPaintText.setTextSize((float)this.mTextSizeNormal);
                this.mTextSizeNormalCenterYOffset = this.getTextCenterYOffset(this.mPaintText.getFontMetrics());
            }
        }
    }

    private void updateNotWrapYLimit() {
        this.mNotWrapLimitYTop = 0;
        this.mNotWrapLimitYBottom = -this.mShowCount * this.mItemHeight;
        if (this.mDisplayedValues != null) {
            this.mNotWrapLimitYTop = (this.getOneRecycleSize() - this.mShowCount / 2 - 1) * this.mItemHeight;
            this.mNotWrapLimitYBottom = -(this.mShowCount / 2) * this.mItemHeight;
        }

    }

    private int limitY(int currDrawGlobalYPreferred) {
        if (this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck) {
            return currDrawGlobalYPreferred;
        } else {
            if (currDrawGlobalYPreferred < this.mNotWrapLimitYBottom) {
                currDrawGlobalYPreferred = this.mNotWrapLimitYBottom;
            } else if (currDrawGlobalYPreferred > this.mNotWrapLimitYTop) {
                currDrawGlobalYPreferred = this.mNotWrapLimitYTop;
            }

            return currDrawGlobalYPreferred;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mItemHeight == 0) {
            return true;
        } else {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(event);
            this.currY = event.getY();
            switch(event.getAction()) {
                case 0:
                    this.mFlagMayPress = true;
                    this.mHandlerInNewThread.removeMessages(1);
                    this.stopScrolling();
                    this.downY = this.currY;
                    this.downYGlobal = (float)this.mCurrDrawGlobalY;
                    this.onScrollStateChange(0);
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case 1:
                    if (this.mFlagMayPress) {
                        this.click(event);
                    } else {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000);
                        int velocityY = (int)(velocityTracker.getYVelocity() * this.mFriction);
                        if (Math.abs(velocityY) > this.mMiniVelocityFling) {
                            this.mScroller.fling(0, this.mCurrDrawGlobalY, 0, -velocityY, -2147483648, 2147483647, this.limitY(-2147483648), this.limitY(2147483647));
                            this.invalidate();
                            this.onScrollStateChange(2);
                        }

                        this.mHandlerInNewThread.sendMessageDelayed(this.getMsg(1), 0L);
                        this.releaseVelocityTracker();
                    }
                    break;
                case 2:
                    float spanY = this.downY - this.currY;
                    if (!this.mFlagMayPress || (float)(-this.mScaledTouchSlop) >= spanY || spanY >= (float)this.mScaledTouchSlop) {
                        this.mFlagMayPress = false;
                        this.mCurrDrawGlobalY = this.limitY((int)(this.downYGlobal + spanY));
                        this.calculateFirstItemParameterByGlobalY();
                        this.invalidate();
                    }

                    this.onScrollStateChange(1);
                    break;
                case 3:
                    this.downYGlobal = (float)this.mCurrDrawGlobalY;
                    this.stopScrolling();
                    this.mHandlerInNewThread.sendMessageDelayed(this.getMsg(1), 0L);
            }

            return true;
        }
    }

    private void click(MotionEvent event) {
        float y = event.getY();

        for(int i = 0; i < this.mShowCount; ++i) {
            if ((float)(this.mItemHeight * i) <= y && y < (float)(this.mItemHeight * (i + 1))) {
                this.clickItem(i);
                break;
            }
        }

    }

    private void clickItem(int showCountIndex) {
        if (0 <= showCountIndex && showCountIndex < this.mShowCount) {
            this.scrollByIndexSmoothly(showCountIndex - this.mShowCount / 2);
        }

    }

    private float getTextCenterYOffset(Paint.FontMetrics fontMetrics) {
        return fontMetrics == null ? 0.0F : Math.abs(fontMetrics.top + fontMetrics.bottom) / 2.0F;
    }

    private void correctPositionByDefaultValue(int defaultPickedIndex, boolean wrap) {
        this.mCurrDrawFirstItemIndex = defaultPickedIndex - (this.mShowCount - 1) / 2;
        this.mCurrDrawFirstItemIndex = this.getIndexByRawIndex(this.mCurrDrawFirstItemIndex, this.getOneRecycleSize(), wrap);
        if (this.mItemHeight == 0) {
            this.mCurrentItemIndexEffect = true;
        } else {
            this.mCurrDrawGlobalY = this.mCurrDrawFirstItemIndex * this.mItemHeight;
            this.mInScrollingPickedOldValue = this.mCurrDrawFirstItemIndex + this.mShowCount / 2;
            this.mInScrollingPickedOldValue %= this.getOneRecycleSize();
            if (this.mInScrollingPickedOldValue < 0) {
                this.mInScrollingPickedOldValue += this.getOneRecycleSize();
            }

            this.mInScrollingPickedNewValue = this.mInScrollingPickedOldValue;
            this.calculateFirstItemParameterByGlobalY();
        }

    }

    @Override
    public void computeScroll() {
        if (this.mItemHeight != 0) {
            if (this.mScroller.computeScrollOffset()) {
                this.mCurrDrawGlobalY = this.mScroller.getCurrY();
                this.calculateFirstItemParameterByGlobalY();
                this.postInvalidate();
            }

        }
    }

    private void calculateFirstItemParameterByGlobalY() {
        this.mCurrDrawFirstItemIndex = (int)Math.floor((double)((float)this.mCurrDrawGlobalY / (float)this.mItemHeight));
        this.mCurrDrawFirstItemY = -(this.mCurrDrawGlobalY - this.mCurrDrawFirstItemIndex * this.mItemHeight);
        if (this.mOnValueChangeListenerInScrolling != null) {
            if (-this.mCurrDrawFirstItemY > this.mItemHeight / 2) {
                this.mInScrollingPickedNewValue = this.mCurrDrawFirstItemIndex + 1 + this.mShowCount / 2;
            } else {
                this.mInScrollingPickedNewValue = this.mCurrDrawFirstItemIndex + this.mShowCount / 2;
            }

            this.mInScrollingPickedNewValue %= this.getOneRecycleSize();
            if (this.mInScrollingPickedNewValue < 0) {
                this.mInScrollingPickedNewValue += this.getOneRecycleSize();
            }

            if (this.mInScrollingPickedOldValue != this.mInScrollingPickedNewValue) {
                this.respondPickedValueChangedInScrolling(this.mInScrollingPickedOldValue, this.mInScrollingPickedNewValue);
            }

            this.mInScrollingPickedOldValue = this.mInScrollingPickedNewValue;
        }

    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }

    }

    private void updateMaxWHOfDisplayedValues(boolean needRequestLayout) {
        this.updateMaxWidthOfDisplayedValues();
        this.updateMaxHeightOfDisplayedValues();
        if (needRequestLayout && (this.mSpecModeW == -2147483648 || this.mSpecModeH == -2147483648)) {
            this.mHandlerInMainThread.sendEmptyMessage(3);
        }

    }

    private int measureWidth(int measureSpec) {
        int specMode = this.mSpecModeW = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == 1073741824) {
            result = specSize;
        } else {
            int marginOfHint = Math.max(this.mWidthOfHintText, this.mWidthOfAlterHint) == 0 ? 0 : this.mMarginEndOfHint;
            int gapOfHint = Math.max(this.mWidthOfHintText, this.mWidthOfAlterHint) == 0 ? 0 : this.mMarginStartOfHint;
            int maxWidth = Math.max(this.mMaxWidthOfAlterArrayWithMeasureHint, Math.max(this.mMaxWidthOfDisplayedValues, this.mMaxWidthOfAlterArrayWithoutMeasureHint) + 2 * (gapOfHint + Math.max(this.mWidthOfHintText, this.mWidthOfAlterHint) + marginOfHint + 2 * this.mItemPaddingHorizontal));
            result = this.getPaddingLeft() + this.getPaddingRight() + maxWidth;
            if (specMode == -2147483648) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = this.mSpecModeH = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == 1073741824) {
            result = specSize;
        } else {
            int maxHeight = this.mShowCount * (this.mMaxHeightOfDisplayedValues + 2 * this.mItemPaddingVertical);
            result = this.getPaddingTop() + this.getPaddingBottom() + maxHeight;
            if (specMode == -2147483648) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawContent(canvas);
        this.drawLine(canvas);
        this.drawHint(canvas);
    }

    private void drawContent(Canvas canvas) {
        float fraction = 0.0F;

        for(int i = 0; i < this.mShowCount + 1; ++i) {
            float y = (float)(this.mCurrDrawFirstItemY + this.mItemHeight * i);
            int index = this.getIndexByRawIndex(this.mCurrDrawFirstItemIndex + i, this.getOneRecycleSize(), this.mWrapSelectorWheel && this.mWrapSelectorWheelCheck);
            int textColor;
            float textSize;
            float textSizeCenterYOffset;
            if (i == this.mShowCount / 2) {
                fraction = (float)(this.mItemHeight + this.mCurrDrawFirstItemY) / (float)this.mItemHeight;
                textColor = this.mTextColorSelected;
                textSize = this.getEvaluateSize(fraction, (float)this.mTextSizeNormal, (float)this.mTextSizeSelected);
                textSizeCenterYOffset = this.getEvaluateSize(fraction, this.mTextSizeNormalCenterYOffset, this.mTextSizeSelectedCenterYOffset);
            } else if (i != this.mShowCount / 2 + 1 && i != this.mShowCount / 2 - 1) {
                textColor = this.mTextColorNormal;
                textSize = (float)this.mTextSizeNormal;
                textSizeCenterYOffset = this.mTextSizeNormalCenterYOffset;
            } else {
                textColor = this.mTextColorLeve2;
                textSize = this.getEvaluateSize(1.0F - fraction, (float)this.mTextSizeNormal, (float)this.mTextSizeSelected);
                textSizeCenterYOffset = this.getEvaluateSize(1.0F - fraction, this.mTextSizeNormalCenterYOffset, this.mTextSizeSelectedCenterYOffset);
            }

            this.mPaintText.setColor(textColor);
            this.mPaintText.setTextSize(textSize);
            if (0 <= index && index < this.getOneRecycleSize()) {
                CharSequence str = this.mDisplayedValues[index + this.mMinShowIndex];
//                if (((CharSequence)str).length() > 8) {
//                    str = ((CharSequence)str).subSequence(0, 8);
//                }

                if (this.mTextEllipsize != null) {
                    str = TextUtils.ellipsize((CharSequence)str, this.mPaintText, (float)(this.getWidth() - 2 * this.mItemPaddingHorizontal), this.getEllipsizeType());
                }

                canvas.drawText(((CharSequence)str).toString(), this.mViewCenterX, y + (float)(this.mItemHeight / 2) + textSizeCenterYOffset, this.mPaintText);
            } else if (!TextUtils.isEmpty(this.mEmptyItemHint)) {
                canvas.drawText(this.mEmptyItemHint, this.mViewCenterX, y + (float)(this.mItemHeight / 2) + textSizeCenterYOffset, this.mPaintText);
            }
        }

    }

    private TextUtils.TruncateAt getEllipsizeType() {
        String var1 = this.mTextEllipsize;
        byte var2 = -1;
        switch(var1.hashCode()) {
            case -1074341483:
                if (var1.equals("middle")) {
                    var2 = 1;
                }
                break;
            case 100571:
                if (var1.equals("end")) {
                    var2 = 2;
                }
                break;
            case 109757538:
                if (var1.equals("start")) {
                    var2 = 0;
                }
        }

        switch(var2) {
            case 0:
                return TextUtils.TruncateAt.START;
            case 1:
                return TextUtils.TruncateAt.MIDDLE;
            case 2:
                return TextUtils.TruncateAt.END;
            default:
                throw new IllegalArgumentException("Illegal text ellipsize type.");
        }
    }

    private void drawLine(Canvas canvas) {
        if (this.mShowDivider) {
            canvas.drawLine((float)(this.getPaddingLeft() + this.mDividerMarginL), this.dividerY0, (float)(this.mViewWidth - this.getPaddingRight() - this.mDividerMarginR), this.dividerY0, this.mPaintDivider);
            canvas.drawLine((float)(this.getPaddingLeft() + this.mDividerMarginL), this.dividerY1, (float)(this.mViewWidth - this.getPaddingRight() - this.mDividerMarginR), this.dividerY1, this.mPaintDivider);
        }

    }

    private void drawHint(Canvas canvas) {
        if (!TextUtils.isEmpty(this.mHintText)) {
            canvas.drawText(this.mHintText, this.mViewCenterX + (float)((this.mMaxWidthOfDisplayedValues + this.mWidthOfHintText) / 2) + (float)this.mMarginStartOfHint, (this.dividerY0 + this.dividerY1) / 2.0F + this.mTextSizeHintCenterYOffset, this.mPaintHint);
        }
    }

    private void updateMaxWidthOfDisplayedValues() {
        float savedTextSize = this.mPaintText.getTextSize();
        this.mPaintText.setTextSize((float)this.mTextSizeSelected);
        this.mMaxWidthOfDisplayedValues = this.getMaxWidthOfTextArray(this.mDisplayedValues, this.mPaintText);
        this.mMaxWidthOfAlterArrayWithMeasureHint = this.getMaxWidthOfTextArray(this.mAlterTextArrayWithMeasureHint, this.mPaintText);
        this.mMaxWidthOfAlterArrayWithoutMeasureHint = this.getMaxWidthOfTextArray(this.mAlterTextArrayWithoutMeasureHint, this.mPaintText);
        this.mPaintText.setTextSize((float)this.mTextSizeHint);
        this.mWidthOfAlterHint = this.getTextWidth(this.mAlterHint, this.mPaintText);
        this.mPaintText.setTextSize(savedTextSize);
    }

    private int getMaxWidthOfTextArray(CharSequence[] array, Paint paint) {
        if (array == null) {
            return 0;
        } else {
            int maxWidth = 0;
            CharSequence[] var4 = array;
            int var5 = array.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                CharSequence item = var4[var6];
                if (item != null) {
                    int itemWidth = this.getTextWidth(item, paint);
                    maxWidth = Math.max(itemWidth, maxWidth);
                }
            }

            return maxWidth;
        }
    }

    private int getTextWidth(CharSequence text, Paint paint) {
        return !TextUtils.isEmpty(text) ? (int)(paint.measureText(text.toString()) + 0.5F) : 0;
    }

    private void updateMaxHeightOfDisplayedValues() {
        float savedTextSize = this.mPaintText.getTextSize();
        this.mPaintText.setTextSize((float)this.mTextSizeSelected);
        this.mMaxHeightOfDisplayedValues = (int)((double)(this.mPaintText.getFontMetrics().bottom - this.mPaintText.getFontMetrics().top) + 0.5D);
        this.mPaintText.setTextSize(savedTextSize);
    }

    private void updateContentAndIndex(String[] newDisplayedValues) {
        this.mMinShowIndex = 0;
        this.mMaxShowIndex = newDisplayedValues.length - 1;
        this.mDisplayedValues = newDisplayedValues;
        this.updateWrapStateByContent();
    }

    private void updateContent(String[] newDisplayedValues) {
        this.mDisplayedValues = newDisplayedValues;
        this.updateWrapStateByContent();
    }

    private void updateValue() {
        this.inflateDisplayedValuesIfNull();
        this.updateWrapStateByContent();
        this.mMinShowIndex = 0;
        this.mMaxShowIndex = this.mDisplayedValues.length - 1;
    }

    private void updateValueForInit() {
        this.inflateDisplayedValuesIfNull();
        this.updateWrapStateByContent();
        if (this.mMinShowIndex == -1) {
            this.mMinShowIndex = 0;
        }

        if (this.mMaxShowIndex == -1) {
            this.mMaxShowIndex = this.mDisplayedValues.length - 1;
        }

        this.setMinAndMaxShowIndex(this.mMinShowIndex, this.mMaxShowIndex, false);
    }

    private void inflateDisplayedValuesIfNull() {
        if (this.mDisplayedValues == null) {
            this.mDisplayedValues = new String[1];
            this.mDisplayedValues[0] = "0";
        }

    }

    private void updateWrapStateByContent() {
        this.mWrapSelectorWheelCheck = this.mDisplayedValues.length > this.mShowCount;
    }

    private int refineValueByLimit(int value, int minValue, int maxValue, boolean wrap) {
        if (wrap) {
            if (value > maxValue) {
                value = (value - maxValue) % this.getOneRecycleSize() + minValue - 1;
            } else if (value < minValue) {
                value = (value - minValue) % this.getOneRecycleSize() + maxValue + 1;
            }

            return value;
        } else {
            if (value > maxValue) {
                value = maxValue;
            } else if (value < minValue) {
                value = minValue;
            }

            return value;
        }
    }

    private void stopRefreshing() {
        if (this.mHandlerInNewThread != null) {
            this.mHandlerInNewThread.removeMessages(1);
        }

    }

    public void stopScrolling() {
        if (this.mScroller != null && !this.mScroller.isFinished()) {
            this.mScroller.startScroll(0, this.mScroller.getCurrY(), 0, 0, 1);
            this.mScroller.abortAnimation();
            this.postInvalidate();
        }

    }

    public void stopScrollingAndCorrectPosition() {
        this.stopScrolling();
        if (this.mHandlerInNewThread != null) {
            this.mHandlerInNewThread.sendMessageDelayed(this.getMsg(1), 0L);
        }

    }

    private Message getMsg(int what) {
        return this.getMsg(what, 0, 0, (Object)null);
    }

    private Message getMsg(int what, int arg1, int arg2, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        return msg;
    }

    private boolean isStringEqual(String a, String b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    private int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }

    private int dp2px(Context context, float dpValue) {
        float densityScale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * densityScale + 0.5F);
    }

    private int getEvaluateColor(float fraction, int startColor, int endColor) {
        int sA = (startColor & -16777216) >>> 24;
        int sR = (startColor & 16711680) >>> 16;
        int sG = (startColor & '\uff00') >>> 8;
        int sB = (startColor & 255) >>> 0;
        int eA = (endColor & -16777216) >>> 24;
        int eR = (endColor & 16711680) >>> 16;
        int eG = (endColor & '\uff00') >>> 8;
        int eB = (endColor & 255) >>> 0;
        int a = (int)((float)sA + (float)(eA - sA) * fraction);
        int r = (int)((float)sR + (float)(eR - sR) * fraction);
        int g = (int)((float)sG + (float)(eG - sG) * fraction);
        int b = (int)((float)sB + (float)(eB - sB) * fraction);
        return a << 24 | r << 16 | g << 8 | b;
    }

    private float getEvaluateSize(float fraction, float startSize, float endSize) {
        return startSize + (endSize - startSize) * fraction;
    }

    private String[] convertCharSequenceArrayToStringArray(CharSequence[] charSequences) {
        if (charSequences == null) {
            return null;
        } else {
            String[] ret = new String[charSequences.length];

            for(int i = 0; i < charSequences.length; ++i) {
                ret[i] = charSequences[i].toString();
            }

            return ret;
        }
    }

    public interface OnScrollListener {
        int SCROLL_STATE_IDLE = 0;
        int SCROLL_STATE_TOUCH_SCROLL = 1;
        int SCROLL_STATE_FLING = 2;

        void onScrollStateChange(NumberPickerView var1, int var2);
    }

    public interface OnValueChangeListenerInScrolling {
        void onValueChangeInScrolling(NumberPickerView var1, int var2, int var3);
    }

    public interface OnValueChangeListenerRelativeToRaw {
        void onValueChangeRelativeToRaw(NumberPickerView var1, int var2, int var3, String[] var4);
    }

    public interface OnValueChangeListener {
        void onValueChange(NumberPickerView var1, int var2, int var3);
    }
}
