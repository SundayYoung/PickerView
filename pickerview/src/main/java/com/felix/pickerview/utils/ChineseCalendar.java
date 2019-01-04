package com.felix.pickerview.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class ChineseCalendar extends GregorianCalendar {
    private static final long serialVersionUID = 8L;
    public static final int CHINESE_YEAR = 801;
    public static final int CHINESE_MONTH = 802;
    public static final int CHINESE_DATE = 803;
    public static final int CHINESE_SECTIONAL_TERM = 804;
    public static final int CHINESE_PRINCIPLE_TERM = 805;
    public static final int CHINESE_HEAVENLY_STEM = 806;
    public static final int CHINESE_EARTHLY_BRANCH = 807;
    public static final int CHINESE_ZODIAC = 808;
    public static final int CHINESE_TERM_OR_DATE = 888;
    private int chineseYear;
    private int chineseMonth;
    private int chineseDate;
    private int sectionalTerm;
    private int principleTerm;
    private boolean areChineseFieldsComputed;
    private boolean areSolarTermsComputed;
    private boolean lastSetChinese;
    private static final int baseYear = 1901;
    private static final int baseMonth = 1;
    private static final int baseDate = 1;
    private static final int baseIndex = 0;
    private static final int baseChineseYear = 1900;
    private static final int baseChineseMonth = 11;
    private static final int baseChineseDate = 11;

    public ChineseCalendar() {
    }

    public ChineseCalendar(Date d) {
        super.setTime(d);
    }

    public ChineseCalendar(Calendar c) {
        this(c.getTime());
    }

    public ChineseCalendar(int y, int m, int d) {
        super(y, m, d);
    }

    public ChineseCalendar(boolean isChinese, int y, int m, int d) {
        if (isChinese) {
            set(801, y);
            set(802, m);
            set(803, d);
        } else {
            set(y, m, d);
        }
    }

    @Override
    public void set(int field, int value) {
        computeIfNeed(field);
        if (isChineseField(field)) {
            switch (field) {
                case 801:
                    this.chineseYear = value;
                    break;
                case 802:
                    this.chineseMonth = value;
                    break;
                case 803:
                    this.chineseDate = value;
                    break;
                default:
                    throw new IllegalArgumentException("field not supported, field : " + field);
            }
            this.lastSetChinese = true;
        } else {
            super.set(field, value);
            this.lastSetChinese = false;
        }
        this.areFieldsSet = false;
        this.areChineseFieldsComputed = false;
        this.areSolarTermsComputed = false;
    }

    @Override
    public int get(int field) {
        computeIfNeed(field);
        if (!isChineseField(field)) {
            return super.get(field);
        }
        switch (field) {
            case 801:
                return this.chineseYear;
            case 802:
                return this.chineseMonth;
            case 803:
                return this.chineseDate;
            case 804:
                return this.sectionalTerm;
            case 805:
                return this.principleTerm;
            case 806:
                return (this.chineseYear - 4) % 10 + 1;
            case 807:
            case 808:
                return (this.chineseYear - 4) % 12 + 1;
            case 888:
                int option;
                if (get(5) == get(804)) {
                    option = 804;
                } else {
                    if (get(5) == get(805)) {
                        option = 805;
                    } else {
                        if (get(803) == 1) {
                            option = 802;
                        } else {
                            option = 803;
                        }
                    }
                }
                return option;
        }
        throw new IllegalArgumentException("field not supported, field : " + field);
    }

    @Override
    public void add(int field, int amount) {
        computeIfNeed(field);
        if (!isChineseField(field)) {
            super.add(field, amount);
            this.lastSetChinese = false;
            this.areChineseFieldsComputed = false;
            this.areSolarTermsComputed = false;
            return;
        }
        switch (field) {
            case 801:
                this.chineseYear += amount;
                break;
            case 802:
                for (int i = 0; i < amount; i++) {
                    this.chineseMonth = nextChineseMonth(this.chineseYear, this.chineseMonth);
                    if (this.chineseMonth == 1) {
                        this.chineseYear += 1;
                    }
                }
                break;
            case 803:
                int maxDate = daysInChineseMonth(this.chineseYear, this.chineseMonth);
                for (int i = 0; i < amount; i++) {
                    this.chineseDate += 1;
                    if (this.chineseDate > maxDate) {
                        this.chineseDate = 1;
                        this.chineseMonth = nextChineseMonth(this.chineseYear, this.chineseMonth);
                        if (this.chineseMonth == 1) {
                            this.chineseYear += 1;
                        }
                        maxDate = daysInChineseMonth(this.chineseYear, this.chineseMonth);
                    }
                }
            default:
                throw new IllegalArgumentException("field not supported, field : " + field);
        }
        this.lastSetChinese = true;
        this.areFieldsSet = false;
        this.areChineseFieldsComputed = false;
        this.areSolarTermsComputed = false;
    }

    @Override
    public void roll(int field, int amount) {
        computeIfNeed(field);
        if (!isChineseField(field)) {
            super.roll(field, amount);
            this.lastSetChinese = false;
            this.areChineseFieldsComputed = false;
            this.areSolarTermsComputed = false;
            return;
        }
        switch (field) {
            case 801:
                this.chineseYear += amount;
                break;
            case 802:
                for (int i = 0; i < amount; i++) {
                    this.chineseMonth = nextChineseMonth(this.chineseYear, this.chineseMonth);
                }
                break;
            case 803:
                int maxDate = daysInChineseMonth(this.chineseYear, this.chineseMonth);
                for (int i = 0; i < amount; i++) {
                    this.chineseDate += 1;
                    if (this.chineseDate > maxDate) {
                        this.chineseDate = 1;
                    }
                }
            default:
                throw new IllegalArgumentException("field not supported, field : " + field);
        }
        this.lastSetChinese = true;
        this.areFieldsSet = false;
        this.areChineseFieldsComputed = false;
        this.areSolarTermsComputed = false;
    }

    public String getChinese(int field) {
        computeIfNeed(field);
        switch (field) {
            case 801:
                return
                        getChinese(806) + getChinese(807) + "年";
            case 802:
                if (this.chineseMonth > 0) {
                    return chineseMonthNames[this.chineseMonth] + "月";
                }
                return "闰" + chineseMonthNames[(-this.chineseMonth)] + "月";
            case 803:
                return chineseDateNames[this.chineseDate];
            case 804:
                return sectionalTermNames[get(2)];
            case 805:
                return principleTermNames[get(2)];
            case 806:
                return stemNames[get(field)];
            case 807:
                return branchNames[get(field)];
            case 808:
                return animalNames[get(field)];
            case 7:
                return chineseWeekNames[get(field)];
            case 888:
                return getChinese(get(888));
        }
        throw new IllegalArgumentException("field not supported, field : " + field);
    }

    public String getSimpleGregorianDateString() {
        return
                get(1) + "-" + (get(2) + 1) + "-" + get(5);
    }

    public String getSimpleChineseDateString() {
        return

                get(801) + "-" + (get(802) > 0 ? "" + get(802) : new StringBuilder().append("*").append(-get(802)).toString()) + "-" + get(803);
    }

    public String getChineseDateString() {
        return

                getChinese(801) + getChinese(802) + getChinese(803);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getSimpleGregorianDateString()).append(" | ")
                .append(getChinese(7)).append(" | [lunar]")
                .append(getChineseDateString()).append(" ")
                .append(getChinese(808)).append("year ")
                .append(get(804)).append("day")
                .append(getChinese(804)).append(" ")
                .append(get(805)).append("day")
                .append(getChinese(805));
        return buf.toString();
    }

    private boolean isChineseField(int field) {
        switch (field) {
            case 801:
            case 802:
            case 803:
            case 804:
            case 805:
            case 806:
            case 807:
            case 808:
            case 888:
                return true;
        }
        return false;
    }

    private boolean isChineseTermsField(int field) {
        switch (field) {
            case 804:
            case 805:
            case 888:
                return true;
        }
        return false;
    }

    private void computeIfNeed(int field) {
        if (isChineseField(field)) {
            if ((!this.lastSetChinese) && (!this.areChineseFieldsComputed)) {
                super.complete();
                computeChineseFields();
                this.areFieldsSet = true;
                this.areChineseFieldsComputed = true;
                this.areSolarTermsComputed = false;
            }
            if ((isChineseTermsField(field)) && (!this.areSolarTermsComputed)) {
                computeSolarTerms();
                this.areSolarTermsComputed = true;
            }
        } else if ((this.lastSetChinese) && (!this.areFieldsSet)) {
            computeGregorianFields();
            super.complete();
            this.areFieldsSet = true;
            this.areChineseFieldsComputed = true;
            this.areSolarTermsComputed = false;
        }
    }

    private void computeGregorianFields() {
        int y = this.chineseYear;
        int m = this.chineseMonth;
        int d = this.chineseDate;
        this.areChineseFieldsComputed = true;
        this.areFieldsSet = true;
        this.lastSetChinese = false;
        if (y < 1900) {
            y = 1899;
        } else if (y > 2100) {
            y = 2101;
        }
        if (m < -12) {
            m = -12;
        } else if (m > 12) {
            m = 12;
        }
        if (d < 1) {
            d = 1;
        } else if (d > 30) {
            d = 30;
        }
        int dateint = y * 10000 + Math.abs(m) * 100 + d;
        if (dateint < 19001111) {
            set(1901, 0, 1);
            super.complete();
        } else if (dateint > 21001201) {
            set(2100, 11, 31);
            super.complete();
        } else {
            if (Math.abs(m) > 12) {
                m = 12;
            }
            int days = daysInChineseMonth(y, m);
            if (days == 0) {
                m = -m;
                days = daysInChineseMonth(y, m);
            }
            if (d > days) {
                d = days;
            }
            set(y, Math.abs(m) - 1, d);
            computeChineseFields();

            int amount = 0;
            while ((this.chineseYear != y) || (this.chineseMonth != m)) {
                amount += daysInChineseMonth(this.chineseYear, this.chineseMonth);
                this.chineseMonth = nextChineseMonth(this.chineseYear, this.chineseMonth);
                if (this.chineseMonth == 1) {
                    this.chineseYear += 1;
                }
            }
            amount += d - this.chineseDate;

            super.add(5, amount);
        }
        computeChineseFields();
    }

    private void computeChineseFields() {
        int gregorianYear = internalGet(1);
        int gregorianMonth = internalGet(2) + 1;
        int gregorianDate = internalGet(5);
        if ((gregorianYear < 1901) || (gregorianYear > 2100)) {
            return;
        }
        int startYear;
        int startMonth;
        int startDate;
        if (gregorianYear < 2000) {
            startYear = 1901;
            startMonth = 1;
            startDate = 1;
            this.chineseYear = 1900;
            this.chineseMonth = 11;
            this.chineseDate = 11;
        } else {
            startYear = 2000;
            startMonth = 1;
            startDate = 1;
            this.chineseYear = 1999;
            this.chineseMonth = 11;
            this.chineseDate = 25;
        }
        int daysDiff = 0;
        for (int i = startYear; i < gregorianYear; i++) {
            if (isGregorianLeapYear(i)) {
                daysDiff += 366;
            } else {
                daysDiff += 365;
            }
        }
        for (int i = startMonth; i < gregorianMonth; i++) {
            daysDiff += daysInGregorianMonth(gregorianYear, i - 1);
        }
        daysDiff += gregorianDate - startDate;

        this.chineseDate += daysDiff;

        int lastDate = daysInChineseMonth(this.chineseYear, this.chineseMonth);
        while (this.chineseDate > lastDate) {
            this.chineseDate -= lastDate;
            this.chineseMonth = nextChineseMonth(this.chineseYear, this.chineseMonth);
            if (this.chineseMonth == 1) {
                this.chineseYear += 1;
            }
            lastDate = daysInChineseMonth(this.chineseYear, this.chineseMonth);
        }
    }

    private void computeSolarTerms() {
        int gregorianYear = internalGet(1);
        int gregorianMonth = internalGet(2);
        if ((gregorianYear < 1901) || (gregorianYear > 2100)) {
            return;
        }
        this.sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);
        this.principleTerm = principleTerm(gregorianYear, gregorianMonth);
    }

    public static boolean isGregorianLeapYear(int year) {
        boolean isLeap = false;
        if (year % 4 == 0) {
            isLeap = true;
        }
        if (year % 100 == 0) {
            isLeap = false;
        }
        if (year % 400 == 0) {
            isLeap = true;
        }
        return isLeap;
    }

    public static int daysInGregorianMonth(int y, int m) {
        int d = daysInGregorianMonth[m];
        if ((m == 1) && (isGregorianLeapYear(y))) {
            d++;
        }
        return d;
    }

    public static int sectionalTerm(int y, int m) {

        if ((y < 1901) || (y > 2100)) {
            return 0;
        }
        int index = 0;
        int ry = y - 1901 + 1;
        while (ry >= sectionalTermYear[(m - 1)][index]) {
            index++;
        }
        int term = sectionalTermMap[(m - 1)][(4 * index + ry % 4)];
        if ((ry == 121) && (m == 4)) {
            term = 5;
        }
        if ((ry == 132) && (m == 4)) {
            term = 5;
        }
        if ((ry == 194) && (m == 6)) {
            term = 6;
        }
        return term;
    }

    public static int principleTerm(int y, int m) {

        if ((y < 1901) || (y > 2100)) {
            return 0;
        }
        int index = 0;
        int ry = y - 1901 + 1;
        while (ry >= principleTermYear[(m - 1)][index]) {
            index++;
        }
        int term = principleTermMap[(m - 1)][(4 * index + ry % 4)];
        if ((ry == 171) && (m == 3)) {
            term = 21;
        }
        if ((ry == 181) && (m == 5)) {
            term = 21;
        }
        return term;
    }

    public static int getMonthLeapByYear(int y) {
        int index = y - 1900 + 0;
        int v = 0;
        v = chineseMonths[(2 * index + 1)];
        v = v >> 4 & 0xF;
        return -v;
    }

    public static int daysInChineseMonth(int y, int m) {
        int index = y - 1900 + 0;
        int v = 0;
        int l = 0;
        int d = 30;
        if ((1 <= m) && (m <= 8)) {
            v = chineseMonths[(2 * index)];
            l = m - 1;
            if ((v >> l & 0x1) == 1) {
                d = 29;
            }
        } else if ((9 <= m) && (m <= 12)) {
            v = chineseMonths[(2 * index + 1)];
            l = m - 9;
            if ((v >> l & 0x1) == 1) {
                d = 29;
            }
        } else {
            v = chineseMonths[(2 * index + 1)];
            v = v >> 4 & 0xF;
            if (v != Math.abs(m)) {
                d = 0;
            } else {
                d = 29;
                for (int i = 0; i < bigLeapMonthYears.length; i++) {
                    if (bigLeapMonthYears[i] == index) {
                        d = 30;
                        break;
                    }
                }
            }
        }
        return d;
    }

    public static int nextChineseMonth(int y, int m) {
        int n = Math.abs(m) + 1;
        if (m > 0) {
            int index = y - 1900 + 0;
            int v = chineseMonths[(2 * index + 1)];
            v = v >> 4 & 0xF;
            if (v == m) {
                n = -m;
            }
        }
        if (n == 13) {
            n = 1;
        }
        return n;
    }

    private static final String[] chineseWeekNames = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private static final String[] chineseMonthNames = {"", "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private static final String[] chineseDateNames = {"", "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};
    private static final String[] principleTermNames = {"大寒", "雨水", "春分", "谷雨", "夏满", "夏至", "大暑", "处暑", "秋分", "霜降", "小雪", "冬至"};
    private static final String[] sectionalTermNames = {"小寒", "立春", "惊蛰", "清明", "立夏", "芒种", "小暑", "立秋", "白露", "寒露", "立冬", "大雪"};
    private static final String[] stemNames = {"", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] branchNames = {"", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    private static final String[] animalNames = {"", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    private static final int[] bigLeapMonthYears = {6, 14, 19, 25, 33, 36, 38, 41, 44, 52, 55, 79, 117, 136, 147, 150, 155, 158, 185, 193};
    private static final char[][] sectionalTermMap = {{'\007', '\006', '\006', '\006', '\006', '\006', '\006', '\006', '\006', '\005', '\006', '\006', '\006', '\005', '\005', '\006', '\006', '\005', '\005', '\005', '\005', '\005', '\005', '\005', '\005', '\004', '\005', '\005'}, {'\005', '\004', '\005', '\005', '\005', '\004', '\004', '\005', '\005', '\004', '\004', '\004', '\004', '\004', '\004', '\004', '\004', '\003', '\004', '\004', '\004', '\003', '\003', '\004', '\004', '\003', '\003', '\003'}, {'\006', '\006', '\006', '\007', '\006', '\006', '\006', '\006', '\005', '\006', '\006', '\006', '\005', '\005', '\006', '\006', '\005', '\005', '\005', '\006', '\005', '\005', '\005', '\005', '\004', '\005', '\005', '\005', '\005'}, {'\005', '\005', '\006', '\006', '\005', '\005', '\005', '\006', '\005', '\005', '\005', '\005', '\004', '\005', '\005', '\005', '\004', '\004', '\005', '\005', '\004', '\004', '\004', '\005', '\004', '\004', '\004', '\004', '\005'}, {'\006', '\006', '\006', '\007', '\006', '\006', '\006', '\006', '\005', '\006', '\006', '\006', '\005', '\005', '\006', '\006', '\005', '\005', '\005', '\006', '\005', '\005', '\005', '\005', '\004', '\005', '\005', '\005', '\005'}, {'\006', '\006', '\007', '\007', '\006', '\006', '\006', '\007', '\006', '\006', '\006', '\006', '\005', '\006', '\006', '\006', '\005', '\005', '\006', '\006', '\005', '\005', '\005', '\006', '\005', '\005', '\005', '\005', '\004', '\005', '\005', '\005', '\005'}, {'\007', '\b', '\b', '\b', '\007', '\007', '\b', '\b', '\007', '\007', '\007', '\b', '\007', '\007', '\007', '\007', '\006', '\007', '\007', '\007', '\006', '\006', '\007', '\007', '\006', '\006', '\006', '\007', '\007'}, {'\b', '\b', '\b', '\t', '\b', '\b', '\b', '\b', '\007', '\b', '\b', '\b', '\007', '\007', '\b', '\b', '\007', '\007', '\007', '\b', '\007', '\007', '\007', '\007', '\006', '\007', '\007', '\007', '\006', '\006', '\007', '\007', '\007'}, {'\b', '\b', '\b', '\t', '\b', '\b', '\b', '\b', '\007', '\b', '\b', '\b', '\007', '\007', '\b', '\b', '\007', '\007', '\007', '\b', '\007', '\007', '\007', '\007', '\006', '\007', '\007', '\007', '\007'}, {'\t', '\t', '\t', '\t', '\b', '\t', '\t', '\t', '\b', '\b', '\t', '\t', '\b', '\b', '\b', '\t', '\b', '\b', '\b', '\b', '\007', '\b', '\b', '\b', '\007', '\007', '\b', '\b', '\b'}, {'\b', '\b', '\b', '\b', '\007', '\b', '\b', '\b', '\007', '\007', '\b', '\b', '\007', '\007', '\007', '\b', '\007', '\007', '\007', '\007', '\006', '\007', '\007', '\007', '\006', '\006', '\007', '\007', '\007'}, {'\007', '\b', '\b', '\b', '\007', '\007', '\b', '\b', '\007', '\007', '\007', '\b', '\007', '\007', '\007', '\007', '\006', '\007', '\007', '\007', '\006', '\006', '\007', '\007', '\006', '\006', '\006', '\007', '\007'}};
    private static final char[][] sectionalTermYear = {{'\r', '1', 'U', 'u', '', '¹', 'É', 'ú', 'ú'}, {'\r', '-', 'Q', 'u', '', '¹', 'É', 'ú', 'ú'}, {'\r', '0', 'T', 'p', '', '¸', 'È', 'É', 'ú'}, {'\r', '-', 'L', 'l', '', '¬', 'È', 'É', 'ú'}, {'\r', ',', 'H', 'h', '', '¨', 'È', 'É', 'ú'}, {'\005', '!', 'D', '`', '|', '', '¼', 'È', 'É'}, {'\035', '9', 'U', 'x', '', '°', 'È', 'É', 'ú'}, {'\r', '0', 'L', 'h', '', '¨', 'Ä', 'È', 'É'}, {'\031', '<', 'X', 'x', '', '¸', 'È', 'É', 'ú'}, {'\020', ',', 'L', 'l', '', '¬', 'È', 'É', 'ú'}, {'\034', '<', '\\', '|', ' ', 'À', 'È', 'É', 'ú'}, {'\021', '5', 'U', '|', '', '¼', 'È', 'É', 'ú'}};
    private static final char[][] principleTermMap = {{'\025', '\025', '\025', '\025', '\025', '\024', '\025', '\025', '\025', '\024', '\024', '\025', '\025', '\024', '\024', '\024', '\024', '\024', '\024', '\024', '\024', '\023', '\024', '\024', '\024', '\023', '\023', '\024'}, {'\024', '\023', '\023', '\024', '\024', '\023', '\023', '\023', '\023', '\023', '\023', '\023', '\023', '\022', '\023', '\023', '\023', '\022', '\022', '\023', '\023', '\022', '\022', '\022', '\022', '\022', '\022', '\022'}, {'\025', '\025', '\025', '\026', '\025', '\025', '\025', '\025', '\024', '\025', '\025', '\025', '\024', '\024', '\025', '\025', '\024', '\024', '\024', '\025', '\024', '\024', '\024', '\024', '\023', '\024', '\024', '\024', '\024'}, {'\024', '\025', '\025', '\025', '\024', '\024', '\025', '\025', '\024', '\024', '\024', '\025', '\024', '\024', '\024', '\024', '\023', '\024', '\024', '\024', '\023', '\023', '\024', '\024', '\023', '\023', '\023', '\024', '\024'}, {'\025', '\026', '\026', '\026', '\025', '\025', '\026', '\026', '\025', '\025', '\025', '\026', '\025', '\025', '\025', '\025', '\024', '\025', '\025', '\025', '\024', '\024', '\025', '\025', '\024', '\024', '\024', '\025', '\025'}, {'\026', '\026', '\026', '\026', '\025', '\026', '\026', '\026', '\025', '\025', '\026', '\026', '\025', '\025', '\025', '\026', '\025', '\025', '\025', '\025', '\024', '\025', '\025', '\025', '\024', '\024', '\025', '\025', '\025'}, {'\027', '\027', '\030', '\030', '\027', '\027', '\027', '\030', '\027', '\027', '\027', '\027', '\026', '\027', '\027', '\027', '\026', '\026', '\027', '\027', '\026', '\026', '\026', '\027', '\026', '\026', '\026', '\026', '\027'}, {'\027', '\030', '\030', '\030', '\027', '\027', '\030', '\030', '\027', '\027', '\027', '\030', '\027', '\027', '\027', '\027', '\026', '\027', '\027', '\027', '\026', '\026', '\027', '\027', '\026', '\026', '\026', '\027', '\027'}, {'\027', '\030', '\030', '\030', '\027', '\027', '\030', '\030', '\027', '\027', '\027', '\030', '\027', '\027', '\027', '\027', '\026', '\027', '\027', '\027', '\026', '\026', '\027', '\027', '\026', '\026', '\026', '\027', '\027'}, {'\030', '\030', '\030', '\030', '\027', '\030', '\030', '\030', '\027', '\027', '\030', '\030', '\027', '\027', '\027', '\030', '\027', '\027', '\027', '\027', '\026', '\027', '\027', '\027', '\026', '\026', '\027', '\027', '\027'}, {'\027', '\027', '\027', '\027', '\026', '\027', '\027', '\027', '\026', '\026', '\027', '\027', '\026', '\026', '\026', '\027', '\026', '\026', '\026', '\026', '\025', '\026', '\026', '\026', '\025', '\025', '\026', '\026', '\026'}, {'\026', '\026', '\027', '\027', '\026', '\026', '\026', '\027', '\026', '\026', '\026', '\026', '\025', '\026', '\026', '\026', '\025', '\025', '\026', '\026', '\025', '\025', '\025', '\026', '\025', '\025', '\025', '\025', '\026'}};
    private static final char[][] principleTermYear = {{'\r', '-', 'Q', 'q', '', '¹', 'É'}, {'\025', '9', ']', '}', '¡', 'Á', 'É'}, {'\025', '8', 'X', 'x', '', '¼', 'È', 'É'}, {'\025', '1', 'Q', 't', '', '°', 'È', 'É'}, {'\021', '1', 'M', 'p', '', '¨', 'È', 'É'}, {'\034', '<', 'X', 't', '', '´', 'È', 'É'}, {'\031', '5', 'T', 'p', '', '¬', 'È', 'É'}, {'\035', '9', 'Y', 'x', '', '´', 'È', 'É'}, {'\021', '-', 'I', 'l', '', '¨', 'È', 'É'}, {'\034', '<', '\\', '|', ' ', 'À', 'È', 'É'}, {'\020', ',', 'P', 'p', '', '´', 'È', 'É'}, {'\021', '5', 'X', 'x', '', '¼', 'È', 'É'}};
    private static final char[] daysInGregorianMonth = {'\037', '\034', '\037', '\036', '\037', '\036', '\037', '\037', '\036', '\037', '\036', '\037'};
    private static final char[] chineseMonths = {'\000', '\004', '­', '\b', 'Z', '\001', 'Õ', 'T', '´', '\t', 'd', '\005', 'Y', 'E', '', '\n', '¦', '\004', 'U', '$', '­', '\b', 'Z', 'b', 'Ú', '\004', '´', '\005', '´', 'U', 'R', '\r', '', '\n', 'J', '*', 'V', '\002', 'm', 'q', 'm', '\001', 'Ú', '\002', 'Ò', 'R', '©', '\005', 'I', '\r', '*', 'E', '+', '\t', 'V', '\001', 'µ', ' ', 'm', '\001', 'Y', 'i', 'Ô', '\n', '¨', '\005', '©', 'V', '¥', '\004', '+', '\t', '', '8', '¶', '\b', 'ì', 't', 'l', '\005', 'Ô', '\n', 'ä', 'j', 'R', '\005', '', '\n', 'Z', 'B', '[', '\004', '¶', '\004', '´', '"', 'j', '\005', 'R', 'u', 'É', '\n', 'R', '\005', '5', 'U', 'M', '\n', 'Z', '\002', ']', '1', 'µ', '\002', 'j', '', 'h', '\005', '©', '\n', '', 'j', '*', '\005', '-', '\t', 'ª', 'H', 'Z', '\001', 'µ', '\t', '°', '9', 'd', '\005', '%', 'u', '', '\n', '', '\004', 'M', 'T', '­', '\004', 'Ú', '\004', 'Ô', 'D', '´', '\005', 'T', '', 'R', '\r', '', '\n', 'V', 'j', 'V', '\002', 'm', '\002', 'j', 'A', 'Ú', '\002', '²', '¡', '©', '\005', 'I', '\r', '\n', 'm', '*', '\t', 'V', '\001', '­', 'P', 'm', '\001', 'Ù', '\002', 'Ñ', ':', '¨', '\005', ')', '', '¥', '\f', '*', '\t', '', 'T', '¶', '\b', 'l', '\t', 'd', 'E', 'Ô', '\n', '¤', '\005', 'Q', '%', '', '\n', '*', 'r', '[', '\004', '¶', '\004', '¬', 'R', 'j', '\005', 'Ò', '\n', '¢', 'J', 'J', '\005', 'U', '', '-', '\n', 'Z', '\002', 'u', 'a', 'µ', '\002', 'j', '\003', 'a', 'E', '©', '\n', 'J', '\005', '%', '%', '-', '\t', '', 'h', 'Ú', '\b', '´', '\t', '¨', 'Y', 'T', '\003', '¥', '\n', '', ':', '', '\004', '­', '°', '­', '\004', 'Ú', '\004', 'ô', 'b', '´', '\005', 'T', '\013', 'D', ']', 'R', '\n', '', '\004', 'U', '"', 'm', '\002', 'Z', 'q', 'Ú', '\002', 'ª', '\005', '²', 'U', 'I', '\013', 'J', '\n', '-', '9', '6', '\001', 'm', '', 'm', '\001', 'Ù', '\002', 'é', 'j', '¨', '\005', ')', '\013', '', 'L', 'ª', '\b', '¶', '\b', '´', '8', 'l', '\t', 'T', 'u', 'Ô', '\n', '¤', '\005', 'E', 'U', '', '\n', '', '\004', 'U', 'D', 'µ', '\004', 'j', '', 'j', '\005', 'Ò', '\n', '', 'j', 'J', '\005', 'U', '\n', '*', 'J', 'Z', '\002', 'µ', '\002', '²', '1', 'i', '\003', '1', 's', '©', '\n', 'J', '\005', '-', 'U', '-', '\t', 'Z', '\001', 'Õ', 'H', '´', '\t', 'h', '', 'T', '\013', '¤', '\n', '¥', 'j', '', '\004', '­', '\b', 'j', 'D', 'Ú', '\004', 't', '\005', '°', '%', 'T', '\003'};
}
