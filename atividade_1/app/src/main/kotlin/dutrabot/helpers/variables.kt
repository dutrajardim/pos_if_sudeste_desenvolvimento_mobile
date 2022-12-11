package dutrabot.helpers

// lista de respostas
val responses = mapOf(
    "Certamente." to 1,
    "Opa! Calma aí!" to 2,
    "Relaxa, eu sei o que faço!" to 3,
    "A responsabilidade é sua." to 4,
    "Não me incomode." to 5,
    "Tudo bem, como quiser." to 6,
    "Essa eu sei." to 7,
    "É para já!" to 8
)

// Chaves (operações conhecidas) e valores (acumulador da operação)
val operations = mapOf(
    "some" to { values: List<Double> -> values.reduce { acc, cur -> acc + cur } },
    "subtraia" to { values: List<Double> -> values.reduce { acc, cur -> acc - cur } },
    "multiplique" to { values: List<Double> -> values.reduce { acc, cur -> acc * cur } },
    "divida" to { values: List<Double> -> values.reduce { acc, cur -> acc / cur } },
)

