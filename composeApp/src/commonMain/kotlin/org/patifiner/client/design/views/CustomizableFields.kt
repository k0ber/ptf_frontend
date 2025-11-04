package org.patifiner.client.design.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcEmail

// Это самодельный текст филд с бэкграундом и логикой что лэйбл улетает за рамку
// После того когда я его сделал, решил, что дефолтный лучше :)
@Composable
fun EmailFieldStates() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // 1) !Focused && Empty
        EmailFieldBasic(
            value = "",
            onValueChange = {},
            label = "E-mail",
            example = "example@name.com",
//                supportingText = "",
            isError = false
        )
        // 1) !Focused && !Empty
        EmailFieldBasic(
            value = "value",
            onValueChange = {},
            label = "E-mail",
            example = "example@name.com",
            supportingText = "Supporting text",
            isError = false
        )
        // can't find a way to show focused text field on preview
//            // 2) Focused && !Empty
//            EmailFieldBasic(
//                value = "",
//                onValueChange = {},
//                label = "E-mail",
//                example = "example@name.com",
//                supportingText = "Supporting when focused",
//                isError = false
//            )
        // 3) ERROR
        EmailFieldBasic(
            value = "bad@value",
            onValueChange = {},
            label = "E-mail",
            example = "example@name.com",
            supportingText = "Invalid email",
            isError = true
        )
    }
}


@Composable
fun EmailFieldBasic(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,

    focusRequester: FocusRequester = remember { FocusRequester() },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    debugForceFocused: Boolean = false, // todo: DELETE?

    // тексты
    label: String,
    labelFontSize: TextUnit = 16.sp,
    example: String = "",
    supportingText: String = "",

    // IME
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},

    // геометрия/анимации
    shape: Shape = RoundedCornerShape(14.dp),
    borderWidth: Dp = 2.dp,            // фиксированная толщина бордера

    // ICON
    iconPaddingStart: Dp = 12.dp,
    iconPaddingEnd: Dp = 12.dp,

    animMillis: Int = 160,
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val density = LocalDensity.current


    // LABEL LINE ???
    val lineHeightUnfocused = MaterialTheme.typography.labelLarge.lineHeight   // TextUnit
    val labelLinePx = with(density) { lineHeightUnfocused.toPx() }

    val isFloated = isFocused || value.isNotEmpty()
    val animState by animateFloatAsState(if (isFloated) 1f else 0f, tween(animMillis), label = "labelT")

    // COLORS
    val colorScheme = MaterialTheme.colorScheme
    val borderColor = if (isError) colorScheme.error else colorScheme.primary

    val borderPaddingTop = 20.dp
    val rowVerticalPadding = 12.dp
    val iconSize = 24.dp

    Column(
        modifier = modifier
//            .background(color = Color.Green) // todo: DEBUG
            .padding(top = borderPaddingTop)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .focusRequester(focusRequester)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusRequester.requestFocus() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawWithCache {
                        val outline = shape.createOutline(size, layoutDirection, this)
                        val stroke = Stroke(width = borderWidth.toPx())
                        onDrawBehind {
//                            withTransform({ translate(top = topInsetPx) }) {
                            drawOutline(outline, color = colorScheme.secondaryContainer)
                            drawOutline(outline, color = borderColor, style = stroke)
//                            }
                        }
                    }
                    .padding(vertical = rowVerticalPadding)
            ) {

                Spacer(Modifier.width(iconPaddingStart))
                Icon(
                    painter = rememberVectorPainter(PtfIcons.IcEmail),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(Modifier.width(iconPaddingEnd))

                // ICON EXAMPLE AND TEXT FIELD
                Box(modifier = Modifier.weight(1f)) {
                    if (isFocused && value.isEmpty()) { // TODO ANIMATE APPEARANCE
                        // ANIMATE EXAMPLE APPEARANCE : animState for alpha
                        Text( // EXAMPLE
                            text = example,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.outline,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, start = 4.dp)
                        )
                    }
                    BasicTextField( // INPUT
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        enabled = enabled,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.primary),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = imeAction
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { onImeAction() },
                            onDone = { onImeAction() }
                        ),
                        interactionSource = interactionSource,
                        cursorBrush = SolidColor(colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // LABEL
            val labelTy = lerp(
                start = 0f,
                stop = with(density) {
                    rowVerticalPadding.toPx() * -1 - borderPaddingTop.toPx() - 2.dp.toPx()
                },
                fraction = animState
            )
            val labelTx: Float = lerp(
                start = with(density) {
                    iconPaddingStart.toPx() + iconSize.toPx() + 2.dp.toPx()
                },
                stop = 0f,
                fraction = animState
            )
            Text(
                text = label,
                fontSize = labelFontSize,
                style = MaterialTheme.typography.labelLarge,
                color = if (isError) colorScheme.error else colorScheme.primary,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier
                    .graphicsLayer {
                        translationY = labelTy
                        translationX = labelTx
                    }
                    .padding(
                        top = rowVerticalPadding + 1.5.dp,
                        start = iconPaddingStart
                    )
            )
        }

        // SUPPORT
        Text(
            text = supportingText,
            style = MaterialTheme.typography.bodySmall,
            color = if (isError) colorScheme.error else colorScheme.outline,
            maxLines = 1,
            modifier = Modifier.padding(start = iconPaddingStart, top = 2.dp)
        )
    }
}

@Preview
@Composable
fun EmailFieldLight() {
    AppTheme(forceDarkMode = false) {
        EmailFieldStates()
    }
}

@Preview
@Composable
fun EmailFieldDark() {
    AppTheme(forceDarkMode = true) {
        EmailFieldStates()
    }
}