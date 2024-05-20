package com.example.yemek_takvimi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class edit_here extends AppCompatActivity {

    // Declare EditText fields
    private EditText editTextMainDishEditHere;
    private EditText editTextSideDishEditHere;
    private EditText editTextSoupEditHere;
    private EditText editTextDessertFruitEditHere;
    private EditText editTextCalorieCountEditHere;

    private TextView textViewFoodTypeEditHere;
    private  TextView textViewMenuDateEditHere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_here);

        // Initialize views
        editTextMainDishEditHere = findViewById(R.id.editTextMainDishEditHere);
        editTextSideDishEditHere = findViewById(R.id.editTextSideDishEditHere);
        editTextSoupEditHere = findViewById(R.id.editTextSoupEditHere);
        editTextDessertFruitEditHere = findViewById(R.id.editTextDessertFruitEditHere);
        editTextCalorieCountEditHere = findViewById(R.id.editTextCalorieCountEditHere);

        textViewFoodTypeEditHere = findViewById(R.id.textViewFoodTypeEditHere);
        textViewMenuDateEditHere = findViewById(R.id.textViewMenuDateEditHere);

        Button buttonBackEditHere = findViewById(R.id.buttonBackEditHere);

        buttonBackEditHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity to navigate back to the previous activity
                finish();
            }
        });


        // Get the selected date and food type from the previous activity
        String selectedDate = getIntent().getStringExtra("menuDate");
        String selectedFoodType = getIntent().getStringExtra("foodType");
        String mainDish = getIntent().getStringExtra("mainDish");
        String sideDish = getIntent().getStringExtra("sideDish");
        String soup = getIntent().getStringExtra("soup");
        String dessertFruit = getIntent().getStringExtra("dessertFruit");
        String calorieCount = getIntent().getStringExtra("calorieCount");

        // Populate EditText fields with data from Firebase
        populateEditTextFields(selectedDate, selectedFoodType, mainDish, sideDish, soup, dessertFruit, calorieCount);
    }

    // Method to populate EditText fields with data from Firebase
      private void populateEditTextFields(String selectedDate, String selectedFoodType, String mainDish, String sideDish, String soup, String dessertFruit, String calorieCount) {
        // Set the retrieved data to EditText fields
        editTextMainDishEditHere.setText(mainDish != null ? mainDish : "");
        editTextSideDishEditHere.setText(sideDish != null ? sideDish : "");
        editTextSoupEditHere.setText(soup != null ? soup : "");
        editTextDessertFruitEditHere.setText(dessertFruit != null ? dessertFruit : "");
        editTextCalorieCountEditHere.setText(calorieCount != null ? calorieCount : "");


          // Set the date and food type
          textViewMenuDateEditHere.setText("Menu Date: " + selectedDate);
          textViewFoodTypeEditHere.setText("Food Type: " + selectedFoodType);
    }

    public void saveChanges(View view) {
        // Get the updated values from EditText fields
        String updatedMainDish = editTextMainDishEditHere.getText().toString().trim();
        String updatedSideDish = editTextSideDishEditHere.getText().toString().trim();
        String updatedSoup = editTextSoupEditHere.getText().toString().trim();
        String updatedDessertFruit = editTextDessertFruitEditHere.getText().toString().trim();
        String updatedCalorieCount = editTextCalorieCountEditHere.getText().toString().trim();

        // Get the selected date and food type
        String selectedDate = textViewMenuDateEditHere.getText().toString().replace("Menu Date: ", "");
        String selectedFoodType = textViewFoodTypeEditHere.getText().toString().replace("Food Type: ", "");

        // Update the data in Firebase
        updateMenuData(selectedDate, selectedFoodType, updatedMainDish, updatedSideDish, updatedSoup, updatedDessertFruit, updatedCalorieCount);
    }

    private void updateMenuData(String selectedDate, String selectedFoodType, String updatedMainDish, String updatedSideDish, String updatedSoup, String updatedDessertFruit, String updatedCalorieCount) {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Daily Menu");

        // Query the database to find the specific menu item to update
        Query query = menuRef.orderByChild("MenuDate").equalTo(selectedDate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String foodType = snapshot.child("FoodType").getValue(String.class);
                        if (foodType != null && foodType.equals(selectedFoodType)) {
                            // Update the menu item with the new values
                            snapshot.getRef().child("MainDish").setValue(updatedMainDish);
                            snapshot.getRef().child("SideDish").setValue(updatedSideDish);
                            snapshot.getRef().child("Soup").setValue(updatedSoup);
                            snapshot.getRef().child("DessertFruit").setValue(updatedDessertFruit);
                            snapshot.getRef().child("CalorieCount").setValue(updatedCalorieCount);

                            // Display a success message
                            Toast.makeText(edit_here.this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                            return; // Exit the loop after updating the menu item
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(edit_here.this, "Failed to save changes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
