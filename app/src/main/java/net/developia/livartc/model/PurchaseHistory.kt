package net.developia.livartc.model

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업내용: 리사이클러뷰 장바구니 목록 조회 및 수정
 *           장바구니 결제 예상 금액 전달
 */
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