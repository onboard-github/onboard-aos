package com.yapp.bol.data.remote

import com.yapp.bol.data.model.base.ErrorResponse
import com.yapp.bol.data.model.group.CheckGroupJonByAccessCodeRequest
import com.yapp.bol.data.model.group.NewGroupApiRequest
import com.yapp.bol.data.model.group.CheckGroupJoinByAccessCodeResponse
import com.yapp.bol.data.model.group.GroupDetailResponse
import com.yapp.bol.data.model.group.GroupSearchApiResponse
import com.yapp.bol.data.model.group.NewGroupApiResponse
import com.yapp.bol.data.model.group.RandomImageResponse
import com.yapp.bol.data.model.group.UserRankApiResponse
import com.yapp.bol.data.model.member.GroupMemberRequest
import com.yapp.bol.data.model.member.MemberDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {

    @POST("v1/group")
    suspend fun postCreateGroup(
        @Body newGroupApiRequest: NewGroupApiRequest,
    ): Response<NewGroupApiResponse>

    @GET("/v1/group/{groupId}/game/{gameId}")
    suspend fun getUserRank(
        @Path("groupId") groupId: Int,
        @Path("gameId") gameId: Int,
    ): Response<UserRankApiResponse>

    @GET("/v1/group/default-image")
    suspend fun getGroupDefaultImage(): Response<RandomImageResponse>

    @GET("/v1/group")
    suspend fun getGroupSearchResult(
        @Query("keyword") name: String,
        @Query("pageNumber") page: String,
        @Query("pageSize") pageSize: String,
    ): Response<GroupSearchApiResponse>

    @GET("/v1/group/{groupId}")
    suspend fun getGroupDetail(
        @Path("groupId") groupId: Long
    ): Response<GroupDetailResponse>

    @POST("/v1/group/{groupId}/accessCode")
    suspend fun checkGroupJoinAccessCode(
        @Path("groupId") groupId: String,
        @Body accessCode: CheckGroupJonByAccessCodeRequest,
    ): Response<CheckGroupJoinByAccessCodeResponse>

    @PATCH("/api/v1/group/{groupId}/member/{memberId}")
    suspend fun patchGroupMemberNickname(
        @Path("groupId") groupId: String,
        @Path("memberId") memberId: String,
        @Body groupMemberRequest: GroupMemberRequest
    ): Response<MemberDTO>

    @PATCH("/v1/group/{groupId}/member/{memberId}/assign-owner")
    suspend fun updateOwner(
        @Path("groupId") groupId: String,
        @Path("memberId") memberId: String,
    ): Response<ErrorResponse>

    @DELETE("/v1/group/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId: String,
    ): Response<ResponseBody>
}
