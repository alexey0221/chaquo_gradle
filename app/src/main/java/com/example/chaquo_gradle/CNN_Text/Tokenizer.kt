package com.example.chaquo_gradle.CNN_Text

data class TokenizerData(val wordIndex: Map<String, Int>, val indexWord: Map<Int, String>)

class Tokenizer(numWords: Int) {
    private var wordIndex: MutableMap<String, Int> = mutableMapOf()
    private var indexWord: MutableMap<Int, String> = mutableMapOf()
    private var numWords = numWords

    fun fitOnTexts(texts: List<String>) {
        var index = 1
        // Разбиваем текст на отдельные слова и строим словарь
        for (text in texts) {
            val words = text.lowercase().split(" ")
            for (word in words) {
                if (word !in wordIndex && wordIndex.size < numWords) {
                    wordIndex[word] = index
                    indexWord[index] = word
                    index++
                }
            }
        }
    }

    fun textsToSequences(texts: List<String>): List<List<Int>> {
        // Преобразуем тексты в последовательности индексов слов
        val sequences = mutableListOf<List<Int>>()
        for (text in texts) {
            val words = text.lowercase().split(" ")
            val sequence = words.mapNotNull { wordIndex[it] }
            sequences.add(sequence)
        }
        return sequences
    }

    fun padSequences(sequences: List<List<Int>>, maxLen: Int): List<List<Int>> {
        return sequences.map { sequence ->
            if (sequence.size < maxLen) {
                val paddedSequence = sequence.toMutableList()
                repeat(maxLen - sequence.size) {
                    paddedSequence.add(0) // Добавляем нули для заполнения до максимальной длины
                }
                paddedSequence
            } else {
                sequence.subList(
                    0,
                    maxLen
                ) // Обрезаем до максимальной длины, если текст слишком длинный
            }
        }
    }
}