@file:OptIn(KoinExperimentalAPI::class)

package org.patifiner.client.root

import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.patifiner.client.root.login.LoginScreen
import org.patifiner.client.root.login.LoginStore
import org.patifiner.client.root.login.LoginStoreFactory
import org.patifiner.client.root.login.LoginViewModel
import org.patifiner.client.root.signup.SignupScreen
import org.patifiner.client.root.signup.SignupStore
import org.patifiner.client.root.signup.SignupStoreFactory
import org.patifiner.client.root.signup.SignupUseCase
import org.patifiner.client.root.signup.SignupViewModel

val rootModule = module {
    singleOf(::RootNavigator)

    factory<LoginStore> { LoginStoreFactory(factory = get(), repo = get()).create() }

    factory<SignupStore> { SignupStoreFactory(factory = get(), signupUseCase = get()).create() }
    factoryOf(::SignupUseCase)

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)

    navigation<PtfRoute.Login> { LoginScreen(viewModel = koinViewModel<LoginViewModel>()) }
    navigation<PtfRoute.Signup> { SignupScreen(viewModel = koinViewModel<SignupViewModel>()) }
}
