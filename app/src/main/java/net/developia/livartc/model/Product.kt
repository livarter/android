package net.developia.livartc.model

import java.io.Serializable

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/23/24
 * Time: 9:05 PM
 * Function: 애플리케이션에서 사용되는 제품 정보를 나타내는 데이터 모델 정의
 */

// 제품 정보를 나타내는 데이터 클래스
data class Product (
    val productId: Int, // 제품 ID
    val productName: String, // 제품 이름
    val productPrice: Long, // 제품 가격
    val productDescription: String?, // 제품 설명 (null 가능)
    val productImage: String, // 제품 이미지 URL
    val brandName: String, // 브랜드 이름
    val hashtags: String, // 제품과 관련된 해시태그 (단일 문자열로 관리)
    val allHashtags: String // 모든 관련 해시태그 (단일 문자열로 관리)
): Serializable // 객체 직렬화를 위해 Serializable 인터페이스 구현
