package net.developia.livartc.product

import androidx.lifecycle.ViewModel

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/29/24
 * Time: 11:10 AM
 * Function: 제품 검색 관련 사용자 인터페이스 상태를 관리하기 위한 뷰 모델
 */

// 제품 검색 기능을 위한 뷰 모델 클래스
class SearchViewModel : ViewModel() {
    var currentQuery: String? = null // 사용자가 입력한 현재 검색 쿼리
    var selectedBrand: String? = null // 사용자가 선택한 브랜드
    var selectedHashTag: String? = null // 사용자가 선택한 해시태그
    var selectedSortOption: Int = 4 // 사용자가 선택한 정렬 옵션, 기본값으로 '최신순' 등을 가정
}
