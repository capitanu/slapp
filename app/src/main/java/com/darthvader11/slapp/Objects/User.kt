package com.darthvader11.slapp.Objects



class User(var username: String, var email: String, var password: String, var user_id: Int) {

    constructor(email: String,password: String) : this("", email, password, 0)
}

object user_id {
    var user_id: Int = 0
}




