package org.patifiner.client.binds

import com.russhwolf.settings.Settings
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import org.patifiner.client.AppScope
import org.patifiner.client.LoggedInScope
import org.patifiner.client.login.LoadProfileUseCase
import org.patifiner.client.login.LoginUseCase
import org.patifiner.client.login.LogoutUseCase
import org.patifiner.client.login.SignupUseCase
import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.login.data.TokenStorage
import org.patifiner.client.login.data.TokenStorageImpl
import org.patifiner.client.main.MainGraph


@ContributesTo(AppScope::class)
@ContributesTo(LoggedInScope::class)
interface BindsCommon {

    @Provides
    @SingleIn(AppScope::class)
    fun provideTokenStorage(settings: Settings): TokenStorage = TokenStorageImpl(settings)

    @Provides
    @SingleIn(AppScope::class)
    fun provideAuthRepo(appScope: CoroutineScope, http: HttpClient, ts: TokenStorage, fct: MainGraph.Factory): AuthRepository =
        AuthRepository(appScope, http, ts, fct)

    @Provides
    @SingleIn(AppScope::class)
    fun provideLogin(repo: AuthRepository): LoginUseCase = LoginUseCase(repo)

    @Provides
    @SingleIn(AppScope::class)
    fun provideLogoutUseCase(repo: AuthRepository): LogoutUseCase = LogoutUseCase(repo)

    @Provides
    @SingleIn(AppScope::class)
    fun provideGetProfileUseCase(repo: AuthRepository): LoadProfileUseCase = LoadProfileUseCase(repo)

    @Provides
    @SingleIn(AppScope::class)
    fun provideSignupUseCase(repo: AuthRepository): SignupUseCase = SignupUseCase(repo)

}
