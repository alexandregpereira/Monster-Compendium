package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.spell.detail.R
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun SpellDescription(
    description: String,
    higherLevel: String? = null,
    modifier: Modifier = Modifier
) = Column(modifier) {
    Text(
        text = description,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
    )
    higherLevel?.let {
        Text(
            buildAnnotatedString {

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                    )
                ) {
                    append("${stringResource(R.string.spell_detail_higher_level)} ")
                }

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Light
                    )
                ) {
                    append(it)
                }

            },
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
private fun SpellDescriptionPreview() = Window {
    SpellDescription(
        description = "asdadsas ads asda sd asd asdasdasd asd asd asd asd asd asd as dassd asdasdasa" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                "asd asdasdasd asdasdasdasdasdasasdasdasdasdasdasdasdasdasd",
        higherLevel = "asdadsas ads asda sd asd asdasdasd asd asd asd asd asd asd as dassd asdasdasa" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd"
    )
}
