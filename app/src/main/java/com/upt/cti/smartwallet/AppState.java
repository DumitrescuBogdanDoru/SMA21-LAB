package com.upt.cti.smartwallet;

import com.google.firebase.firestore.FirebaseFirestore;

public class AppState {
    private static AppState singletonObject;

    public static synchronized AppState get() {
        if (singletonObject == null) {
            singletonObject = new AppState();
        }
        return singletonObject;
    }

    private FirebaseFirestore databaseReference;
    private Payment currentPayment;

    public FirebaseFirestore getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(FirebaseFirestore databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void setCurrentPayment(Payment currentPayment) {
        this.currentPayment = currentPayment;
    }

    public Payment getCurrentPayment() {
        return currentPayment;
    }
}
