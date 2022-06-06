package com.photoframe.game

import androidx.lifecycle.*

class ViewModelCard: ViewModel() {

    val selectedCard = MutableLiveData<Card>()

    fun onSelect(card: Card) = selectedCard.postValue(card)


    enum class Card(
        val idImg: Int,
        val title: Int,
        val content: Int
    ) {
        Lancut(
            R.drawable.card_lancut,
            R.string.lancut,
            R.string.lancut_content,
        ),
        Ordynata(
            R.drawable.card_ordynata,
            R.string.ordynata,
            R.string.ordynata_content,
        ),
        Fontanna(
            R.drawable.card_fontanna,
            R.string.fontanna,
            R.string.fontanna_content,
        )
    }
}