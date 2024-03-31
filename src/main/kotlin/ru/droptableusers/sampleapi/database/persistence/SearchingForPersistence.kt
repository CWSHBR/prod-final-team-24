package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.SearchingForModel
import ru.droptableusers.sampleapi.database.schema.SearchingForTable
import ru.droptableusers.sampleapi.database.schema.SearchingForTagsTable

class SearchingForPersistence {
    private fun resultRowToSearchingFor(resultRow: ResultRow) =
        SearchingForModel(
            id = resultRow[SearchingForTable.id].value,
            teamId = resultRow[SearchingForTable.teamId].value
        )

    fun selectByTeamId(teamId: Int): SearchingForModel? {
        return try {
            transaction {
                SearchingForTable.selectAll()
                    .where { SearchingForTable.teamId.eq(teamId) }
                    .single()
                    .let(::resultRowToSearchingFor)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun select(id: Int): SearchingForModel? {
        return try {
            transaction {
                SearchingForTable.selectAll()
                    .where { SearchingForTable.id.eq(id) }
                    .single()
                    .let(::resultRowToSearchingFor)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun insert(teamId: Int) {
        try {
            transaction {
                SearchingForTable.insert {
                    it[SearchingForTable.teamId] = teamId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteById(id: Int): Boolean {
        return try {
            transaction {
                SearchingForTable.deleteWhere { SearchingForTable.id.eq(id) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun deleteByTeamId(teamId: Int): Boolean {
        return try {
            transaction {
                SearchingForTable.deleteWhere { SearchingForTable.teamId.eq(id) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun addTagSearchingFor(tagId: Int, searchingForId: Int) {
        try {
            transaction {
                SearchingForTagsTable.insert {
                    it[SearchingForTagsTable.searchingForId] = searchingForId
                    it[SearchingForTagsTable.tagId] = tagId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectTagIds(searchingForId: Int): List<Int> {
        return try {
            transaction {
                SearchingForTagsTable.selectAll()
                    .where { SearchingForTagsTable.searchingForId.eq(searchingForId) }
                    .map { it[SearchingForTagsTable.searchingForId].value }
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun deleteTags(searchingForId: Int): Boolean {
        return try {
            transaction {
                SearchingForTagsTable.deleteWhere {
                    SearchingForTagsTable.searchingForId.eq(searchingForId)
                } > 0
            }
        } catch (e: Exception) {
            false
        }
    }
}