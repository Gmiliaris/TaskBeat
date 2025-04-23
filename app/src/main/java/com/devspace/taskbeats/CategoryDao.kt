package com.devspace.taskbeats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CategoryDao{

    @Query("SELECT * FROM categoryentity")
    fun getAllCategories(): List<CategoryEntity>

    @Insert
    fun insertAll(categoryEntities: List<CategoryEntity>)
}
