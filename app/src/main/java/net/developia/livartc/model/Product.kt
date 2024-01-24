package net.developia.livartc.model

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/23/24
 * Time: 9:05 PM
 */
data class Product (
    val productId: Int,
    val productName: String,
    val productPrice: Long,
    val productDescription: String?,
    val productImage: String,
    val brandName: String,
    val hashtags: String,
    val allHashtags: String
)