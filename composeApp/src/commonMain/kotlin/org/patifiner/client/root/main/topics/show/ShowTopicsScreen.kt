package org.patifiner.client.root.main.topics.show

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.patifiner.client.design.icons.IcEmail
import org.patifiner.client.design.icons.IcVisibilityOff
import org.patifiner.client.root.RootSnackbarHost
import org.patifiner.client.root.main.topics.UserTopicDto

@Composable
fun ShowTopicsScreen(viewModel: ShowTopicsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHost = RootSnackbarHost.current

    LaunchedEffect(Unit) {
        viewModel.labels.collect { e ->
            when (e) {
                is ShowTopicsEvent.Error -> snackbarHost.showSnackbar(e.message)
                // is it needed?
//                is ShowTopicsEvent.Removed -> snackbarHost.showSnackbar("Удалено: ${e.count}")
//                is ShowTopicsEvent.Updated -> snackbar.showSnackbar("Обновлено: ${e.topic.topic.name}")
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onAddClick) {
                Icon(IcEmail, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = padding
            ) {
                items(state.topics, key = { it.id }) { ut ->
                    UserTopicRow(
                        userTopic = ut,
                        onRemove = { viewModel.onIntent(ShowTopicsIntent.Remove(ut.id)) })
                }
            }
        }
    }
}

@Composable
fun UserTopicRow(
    userTopic: UserTopicDto,
    onRemove: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userTopic.topic.name,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = userTopic.level.name,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = onRemove) {
                Icon(IcVisibilityOff, contentDescription = "Удалить")
            }
        }
    }
}
