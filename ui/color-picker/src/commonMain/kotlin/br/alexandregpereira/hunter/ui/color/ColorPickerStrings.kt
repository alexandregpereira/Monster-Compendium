package br.alexandregpereira.hunter.ui.color

import br.alexandregpereira.hunter.localization.Language

internal interface ColorPickerStrings {
    val bottomSheetTitle: String
    val bottonSheetButonText: String
}

private data class ColorPickerEnStrings(
    override val bottomSheetTitle: String = "Color picker",
    override val bottonSheetButonText: String = "Confirm",
) : ColorPickerStrings

private data class ColorPickerPtStrings(
    override val bottomSheetTitle: String = "Escolher cor",
    override val bottonSheetButonText: String = "Confirmar",
) : ColorPickerStrings

private data class ColorPickerEsStrings(
    override val bottomSheetTitle: String = "Elige el color",
    override val bottonSheetButonText: String = "Confirmar",
) : ColorPickerStrings


internal fun Language.getStrings(): ColorPickerStrings {
    return when (this) {
        Language.ENGLISH -> ColorPickerEnStrings()
        Language.PORTUGUESE -> ColorPickerPtStrings()
        Language.SPANISH -> ColorPickerEsStrings()
    }
}
