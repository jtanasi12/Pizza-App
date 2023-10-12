package com.example.pizzaapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// Implement the OnItemSelectedListener interface
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    // ********GLOBALS********
    private var  subTotal : Double = 0.00 // DEFAULT
    private var  totalPrice : Double = 0.00
    var pizzaPosition: Int? = null
    var pizzaSizeState : String? = "none" // DEFAULT
    var checked : Boolean = false
    var pizzaPrice : Double = 0.00
    var deliveryFee : Double = 0.00
    private var ingrediantPrice : Double = 0.00
    private var pizzaType : String? = null
    private var tomatoTopping : String? = null
    private var mushroomTopping : String? = null
    private var onionTopping : String? = null
    private var spinachTopping : String? = null
    private var ingrediantNum : Int = 0
    private var calculateTipFormatted:Double = 0.00
    private var deliveryBool = false
    private var totalPriceFormatted:Double = 0.00

    // Store the totalPriceNum (EditText) into a variable
    private lateinit var subTotalPriceID: TextView
    private lateinit var totalPriceID : TextView
    private lateinit var seek : SeekBar
    private lateinit var tipTotal : TextView
    private lateinit var percentageLabel : TextView
    var calcluateTip : Double = 0.00
    var progressTracker : Int = 0
    // ********GLOBALS********



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            init()
            handleSeekBar()

    }

     // Adapter method for the Spinner widget, when an item is selected
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

         val largePizzaRadio = findViewById<RadioButton>(R.id.largeSizeButton)
         val xlPizzaRadio = findViewById<RadioButton>(R.id.xlSizeButton)

         // Get the item that was selected in the drop top menu
         val selectedItem = parent?.getItemAtPosition(position).toString()

         // Determine which picture we should load
         // We use 'When' which is similar to a switch statement
         // bbq_chicken is the default
          var imageID = when(position){
             0->R.drawable.bbq_chicken
             1->R.drawable.hawaiian
             2->R.drawable.margheritta
             3->R.drawable.pepperoni
             else->R.drawable.bbq_chicken
         }


         // NOTE: I KNOW THIS IS A DUPLICATE CODE BUT I COULDN'T ADD
         // PIZZA TYPE IN THE CODE ABOVE, NOT SURE WHY
          if(position == 0){
              pizzaType = "barbeque"
          }
         else if(position == 1){
             pizzaType = "hawaiian"
          }
         else if(position == 2){
             pizzaType = "margheritta"
          }
         else if(position == 3){
             pizzaType = "pepperoni"
          }
         else
             pizzaType = "none"



         // Adjust the large and extra large text to incorporate the hawaiian and margarita pizzas premium charges.
         if (position == 1 || position == 2){
             largePizzaRadio.text = "Lrg ($15.99)"
             xlPizzaRadio.text = "XLrg ($17.99)"


         }
         else{
             largePizzaRadio.text = "Lrg ($13.99)"
             xlPizzaRadio.text = "XLrg ($15.99)"
         }


         // Keeps track of the pizza type. For example: pizzaPosition = 0 is bbqChicken
         pizzaPosition = position

         // Load the appropriate image based on the selection from the Spinner widget
         findViewById<ImageView>(R.id.pizzaImage).setImageResource(imageID)

         // Update the price of the pizza state
         // If we are on BBQ and on XL and then switch to a premium pizza we need to update the price
             pizzaState()


    }

    // Adapter method for the Spinner widget, when no items are selected
    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show()
    }

    // Check if the check boxes for the extra ingredients are clicked
    fun checkBoxClick(view : View){

        // A pizza must be selected in order to add ingredients

            if(view is CheckBox) {

                checked = view.isChecked

                if (checked) {
                    ingrediantPrice += 1.69
                } else {
                    ingrediantPrice -= 1.69
                }

                when (view.id) {
                    R.id.tomatoCheckBox -> {
                        if (checked) {
                            tomatoTopping = "tomato"
                            ingrediantNum++;
                        }
                        else {
                            tomatoTopping = null
                            ingrediantNum--;
                        }
                    }
                    R.id.mushroomCheckBox -> {
                        if (checked) {
                            mushroomTopping = "mushroom"
                            ingrediantNum++
                        }
                        else {
                            mushroomTopping = null
                            ingrediantNum--
                        }
                    }
                    R.id.onionCheckBox -> {
                        if (checked) {
                            onionTopping = "onion"
                            ingrediantNum++
                        }
                        else {
                            onionTopping = null
                            ingrediantNum--
                        }
                    }
                    R.id.spinachCheckBox -> {
                        if (checked) {
                            spinachTopping = "spinach"
                            ingrediantNum++
                        }
                        else {
                            spinachTopping = null
                            ingrediantNum--
                        }
                    }
                }

            }






            // This protects against if we pick ingredients before selecting a pizza size
            // We don't want to display the extra topping prices without a pizza selected
            if(pizzaSizeState != "none")
                setSubTotalPrice()

        updateTip()

    }
    fun radioButtonClick(view: View){

        /*Determine the pizza size that is selected
            We then save a string that represents the pizza, this de-couples the code
            Now we can check the pizza state, when the user switches from a premium pizza
            and a regular pizza. For ex: if we are on bbq extra large that's 15.99 and if we switch
            to hawaiian, the price should change to 17.99
         */
      val selectedPizzaSize = when(view.id){
            R.id.mediumSizeButton-> pizzaSizeState = "medium"
            R.id.largeSizeButton->  pizzaSizeState = "large"
            R.id.xlSizeButton->  pizzaSizeState = "xl"
            else-> pizzaSizeState = "none" // DEFAULT
        }

        if(pizzaSizeState != "none"){
            // Update the subtotal depending on the size of the pizza selected
            pizzaState()

            checkBoxClick(view)

            updateTip()
            }

    }

   private fun pizzaState(){

       // Check if the pizza is a premium
       // 1 represents hawaiian, 2 represents margarita
       if(pizzaPosition == 1 || pizzaPosition == 2) {
           if (pizzaSizeState == "large") {
               pizzaPrice = 15.99
           } else if (pizzaSizeState == "xl") {
               pizzaPrice = 17.99
           }

       }

       // If the pizza is not premium, it has to be regular
       else{

            if(pizzaSizeState =="large"){
              pizzaPrice = 13.99
           }
           else if(pizzaSizeState == "xl")
               pizzaPrice= 15.99
           else // DEFAULT (WHEN THE USER FIRST LOADS THE APP)
               pizzaPrice = 0.00
       }

       if(pizzaSizeState == "medium")
           pizzaPrice = 9.99

       if(pizzaSizeState != "none") {
           // Update the TotalPrice
           setSubTotalPrice()

           updateTip()
       }

   }


    private fun init(){
        // Store the totalPriceNum (EditText) into a variable
        subTotalPriceID = findViewById<TextView>(R.id.subTotalPrice1)
        totalPriceID = findViewById<TextView>(R.id.totalPrice)

        // Create a string of pizza list items into an array
        val myPizzaList = listOf<String>("Barbeque", "Hawaiian", "Margherita", "Pepperoni")

        // Converts the data items (String array) into View items
        val myPizzaAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, myPizzaList)

        // Get the PizzaSpinner ID and store it into a variable
        val pizzaSpinnerID = findViewById<Spinner>(R.id.pizzaTypeSpinner)

        // Set the Adapter to our pizza adapter
        pizzaSpinnerID.adapter = myPizzaAdapter

        // This stores the MainActivity class in which we are implementing the OnItemSelectedListener interface
        pizzaSpinnerID.onItemSelectedListener = this

    }

    private fun setSubTotalPrice(){


        subTotal = pizzaPrice + ingrediantPrice

        val subTotalFormatted:Double = String.format("%.2f", subTotal).toDouble()

        subTotalPriceID.setText("$ $subTotalFormatted")

        setTotalPrice()
    }

    private fun setTotalPrice(){
        totalPrice = subTotal + calcluateTip + deliveryFee

        totalPriceFormatted = String.format("%.2f", totalPrice).toDouble()

        totalPriceID.setText("$ $totalPriceFormatted")


    }

    private fun handleSeekBar(){

        percentageLabel = findViewById<TextView>(R.id.seekBarLabel)

         seek =  findViewById<SeekBar>(R.id.seekBarID)
         tipTotal = findViewById<TextView>(R.id.tipPrice)

        findViewById<SeekBar>(R.id.seekBarID).setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

                progressTracker = progress
                updateTip()

                    setTotalPrice()

            }

            override fun onStartTrackingTouch(progress: SeekBar?) {
            }

            override fun onStopTrackingTouch(progress: SeekBar?) {
            }



        })

    }

    private fun updateTip(){

        if(progressTracker != 0) {
            calcluateTip = ( subTotal * (progressTracker / 100.00) )
        }
        else
            calcluateTip = 0.00

         calculateTipFormatted = String.format("%.2f", calcluateTip).toDouble()

        tipTotal.text = "$ $calculateTipFormatted"
        percentageLabel.text = "$progressTracker%"
    }


    fun switchClick(view: View){

        // Check if the switch is checked

        val switchButton = findViewById<Switch>(R.id.switchButton)

        val switchText: String

        if(switchButton.isChecked) {
            switchText = "Yes, $2.00"
            deliveryFee = 2.00
            deliveryBool = true
        }
        else {
            switchText = "No, $0.00"
            deliveryFee = 0.00
            deliveryBool = false
        }
        switchButton.text = switchText

        if(pizzaSizeState != "none")
             setTotalPrice()
    }

    fun completeButton(view: View){
        val myIntent = Intent(this, completeOrderActivity::class.java)


        if(pizzaSizeState != "none") {
            myIntent.putExtra("pizzaType", pizzaType)
            myIntent.putExtra("pizzaSize", pizzaSizeState)
            myIntent.putExtra("tomatoTopping", tomatoTopping)
            myIntent.putExtra("mushroomTopping", mushroomTopping)
            myIntent.putExtra("onionTopping", onionTopping)
            myIntent.putExtra("spinachTopping", spinachTopping)
            myIntent.putExtra("ingrediantSize", ingrediantNum)
            myIntent.putExtra("deliveryBool", deliveryBool)
            myIntent.putExtra("totalPrice", totalPriceFormatted)



            myIntent.putExtra("tipTotal", calculateTipFormatted)

            startActivity(myIntent)
        }
        else
            Toast.makeText(this, "Please finish comleting order", Toast.LENGTH_SHORT).show()


    }

}
