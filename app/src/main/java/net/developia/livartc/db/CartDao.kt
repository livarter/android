package net.developia.livartc.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 17:38
 */
@Dao
interface CartDao {
    @Query("SELECT id, product_id, name, price, product_cnt, image FROM CartEntity")
    fun getAll() : List<CartEntity>

    @Insert
    fun insert(cart: CartEntity)

    @Delete
    fun delete(cart: CartEntity)

    @Update
    fun update(cart: CartEntity)

    @Query("DELETE FROM CartEntity")
    fun deleteAll()
}