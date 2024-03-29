package net.developia.livartc.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업 내용: 룸데이터베이스 장바구니 Entity
 */


@Entity
data class CartEntity (
    @PrimaryKey(autoGenerate = true) var id : Int? = null,
    @ColumnInfo(name= "product_id") var product_id : Int? = null,
    @ColumnInfo(name= "name") var name : String? = null,
    @ColumnInfo(name= "price") var price : Long? = null,
    @ColumnInfo(name= "product_cnt") var product_cnt : Int? = null,
    @ColumnInfo(name= "image") var image : String? = null
)