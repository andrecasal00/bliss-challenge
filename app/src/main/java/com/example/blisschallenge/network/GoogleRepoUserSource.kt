package com.example.blisschallenge.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.example.blisschallenge.data.GoogleRepo
import com.example.blisschallenge.data.Items
import io.ktor.utils.io.errors.IOException

class GoogleRepoUserSource: PagingSource<Int, Items>() {
    override fun getRefreshKey(state: PagingState<Int, Items>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Items> {
        val repository = HttpRequest()
        return try {
            // Determine the next page to load (default to 1 if no key is provided)
            val nextPage = params.key ?: 1

            // Fetch data from the API
            val repos = repository.getGoogleRepos(nextPage)

            // Return the page of data
            LoadResult.Page(
                data = repos.items,
                prevKey = if (nextPage == 1) null else nextPage - 1, // No previous page if we're on the first page
                nextKey = if (repos.items.isEmpty()) null else nextPage + 1 // No next page if no more items
            )
        } catch (exception: IOException) {
            // Handle network errors
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // Handle HTTP errors
            return LoadResult.Error(exception)
        }
    }

}