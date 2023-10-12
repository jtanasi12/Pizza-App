package com.example.pizzaapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class completeOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_order)

       val foodOrder = findViewById<TextView>(R.id.foodStatus)

        var fullOrder : String? = null

        val pizzaType = intent.getStringExtra("pizzaType")
        val pizzaSize = intent.getStringExtra("pizzaSize")
        var tomatoTopping = intent.getStringExtra("tomatoTopping")
        var mushroomTopping = intent.getStringExtra("mushroomTopping")
        var onionTopping = intent.getStringExtra("onionTopping")
        var spinachTopping = intent.getStringExtra("spinachTopping")
        var ingNumber = intent.getIntExtra("ingrediantSize", 0)
        val tipTotal = intent.getDoubleExtra("tipTotal", 0.00)
        val deliveryBool = intent.getBooleanExtra("deliveryBool", false)
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.00)

        fullOrder = "Thank you for your order! You ordered a $pizzaSize size $pizzaType pizza with"

        if(ingNumber != 0){

            while( ingNumber != 0) {
                if (tomatoTopping != null) {
                    fullOrder += " tomato"
                    tomatoTopping = null
                }
                else if (mushroomTopping != null) {
                    fullOrder += " mushroom"
                    mushroomTopping = null
                }
               else if (onionTopping != null) {
                    fullOrder += " onion"
                    onionTopping = null
                }
               else if (spinachTopping != null) {
                    // Spinach
                    fullOrder += " spinach"
                    spinachTopping = null
                }

                // Only put commas if there is more than 1 ingredient
                if(ingNumber > 1)
                    fullOrder += ", "

                if(ingNumber == 1){
                    fullOrder += "."
                }
                --ingNumber
            }
        }
        else
            fullOrder += " no ingredients"


        fullOrder += " You tipped a total of $ $tipTotal."

        if(deliveryBool)
            fullOrder += "You chose a delivery fee of $2.00."
        else
            fullOrder += "You chose no delivery."

        fullOrder += "You paid a total amount of $ $totalPrice."

        foodOrder.text = fullOrder


        }
    }

