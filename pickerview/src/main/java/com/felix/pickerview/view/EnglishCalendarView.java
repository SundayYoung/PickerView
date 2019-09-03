package com.felix.pickerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.felix.pickerview.R;
import com.felix.pickerview.utils.ChineseCalendar;
import com.felix.pickerview.utils.Util;

import java.util.Calendar;

/**
 * mm/dd/yy
 */
public class EnglishCalendarView extends LinearLayout implements NumberPickerView.OnValueChangeListener {
    private static final int DEFAULT_GREGORIAN_COLOR = -13399809;
    private static final int DEFAULT_LUNAR_COLOR = -1157820;
    private static final int DEFAULT_NORMAL_TEXT_COLOR = 856824883;
    private static int YEAR_START = 1901;
    private static int YEAR_STOP = 2100;
    private static final int YEAR_SPAN = YEAR_STOP - YEAR_START + 1;
    private static final int MONTH_START = 1;
    private static final int MONTH_START_GREGORIAN = 1;
    private static final int MONTH_STOP_GREGORIAN = 12;
    private static final int MONTH_SPAN_GREGORIAN = 12;
    private static final int MONTH_START_LUNAR = 1;
    private static final int MONTH_START_LUNAR_NORMAL = 1;
    private static final int MONTH_STOP_LUNAR_NORMAL = 12;
    private static final int MONTH_SPAN_LUNAR_NORMAL = 12;
    private static final int MONTH_START_LUNAR_LEAP = 1;
    private static final int MONTH_STOP_LUNAR_LEAP = 13;
    private static final int MONTH_SPAN_LUNAR_LEAP = 13;
    private static final int DAY_START = 1;
    private static final int DAY_STOP = 30;
    private static final int DAY_START_GREGORIAN = 1;
    private static final int DAY_STOP_GREGORIAN = 31;
    private static final int DAY_SPAN_GREGORIAN = 31;
    private static final int DAY_START_LUNAR = 1;
    private static final int DAY_STOP_LUNAR = 30;
    private static final int DAY_SPAN_LUNAR = 30;
    private NumberPickerView mYearPickerView;
    private NumberPickerView mMonthPickerView;
    private NumberPickerView mDayPickerView;
    private int mThemeColorG = -13399809;
    private int mThemeColorL = -1157820;
    private int mNormalTextColor = 856824883;
    private String[] mDisplayYearsGregorian;
    private String[] mDisplayMonthsGregorian;
    private String[] mDisplayDaysGregorian;
    private String[] mDisplayYearsLunar;
    private String[] mDisplayMonthsLunar;
    private String[] mDisplayDaysLunar;
    private String[] mCurrDisplayMonthsLunar;
    private boolean mIsGregorian = true;
    private boolean mScrollAnim = true;
    private OnDateChangedListener mOnDateChangedListener;

    public EnglishCalendarView(Context context) {
        super(context);
        initInternal(context);
    }

    public EnglishCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initInternal(context);
    }

    public EnglishCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(context, attrs);
        initInternal(context);
    }

    private void initInternal(Context context) {
        View contentView = inflate(context, R.layout.view_english_calendar, this);

        this.mYearPickerView = ((NumberPickerView) contentView.findViewById(R.id.picker_year));
        this.mMonthPickerView = ((NumberPickerView) contentView.findViewById(R.id.picker_month));
        this.mDayPickerView = ((NumberPickerView) contentView.findViewById(R.id.picker_day));

        this.mYearPickerView.setOnValueChangedListener(this);
        this.mMonthPickerView.setOnValueChangedListener(this);
        this.mDayPickerView.setOnValueChangedListener(this);
    }

    public void setStartYear(int mStartYear) {
        YEAR_START = mStartYear;
    }

    public void setStopYear(int mStopYear) {
        YEAR_STOP = mStopYear;
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GregorianLunarCalendarView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_ScrollAnimation) {
                this.mScrollAnim = a.getBoolean(attr, true);
            } else if (attr == R.styleable.GregorianLunarCalendarView_glcv_GregorianThemeColor) {
                this.mThemeColorG = a.getColor(attr, -13399809);
            }
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_LunarThemeColor) {
                this.mThemeColorL = a.getColor(attr, -1157820);
            }
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_NormalTextColor) {
                this.mNormalTextColor = a.getColor(attr, 856824883);
            }
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_EndYear) {
                YEAR_STOP = a.getInt(attr, YEAR_STOP);
            }
            if (attr == R.styleable.GregorianLunarCalendarView_glcv_StartYear) {
                YEAR_START = a.getInt(attr, YEAR_START);
            }
        }
        a.recycle();
    }

    public void init(int startYear, int stopYear) {
        YEAR_STOP = stopYear;
        YEAR_START = startYear;
        setColor(this.mThemeColorG, this.mNormalTextColor);
        setConfigs(Calendar.getInstance(), true, false);
    }

    public void init(Calendar calendar, int startYear, int stopYear) {
        YEAR_STOP = stopYear;
        YEAR_START = startYear;
        setColor(this.mThemeColorG, this.mNormalTextColor);
        setConfigs(calendar, true, false);
    }

    public void init(Calendar calendar, boolean isGregorian) {
        setColor(isGregorian ? this.mThemeColorG : this.mThemeColorL, this.mNormalTextColor);
        setConfigs(calendar, isGregorian, false);
    }

    private void setConfigs(Calendar c, boolean isGregorian, boolean anim) {
        if (c == null) {
            c = Calendar.getInstance();
        }
        if (!checkCalendarAvailable(c, YEAR_START, YEAR_STOP, isGregorian)) {
            c = adjustCalendarByLimit(c, YEAR_START, YEAR_STOP, isGregorian);
        }
        this.mIsGregorian = isGregorian;
        ChineseCalendar cc;
        if ((c instanceof ChineseCalendar)) {
            cc = (ChineseCalendar) c;
        } else {
            cc = new ChineseCalendar(c);
        }
        setDisplayValuesForAll(cc, this.mIsGregorian, anim);
    }

    private Calendar adjustCalendarByLimit(Calendar c, int yearStart, int yearStop, boolean isGregorian) {
        int yearGrego = c.get(1);
        if (isGregorian) {
            if (yearGrego < yearStart) {
                c.set(1, yearStart);
                c.set(2, 1);
                c.set(5, 1);
            }
            if (yearGrego > yearStop) {
                c.set(1, yearStop);
                c.set(2, 11);
                int daySway = Util.getSumOfDayInMonthForGregorianByMonth(yearStop, 12);
                c.set(5, daySway);
            }
        } else if (Math.abs(yearGrego - yearStart) < Math.abs(yearGrego - yearStop)) {
            c = new ChineseCalendar(true, yearStart, 1, 1);
        } else {
            int daySway = Util.getSumOfDayInMonthForLunarByMonthLunar(yearStop, 12);
            c = new ChineseCalendar(true, yearStop, 12, daySway);
        }
        return c;
    }

    public void toGregorianMode() {
        setThemeColor(this.mThemeColorG);
        setGregorian(true, true);
    }

    public void toLunarMode() {
        setThemeColor(this.mThemeColorL);
        setGregorian(false, true);
    }

    public void setColor(int themeColor, int normalColor) {
        setThemeColor(themeColor);
        setNormalColor(normalColor);
    }

    public void setThemeColor(int themeColor) {
        this.mYearPickerView.setHintTextColor(themeColor);
        this.mMonthPickerView.setHintTextColor(themeColor);
        this.mDayPickerView.setHintTextColor(themeColor);
    }

    public void setNormalColor(int normalColor) {
        this.mYearPickerView.setNormalTextColor(normalColor);
        this.mMonthPickerView.setNormalTextColor(normalColor);
        this.mDayPickerView.setNormalTextColor(normalColor);
    }

    private void setDisplayValuesForAll(ChineseCalendar cc, boolean isGregorian, boolean anim) {
        setDisplayData(isGregorian);
        initValuesForY(cc, isGregorian, anim);
        initValuesForM(cc, isGregorian, anim);
        initValuesForD(cc, isGregorian, anim);
        if (this.mOnDateChangedListener != null) {
            this.mOnDateChangedListener.onDateChanged(new CalendarData(cc.get(1), cc.get(2) + 1, cc.get(5), false));
        }
    }

    private void setDisplayData(boolean isGregorian) {
        if (isGregorian) {
            if (this.mDisplayYearsGregorian == null) {
                this.mDisplayYearsGregorian = new String[YEAR_SPAN];
                for (int i = 0; i < YEAR_SPAN; i++) {
                    this.mDisplayYearsGregorian[i] = (String.valueOf(YEAR_START + i));
                }
            }
            if (this.mDisplayMonthsGregorian == null) {
                this.mDisplayMonthsGregorian = new String[12];
                for (int i = 0; i < 12; i++) {
                    this.mDisplayMonthsGregorian[i] = (String.valueOf(1 + i));
                }
            }
            if (this.mDisplayDaysGregorian == null) {
                this.mDisplayDaysGregorian = new String[31];
                for (int i = 0; i < 31; i++) {
                    this.mDisplayDaysGregorian[i] = (String.valueOf(1 + i));
                }
            }
        } else {
            if (this.mDisplayYearsLunar == null) {
                this.mDisplayYearsLunar = new String[YEAR_SPAN];
                for (int i = 0; i < YEAR_SPAN; i++) {
                    this.mDisplayYearsLunar[i] = Util.getLunarNameOfYear(i + YEAR_START);
                }
            }
            if (this.mDisplayMonthsLunar == null) {
                this.mDisplayMonthsLunar = new String[12];
                for (int i = 0; i < 12; i++) {
                    this.mDisplayMonthsLunar[i] = Util.getLunarNameOfMonth(i + 1);
                }
            }
            if (this.mDisplayDaysLunar == null) {
                this.mDisplayDaysLunar = new String[30];
                for (int i = 0; i < 30; i++) {
                    this.mDisplayDaysLunar[i] = Util.getLunarNameOfDay(i + 1);
                }
            }
        }
    }

    private void initValuesForY(ChineseCalendar cc, boolean isGregorian, boolean anim) {
        if (isGregorian) {
            int yearSway = cc.get(1);
            setValuesForPickerView(this.mYearPickerView, yearSway, YEAR_START, YEAR_STOP, this.mDisplayYearsGregorian, false, anim);
        } else {
            int yearSway = cc.get(801);
            setValuesForPickerView(this.mYearPickerView, yearSway, YEAR_START, YEAR_STOP, this.mDisplayYearsLunar, false, anim);
        }
    }

    private void initValuesForM(ChineseCalendar cc, boolean isGregorian, boolean anim) {
        String[] newDisplayedVales = null;
        int monthStart = 0;
        int monthStop = 0;
        int monthSway = 0;
        if (isGregorian) {
            monthStart = 1;
            monthStop = 12;
            monthSway = cc.get(2) + 1;
            newDisplayedVales = this.mDisplayMonthsGregorian;
        } else {
            int monthLeap = Util.getMonthLeapByYear(cc.get(801));
            if (monthLeap == 0) {
                monthStart = 1;

                monthStop = 12;
                monthSway = cc.get(802);
                newDisplayedVales = this.mDisplayMonthsLunar;
            } else {
                monthStart = 1;
                monthStop = 13;
                monthSway = Util.convertMonthLunarToMonthSway(cc.get(802), monthLeap);
                newDisplayedVales = Util.getLunarMonthsNamesWithLeap(monthLeap);
            }
        }
        setValuesForPickerView(this.mMonthPickerView, monthSway, monthStart, monthStop, newDisplayedVales, false, anim);
    }

    private void initValuesForD(ChineseCalendar cc, boolean isGregorian, boolean anim) {
        if (isGregorian) {
            int dayStart = 1;
            int dayStop = Util.getSumOfDayInMonthForGregorianByMonth(cc.get(1), cc.get(2) + 1);
            int daySway = cc.get(5);
            setValuesForPickerView(this.mDayPickerView, daySway, dayStart, dayStop, this.mDisplayDaysGregorian, false, anim);
        } else {
            int dayStart = 1;
            int dayStop = Util.getSumOfDayInMonthForLunarByMonthLunar(cc.get(801), cc.get(802));
            int daySway = cc.get(803);
            this.mDayPickerView.setHintText("");
            setValuesForPickerView(this.mDayPickerView, daySway, dayStart, dayStop, this.mDisplayDaysLunar, false, anim);
        }
    }

    private void setValuesForPickerView(NumberPickerView pickerView, int newSway, int newStart, int newStop, String[] newDisplayedVales, boolean needRespond, boolean anim) {
        if (newDisplayedVales == null) {
            throw new IllegalArgumentException("newDisplayedVales should not be null.");
        }
        if (newDisplayedVales.length == 0) {
            throw new IllegalArgumentException("newDisplayedVales's length should not be 0.");
        }
        int newSpan = newStop - newStart + 1;
        if (newDisplayedVales.length < newSpan) {
            throw new IllegalArgumentException("newDisplayedVales's length should not be less than newSpan.");
        }
        int oldStart = pickerView.getMinValue();
        int oldStop = pickerView.getMaxValue();
        int oldSpan = oldStop - oldStart + 1;
        int fromValue = pickerView.getValue();
        pickerView.setMinValue(newStart);
        if (newSpan > oldSpan) {
            pickerView.setDisplayedValues(newDisplayedVales);
            pickerView.setMaxValue(newStop);
        } else {
            pickerView.setMaxValue(newStop);
            pickerView.setDisplayedValues(newDisplayedVales);
        }
        if ((this.mScrollAnim) && (anim)) {
            int toValue = newSway;
            if (fromValue < newStart) {
                fromValue = newStart;
            }
            pickerView.smoothScrollToValue(fromValue, toValue, needRespond);
        } else {
            pickerView.setValue(newSway);
        }
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (picker == null) {
            return;
        }
        if (picker == this.mYearPickerView) {
            passiveUpdateMonthAndDay(oldVal, newVal, this.mIsGregorian);
        } else if (picker == this.mMonthPickerView) {
            int fixYear = this.mYearPickerView.getValue();
            passiveUpdateDay(fixYear, fixYear, oldVal, newVal, this.mIsGregorian);
        } else if ((picker == this.mDayPickerView) &&
                (this.mOnDateChangedListener != null)) {
            this.mOnDateChangedListener.onDateChanged(getCalendarData());
        }
    }

    private void passiveUpdateMonthAndDay(int oldYearFix, int newYearFix, boolean isGregorian) {
        int oldMonthSway = this.mMonthPickerView.getValue();
        int oldDaySway = this.mDayPickerView.getValue();
        if (isGregorian) {
            int newMonthSway = oldMonthSway;
            int oldDayStop = Util.getSumOfDayInMonth(oldYearFix, oldMonthSway, true);
            int newDayStop = Util.getSumOfDayInMonth(newYearFix, newMonthSway, true);
            if (oldDayStop == newDayStop) {
                if (this.mOnDateChangedListener != null) {
                    this.mOnDateChangedListener.onDateChanged(getCalendarData(newYearFix, newMonthSway, oldDaySway, isGregorian));
                }
                return;
            }
            int newDaySway = oldDaySway <= newDayStop ? oldDaySway : newDayStop;
            setValuesForPickerView(this.mDayPickerView, newDaySway, 1, newDayStop, this.mDisplayDaysGregorian, true, true);
            if (this.mOnDateChangedListener != null) {
                this.mOnDateChangedListener.onDateChanged(getCalendarData(newYearFix, newMonthSway, newDaySway, isGregorian));
            }
            return;
        }
        int newMonthSway = 0;

        int newYearFixMonthLeap = Util.getMonthLeapByYear(newYearFix);
        int oldYearFixMonthLeap = Util.getMonthLeapByYear(oldYearFix);
        if (newYearFixMonthLeap == oldYearFixMonthLeap) {
            newMonthSway = oldMonthSway;

            int oldMonthLunar = Util.convertMonthSwayToMonthLunar(oldMonthSway, oldYearFixMonthLeap);
            int newMonthLunar = Util.convertMonthSwayToMonthLunar(newMonthSway, newYearFixMonthLeap);
            int oldDayStop = Util.getSumOfDayInMonthForLunarByMonthLunar(oldYearFix, oldMonthLunar);
            int newDayStop = Util.getSumOfDayInMonthForLunarByMonthLunar(newYearFix, newMonthLunar);
            if (oldDayStop == newDayStop) {
                if (this.mOnDateChangedListener != null) {
                    this.mOnDateChangedListener.onDateChanged(getCalendarData(newYearFix, newMonthSway, oldDaySway, isGregorian));
                }
                return;
            }
            int newDaySway = oldDaySway <= newDayStop ? oldDaySway : newDayStop;
            setValuesForPickerView(this.mDayPickerView, newDaySway, 1, newDayStop, this.mDisplayDaysLunar, true, true);
            if (this.mOnDateChangedListener != null) {
                this.mOnDateChangedListener.onDateChanged(getCalendarData(newYearFix, newMonthSway, newDaySway, isGregorian));
            }
            return;
        }
        this.mCurrDisplayMonthsLunar = Util.getLunarMonthsNamesWithLeap(newYearFixMonthLeap);

        int oldMonthLunar = Util.convertMonthSwayToMonthLunar(oldMonthSway, oldYearFixMonthLeap);
        int oldMonthLunarAbs = Math.abs(oldMonthLunar);
        newMonthSway = Util.convertMonthLunarToMonthSway(oldMonthLunarAbs, newYearFixMonthLeap);
        setValuesForPickerView(this.mMonthPickerView, newMonthSway, 1, newYearFixMonthLeap == 0 ? 12 : 13, this.mCurrDisplayMonthsLunar, false, true);

        int oldDayStop = Util.getSumOfDayInMonth(oldYearFix, oldMonthSway, false);
        int newDayStop = Util.getSumOfDayInMonth(newYearFix, newMonthSway, false);
        if (oldDayStop == newDayStop) {
            if (this.mOnDateChangedListener != null) {
                this.mOnDateChangedListener.onDateChanged(getCalendarData(newYearFix, newMonthSway, oldDaySway, isGregorian));
            }
            return;
        }
        int newDaySway = oldDaySway <= newDayStop ? oldDaySway : newDayStop;
        setValuesForPickerView(this.mDayPickerView, newDaySway, 1, newDayStop, this.mDisplayDaysLunar, true, true);
        if (this.mOnDateChangedListener != null) {
            this.mOnDateChangedListener.onDateChanged(getCalendarData(newYearFix, newMonthSway, newDaySway, isGregorian));
        }
    }

    private void passiveUpdateDay(int oldYear, int newYear, int oldMonth, int newMonth, boolean isGregorian) {
        int oldDaySway = this.mDayPickerView.getValue();

        int oldDayStop = Util.getSumOfDayInMonth(oldYear, oldMonth, isGregorian);
        int newDayStop = Util.getSumOfDayInMonth(newYear, newMonth, isGregorian);
        if (oldDayStop == newDayStop) {
            if (this.mOnDateChangedListener != null) {
                this.mOnDateChangedListener.onDateChanged(getCalendarData(newYear, newMonth, oldDaySway, isGregorian));
            }
            return;
        }
        int newDaySway = oldDaySway <= newDayStop ? oldDaySway : newDayStop;
        setValuesForPickerView(this.mDayPickerView, newDaySway, 1, newDayStop, isGregorian ? this.mDisplayDaysGregorian : this.mDisplayDaysLunar, true, true);
        if (this.mOnDateChangedListener != null) {
            this.mOnDateChangedListener.onDateChanged(getCalendarData(newYear, newMonth, newDaySway, isGregorian));
        }
    }

    public void setGregorian(boolean isGregorian, boolean anim) {
        if (this.mIsGregorian == isGregorian) {
            return;
        }
        ChineseCalendar cc = (ChineseCalendar) getCalendarData().getCalendar();
        if (!checkCalendarAvailable(cc, YEAR_START, YEAR_STOP, isGregorian)) {
            cc = (ChineseCalendar) adjustCalendarByLimit(cc, YEAR_START, YEAR_STOP, isGregorian);
        }
        this.mIsGregorian = isGregorian;
        setConfigs(cc, isGregorian, anim);
    }

    private boolean checkCalendarAvailable(Calendar cc, int yearStart, int yearStop, boolean isGregorian) {
        int year = isGregorian ? cc.get(1) : ((ChineseCalendar) cc).get(801);
        return (yearStart <= year) && (year <= yearStop);
    }

    public View getNumberPickerYear() {
        return this.mYearPickerView;
    }

    public View getNumberPickerMonth() {
        return this.mMonthPickerView;
    }

    public View getNumberPickerDay() {
        return this.mDayPickerView;
    }

    public void setNumberPickerYearVisibility(int visibility) {
        setNumberPickerVisibility(this.mYearPickerView, visibility);
    }

    public void setNumberPickerMonthVisibility(int visibility) {
        setNumberPickerVisibility(this.mMonthPickerView, visibility);
    }

    public void setNumberPickerDayVisibility(int visibility) {
        setNumberPickerVisibility(this.mDayPickerView, visibility);
    }

    public void setNumberPickerVisibility(NumberPickerView view, int visibility) {
        if (view.getVisibility() == visibility) {
            return;
        }
        if ((visibility == 8) || (visibility == 0) || (visibility == 4)) {
            view.setVisibility(visibility);
        }
    }

    public boolean getIsGregorian() {
        return this.mIsGregorian;
    }

    private CalendarData getCalendarData(int pickedYear, int pickedMonthSway, int pickedDay, boolean mIsGregorian) {
        return new CalendarData(pickedYear, pickedMonthSway, pickedDay, mIsGregorian);
    }

    public CalendarData getCalendarData() {
        int pickedYear = this.mYearPickerView.getValue();
        int pickedMonthSway = this.mMonthPickerView.getValue();
        int pickedDay = this.mDayPickerView.getValue();
        return new CalendarData(pickedYear, pickedMonthSway, pickedDay, this.mIsGregorian);
    }

    public static abstract interface OnDateChangedListener {
        public abstract void onDateChanged(CalendarData paramCalendarData);
    }

    public static class CalendarData {
        public boolean isGregorian = false;
        public int pickedYear;
        public int pickedMonthSway;
        public int pickedDay;
        public ChineseCalendar chineseCalendar;

        public CalendarData(int pickedYear, int pickedMonthSway, int pickedDay, boolean isGregorian) {
            this.pickedYear = pickedYear;
            this.pickedMonthSway = pickedMonthSway;
            this.pickedDay = pickedDay;
            this.isGregorian = isGregorian;
            initChineseCalendar();
        }

        private void initChineseCalendar() {
            if (this.isGregorian) {
                this.chineseCalendar = new ChineseCalendar(this.pickedYear, this.pickedMonthSway - 1, this.pickedDay);
            } else {
                int y = this.pickedYear;
                int m = Util.convertMonthSwayToMonthLunarByYear(this.pickedMonthSway, this.pickedYear);
                int d = this.pickedDay;

                this.chineseCalendar = new ChineseCalendar(true, y, m, d);
            }
        }

        public Calendar getCalendar() {
            return this.chineseCalendar;
        }
    }

    public void setOnDateChangedListener(OnDateChangedListener listener) {
        this.mOnDateChangedListener = listener;
    }
}
