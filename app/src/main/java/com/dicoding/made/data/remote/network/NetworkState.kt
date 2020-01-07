package com.dicoding.made.data.remote.network

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: Int? = null
) {
    companion object {
        val LOADED =
            NetworkState(Status.SUCCESS)
        val LOADING =
            NetworkState(Status.LOADING)

        fun error(msg: Int?) = NetworkState(
            Status.ERROR,
            msg
        )
    }
}