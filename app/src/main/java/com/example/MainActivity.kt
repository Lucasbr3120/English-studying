package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.audio.TtsManager
import com.example.data.repository.AppRepository
import com.example.ui.navigation.Screen
import com.example.ui.screens.contractions.ContractionLabScreen
import com.example.ui.screens.exercise.ExerciseScreen
import com.example.ui.screens.exercise.ExerciseViewModel
import com.example.ui.screens.generator.AiSceneGeneratorScreen
import com.example.ui.screens.generator.AiSceneGeneratorViewModel
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.progress.ProgressViewModel
import com.example.ui.screens.pronunciation.PronunciationScreen
import com.example.ui.screens.scenes.SceneDetailScreen
import com.example.ui.screens.scenes.ScenesListScreen
import com.example.ui.screens.scenes.ScenesViewModel
import com.example.ui.screens.vocabulary.VocabularyScreen
import com.example.ui.screens.vocabulary.VocabularyViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var appRepository: AppRepository
    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appRepository = AppRepository(applicationContext)
        ttsManager = TtsManager(applicationContext)

        setContent {
            MyApplicationTheme {
                SceneEnglishApp(
                    repository = appRepository,
                    ttsManager = ttsManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

@Composable
fun SceneEnglishApp(
    repository: AppRepository,
    ttsManager: TtsManager
) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home Screen
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.Factory(repository)
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToSceneDetail = { sceneId ->
                        navController.navigate(Screen.SceneDetail.createRoute(sceneId))
                    },
                    onNavigateToScenes = {
                        navController.navigate(Screen.Scenes.route)
                    },
                    onNavigateToVocabulary = {
                        navController.navigate(Screen.Vocabulary.route)
                    },
                    onNavigateToProgress = {
                        navController.navigate(Screen.Progress.route)
                    },
                    onNavigateToContractions = {
                        navController.navigate(Screen.Contractions.route)
                    },
                    onNavigateToPronunciation = {
                        navController.navigate(Screen.Pronunciation.route)
                    },
                    onNavigateToYouTube = {
                        navController.navigate(Screen.YouTubeSearch.route)
                    },
                    onNavigateToMistakes = {
                        navController.navigate(Screen.MistakesReview.route)
                    },
                    onNavigateToAiGenerator = {
                        navController.navigate(Screen.AiGenerator.route)
                    },
                    onStartExercise = { sceneId ->
                        navController.navigate(Screen.Exercise.createRoute(sceneId))
                    }
                )
            }

            // Scenes List Screen
            composable(Screen.Scenes.route) {
                val scenesViewModel: ScenesViewModel = viewModel(
                    factory = ScenesViewModel.Factory(repository)
                )
                ScenesListScreen(
                    viewModel = scenesViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSceneDetail = { sceneId ->
                        navController.navigate(Screen.SceneDetail.createRoute(sceneId))
                    }
                )
            }

            // Scene Detail Screen
            composable(
                route = Screen.SceneDetail.route,
                arguments = listOf(navArgument("sceneId") { type = NavType.StringType })
            ) { backStackEntry ->
                val sceneId = backStackEntry.arguments?.getString("sceneId") ?: ""
                SceneDetailScreen(
                    sceneId = sceneId,
                    repository = repository,
                    ttsManager = ttsManager,
                    onNavigateBack = { navController.popBackStack() },
                    onStartExercise = { id ->
                        navController.navigate(Screen.Exercise.createRoute(id))
                    }
                )
            }

            // Exercise Screen (The 8-step contraction & translation engine)
            composable(
                route = Screen.Exercise.route,
                arguments = listOf(navArgument("sceneId") { type = NavType.StringType })
            ) { backStackEntry ->
                val sceneId = backStackEntry.arguments?.getString("sceneId") ?: ""
                val exerciseViewModel: ExerciseViewModel = viewModel(
                    factory = ExerciseViewModel.Factory(sceneId, repository)
                )
                ExerciseScreen(
                    viewModel = exerciseViewModel,
                    ttsManager = ttsManager,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Vocabulary & Expressions Screen
            composable(Screen.Vocabulary.route) {
                val vocabViewModel: VocabularyViewModel = viewModel(
                    factory = VocabularyViewModel.Factory(repository)
                )
                VocabularyScreen(
                    viewModel = vocabViewModel,
                    ttsManager = ttsManager,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Progress & Evolution Screen
            composable(Screen.Progress.route) {
                val progressViewModel: ProgressViewModel = viewModel(
                    factory = ProgressViewModel.Factory(repository)
                )
                ProgressScreen(
                    viewModel = progressViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onStartAdaptiveRevision = { sceneId ->
                        navController.navigate(Screen.Exercise.createRoute(sceneId))
                    }
                )
            }

            // Contraction Lab Screen
            composable(Screen.Contractions.route) {
                ContractionLabScreen(
                    ttsManager = ttsManager,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // AI Scene Generator Screen
            composable(Screen.AiGenerator.route) {
                val genViewModel: AiSceneGeneratorViewModel = viewModel(
                    factory = AiSceneGeneratorViewModel.Factory(repository)
                )
                AiSceneGeneratorScreen(
                    viewModel = genViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onSceneGenerated = { newSceneId ->
                        navController.popBackStack()
                        navController.navigate(Screen.SceneDetail.createRoute(newSceneId))
                    }
                )
            }

            // YouTube Search Screen
            composable(Screen.YouTubeSearch.route) {
                val youtubeSearchViewModel: com.example.ui.screens.youtube.YouTubeSearchViewModel = viewModel(
                    factory = com.example.ui.screens.youtube.YouTubeSearchViewModel.Factory(repository.youtubeRepository)
                )
                com.example.ui.screens.youtube.YouTubeSearchScreen(
                    viewModel = youtubeSearchViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onSelectVideo = { videoId ->
                        navController.navigate(Screen.YouTubeStudy.createRoute(videoId))
                    }
                )
            }

            // YouTube Study Screen
            composable(
                route = Screen.YouTubeStudy.route,
                arguments = listOf(navArgument("videoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
                val youtubeStudyViewModel: com.example.ui.screens.youtube.YouTubeStudyViewModel = viewModel(
                    factory = com.example.ui.screens.youtube.YouTubeStudyViewModel.Factory(videoId, repository.youtubeRepository)
                )
                com.example.ui.screens.youtube.YouTubeStudyScreen(
                    viewModel = youtubeStudyViewModel,
                    ttsManager = ttsManager,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToScenes = {
                        navController.popBackStack(Screen.Home.route, false)
                        navController.navigate(Screen.Scenes.route)
                    },
                    onNavigateToMistakes = {
                        navController.navigate(Screen.MistakesReview.route)
                    }
                )
            }

            // Mistakes Review Screen
            composable(Screen.MistakesReview.route) {
                com.example.ui.screens.mistakes.MistakesReviewScreen(
                    repository = repository,
                    ttsManager = ttsManager,
                    onNavigateBack = { navController.popBackStack() },
                    onStartPractice = {
                        navController.navigate(Screen.Contractions.route)
                    }
                )
            }
        }
    }
}
