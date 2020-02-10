package com.example.gheventshelper

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class CityEvents : AppCompatActivity() {

    lateinit var CardImage: ImageView
    lateinit var ChooseA_Image: ImageView
    lateinit var ChooseB_Image: ImageView
    val ListOfCards: MutableList<Int> = ArrayList()
    val PREFS_FILENAME = "com.example.gheventshelper.city_prefs"
    val DECK_LIST = "deck_list"
    lateinit var prefs: SharedPreferences

    val SetDeckText = "Card Numbers in Order Split by ,"
    val AddCardText = "Add Card Number:"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_events)
        prefs = getSharedPreferences(PREFS_FILENAME, 0)
        val savedDeck = prefs.getString(DECK_LIST,null)

        CardImage = findViewById(R.id.CardImage) as ImageView
        ChooseA_Image = findViewById(R.id.ChooseA_Image) as ImageView
        ChooseB_Image = findViewById(R.id.ChooseB_Image) as ImageView

        if( savedDeck != null) {
            StringToDeckList(savedDeck)
        }
        else {
            for(i in 1..90) {
                ListOfCards.add(i)
                SaveDeckList()
            }
        }

        val draw_button = findViewById(R.id.DrawButton) as Button
        draw_button.setOnClickListener {
            DrawCardImage()
        }

        val choosea_button = findViewById(R.id.ChooseA_Button) as Button
        choosea_button.setOnClickListener {
            ChooseA()
            ShowActionButtons()
        }

        val chooseb_button = findViewById(R.id.ChooseB_Button) as Button
        chooseb_button.setOnClickListener {
            ChooseB()
            ShowActionButtons()
        }

        val destroy_button = findViewById(R.id.Destroy_Button) as Button
        destroy_button.setOnClickListener {
            DestroyCard()
            ShowChooseButtons()
        }

        val buttom_button = findViewById(R.id.Bottom_Button) as Button
        buttom_button.setOnClickListener {
            BottomCard()
            ShowChooseButtons()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.city_events_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.SetDeck -> {
                PromptInputText(SetDeckText)
                return true
            }
            R.id.AddCard -> {
                PromptInputText(AddCardText)
                return true
            }
            R.id.SaveItem -> {
                SaveDeckList()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        SaveDeckList()
        super.onBackPressed()
    }

    private fun DrawCardImage() {
        ChooseClear()
        val CardName = "ce_"+"%02d".format(ListOfCards.get(0))+"_f"
        val DrawableId = getResources().getIdentifier(CardName, "drawable", this.getPackageName());
        CardImage.setImageResource(DrawableId);
    }

    private fun FlipCard() {
        val CardName = "ce_"+"%02d".format(ListOfCards.get(0))+"_b"
        val DrawableId = getResources().getIdentifier(CardName, "drawable", this.getPackageName());
        CardImage.setImageResource(DrawableId);
    }

    private fun ChooseA() {
        ChooseA_Image.alpha = 0.0f
        ChooseB_Image.alpha = 1.0f
        FlipCard()
    }

    private fun ChooseB() {
        ChooseA_Image.alpha = 1.0f
        ChooseB_Image.alpha = 0.0f
        FlipCard()
    }

    private fun ChooseClear() {
        ChooseA_Image.alpha = 0.0f
        ChooseB_Image.alpha = 0.0f
    }

    private fun ChooseBlank() {
        ChooseA_Image.alpha = 1.0f
        ChooseB_Image.alpha = 1.0f
    }

    //Destroys the card and removes it from the deck.
    private fun DestroyCard() {
        ListOfCards.removeAt(0)
        SaveDeckList()
        ChooseBlank() //Blank out the card until card gets drawn.
    }

    //Puts the card onto the bottom of the deck.
    private fun BottomCard() {
        var TopCard: Int = ListOfCards.removeAt(0)
        ListOfCards.add(TopCard) //Adds the TopCard to the bottom.
        SaveDeckList()
        ChooseBlank() //Blank out the card until card gets drawn.
    }

    private fun SaveDeckList() {
        val DeckListString = ListOfCards.joinToString(separator = ",")
        val editor = prefs.edit()
        editor.putString(DECK_LIST, DeckListString)
        editor.apply()
    }

    private fun StringToDeckList(combinedString:String) {
        val SplitDeck = combinedString.split(",")
        for(card in SplitDeck){
            ListOfCards.add(card.toInt())
        }
    }

    private fun ShowChooseButtons(){
        val chooselayout = findViewById<FrameLayout>(R.id.ChooseButtons)
        chooselayout.setVisibility(View.VISIBLE)
        val actionlayout = findViewById<FrameLayout>(R.id.CardActionButtons)
        actionlayout.setVisibility(View.GONE)
    }

    private fun ShowActionButtons(){
        val chooselayout = findViewById<FrameLayout>(R.id.ChooseButtons)
        chooselayout.setVisibility(View.GONE)
        val actionlayout = findViewById<FrameLayout>(R.id.CardActionButtons)
        actionlayout.setVisibility(View.VISIBLE)
    }

    private fun PromptInputText(InputMessage:String){
        var result: String
        // get prompts.xml view
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.prompts, null)
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setView(promptsView) // set prompts.xml to alertdialog builder

        val userInput = promptsView.findViewById(R.id.editTextDialogUserInput) as EditText
        val curText = promptsView.findViewById(R.id.textView1) as TextView
        curText.setText(InputMessage)

        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id ->
                result = userInput.text.toString()
                if( InputMessage == SetDeckText){
                    ListOfCards.clear()
                    StringToDeckList(result)
                    ChooseBlank()
                    SaveDeckList()
                }
                else if( InputMessage == AddCardText ){
                    ListOfCards.add(result.toInt()) //Turn the input text into a card.
                    ListOfCards.shuffle()
                    ChooseBlank()
                    SaveDeckList()
                }
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}
