package ru.orange.studback.dataservices.connections.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.neo4j.driver.Driver
import org.neo4j.driver.Query
import ru.orange.studback.common.extensions.respondBadRequest
import ru.orange.studback.common.extensions.respondInternalFailure
import ru.orange.studback.common.extensions.respondJson
import ru.orange.studback.common.extensions.respondSuccess
import ru.orange.studback.dataservices.connections.data.ConnectionDto

internal class Controller(private val neoDriver: Driver) {

    suspend fun putConnection(call: ApplicationCall) {
        val requestModel = call.receive<ConnectionDto>()

        val result = runCatching {
            neoDriver.session().use { session ->
                session.executeWrite { tx ->
                    val query = Query(
                        """
                            MERGE (course:Course {name: '${requestModel.courseName}'})
                            MERGE (group:Group {name: '${requestModel.groupName}'})
                            MERGE (student:Student {name: '${requestModel.studentNumber}'})
                            
                            MERGE (student)-[:CONTAINS_IN]->(group)-[:CONTAINS_IN]->(course)
                            
                        """.trimIndent()
                    )
                    val result = tx.run(query)
                }
            }

        }

        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }

    suspend fun getConnection(call: ApplicationCall) {
        val studentNum = call.request.queryParameters["studentNumber"]
        if (studentNum == null) {
            call.respondBadRequest()
            return
        }

        val result = runCatching {
            neoDriver.session().use { session ->
                session.executeWrite { tx ->
                    val query = Query(
                        """
                            MATCH (student:Student {name: '$studentNum'})-[:CONTAINS_IN]->(group)-[:CONTAINS_IN]->(course)
                            RETURN course.name, group.name, student.name
                            
                        """.trimIndent()
                    )
                    val result = tx.run(query)
                    val record = result.single()
                    val model = ConnectionDto(
                        courseName = record.get(0).asString(),
                        groupName = record.get(1).asString(),
                        studentNumber = record.get(2).asString(),
                    )
                    Json.encodeToString(model)
                }
            }

        }

        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondJson(result.getOrNull()!!)
    }

    /**
     * update:
     * MATCH (student:Student {name: '$studentNum'})-[r1:CONTAINS_IN]->(group)-[r2:CONTAINS_IN]->(course)
     * DELETE r1, r2
     *
     * MERGE (newGroup:Group {name: '${requestModel.groupName}'})
     * MERGE (newCourse:Course {name: '${requestModel.courseName}'})
     *
     * MERGE (student)-[:CONTAINS_IN]->(newGroup)-[:CONTAINS_IN]->(newCourse)
     */

    suspend fun removeConnection(call: ApplicationCall) {
        val studentNum = call.receive<String>()
        val result = runCatching {
            neoDriver.session().use { session ->
                session.executeWrite { tx ->
                    val query = Query(
                        """
                            MATCH (student:Student {name: '$studentNum'})
                            DETACH DELETE student
                        """.trimIndent()
                    )
                    val result = tx.run(query)
                }
            }
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }
}

