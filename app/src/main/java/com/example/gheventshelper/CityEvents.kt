package com.example.gheventshelper

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView

class CityEvents : AppCompatActivity() {

    lateinit var CardImage: ImageView
    lateinit var ChooseA_Image: ImageView
    lateinit var ChooseB_Image: ImageView
    val ListOfCards: MutableList<Int> = ArrayList()
    val PREFS_FILENAME = "com.example.gheventshelper.city_prefs"
    val DECK_LIST = "deck_list"
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_events)
        prefs = getSharedPreferences(PREFS_FILENAME, 0)
        val savedDeck = prefs.getString(DECK_LIST,null)

        CardImage = findViewById(R.id.CardImage) as ImageView
        ChooseA_Image = findViewById(R.id.ChooseA_Image) as ImageView
        ChooseB_Image = findViewById(R.id.ChooseB_Image) as ImageView

        if( savedDeck != null) {
            val SplitDeck = savedDeck.split(",")
            for(card in SplitDeck){
                ListOfCards.add(card.toInt())
            }
        }
        else {
            for(i in 1..90) {
                ListOfCards.add(i)
            }
        }

        val draw_button = findViewById(R.id.DrawButton) as Button
        draw_button.setOnClickListener {
            CycleCardImage()
        }

        val choosea_button = findViewById(R.id.ChooseA_Button) as Button
        choosea_button.setOnClickListener {
            ChooseA()
        }

        val chooseb_button = findViewById(R.id.ChooseB_Button) as Button
        chooseb_button.setOnClickListener {
            ChooseB()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.city_events_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ShuffleItem -> {
                ListOfCards.shuffle()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        SaveDeckList()
        super.onBackPressed()
    }

    var CardIdx = 0
    private fun CycleCardImage() {
        ChooseClear()
        val CardName = "ce_"+"%02d".format(ListOfCards.get(CardIdx))+"_f"
        val DrawableId = getResources().getIdentifier(CardName, "drawable", this.getPackageName());
        CardImage.setImageResource(DrawableId);
        if(CardIdx == 89) {
            CardIdx = 0
        }
        else {
            CardIdx += 1
        }
    }

    private fun FlipCard() {
        val CardName = "ce_"+"%02d".format(ListOfCards.get(CardIdx))+"_b"
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

    private fun SaveDeckList() {
        val DeckListString = ListOfCards.joinToString(separator = ",")
        val editor = prefs.edit()
        editor.putString(DECK_LIST, DeckListString)
        editor.apply()
    }
}
