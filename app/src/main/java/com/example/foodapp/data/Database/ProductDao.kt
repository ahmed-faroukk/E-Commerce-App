package com.example.foodapp.data.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodapp.models.Product

@Dao
interface ProductDao {
    // onConflict job is replace products with the same id if it found in database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert (product: Product) : Long

    // this method to use as live data object to get all data when fragment destroy
    @Query("SELECT * FROM product")
    fun getAllProducts() : LiveData<List<Product>>


    @Delete
    suspend fun deleteProduct(product: Product)
}