package me.auuki.permissibles

data class Permissions(
    val Id: String,
    val permissions: Set<String> = setOf(),
    val inheritance: Set<String> = setOf()
)

