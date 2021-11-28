package com.upt.cti.smartwallet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPaymentActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        setTitle("Add or edit payment");

        // ui
        EditText eName = (EditText) findViewById(R.id.eName);
        EditText eCost = (EditText) findViewById(R.id.eCost);
        Spinner sType = (Spinner) findViewById(R.id.sType);
        TextView tTimestamp = (TextView) findViewById(R.id.tTimestamp);
        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        Button deleteBtn = (Button) findViewById(R.id.deleteBtn);

        // spinner adapter
        List<String> types = PaymentType.getTypes();
        final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this,

                android.R.layout.simple_spinner_item, types);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(sAdapter);

        // initialize UI if editing
        Payment payment = AppState.get().getCurrentPayment();
        if (payment != null) {
            eName.setText(payment.getName());
            eCost.setText(String.valueOf(payment.getCost()));
            tTimestamp.setText("Time of payment: " + payment.getTimestamp());
            try {
                sType.setSelection(types.indexOf(payment.getType()));
            } catch (Exception e) {
            }
        } else {
            tTimestamp.setText("No payment selected");
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payment != null){
                    final DocumentReference docRef = db.collection("wallet2").document(payment.getId().toString());
                    String name = eName.getText().toString();
                    double cost = Double.parseDouble(eCost.getText().toString());
                    String type = sType.getSelectedItem().toString();

                    docRef.update(
                            "cost", cost,
                            "name", name,
                            "type", type,
                            "timestamp", getCurrentTimeDate()
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            tTimestamp.setText("Payment updated");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tTimestamp.setText("Update failed");
                        }
                    });
                }
                else {
                    CollectionReference documentReference = db.collection("wallet2");
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshots = task.getResult();
                                Integer length = snapshots.size();

                                String name = eName.getText().toString();
                                double cost = Double.parseDouble(eCost.getText().toString());
                                String type = sType.getSelectedItem().toString();

                                Map<String, Object> newPayment = new HashMap<>();
                                newPayment.put("cost", cost);
                                newPayment.put("name", name);
                                newPayment.put("timestamp", getCurrentTimeDate());
                                newPayment.put("type", type);

                                db.collection("wallet2").document(length.toString()).set(newPayment);
                            }
                        }
                    });


                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("wallet2").document(payment.getId().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Deleted");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Failed to delete");
                            }
                        });
            }
        });
    }

    public static String getCurrentTimeDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }
}