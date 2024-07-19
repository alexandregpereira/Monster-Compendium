package br.alexandregpereira.hunter.shareContent.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ShareSpell(
    val index: String,
    val name: String,
    val level: Int,
    val castingTime: String,
    val components: String,
    val duration: String,
    val range: String,
    val ritual: Boolean,
    val concentration: Boolean,
    val savingThrowType: String?,
    val damageType: String?,
    val school: String,
    val description: String,
    val higherLevel: String?,
)
