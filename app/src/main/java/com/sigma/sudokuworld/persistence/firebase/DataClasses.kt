package com.sigma.sudokuworld.persistence.firebase

data class FireBaseSet(
        var key: String = "",
        var name: String = "",
        var description: String = ""
) {
    //Overridden so list.contains works if keys are different but everything else is the same
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FireBaseSet

        if (name != other.name) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}


data class FireBaseWordSet(
        var key: String = "",
        var name: String = "",
        var description: String = "",
        var nativeLanguageCode: String = "",
        var foreignLanguageCode: String = "",
        var wordPairs: List<FireBaseWordPair> = listOf()
)

data class FireBaseWordPair(
        var nativeWord: String = "",
        var foreignWord: String = ""
)