package br.com.fiap.inovagab.model

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val perfil: String = "", // "operador", "gestor", "lider"
    val pontos: Int = 0
)
