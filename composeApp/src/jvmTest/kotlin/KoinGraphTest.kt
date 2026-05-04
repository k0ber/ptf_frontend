import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import org.patifiner.client.ApiConfig
import org.patifiner.client.KoinAppConfig
import org.patifiner.client.prepareModulesFromConfig

// koin verifyAll(..) requires jvm, so we can't put it in common tests
class CheckModulesTest : KoinTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        val testConfig = KoinAppConfig(
            engine = mockk(relaxed = true),
            apiConfig = ApiConfig(baseUrl = "http://localhost", port = 8080),
            appScope = mockk(relaxed = true),
            isDev = true
        )

        val allAppModules = prepareModulesFromConfig(testConfig)

        module { includes(allAppModules) }.verify(
            extraTypes = listOf(
                Boolean::class, // for config constructor
            )
        )
    }
}
