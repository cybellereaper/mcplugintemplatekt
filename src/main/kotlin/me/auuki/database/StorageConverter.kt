package me.auuki.database

interface StorageConverter {
    suspend fun <T : Any> transferAll(source: StorageContainer<T>, destination: StorageContainer<T>): T?
    suspend fun <T : Any> transferSingle(id: Any, source: StorageContainer<T>, destination: StorageContainer<T>): T?
    suspend fun <T : Any> transferSingleOrDefault(
        id: Any,
        default: T,
        source: StorageContainer<T>,
        destination: StorageContainer<T>
    ): T
}