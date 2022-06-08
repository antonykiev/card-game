package com.photoframe.game.viewmodels

import androidx.lifecycle.*
import com.photoframe.game.R

class ViewModelCard: ViewModel() {

    val selectedCard = MutableLiveData<Card>()
    val selectedFrame = MutableLiveData(Frame.zero)
    val selectedMessage = MutableLiveData("")
    val scoreCounter = MutableLiveData<HashSet<ScoreState>>(hashSetOf())

    private fun incrementScore(state: ScoreState) {
        val incrementStep = 50
        val currentScore = scoreCounter.value ?: hashSetOf()
        currentScore.add(state)
        scoreCounter.postValue(currentScore)
    }

    fun onSelectCard(card: Card) {
        selectedCard.postValue(card)
        selectedFrame.postValue(Frame.zero)
        selectedMessage.postValue("")
        scoreCounter.postValue(hashSetOf())
    }

    fun onSelectFrame(frame: Frame) {
        incrementScore(ScoreState.FrameAdded)
        selectedFrame.postValue(frame)
    }

    fun onSelectMessage(message: String) {
        incrementScore(ScoreState.WritedMessage)
        selectedMessage.postValue(message)
    }

    fun onSaved() {
        incrementScore(ScoreState.Saved)
    }

    fun onShared() {
        incrementScore(ScoreState.Shared)
    }

    enum class ScoreState {
        FrameAdded,
        WritedMessage,
        Saved,
        Shared
    }

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