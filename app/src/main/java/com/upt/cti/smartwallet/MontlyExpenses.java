package com.upt.cti.smartwallet;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
class MonthlyExpenses {

    public String month;
    private float income, expenses;

    public MonthlyExpenses() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public MonthlyExpenses(String month, float income, float expenses) {
        this.month = month;
        this.income = income;
        this.expenses = expenses;
    }

    public String getMonth() {
        return month;
    }

    public float getExpenses() {
        return expenses;
    }

    public float getIncome() {
        return income;
    }
}
