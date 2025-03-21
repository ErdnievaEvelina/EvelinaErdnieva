package com.example.eveerdnieva.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eveerdnieva.data.Result
import com.example.eveerdnieva.data.model.Article
import com.example.eveerdnieva.data.repository.Repository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepository:Repository
): ViewModel() {
    private val _news= MutableStateFlow<List<Article>>(emptyList())
    val news=_news.asStateFlow()
    private val _showError=Channel<Boolean>()
    val showError=_showError.receiveAsFlow()
    init {
        changeCategory()

    }
    fun changeCategory(category:String="GENERAL"){
        viewModelScope.launch {
            newsRepository.getNewList(category).collectLatest { result->
                when(result){
                    is Result.Error -> {
                        _showError.send(true)
                    }
                    is Result.Success -> {
                        result.data?.let { news->
                            _news.update { news }

                        }
                    }
                }

            }
        }
    }

}