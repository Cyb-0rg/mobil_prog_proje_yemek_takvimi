package com.example.yemek_takvimi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class edit_menu extends AppCompatActivity {

    // Declare your views
    private DatePicker datePickerEditMenu;
    private Spinner spinnerFoodTypeEditMenu;
    private TextView textViewMenuDateEditMenu;
    private TextView textViewSideDishEditMenu;
    private TextView textViewdessertEditMenu;
    private TextView textViewSoupEditMenu;

    private TextView textViewCalorieEditMenu;


    private Spinner spinnerVegetarianEditMenu;
    private TextView textViewMainDishEditMenu;
    // Declare other TextViews for additional menu details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        // Initialize views
        datePickerEditMenu = findViewById(R.id.datePickerEditMenu);
        spinnerFoodTypeEditMenu = findViewById(R.id.spinnerFoodTypeEditMenu);
        Button buttonShowMenu = findViewById(R.id.buttonShowMenu);
        Button buttonDeleteMenu = findViewById(R.id.buttonDeleteMenu);
        Button buttonUpdateMenu = findViewById(R.id.buttonUpdateMenu);


        textViewMenuDateEditMenu = findViewById(R.id.textViewMenuDateEditMenu);
        textViewMainDishEditMenu = findViewById(R.id.textViewMainDishEditMenu);
        textViewSideDishEditMenu = findViewById(R.id.textViewSideDishEditMenu);
        textViewdessertEditMenu = findViewById(R.id.textViewdessertEditMenu);
        textViewCalorieEditMenu = findViewById(R.id.textViewCalorieEditMenu);
        textViewSoupEditMenu = findViewById(R.id.textViewSoupEditMenu12);

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

// Set the minimum date to today
        datePickerEditMenu.setMinDate(calendar.getTimeInMillis());

// Set the current date as the default selected date
        datePickerEditMenu.init(currentYear, currentMonth, currentDay, null);

        buttonDeleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected date and food type
                int day = datePickerEditMenu.getDayOfMonth();
                int month = datePickerEditMenu.getMonth() + 1; // Month is zero-based
                int year = datePickerEditMenu.getYear();
                String foodType = spinnerFoodTypeEditMenu.getSelectedItem().toString();
                String menuDate = day + "/" + month + "/" + year;

                // Delete the menu record from Firebase
                deleteMenu(menuDate, foodType);
            }
        });



        // Set OnClickListener for the "Show Menu" button
        buttonShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected date from DatePicker
                int day = datePickerEditMenu.getDayOfMonth();
                int month = datePickerEditMenu.getMonth() + 1; // Month is zero-based
                int year = datePickerEditMenu.getYear();

                // Get selected food type from Spinner
                String foodType = spinnerFoodTypeEditMenu.getSelectedItem().toString();

                // Construct the menu date string
                String menuDate = day + "/" + month + "/" + year;

                // Set the menu date in the TextView
                textViewMenuDateEditMenu.setText("Today's Menu - " + menuDate);

                // Populate menu details based on the selected date and food type
                populateMenuDetails(menuDate , foodType);
            }
        });

        buttonUpdateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected date and food type
                int day = datePickerEditMenu.getDayOfMonth();
                int month = datePickerEditMenu.getMonth() + 1; // Month is zero-based
                int year = datePickerEditMenu.getYear();
                String foodType = spinnerFoodTypeEditMenu.getSelectedItem().toString();
                String menuDate = day + "/" + month + "/" + year;

                // Check if menu exists for the selected date and food type
                checkMenuExists(menuDate, foodType);
            }
        });

    }

    // Method to populate menu details based on date and food type
    private void populateMenuDetails(String menuDate, String foodType) {
        // Assuming you have a Firebase database reference
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Daily Menu");

        // Query the database to retrieve the menu for the specified date and food type
        Query query = menuRef.orderByChild("MenuDate").equalTo(menuDate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String type = snapshot.child("FoodType").getValue(String.class);

                        //Toast.makeText(edit_menu.this, " " + snapshot.child("FoodType").getValue(String.class), Toast.LENGTH_SHORT).show();


                        if (type != null && type.equals(foodType)) {
                            found = true;
                            // Retrieve menu details from the snapshot
                            String mainDish = snapshot.child("MainDish").getValue(String.class);
                            String sideDish = snapshot.child("SideDish").getValue(String.class);
                            String dessertFruit = snapshot.child("DessertFruit").getValue(String.class);
                            String soup = snapshot.child("Soup").getValue(String.class);

                            String calorieCount = snapshot.child("CalorieCount").getValue(String.class);
   /*

*/
                            //Toast.makeText(edit_menu.this, " " + soup, Toast.LENGTH_SHORT).show();
                            textViewMainDishEditMenu.setText("Main Dish: " + mainDish);
                            textViewSideDishEditMenu.setText("Side Dish: " + sideDish);
                            textViewdessertEditMenu.setText("Dessert/Fruit: " + dessertFruit);
                            textViewCalorieEditMenu.setText("Calorie Count: " + calorieCount);
                            textViewSoupEditMenu.setText("Soup: " + soup);


                            // Display a toast message indicating success
                            Toast.makeText(edit_menu.this, "Menu data loaded successfully!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if (!found) {
                        // No menu found for the selected date and food type
                        Toast.makeText(edit_menu.this, "No menu available for the selected date and food type.", Toast.LENGTH_SHORT).show();
                        // Optionally, clear TextViews or show a message in the UI
                        // Optionally, clear TextViews or show a message in the UI
                        textViewMainDishEditMenu.setText("****");
                        textViewSideDishEditMenu.setText("****");
                        textViewdessertEditMenu.setText("****");
                        textViewCalorieEditMenu.setText("****");
                        textViewSoupEditMenu.setText("****");
                    }
                } else {
                    // Menu data not found for the specified date
                    Toast.makeText(edit_menu.this, "No menu data available for the selected date.", Toast.LENGTH_SHORT).show();
                    // Optionally, clear TextViews or show a message in the UI
                    textViewMainDishEditMenu.setText("****");
                    textViewSideDishEditMenu.setText("****");
                    textViewdessertEditMenu.setText("****");
                    textViewCalorieEditMenu.setText("****");
                    textViewSoupEditMenu.setText("****");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential database error
                Toast.makeText(edit_menu.this, "Failed to retrieve menu details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMenu(String menuDate, String foodType) {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Daily Menu");
        Query query = menuRef.orderByChild("MenuDate").equalTo(menuDate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String type = snapshot.child("FoodType").getValue(String.class);
                    if (type != null && type.equals(foodType)) {
                        snapshot.getRef().removeValue(); // Delete the menu record
                        // Update UI or display a message indicating success
                        Toast.makeText(edit_menu.this, "Menu deleted successfully!", Toast.LENGTH_SHORT).show();
                        // Optionally, clear TextViews or show a message in the UI
                        textViewMainDishEditMenu.setText("****");
                        textViewSideDishEditMenu.setText("****");
                        textViewdessertEditMenu.setText("****");
                        textViewCalorieEditMenu.setText("****");
                        textViewSoupEditMenu.setText("****");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential database error
                Toast.makeText(edit_menu.this, "Failed to delete menu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkMenuExists(String menuDate, String foodType) {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Daily Menu");
        Query query = menuRef.orderByChild("MenuDate").equalTo(menuDate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String type = snapshot.child("FoodType").getValue(String.class);
                        if (type != null && type.equals(foodType)) {
                            found = true;
                            // Menu exists, redirect to edit_here activity
                            Intent intent = new Intent(edit_menu.this, edit_here.class);
                            // Pass menu details as extras to edit_here activity
                            intent.putExtra("menuDate", menuDate);
                            intent.putExtra("foodType", foodType);
                            intent.putExtra("mainDish", snapshot.child("MainDish").getValue(String.class));
                            intent.putExtra("sideDish", snapshot.child("SideDish").getValue(String.class));
                            intent.putExtra("soup", snapshot.child("Soup").getValue(String.class));
                            intent.putExtra("dessertFruit", snapshot.child("DessertFruit").getValue(String.class));
                            intent.putExtra("calorieCount", snapshot.child("CalorieCount").getValue(String.class));
                            startActivity(intent);
                            break;
                        }
                    }
                    if (!found) {
                        // No menu found for the selected date and food type
                        Toast.makeText(edit_menu.this, "No menu available for the selected date and food type.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Menu data not found for the specified date
                    Toast.makeText(edit_menu.this, "No menu data available for the selected date.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential database error
                Toast.makeText(edit_menu.this, "Failed to retrieve menu details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}