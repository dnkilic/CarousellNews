package com.dnkilic.carousellnews.repository.model

sealed class Command {
    object Loading : Command()
    class Error(val code: Int) : Command()
    class Success(val news: List<News>) : Command()
}