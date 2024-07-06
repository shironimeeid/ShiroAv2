package ani.dantotsu.connections.github

import ani.dantotsu.Mapper
import ani.dantotsu.R
import ani.dantotsu.client
import ani.dantotsu.getAppString
import ani.dantotsu.settings.Developer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement

class Contributors {

    fun getContributors(): Array<Developer> {
        var developers = arrayOf<Developer>()
        runBlocking(Dispatchers.IO) {
            val repo = getAppString(R.string.repo)
            val res = client.get("https://api.github.com/repos/$repo/contributors")
                .parsed<JsonArray>().map {
                    Mapper.json.decodeFromJsonElement<GithubResponse>(it)
                }
            res.forEach {
                if (it.login == "SunglassJerry") return@forEach
                val role = when (it.login) {
                    "rebelonion" -> "Owner & Maintainer"
                    "sneazy-ibo" -> "Contributor & Comment Moderator"
                    "WaiWhat" -> "Icon Designer"
                    else -> "Contributor"
                }
                developers = developers.plus(
                    Developer(
                        it.login,
                        it.avatarUrl,
                        role,
                        it.htmlUrl
                    )
                )
            }
            developers = developers.plus(
                arrayOf(
                    Developer(
                        "renn.ezzcos",
                        "https://avatars.githubusercontent.com/u/120251625?v=4",
                        "Moderator and Translator",
                        "https://shirokochan672.pythonanywhere.com/"
                    ),
                )
            )
        }
        return developers
    }


    @Serializable
    data class GithubResponse(
        @SerialName("login")
        val login: String,
        @SerialName("avatar_url")
        val avatarUrl: String,
        @SerialName("html_url")
        val htmlUrl: String
    )
}