package net.developia.livartc.model

class Product : ArrayList<Product.ProductItem>(){
    data class ProductItem(
        val name: String,
        val price: Int,
        val productImage: String
    )
}