package com.citrus.cookiteasy.data.database

import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val userDao: UserDao, private val trainingDao: TrainingDao){

    suspend fun getUserByUsername(username: String) = userDao.getUserByUserName(username)

    suspend fun insertUser(user:User) = userDao.insertUser(user)

    suspend fun deleteUser(user:User) = userDao.deleteUser(user.username)

    suspend fun getAllUsers() = userDao.getAllUsers()

    suspend fun updateUser(user: User) = userDao.updateUser(user)


}