package org.patifiner.client

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.jetbrains.compose.resources.stringResource
import org.patifiner.client.base.trackCompositions
import org.patifiner.client.design.PtfAnim
import org.patifiner.client.design.PtfScreen
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.views.PtfAlert
import org.patifiner.client.design.views.PtfScaffold
import org.patifiner.client.login.LoginScreen
import org.patifiner.client.main.MainScreen
import org.patifiner.client.signup.SignupScreen
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.no_connection

const val ROOT_CHILDREN_TAG = "RootChildren"

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun RootScreen(rootComponent: RootComponent) {
//    val isOnline = remember {true} // by  rootComponent.isOnline.collectAsState()
//    val snackbarHost = remember { SnackbarHostState() }
//
//    PtfTheme {
//        PtfScaffold(snackbarHostState = snackbarHost) {
//            Column {
//                AnimatedVisibility(visible = !isOnline) {
//                    PtfAlert(stringResource(Res.string.no_connection))
//                }
//                Children(
//                    modifier = Modifier.trackCompositions(ROOT_CHILDREN_TAG),
//                    stack = rootComponent.stack,
//                    animation = PtfAnim.provideStackAnimation(rootComponent)
//                ) { child ->
//                    when (val instance = child.instance) {
//                        is RootChild.Empty -> PtfScreen("Empty") {  }
//                        is RootChild.Main -> MainScreen(instance.component, snackbarHost)
//                        is RootChild.Login -> LoginScreen(instance.component, snackbarHost)
//                        is RootChild.Signup -> SignupScreen(instance.component, snackbarHost)
//                    }
//                }
//            }
//        }
//    }
}
