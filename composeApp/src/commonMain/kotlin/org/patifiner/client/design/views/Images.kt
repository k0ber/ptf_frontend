package org.patifiner.client.design.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.startAccessingSecurityScopedResource
import io.github.vinceglb.filekit.stopAccessingSecurityScopedResource
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.icons.IcArrowUp

@Stable
sealed interface AvatarSource {
    data class Remote(val url: String) : AvatarSource
    data class Local(val file: PlatformFile) : AvatarSource
}

@Composable
fun PtfAvatar(
    source: AvatarSource?,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isUploading: Boolean = false,
    placeholderText: String = "?"
) {
    if (source is AvatarSource.Local) {  // iOS/macOS Sandbox Access
        DisposableEffect(source.file) {
            source.file.startAccessingSecurityScopedResource()
            onDispose {
                source.file.stopAccessingSecurityScopedResource()
            }
        }
    } // extension for source?

    val context = LocalPlatformContext.current
    val model = remember(source, context) {
        ImageRequest.Builder(context)
            .data(
                when (source) {
                    is AvatarSource.Remote -> source.url
                    is AvatarSource.Local -> source.file
                    null -> null
                }
            )
            .crossfade(true)
            .build()
    }

    Box(
        modifier = modifier
            .size(AVATAR_SIZE.dp)
            .clip(CircleShape)
            .background(colorScheme.surfaceVariant)
            .clickable(enabled = !isUploading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        PtfText(text = placeholderText, fontSize = 40)

        // todo further customisation required
        AsyncImage(
            model = model,
            contentDescription = "Avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        if (isUploading) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = colorScheme.primary,
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
fun PhotoThumbnail(url: String, onDelete: () -> Unit) {
    Box(
        Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(Color.Black.copy(0.5f), CircleShape)
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                IcArrowUp,
                null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ImagesPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PtfAvatar(source = null, onClick = {})
        PhotoThumbnail("") {}
    }
}

@Preview
@Composable
fun ImagesPreviewLight() {
    PtfPreview { ImagesPreview() }
}

@Preview
@Composable
fun ImagesPreviewDark() {
    PtfPreview(forceDarkMode = true) { ImagesPreview() }
}
