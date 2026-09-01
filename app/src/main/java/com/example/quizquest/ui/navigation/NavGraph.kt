package com.example.quizquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quizquest.data.local.QuizDatabase
import com.example.quizquest.data.repository.QuizRepository
import com.example.quizquest.ui.screens.*
import com.example.quizquest.viewmodel.AuthViewModel
import com.example.quizquest.viewmodel.HistoryViewModel
import com.example.quizquest.viewmodel.QuizViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun QuizNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val quizRepository = QuizRepository(QuizDatabase.getDatabase(context).quizDao())

    NavHost(
        navController = navController,
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(RegisterRoute) },
                onLoginSuccess = { navController.navigate(HomeRoute) { popUpTo(LoginRoute) { inclusive = true } } }
            )
        }
        
        composable<RegisterRoute> {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        
        composable<HomeRoute> {
            HomeScreen(
                authViewModel = authViewModel,
                onStartQuiz = { navController.navigate(QuizRoute) },
                onViewHistory = { navController.navigate(HistoryRoute) },
                onLogout = { navController.navigate(LoginRoute) { popUpTo(HomeRoute) { inclusive = true } } }
            )
        }
        
        composable<QuizRoute> {
            val userId = authViewModel.user.value?.uid ?: ""
            val quizViewModel: QuizViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return QuizViewModel(quizRepository) as T
                }
            })
            QuizScreen(
                viewModel = quizViewModel,
                userId = userId,
                onQuizFinished = { score, total -> 
                    navController.navigate(ResultRoute(score, total)) { popUpTo(QuizRoute) { inclusive = true } }
                }
            )
        }
        
        composable<ResultRoute> { backStackEntry ->
            val route: ResultRoute = backStackEntry.toRoute()
            ResultScreen(
                score = route.score,
                total = route.total,
                onBackToHome = { navController.navigate(HomeRoute) { popUpTo(HomeRoute) { inclusive = true } } }
            )
        }
        
        composable<HistoryRoute> {
            val userId = authViewModel.user.value?.uid ?: ""
            val historyViewModel: HistoryViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(quizRepository, userId) as T
                }
            })
            HistoryScreen(
                viewModel = historyViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
