package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedState
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedStrings
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Composable
internal fun ShareContentExtracted(
    state: ShareContentExtractedState,
    strings: ShareContentExtractedStrings,
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
                            label = strings.title,
                            value = state.contentTitle,
                        )
                    }
                    state.contentDescription?.let {
                        Divider(
                            color = MaterialTheme.colors.background.copy(alpha = .3f),
                        )
                        ContentInfo(
                            label = strings.description,
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
                    label = strings.file,
                    value = state.fileName,
                )
                Divider(
                    color = MaterialTheme.colors.background.copy(alpha = .3f),
                )
                ContentInfo(
                    label = strings.size,
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
                        label = strings.titleOptional,
                        value = state.contentTitle,
                        isEditable = state.isContentEditable,
                        onValueChange = onContentTitleChange,
                    )
                }
                state.contentDescription?.let {
                    ContentInfoEditable(
                        label = strings.descriptionOptional,
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
        state.contentEntries.forEach { contentEntry ->
            ContentEntry(
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
    value: String,
    content: String,
    contentWarning: String?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 8.dp),
    ) {
        Row(
            modifier = Modifier
        ) {
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
