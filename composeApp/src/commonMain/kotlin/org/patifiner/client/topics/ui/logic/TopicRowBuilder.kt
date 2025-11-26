package org.patifiner.client.topics.ui.logic

import TopicViewModel
import androidx.compose.ui.layout.Placeable
import kotlin.math.max
import kotlin.math.min

data class RowBuildingResult(val rows: List<LayoutItem>, val bgSizes: BgSizes?)

data class BgSizes(val startX: Int, val width: Int, val startY: Int, val height: Int)

fun buildTopicTreeRows(
    measuredTopics: List<MeasuredTopic>,
    measuredChildren: List<MeasuredTopic>,
    maxWidth: Int,
    hSpacingPx: Int,
    rowBaseHeight: Int,
    verticalSpacingPx: Int,
    openedTopic: TopicViewModel?,
    breadcrumbPlaceable: Placeable?,
): RowBuildingResult {
    val layoutItems = mutableListOf<LayoutItem>()
    var currentRowItems = mutableListOf<MeasuredTopic>()
    var currentRowWidth = 0
    var currentRowHeight = 0

    // Переменные для однопроходного расчета геометрии
    var currentY = 0 // Текущая координата Y (верх следующей строки)
    var bgStartY = -1
    var activeRowInsertionIndex = -1 // Куда вставлять детей
    var maxBlockWidth = 0 // Переменная для отслеживания ширины фона

    fun addRow(row: LayoutItem.TopicsRow) { // todo: i hate this..
        if (row.selected != null) {
            bgStartY = currentY + rowBaseHeight + verticalSpacingPx / 2
            activeRowInsertionIndex = layoutItems.size + 1
            maxBlockWidth = max(maxBlockWidth, row.rowWidth)
        }
        layoutItems.add(row)
        currentY += row.height + verticalSpacingPx
        if (activeRowInsertionIndex == -1) {
            maxBlockWidth = max(maxBlockWidth, row.rowWidth)
        }
    }

    var openedMeasuredTopic: MeasuredTopic? = null
    measuredTopics.forEach { measuredTopic ->
        val isOpened = measuredTopic.topic.isOpenedOrContainsOpened(openedTopic)
        val rowHeightForCalc = if (isOpened) rowBaseHeight * 2 + verticalSpacingPx else rowBaseHeight
        val itemWidth = measuredTopic.placeable.width

        val newRowWidth = if (currentRowItems.isEmpty()) itemWidth else currentRowWidth + hSpacingPx + itemWidth

        if (newRowWidth <= maxWidth) {
            currentRowItems.add(measuredTopic)
            currentRowWidth = newRowWidth
            currentRowHeight = max(currentRowHeight, rowHeightForCalc)
        } else {
            // Завершаем текущую строку
            val row = createRow(currentRowItems, currentRowHeight, currentRowWidth, openedMeasuredTopic, breadcrumbPlaceable, false)
            addRow(row)

            // Начинаем новую
            currentRowItems = mutableListOf(measuredTopic)
            currentRowWidth = itemWidth
            currentRowHeight = rowHeightForCalc
        }

        if (isOpened) {
            openedMeasuredTopic = measuredTopic
        }
    }

    // Добавляем последнюю строку, если есть
    if (currentRowItems.isNotEmpty()) {
        val row = createRow(currentRowItems, currentRowHeight, currentRowWidth, openedMeasuredTopic, breadcrumbPlaceable, false)
        addRow(row)
    }

    // 2. ВСТАВКА ДЕТЕЙ И РАСЧЕТ КОНЦА ФОНА
    var bgHeight = 0
    var bgWidth = 0
    var bgStartX = 0
    if (openedMeasuredTopic != null && activeRowInsertionIndex != -1) {
        val childRows = buildChildRows(measuredChildren, maxWidth, hSpacingPx, rowBaseHeight)
        val safeInsertIndex = min(activeRowInsertionIndex, layoutItems.size)

        val heightBeforeChildren = layoutItems.take(safeInsertIndex).sumOf { it.height }
        val spacingBeforeChildren = safeInsertIndex * verticalSpacingPx
        val childrenStartY = heightBeforeChildren + spacingBeforeChildren

        var childrenMaxWidth = 0
        childRows.forEach { childRow ->
            childrenMaxWidth = max(childrenMaxWidth, childRow.rowWidth)
        }

        // --- Расчет ширины и X-координаты ---
        // Добавим небольшой горизонтальный отступ к самой широкой строке для "обхвата"
        val horizontalPadding = hSpacingPx * 2
        bgWidth = min(maxWidth, childrenMaxWidth + horizontalPadding)

        // Центрирование: (Общая ширина - Ширина фона) / 2
        bgStartX = max(0, (maxWidth - bgWidth) / 2)

        // --- Финализация Y-координат ---
        layoutItems.addAll(safeInsertIndex, childRows)

        val childrenTotalHeight = childRows.sumOf { it.height + verticalSpacingPx }
        val bgEndYWithFinalPadding = childrenStartY + childrenTotalHeight
        val bgEndY = bgEndYWithFinalPadding - verticalSpacingPx / 2

        bgHeight = bgEndY - bgStartY
    }

    val bgSizes = if (bgHeight > 0) BgSizes(
        startX = bgStartX,
        width = bgWidth,
        startY = bgStartY,
        height = bgHeight
    ) else null

    return RowBuildingResult(layoutItems, bgSizes)
}

// --- Helpers ---

private fun createRow(
    topics: List<MeasuredTopic>,
    height: Int,
    width: Int,
    activeMeasuredTopic: MeasuredTopic?,
    breadcrumbPlaceable: Placeable?,
    isChild: Boolean
): LayoutItem.TopicsRow {
    val selected = topics.firstOrNull { it.topic.id == activeMeasuredTopic?.topic?.id }
    val breadcrumbs = if (selected != null) breadcrumbPlaceable else null
    return LayoutItem.TopicsRow(
        topics = topics,
        height = height,
        rowWidth = width,
        selected = selected,
        breadcrumbPlaceable = breadcrumbs,
        isChildRow = isChild
    )
}

private fun buildChildRows(
    measuredChildren: List<MeasuredTopic>,
    maxWidth: Int,
    hSpacingPx: Int,
    childRowHeight: Int
): List<LayoutItem.TopicsRow> {
    val childRows = mutableListOf<LayoutItem.TopicsRow>()
    var currentItems = mutableListOf<MeasuredTopic>()
    var currentWidth = 0

    measuredChildren.forEach { child ->
        val newWidth = if (currentItems.isEmpty()) child.placeable.width
        else currentWidth + hSpacingPx + child.placeable.width

        if (newWidth <= maxWidth) {
            currentItems.add(child)
            currentWidth = newWidth
        } else {
            childRows.add(createRow(currentItems, childRowHeight, currentWidth, null, null, true))
            currentItems = mutableListOf(child)
            currentWidth = child.placeable.width
        }
    }
    if (currentItems.isNotEmpty()) {
        childRows.add(createRow(currentItems, childRowHeight, currentWidth, null, null, true))
    }
    return childRows
}