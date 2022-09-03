package me.auuki.database

abstract class MongoWrapper<T: Any> : StorageContainer<T>  {
    override suspend fun add(entity: T) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(entity: T) {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Any): T? {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<T> {
        TODO("Not yet implemented")
    }
}