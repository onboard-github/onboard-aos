package com.yapp.bol.data.remote

import com.yapp.bol.data.model.game.GameApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApi {

    @GET("/v1/group/{groupId}/game")
    suspend fun getGameList(
        @Path("groupId") groupId: Int,
        @Query("sort") sort: String,
    ): Response<GameApiResponse>
}
