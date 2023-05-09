package framework.permissibles

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    @SerialName("_id") val _id: String,
    val format: String = "[%s]: %s",
    val permissions: ArrayList<String> = arrayListOf(),
    val inheritance: ArrayList<String> = arrayListOf("Member"),
)

