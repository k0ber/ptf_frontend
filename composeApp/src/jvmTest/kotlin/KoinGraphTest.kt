import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import org.patifiner.client.ApiConfig
import org.patifiner.client.NetworkObserver
import org.patifiner.client.appModule

// koin verify(..) requires jvm, so we can't put it in common tests
class CheckModulesTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        appModule.verify(
            extraTypes = listOf(
                HttpClientEngineFactory::class,
                HttpClientEngine::class,
                ApiConfig::class,
                CoroutineScope::class,
                Settings::class,
                NetworkObserver::class,
                HttpClient::class
            )
        )
    }

}
