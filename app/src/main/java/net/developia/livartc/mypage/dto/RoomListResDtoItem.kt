package net.developia.livartc.mypage.dto

data class CatalogListResDto(
    val catalogs: List<Catalog> = listOf(
        Catalog(
            id = 1,
            leftChair = "https://github.com/livarter/backend/assets/77563814/8b45040d-f479-480e-be3b-f7d6b407c8dd",
            rightChair = "https://github.com/livarter/backend/assets/77563814/10223998-89f3-48dc-a58f-230d4d871bf0",
            sofa = "https://github.com/livarter/backend/assets/77563814/ed33b2de-4862-4e9c-924b-eeadb96cb35d",
            hashtag = "귀여운"
        ),
        Catalog(
            id = 2,
            leftChair = "https://github.com/livarter/backend/assets/77563814/af02b67d-b446-4e7d-a093-f231087439f8",
            rightChair = "https://github.com/livarter/backend/assets/77563814/adf70d26-a950-4f62-a3b4-9372ee7b11a6",
            sofa = "https://github.com/livarter/backend/assets/77563814/70fc387b-f0fd-499c-8761-f9fb8b7676d5",
            hashtag = "시크한"
        )
    )
)