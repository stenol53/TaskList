package com.voak.android.tasklist.db.dao

import androidx.room.*
import com.voak.android.tasklist.db.entities.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE task_type = :type AND is_finished = 0")
    fun getActiveTasksByType(type: Int): Flowable<List<Task>>

    @Query("SELECT * FROM task WHERE task_type = :type AND is_finished = 1")
    fun getFinishedTasksByType(type: Int): Flowable<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTaskById(id: Int): Flowable<List<Task>>

    @Query("SELECT * FROM task WHERE is_finished = 0 AND need_notification = 1")
    fun getNeedNotificationActiveTasks(): Flowable<List<Task>>

    @Query("DELETE FROM task WHERE task_type = :type AND is_finished = 1")
    fun deleteAllFinishedTasksByType(type: Int): Completable

    @Update
    fun update(task: Task): Completable

    @Insert
    fun insert(task: Task): Single<Long>

    @Delete
    fun delete(task: Task): Completable
}