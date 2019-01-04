package com.felix.pickerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.felix.pickerview.R;
import com.felix.pickerview.bean.AreaBean;
import com.felix.pickerview.bean.CityBean;
import com.felix.pickerview.bean.JsonBean;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocalPickerView extends LinearLayout implements NumberPickerView.OnValueChangeListener {
    List<JsonBean> jsonBean;
    private NumberPickerView mProvideView;
    private NumberPickerView mCityView;
    private NumberPickerView mLocalView;
    LocalPickerView.OnDateChangedListener mOnDateChangedListener;
    private int mProIndexResult;
    private int mCityIndexResult;
    private int mLocalIndexResult;
    private static int NORMAL_VALUE_PRO_INDEX = 0;
    private static int NORMAL_VALUE_CITY_INDEX = 0;
    private static int NORMAL_VALUE_LOCAL_INDEX = 0;

    public LocalPickerView(Context context) {
        this(context, null);
    }

    public LocalPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocalPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mProIndexResult = 0;
        this.mCityIndexResult = 0;
        this.mLocalIndexResult = 0;
        View contentView = inflate(context, R.layout.view_gregorian_lunar_calendar, this);
        this.mProvideView = contentView.findViewById(R.id.picker_year);
        this.mCityView = contentView.findViewById(R.id.picker_month);
        this.mLocalView = contentView.findViewById(R.id.picker_day);
    }

    public void setData(List<JsonBean> list) {
        this.jsonBean = list;
        setDataIndex(0, 0, 0);
    }

    public void setDataIndex(int pIndex, int cIndex, int lIndex) {
        this.setPro(pIndex);
        this.setCity(pIndex, cIndex);
        this.setLocal(pIndex, cIndex, lIndex);
        this.mProvideView.setOnValueChangedListener(this);
        this.mCityView.setOnValueChangedListener(this);
        this.mLocalView.setOnValueChangedListener(this);
        if (this.mOnDateChangedListener != null) {
            this.mOnDateChangedListener.onDateChanged(this.getResult(pIndex, cIndex, lIndex));
        }

    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (oldVal != newVal) {
            LocalPickerView.LocationResult mLopri;
            if (picker == this.mProvideView) {
                this.mProIndexResult = newVal;
                this.setCity(newVal, NORMAL_VALUE_CITY_INDEX);
                this.setLocal(newVal, NORMAL_VALUE_CITY_INDEX, NORMAL_VALUE_LOCAL_INDEX);
                mLopri = this.getResult(this.mProIndexResult, NORMAL_VALUE_CITY_INDEX, NORMAL_VALUE_LOCAL_INDEX);
                if (this.mOnDateChangedListener != null) {
                    this.mOnDateChangedListener.onDateChanged(mLopri);
                }
            } else if (picker == this.mCityView) {
                this.mCityIndexResult = newVal;
                this.setLocal(this.mProIndexResult, newVal, NORMAL_VALUE_LOCAL_INDEX);
                mLopri = this.getResult(this.mProIndexResult, this.mCityIndexResult, 0);
                if (this.mOnDateChangedListener != null) {
                    this.mOnDateChangedListener.onDateChanged(mLopri);
                }
            } else if (picker == this.mLocalView) {
                this.mLocalIndexResult = newVal;
                mLopri = this.getResult(this.mProIndexResult, this.mCityIndexResult, this.mLocalIndexResult);
                if (this.mOnDateChangedListener != null) {
                    this.mOnDateChangedListener.onDateChanged(mLopri);
                }
            }

        }
    }

    private void setPro(int pIndex) {
        this.mProvideView.setDisplayedValusArray(this.getNameForPri(this.jsonBean));
        this.mProvideView.setMaxValue(this.getNameForPri(this.jsonBean).size() - 1);
        this.mProvideView.setValue(pIndex);
    }

    private void setCity(int pIndex, int cIndex) {
        this.mCityView.setDisplayedValusArray(this.getNameForCity(((JsonBean) this.jsonBean.get(pIndex)).getCity()));
        this.mCityView.setMaxValue(this.getNameForCity(((JsonBean) this.jsonBean.get(pIndex)).getCity()).size() - 1);
        this.mCityView.setValue(cIndex);
    }

    private void setLocal(int pIndex, int cIndex, int lIndex) {
        List<AreaBean> alist = this.jsonBean.get(pIndex).getCity().get(cIndex).getArea();
        if (alist != null && alist.size() > 0) {
            this.mLocalView.setDisplayedValusArray(this.getNameForLocal(this.jsonBean.get(pIndex).getCity().get(cIndex).getArea()));
            this.mLocalView.setMaxValue(this.getNameForLocal(this.jsonBean.get(pIndex).getCity().get(cIndex).getArea()).size() - 1);
            this.mLocalView.setValue(lIndex);
        } else {
            ArrayList<String> strings = new ArrayList();
            strings.add(this.jsonBean.get(pIndex).getCity().get(cIndex).getCityName());
            this.mLocalView.setDisplayedValusArray(strings);
            this.mLocalView.setMaxValue(strings.size() - 1);
            this.mLocalView.setValue(0);
        }

    }

    private ArrayList<String> getNameForPri(List<JsonBean> jsonListH) {
        ArrayList<String> mProviceList = new ArrayList();
        Iterator var3 = jsonListH.iterator();

        while (var3.hasNext()) {
            JsonBean jbean = (JsonBean) var3.next();
            mProviceList.add(jbean.getProvinceName());
        }

        return mProviceList;
    }

    private ArrayList<String> getNameForCity(List<CityBean> cityList) {
        ArrayList<String> result = new ArrayList();
        Iterator var3 = cityList.iterator();

        while (var3.hasNext()) {
            CityBean cityBean = (CityBean) var3.next();
            result.add(cityBean.getCityName());
        }

        return result;
    }

    private ArrayList<String> getNameForLocal(List<AreaBean> areaList) {
        ArrayList<String> result = new ArrayList();
        Iterator var3 = areaList.iterator();

        while (var3.hasNext()) {
            AreaBean areaBean = (AreaBean) var3.next();
            result.add(areaBean.getAreaName());
        }

        return result;
    }

    private LocalPickerView.LocationResult getResult(int pIndex, int cIndex, int lIndex) {
        JsonBean mRjpBean = this.jsonBean.get(pIndex);
        CityBean mRjcbean = this.jsonBean.get(pIndex).getCity().get(cIndex);
        List<AreaBean> alist = this.jsonBean.get(pIndex).getCity().get(cIndex).getArea();
        String mProName = mRjpBean.getProvinceName();
        String mProCode = mRjpBean.getProvinceCode();
        String mCityName = mRjcbean.getCityName();
        String mCityCode = mRjcbean.getCityCode();
        String mLocalname;
        String mLocalCode;
        if (alist != null && alist.size() > 0) {
            AreaBean mRarbean = this.jsonBean.get(pIndex).getCity().get(cIndex).getArea().get(lIndex);
            mLocalname = mRarbean.getAreaName();
            mLocalCode = mRarbean.getAreaCode();
        } else {
            mLocalname = mCityName;
            mLocalCode = mCityCode;
        }

        return new LocalPickerView.LocationResult(mProName, mCityName, mLocalname, mProCode, mCityCode, mLocalCode);
    }

    public void setOnDateChangedListener(LocalPickerView.OnDateChangedListener listener) {
        this.mOnDateChangedListener = listener;
    }

    public class LocationResult {
        private String mRprivode;
        private String mRcity;
        private String mRlocal;
        private String mProCode;
        private String mCityCode;
        private String mLocalCode;

        public LocationResult() {
        }

        public LocationResult(String mRprivode, String mRcity, String mRlocal, String mProCode, String mCityCode, String mLocalCode) {
            this.mRprivode = mRprivode;
            this.mRcity = mRcity;
            this.mRlocal = mRlocal;
            this.mProCode = mProCode;
            this.mCityCode = mCityCode;
            this.mLocalCode = mLocalCode;
        }

        public String getmRprivode() {
            return this.mRprivode;
        }

        public void setmRprivode(String mRprivode) {
            this.mRprivode = mRprivode;
        }

        public String getmRcity() {
            return this.mRcity;
        }

        public void setmRcity(String mRcity) {
            this.mRcity = mRcity;
        }

        public String getmRlocal() {
            return this.mRlocal;
        }

        public void setmRlocal(String mRlocal) {
            this.mRlocal = mRlocal;
        }

        public String getmProCode() {
            return this.mProCode;
        }

        public void setmProCode(String mProCode) {
            this.mProCode = mProCode;
        }

        public String getmCityCode() {
            return this.mCityCode;
        }

        public void setmCityCode(String mCityCode) {
            this.mCityCode = mCityCode;
        }

        public String getmLocalCode() {
            return this.mLocalCode;
        }

        public void setmLocalCode(String mLocalCode) {
            this.mLocalCode = mLocalCode;
        }

        @Override
        public String toString() {
            return "LocationResult{mRprivode='" + this.mRprivode + '\'' + ", mRcity='" + this.mRcity + '\'' + ", mRlocal='" + this.mRlocal + '\'' + ", mProCode='" + this.mProCode + '\'' + ", mCityCode='" + this.mCityCode + '\'' + ", mLocalCode='" + this.mLocalCode + '\'' + '}';
        }
    }

    public interface OnDateChangedListener {
        void onDateChanged(LocalPickerView.LocationResult var1);
    }
}
