package com.example.stockexample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dreesj1 on 12/17/14.
 */
public class MyCompany implements Parcelable {

    private String companySymbol;
    private String companyName;
    private String companyStock;

    public MyCompany(String companySymbol, String companyName, String companyStock) {
        super();
        this.companySymbol = companySymbol;
        this.companyName = companyName;
        this.companyStock = companyStock;
    }

    public String getCompanySymbol() {
        return companySymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyStock() {
        return companyStock;
    }

    public void setCompanySymbol(String companySymbol) {
        this.companySymbol = companySymbol;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyStock(String companyStock) {
        this.companyStock = companyStock;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof MyCompany) {
            MyCompany company = (MyCompany) o;
            if (companySymbol == null)
                return (company.companySymbol == null);
            else
                return companySymbol.equals(company.companySymbol);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    private MyCompany(Parcel in) {
        companySymbol = in.readString();
        companyName = in.readString();
        companyStock = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(companySymbol);
        out.writeString(companyName);
        out.writeString(companyStock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MyCompany> CREATOR = new Parcelable.Creator<MyCompany>() {
        public MyCompany createFromParcel(Parcel in) {
            return new MyCompany(in);
        }

        public MyCompany[] newArray(int size) {
            return new MyCompany[size];
        }
    };
}
