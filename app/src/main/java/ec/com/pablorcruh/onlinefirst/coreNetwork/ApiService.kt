package ec.com.pablorcruh.onlinefirst.coreNetwork

import ec.com.pablorcruh.onlinefirst.coreNetwork.models.CreateTaskRequest
import ec.com.pablorcruh.onlinefirst.coreNetwork.models.SyncResponse
import ec.com.pablorcruh.onlinefirst.coreNetwork.models.TaskDto
import ec.com.pablorcruh.onlinefirst.coreNetwork.models.UpdateTaskRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/sync")
    suspend fun sync(
        @Query("cursos") cursor: Int
    ): Response<SyncResponse>

    @POST("/tasks")
    suspend fun createTask(
        @Body createTaskRequest: CreateTaskRequest
    ): Response<TaskDto>

    @PUT("/tasks/{id}")
    suspend fun updateTask(
        @Path("id")id: String,
        @Body updateTaskRequest: UpdateTaskRequest
    ): Response<TaskDto>

    @DELETE("/task/{id}")
    suspend fun deleteTask(
        @Path("id") id: String
    ): Response<Unit>
}