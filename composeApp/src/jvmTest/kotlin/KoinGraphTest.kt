import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verifyAll
import org.patifiner.client.ApiConfig
import org.patifiner.client.KoinAppConfig
import org.patifiner.client.NetworkObserver
import org.patifiner.client.appModule
import org.patifiner.client.root.RootNavigator
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.login.data.SessionManager
import org.patifiner.client.root.login.data.SessionStorage
import org.patifiner.client.root.main.MainNavigator
import org.patifiner.client.root.main.intro.IntroNavigator
import org.patifiner.client.root.main.intro.user.UserInteractor
import org.patifiner.client.root.main.mainModule
import org.patifiner.client.root.rootModule

// koin verifyAll(..) requires jvm, so we can't put it in common tests
class CheckModulesTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        listOf(
            appModule,
            rootModule,
            mainModule
        ).verifyAll(
            extraTypes = listOf(
                HttpClientEngineFactory::class,
                HttpClientEngine::class,
                ApiConfig::class,
                CoroutineScope::class,
                Settings::class,
                NetworkObserver::class,
                HttpClient::class,
                KoinAppConfig::class,

                AuthRepository::class,
                SessionStorage::class,
                SessionManager::class,

                RootNavigator::class,
                MainNavigator::class,
                IntroNavigator::class,

                UserInteractor::class,
            )
        )
    }

}
