package com.darthvader11.slapp.server

import com.darthvader11.slapp.Objects.User

interface GetUserCallback {

    fun done(returnedUser: User?)


}