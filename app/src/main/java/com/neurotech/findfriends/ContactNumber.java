package com.neurotech.findfriends;

public class ContactNumber {

    private String mDisplayName;
    private String mPhoneNumber;

    public ContactNumber(String mDisplayName, String mPhoneNumber) {
        this.mDisplayName = mDisplayName;
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }
}
