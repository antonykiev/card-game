package com.photoframe.game

import androidx.lifecycle.*

class ViewModelCard: ViewModel() {

    val selectedCard = MutableLiveData<Card>()
    val selectedFrame = MutableLiveData(Frame.zero)
    val selectedMessage = MutableLiveData("")

    fun onSelectCard(card: Card) = selectedCard.postValue(card)

    fun onSelectFrame(frame: Frame) = selectedFrame.postValue(frame)

    fun onSelectMessage(message: String) = selectedMessage.postValue(message)


    enum class Frame(val value: Int) {
        zero(0),
        one(1),
        two(2),
        three(3),
        four(4),
    }

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