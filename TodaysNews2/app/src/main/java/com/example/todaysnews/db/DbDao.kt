package com.example.todaysnews.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todaysnews.model.Post

@Dao
interface DbDao {
    @Insert
    suspend fun addPosts(post: List<Post>)
    @Query("SELECT * FROM posts")
    suspend fun getPosts(post: List<Post>)
}