package ru.orange.studback.dataservices.student_groups.routing

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document
import ru.orange.studback.common.extensions.*
import ru.orange.studback.dataservices.student_groups.data.GroupDbDto
import ru.orange.studback.dataservices.student_groups.data.GroupRequestDto


internal class Controller {

    private val mongoClient: MongoClient
        get() = MongoClients.create("mongodb://localhost:27017")

    suspend fun putGroup(call: ApplicationCall) {
        val requestModel = call.receive<GroupRequestDto>()
        val dbModel = requestModel.toDbDto()
        val dbJsonString = Json.encodeToString(dbModel)
        runCatching {
            mongoClient.use { mongoClient ->
                val database: MongoDatabase = mongoClient.getDatabase("learn")
                val collection: MongoCollection<Document?> = database.getCollection("groups")
                collection.insertOne(
                    Document.parse(dbJsonString)
                )
                call.respondSuccess()
            }
        }.onFailure {
            call.respondInternalFailure(it)
        }
    }

    suspend fun getGroup(call: ApplicationCall) {
        val groupName = call.request.queryParameters["groupName"]
        if (groupName == null) {
            call.respondBadRequest()
            return
        }
        val result = runCatching<String?> {
            val database: MongoDatabase = mongoClient.getDatabase("learn")
            val collection: MongoCollection<Document?> = database.getCollection("groups")
            val doc: Document? = collection.find(Document("groupName", groupName)).firstOrNull()
            if (doc != null) {
                println(doc.toJson())
            } else {
                println("No matching documents found.")
            }
            doc?.toJson()
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        val dbString = result.getOrNull()
        if (dbString == null) {
            call.respondNoContent()
            return
        }
        call.respondJson(dbString)
    }

    suspend fun updateGroup(call: ApplicationCall) {
        val requestModel = call.receive<GroupRequestDto>()
        val dbModel = requestModel.toDbDto()
        val dbJsonString = Json.encodeToString(dbModel)
        runCatching {
            mongoClient.use { mongoClient ->
                val database: MongoDatabase = mongoClient.getDatabase("learn")
                val collection: MongoCollection<Document?> = database.getCollection("groups")
                collection.replaceOne(
                    Document("groupName", requestModel.groupName),
                    Document.parse(dbJsonString)
                )
                call.respondSuccess()
            }
        }.onFailure {
            call.respondInternalFailure(it)
        }
    }

    suspend fun removeGroup(call: ApplicationCall) {
        val groupName = call.request.queryParameters["groupName"]
        val result = runCatching {
            mongoClient.use { mongoClient ->
                val database: MongoDatabase = mongoClient.getDatabase("learn")
                val collection: MongoCollection<Document?> = database.getCollection("groups")
                collection.deleteOne(Document("groupName", groupName))
                call.respondSuccess()
            }
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }
}

private fun GroupRequestDto.toDbDto(): GroupDbDto = GroupDbDto(
    groupName = groupName,
    educationBeginDate = educationBeginDate,
    studentNumbers = studentNumbers
)