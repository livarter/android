package net.developia.livartc.model

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/23/24
 * 작업내용: 구매 내역 조회
 */
data class PurchaseReqDto (
    val memberId: Int,
    val receiptId: String,
    val createdAt: String,
    val address: String?,
    val zipcode: String,
    val receiverName: String,
    val receiverPhone: String,
    val purchaseDetailStatus: Int,
    val items: List<Item>
) {
    data class Item(
        val id: Int,
        val qty: Int,
        val price: Long
    )
}
