package com.example.eveerdnieva.data.repository

import com.example.eveerdnieva.data.Result
import com.example.eveerdnieva.data.model.Api
import com.example.eveerdnieva.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class RepositoryImpl(
    private val api:Api
):Repository {
    override suspend fun getNewList(category: String): Flow<Result<List<Article>>> {
        return flow {
            val newsFromApi=try{
                api.getNewsList(category)
            }catch (e:IOException){
                e.printStackTrace()
                emit(Result.Error(message="Error loading"))
                return@flow
            }catch(e:HttpException){
                e.printStackTrace()
                emit(Result.Error(message="Error loading"))
                return@flow
            }catch(e:Exception){
                e.printStackTrace()
                emit(Result.Error(message="Error loading"))
                return@flow
            }
            emit(Result.Success(newsFromApi.articles))
        }

    }


}