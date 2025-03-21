package com.example.eveerdnieva.data.repository

import com.example.eveerdnieva.data.Result
import com.example.eveerdnieva.data.model.Article
import kotlinx.coroutines.flow.Flow


interface Repository {
    suspend fun getNewList(category:String):Flow<Result<List<Article>>>

}