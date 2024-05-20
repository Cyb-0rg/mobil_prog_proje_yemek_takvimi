package com.example.yemek_takvimi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class add_new_menu extends AppCompatActivity {
    private Spinner spinnerFoodType;
    private EditText editTextMainDish, editTextSideDish, editTextSoup, editTextDessertFruit, editTextCalorieCount;
    private DatePicker datePickerMenuDate;
    private Button buttonAddMenu;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_menu);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Daily Menu");

        // Initialize the views
        spinnerFoodType = findViewById(R.id.spinnerFoodType);
        editTextMainDish = findViewById(R.id.editTextMainDish);
        editTextSideDish = findViewById(R.id.editTextSideDish);
        editTextSoup = findViewById(R.id.editTextSoup);
        editTextDessertFruit = findViewById(R.id.editTextDessertFruit);
        editTextCalorieCount = findViewById(R.id.editTextCalorieCount);
        datePickerMenuDate = findViewById(R.id.datePickerMenuDate);
        buttonAddMenu = findViewById(R.id.buttonAddMenu);

        // Set minimum date to today
        Calendar calendar = Calendar.getInstance();
        datePickerMenuDate.setMinDate(calendar.getTimeInMillis());

        // Set OnClickListener for Add Menu button
        buttonAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu();
            }
        });
    }

    private void addMenu() {
        String foodType = spinnerFoodType.getSelectedItem().toString();
        String mainDish = editTextMainDish.getText().toString().trim();
        String sideDish = editTextSideDish.getText().toString().trim();
        String soup = editTextSoup.getText().toString().trim();
        String dessertFruit = editTextDessertFruit.getText().toString().trim();
        String calorieCount = editTextCalorieCount.getText().toString().trim();

        int day = datePickerMenuDate.getDayOfMonth();
        int month = datePickerMenuDate.getMonth() + 1; // Month is 0-based in DatePicker
        int year = datePickerMenuDate.getYear();
        String menuDate = day + "/" + month + "/" + year;

        if (foodType.isEmpty() || mainDish.isEmpty() || sideDish.isEmpty() || soup.isEmpty() || dessertFruit.isEmpty() || calorieCount.isEmpty()) {
            Toast.makeText(add_new_menu.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            checkDuplicateAndSaveMenu(foodType, mainDish, sideDish, soup, dessertFruit, calorieCount, menuDate);
        }
    }

    private void checkDuplicateAndSaveMenu(String foodType, String mainDish, String sideDish, String soup, String dessertFruit, String calorieCount, String menuDate) {
        databaseReference.orderByChild("MenuDate").equalTo(menuDate).addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isDuplicate = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String existingFoodType = snapshot.child("FoodType").getValue(String.class);
                    if (existingFoodType != null && existingFoodType.equals(foodType)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    Toast.makeText(add_new_menu.this, "Menu for the same food type and date already exists. Please change the date or food type.", Toast.LENGTH_LONG).show();
                } else {
                    saveMenuToDatabase(foodType, mainDish, sideDish, soup, dessertFruit, calorieCount, menuDate);
                }
            }


            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(add_new_menu.this, "Failed to check duplicates. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMenuToDatabase(String foodType, String mainDish, String sideDish, String soup, String dessertFruit, String calorieCount, String menuDate) {
        String key = databaseReference.push().getKey();
        Map<String, String> menuData = new HashMap<>();
        menuData.put("FoodType", foodType);
        menuData.put("MainDish", mainDish);
        menuData.put("SideDish", sideDish);
        menuData.put("Soup", soup);
        menuData.put("DessertFruit", dessertFruit);
        menuData.put("CalorieCount", calorieCount);
        menuData.put("MenuDate", menuDate);

        if (key != null) {
            databaseReference.child(key).setValue(menuData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(add_new_menu.this, "Menu added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(add_new_menu.this, "Failed to add menu. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}