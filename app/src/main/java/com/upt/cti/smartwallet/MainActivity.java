package com.upt.cti.smartwallet;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView message;
    EditText income, expenses;
    Button bUpdate;
    Spinner spinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = findViewById(R.id.message);
        income = findViewById(R.id.income);
        expenses = findViewById(R.id.expenses);
        bUpdate = findViewById(R.id.bUpdate);
        spinner = findViewById(R.id.spinner);

        List<String> monthNames = new ArrayList<>();
        List<MonthlyExpenses> monthlyExpensesList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    MonthlyExpenses monthlyExpenses = childSnapshot.getValue(MonthlyExpenses.class);
                    monthNames.add(monthlyExpenses.getMonth());
                    monthlyExpensesList.add(monthlyExpenses);
                }
                ArrayAdapter<String> sAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, monthNames);
                sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(sAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bUpdate:
                DocumentReference monthReference = db.collection("calendar").document(spinner.getSelectedItem().toString());
                Integer updatedIncome = Integer.parseInt(income.getText().toString());
                Integer updatedExpenses = Integer.parseInt(expenses.getText().toString());

                monthReference
                        .update(
                                "income", updatedIncome,
                                "expenses", updatedExpenses
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                message.setText(spinner.getSelectedItem().toString() + " was updated");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                message.setText("Error while updating");
                            }
                        });
                break;
        }
    }
}