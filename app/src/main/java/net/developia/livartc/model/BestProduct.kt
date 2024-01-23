package net.developia.livartc.model

//임시로 클래스명을 BestProduct로 지음
class BestProduct : ArrayList<BestProduct.BestProductItem>(){
    data class BestProductItem(
        val name: String,
        val price: Int,
        val productImage: String
    )
}