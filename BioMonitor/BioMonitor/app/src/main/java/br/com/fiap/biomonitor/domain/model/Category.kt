package br.com.fiap.biomonitor.domain.model

enum class Category(val displayName: String, val color: Long) {
    FLORA("Flora", 0xFF4CAF50),
    AVES("Aves", 0xFF2196F3),
    MAMIFEROS("Mamíferos", 0xFFFF9800),
    REPTEIS_ANFIBIOS("Répteis/Anfíbios", 0xFF9C27B0),
    INSETOS("Insetos", 0xFFFFEB3B),
    PEIXES("Peixes", 0xFF00BCD4),
    FUNGOS("Fungos", 0xFF795548),
    OUTROS("Outros", 0xFFFFFFFF)
}
