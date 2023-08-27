package com.example.messutilities.demopaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.messutilities.network.FakeApi
import retrofit2.HttpException
import java.io.IOException

class DemoPagingSource(
    private val apiService : FakeApi
    ) : PagingSource<Int,Any>(){

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        // method to implement the load results that returns a load result
        val position: Int = params.key ?: STARTING_INDEX

        return try {
            val demoResponse = apiService.getPics(LIMIT, STARTING_INDEX)
            LoadResult.Page(
                data = demoResponse,
                prevKey = if(position == STARTING_INDEX) null else position-1,
                nextKey = if(demoResponse.isEmpty()) null else position+1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }


    }
    companion object {
        const val LIMIT = 10
        const val STARTING_INDEX = 1
    }
}