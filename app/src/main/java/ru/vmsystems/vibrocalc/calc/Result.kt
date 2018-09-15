package ru.vmsystems.vibrocalc.calc

class Result(
        private val values: Map<String, Value>
) {
    fun getValue(parameterName: Parameter): Double {
        return values[parameterName.name]?.value ?: 0.0
    }
}
