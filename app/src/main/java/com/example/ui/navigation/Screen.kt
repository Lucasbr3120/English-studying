package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Scenes : Screen("scenes")
    object SceneDetail : Screen("scene_detail/{sceneId}") {
        fun createRoute(sceneId: String) = "scene_detail/$sceneId"
    }
    object Exercise : Screen("exercise/{sceneId}") {
        fun createRoute(sceneId: String) = "exercise/$sceneId"
    }
    object Vocabulary : Screen("vocabulary")
    object Progress : Screen("progress")
    object Contractions : Screen("contractions")
    object Pronunciation : Screen("pronunciation")
    object AiGenerator : Screen("ai_generator")
    object YouTubeSearch : Screen("youtube_search")
    object YouTubeStudy : Screen("youtube_study/{videoId}") {
        fun createRoute(videoId: String) = "youtube_study/$videoId"
    }
    object MistakesReview : Screen("mistakes_review")
}
