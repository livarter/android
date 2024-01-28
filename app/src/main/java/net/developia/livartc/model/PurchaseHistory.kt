package net.developia.livartc.model

data class PurchaseHistory(
    val address: String,
    val createdAt: String,
    val createdAtTime: String,
    val historyDetailId: Int,
    val id: Int,
    val memberId: Int,
    val productCnt: Int,
    val productId: Int,
    val productImage: String,
    val productName: String,
    val productPrice: Long,
    val productDesc: String,
    val productBrand: String,
    val purchaseDetailStatus: String,
    val receiptId: String,
    val receiverName: String,
    val receiverPhone: String,
    val zipcode: String
)