package net.developia.livartc.product

import androidx.lifecycle.ViewModel

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/29/24
 * Time: 11:10 AM
 */
class SearchViewModel : ViewModel() {
    var currentQuery: String? = null
    var selectedBrand: String? = null
    var selectedHashTag: String? = null
    var selectedSortOption: Int = 4 // 기본 정렬 옵션, 예를 들어 '최신순'
}