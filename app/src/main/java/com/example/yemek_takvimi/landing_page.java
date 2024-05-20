package com.example.yemek_takvimi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class landing_page extends AppCompatActivity {

    private TextView textViewSelectDate;
    private TextView textViewNonVegMainDish, textViewNonVegSideDish, textViewNonVegDessert, textViewNonVegSoup, textViewNonVegCalorie;
    private TextView textViewVegMainDish, textViewVegSideDish, textViewVegDessert, textViewVegSoup, textViewVegCalorie;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        textViewSelectDate = findViewById(R.id.textViewSelectDate);
        textViewNonVegMainDish = findViewById(R.id.textViewNonVegMainDish);
        textViewNonVegSideDish = findViewById(R.id.textViewNonVegSideDish);
        textViewNonVegDessert = findViewById(R.id.textViewNonVegDessert);
        textViewNonVegSoup = findViewById(R.id.textViewNonVegSoup);
        textViewNonVegCalorie = findViewById(R.id.textViewNonVegCalorie);
        textViewVegMainDish = findViewById(R.id.textViewVegMainDish);
        textViewVegSideDish = findViewById(R.id.textViewVegSideDish);
        textViewVegDessert = findViewById(R.id.textViewVegDessert);
        textViewVegSoup = findViewById(R.id.textViewVegSoup);
        textViewVegCalorie = findViewById(R.id.textViewVegCalorie);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Daily Menu");

        textViewSelectDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            textViewSelectDate.setText(selectedDate);
            displayMenuForSelectedDate(selectedDate);
        }, year, month, day);

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void displayMenuForSelectedDate(String selectedDate) {
        databaseReference.orderByChild("MenuDate").equalTo(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean foundNonVeg = false;
                boolean foundVeg = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String foodType = snapshot.child("FoodType").getValue(String.class);

                    if (foodType != null) {
                        if (foodType.equals("Non-vegetarian")) {
                            textViewNonVegMainDish.setText("Main Dish: " + snapshot.child("MainDish").getValue(String.class));
                            textViewNonVegSideDish.setText("Side Dish: " + snapshot.child("SideDish").getValue(String.class));
                            textViewNonVegDessert.setText("Dessert: " + snapshot.child("DessertFruit").getValue(String.class));
                            textViewNonVegSoup.setText("Soup: " + snapshot.child("Soup").getValue(String.class));
                            textViewNonVegCalorie.setText("Calories: " + snapshot.child("CalorieCount").getValue(String.class));
                            foundNonVeg = true;
                        } else if (foodType.equals("Vegetarian")) {
                            textViewVegMainDish.setText("Main Dish: " + snapshot.child("MainDish").getValue(String.class));
                            textViewVegSideDish.setText("Side Dish: " + snapshot.child("SideDish").getValue(String.class));
                            textViewVegDessert.setText("Dessert: " + snapshot.child("DessertFruit").getValue(String.class));
                            textViewVegSoup.setText("Soup: " + snapshot.child("Soup").getValue(String.class));
                            textViewVegCalorie.setText("Calories: " + snapshot.child("CalorieCount").getValue(String.class));
                            foundVeg = true;
                        }
                    }
                }

                if (!foundNonVeg) {
                    textViewNonVegMainDish.setText("Main Dish: N/A");
                    textViewNonVegSideDish.setText("Side Dish: N/A");
                    textViewNonVegDessert.setText("Dessert: N/A");
                    textViewNonVegSoup.setText("Soup: N/A");
                    textViewNonVegCalorie.setText("Calories: N/A");
                }

                if (!foundVeg) {
                    textViewVegMainDish.setText("Main Dish: N/A");
                    textViewVegSideDish.setText("Side Dish: N/A");
                    textViewVegDessert.setText("Dessert: N/A");
                    textViewVegSoup.setText("Soup: N/A");
                    textViewVegCalorie.setText("Calories: N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}
