package com.example.bootcounter.room

import androidx.room.*

@Database(entities = [BootEntity::class], version = 1)
abstract class BootDatabase : RoomDatabase() {
    abstract fun bootDao(): BootDao
}

@Dao
interface BootDao {
    @Query("SELECT * FROM BootEntity")
    fun getAll(): List<BootEntity>

    @Insert
    fun insert(boot: BootEntity)
}

@Entity
data class BootEntity(
    @PrimaryKey val timeStamp: Long
)