package org.patifiner.client.topics.ui.viewing

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.design.icons.ptficons.IcVisibilityOff
import org.patifiner.client.topics.UserTopicDto

@Composable
fun UserTopicsScreen(component: UserTopicsComponent, snackbar: SnackbarHostState) {
    val state by component.state.collectAsState()

    LaunchedEffect(Unit) {
        component.events.collect { e ->
            when (e) {
                is UserTopicsEvent.Error -> snackbar.showSnackbar(e.message)
                is UserTopicsEvent.Removed -> snackbar.showSnackbar("Удалено: ${e.count}")
                is UserTopicsEvent.Updated -> snackbar.showSnackbar("Обновлено: ${e.topic.topic.name}")
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = component::onAddClick) {
                Icon(PtfIcons.IcEmail, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        if (state.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(contentPadding = padding) {
                items(state.topics, key = { it.id }) { ut ->
                    UserTopicRow(
                        userTopic = ut,
                        onRemove = { component.onRemoveTopic(ut.id) },
                        // TODO: onUpdateLevel etc.
                    )
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
            Text(userTopic.topic.name, Modifier.weight(1f))
            Text(userTopic.level.name)
            IconButton(onClick = onRemove) {
                Icon(PtfIcons.IcVisibilityOff, contentDescription = "Удалить")
            }
        }
    }
}
