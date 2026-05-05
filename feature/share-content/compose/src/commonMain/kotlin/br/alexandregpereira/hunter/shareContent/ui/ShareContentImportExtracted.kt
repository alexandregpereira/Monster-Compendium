package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
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
    onFileNameChange: (String) -> Unit = {},
) = Column(
    verticalArrangement = Arrangement.spacedBy(32.dp)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "File",
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colors.onSurface.copy(alpha = .7f),
            fontSize = 14.sp,
        )
        if (state.isFileNameEditable) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppTextField(
                    text = state.fileName,
                    label = "Name",
                    onValueChange = onFileNameChange,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = ".compendium",
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            Text(
                text = state.fileName,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Size",
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colors.onSurface.copy(alpha = .7f),
            fontSize = 14.sp,
        )
        Text(
            text = state.contentSize,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colors.onSurface,
            fontSize = 18.sp,
        )
    }

    state.contentEntries.forEachIndexed { _, contentEntry ->
        ContentEntry(
            checked = null,
            value = contentEntry.quantity,
            content = contentEntry.content,
            contentWarning = contentEntry.contentWarning,
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
        modifier = Modifier.alpha(alpha).noIndicationClick(onCheckedChange),
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
