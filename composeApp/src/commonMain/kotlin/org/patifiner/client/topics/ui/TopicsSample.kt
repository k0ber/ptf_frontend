package org.patifiner.client.topics.ui

import org.patifiner.client.topics.TopicDto

fun sampleTopicsTree(): List<TopicDto> = listOf(
    TopicDto(
        locale = "en",
        id = 1,
        name = "🏃‍♂️ Sports & Outdoor",
        slug = "sports_outdoor",
        description = "Active recreation, team sports, and fitness.",
        tags = listOf("sports", "fitness", "outdoor"),
        icon = "🏃",
        parentId = null,
        children = listOf(
            TopicDto(
                locale = "en",
                id = 2,
                name = "⚽ Football / Soccer",
                slug = "football",
                description = "All about football — from local leagues to world cups.",
                tags = listOf("football", "soccer"),
                icon = "⚽",
                parentId = 1,
                children = listOf(
                    TopicDto(
                        locale = "en",
                        id = 3,
                        name = "🏆 Tactics & Training",
                        slug = "football_training",
                        description = "Guides and drills for improving your game.",
                        tags = listOf("training", "tactics"),
                        icon = "🏋️",
                        parentId = 2,
                        children = emptyList()
                    )
                )
            ),
            TopicDto(
                locale = "en",
                id = 4,
                name = "🏀 Basketball",
                slug = "basketball",
                description = "From streetball to professional leagues.",
                tags = listOf("nba", "streetball"),
                icon = "🏀",
                parentId = 1,
                children = emptyList()
            ),
            TopicDto(
                locale = "en",
                id = 5,
                name = "⛷️ Winter Sports",
                slug = "winter_sports",
                description = "Skiing, snowboarding, skating, and more.",
                tags = listOf("ski", "snowboard"),
                icon = "🎿",
                parentId = 1,
                children = listOf(
                    TopicDto(
                        locale = "en",
                        id = 6,
                        name = "🏂 Snowboarding",
                        slug = "snowboarding",
                        description = "All about boards, tricks, and mountains.",
                        tags = listOf("snowboard"),
                        icon = "🏔️",
                        parentId = 5,
                        children = emptyList()
                    )
                )
            )
        )
    ),
    TopicDto(
        locale = "en",
        id = 7,
        name = "🎮 Entertainment",
        slug = "entertainment",
        description = "Movies, games, and pop culture.",
        tags = listOf("media", "fun"),
        icon = "🎬",
        parentId = null,
        children = listOf(
            TopicDto(
                locale = "en",
                id = 8,
                name = "🎥 Cinema",
                slug = "cinema",
                description = "Movie discussions, reviews, and recommendations.",
                tags = listOf("movies", "film"),
                icon = "🍿",
                parentId = 7,
                children = emptyList()
            ),
            TopicDto(
                locale = "en",
                id = 9,
                name = "🎮 Video Games",
                slug = "video_games",
                description = "Game news, reviews, and community.",
                tags = listOf("gaming", "pc", "console"),
                icon = "🕹️",
                parentId = 7,
                children = listOf(
                    TopicDto(
                        locale = "en",
                        id = 10,
                        name = "🧩 Indie Games",
                        slug = "indie_games",
                        description = "Discover hidden indie gems.",
                        tags = listOf("indie"),
                        icon = "✨",
                        parentId = 9,
                        children = emptyList()
                    )
                )
            )
        )
    ),
    TopicDto(
        locale = "en",
        id = 11,
        name = "📚 Education",
        slug = "education",
        description = "Learning, teaching, and personal growth.",
        tags = listOf("study", "learning"),
        icon = "📖",
        parentId = null,
        children = listOf(
            TopicDto(
                locale = "en",
                id = 12,
                name = "💻 Programming",
                slug = "programming",
                description = "Languages, frameworks, and development practices.",
                tags = listOf("code", "software", "dev"),
                icon = "💻",
                parentId = 11,
                children = emptyList()
            ),
            TopicDto(
                locale = "en",
                id = 13,
                name = "🧠 Psychology",
                slug = "psychology",
                description = "Mind, behavior, and emotional intelligence.",
                tags = listOf("mind", "health"),
                icon = "🧠",
                parentId = 11,
                children = emptyList()
            )
        )
    )
)