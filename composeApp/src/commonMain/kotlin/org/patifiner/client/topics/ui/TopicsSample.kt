package org.patifiner.client.topics.ui

import TopicViewModel


fun fakeTopicsTree() = listOf(
    TopicViewModel(
        id = 1,
        name = "Спорт",
        slug = "sport",
        description = "Разные виды спорта",
        tags = null,
        icon = null,
        parentId = null,
        children = listOf(
            TopicViewModel(
                id = 2,
                name = "Водные",
                slug = "water",
                description = "Плавание и сапы",
                tags = null,
                icon = null,
                parentId = 1,
                children = listOf(
                    TopicViewModel(
                        id = 3,
                        name = "Плавание",
                        slug = "swimming",
                        description = null,
                        tags = null,
                        icon = null,
                        parentId = 2,
                        children = emptyList()
                    ),
                    TopicViewModel(
                        id = 4,
                        name = "Сапы",
                        slug = "sup",
                        description = null,
                        tags = null,
                        icon = null,
                        parentId = 2,
                        children = emptyList(),

                        )
                )
            ),
            TopicViewModel(
                id = 5,
                name = "Бег",
                slug = "running",
                description = null,
                tags = null,
                icon = null,
                parentId = 1,
                children = emptyList(),
            ),
            TopicViewModel(
                id = 6,
                name = "Футбол",
                slug = "football",
                description = null,
                tags = null,
                icon = null,
                parentId = 1,
                children = emptyList(),
            )
        )
    ),
    TopicViewModel(
        id = 7,
        name = "Путешествия",
        slug = "travel",
        description = "Места, маршруты, отдых",
        tags = null,
        icon = null,
        parentId = null,
        children = emptyList()
    ),
    // добавим несколько для проверки переноса
    TopicViewModel(id = 8, name = "Йога", slug = "yoga", description = null, tags = null, icon = null, parentId = null, children = emptyList()),
    TopicViewModel(id = 9, name = "Трилатлон", slug = "tri", description = null, tags = null, icon = null, parentId = null, children = emptyList()),
    TopicViewModel(id = 10, name = "Каякинг", slug = "kayak", description = null, tags = null, icon = null, parentId = null, children = emptyList()),
)

fun fakeTopicsRow() = listOf(
    TopicViewModel(1, "Kotlin", "kotlin", null, null, null, null, emptyList()),
    TopicViewModel(2, "Compose", "compose", null, null, null, null, emptyList()),
    TopicViewModel(3, "Multiplatform", "mp", null, null, null, null, emptyList())
)