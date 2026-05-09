package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedState
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.noIndicationClick

@Composable
internal fun ShareContentExtracted(
    state: ShareContentExtractedState,
    onContentTitleChange: (String) -> Unit = {},
    onContentDescriptionChange: (String) -> Unit = {},
) = Column(
    verticalArrangement = Arrangement.spacedBy(32.dp),
    modifier = Modifier.padding(top = 8.dp),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if ((state.contentTitle != null || state.contentDescription != null) && state.isContentEditable.not()) {
            AppCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    state.contentTitle?.let {
                        ContentInfo(
                            label = "Title",
                            value = state.contentTitle,
                        )
                    }
                    state.contentDescription?.let {
                        Divider(
                            color = MaterialTheme.colors.background.copy(alpha = .3f),
                        )
                        ContentInfo(
                            label = "Description",
                            value = state.contentDescription,
                        )
                    }
                }
            }
        }

        AppCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ContentInfo(
                    label = "File",
                    value = state.fileName,
                )
                Divider(
                    color = MaterialTheme.colors.background.copy(alpha = .3f),
                )
                ContentInfo(
                    label = "Size",
                    value = state.contentSize,
                )
            }
        }

        if ((state.contentTitle != null || state.contentDescription != null) && state.isContentEditable) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.contentTitle?.let {
                    ContentInfoEditable(
                        label = "Title (optional)",
                        value = state.contentTitle,
                        isEditable = state.isContentEditable,
                        onValueChange = onContentTitleChange,
                    )
                }
                state.contentDescription?.let {
                    ContentInfoEditable(
                        label = "Description (optional)",
                        value = state.contentDescription,
                        isEditable = state.isContentEditable,
                        multiline = true,
                        onValueChange = onContentDescriptionChange,
                    )
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        state.contentEntries.forEachIndexed { _, contentEntry ->
            ContentEntry(
                checked = null,
                value = contentEntry.quantity,
                content = contentEntry.content,
                contentWarning = contentEntry.contentWarning,
            )
        }
    }
}

@Composable
private fun ContentInfo(
    label: String,
    value: String,
) = Column(
    verticalArrangement = Arrangement.spacedBy(4.dp),
    modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
) {
    Text(
        text = label,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colors.onSurface.copy(alpha = .7f),
        fontSize = 14.sp,
    )

    Text(
        text = value,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colors.onSurface,
        fontSize = 18.sp,
    )

}

@Composable
private fun ContentInfoEditable(
    label: String,
    value: String,
    isEditable: Boolean = false,
    multiline: Boolean = false,
    onValueChange: (String) -> Unit,
) = Column(
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    if (isEditable) {
        AppTextField(
            text = value,
            label = label,
            multiline = multiline,
            onValueChange = onValueChange,
            modifier = Modifier
        )
    } else {
        ContentInfo(
            label = label,
            value = value,
        )
    }
}

@Composable
private fun ContentEntry(
    checked: Boolean?,
    value: String,
    content: String,
    contentWarning: String?,
    onCheckedChange: () -> Unit = {},
) {
    val alpha by animateFloatAsState(
        targetValue = if (checked == false) .5f else 1f,
        label = "Content alpha",
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.alpha(alpha)
            .padding(horizontal = 8.dp)
            .noIndicationClick(onCheckedChange),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            if (checked != null) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = null,
                )
            }

            Text(
                text = value,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface,
                fontSize = 20.sp,
            )
        }

        if (contentWarning.isNullOrBlank().not()) {
            ContentWarning(
                contentWarning = contentWarning,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        if (content.isNotBlank()) {
            Text(
                text = content,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colors.onSurface,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun ContentWarning(
    contentWarning: String,
    modifier: Modifier = Modifier,
) {
    AppCard(
        elevation = 8.dp,
        modifier = modifier
    ) {
        Text(
            text = contentWarning,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colors.onSurface,
            fontSize = 16.sp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
