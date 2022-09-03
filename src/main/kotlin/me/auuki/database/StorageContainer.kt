package me.auuki.database

interface StorageContainer<T: Any> {
    suspend fun add(entity: T)
    suspend fun remove(entity: T)
    suspend fun get(id: Any): T?
    suspend fun getAll(): List<T>
}