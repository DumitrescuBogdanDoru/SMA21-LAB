package com.upt.cti.smartwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Payments extends AppCompatActivity {

    // firebase
    private DatabaseReference databaseReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int currentMonth;
    private List<Payment> payments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        TextView tStatus = (TextView) findViewById(R.id.tStatus);
        Button bPrevious = (Button) findViewById(R.id.bPrevious);
        Button bNext = (Button) findViewById(R.id.bNext);
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        ListView listPayments = (ListView) findViewById(R.id.listPayments);
        final PaymentAdapter adapter = new PaymentAdapter(this, R.layout.item_payment, payments);
        listPayments.setAdapter(adapter);

        CollectionReference documentReference = db.collection("wallet2");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshots = task.getResult();
                    if (!snapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : snapshots) {
                            Payment payment = documentSnapshot.toObject(Payment.class);
                            String id = documentSnapshot.getId();
                            payment.setId(Integer.parseInt(id));
                            payments.add(payment);
                            tStatus.setText("Payments were found");
                        }
                    } else {
                        tStatus.setText("Error while getting payments");
                    }
                }
            }
        });

        listPayments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppState.get().setDatabaseReference(db);
                AppState.get().setCurrentPayment(payments.get(i));
                startActivity(new Intent(getApplicationContext(), AddPaymentActivity.class));
            }
        });


    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                startActivity(new Intent(getApplicationContext(), AddPaymentActivity.class));
        }
    }
}