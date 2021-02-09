package com.soufianekre.smallhub.data.models

import com.google.gson.annotations.SerializedName

data class GitHubStatusModel(
        @SerializedName("status") var status: GithubStatus? = null
)

data class GithubStatus(
        @SerializedName("description") var description: String? = null,
        @SerializedName("indicator") var indicator: String? = null
)