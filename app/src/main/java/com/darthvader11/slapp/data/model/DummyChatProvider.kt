package com.darthvader11.slapp.data.model

import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class DummyChatProvider {

    companion object {

        fun dummyMessageList() : List<UserMessage> {

            var userMessageList = ArrayList<UserMessage>()
            val selfProfile = DummmySelfProfile.dummySelfUser()

            userMessageList.add(
                UserMessage(
                message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
            ))

            userMessageList.add(
                UserMessage(
                    message = "Proin nibh nisl condimentum id venenatis",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Nibh ipsum consequat nisl vel pretium lectus. Amet purus gravida quis blandit turpis. Aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod. ",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Diam in arcu cursus euismod quis viverra nibh. Interdum varius sit amet mattis.",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Nec dui nunc mattis enim ut tellus elementum sagittis vitae.",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "In ante metus dictum at tempor commodo ullamcorper a. Consequat nisl vel pretium lectus quam id leo in. Sit amet cursus sit amet dictum sit amet justo donec.",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Leo in vitae turpis massa sed elementum tempus egestas. A pellentesque sit amet porttitor eget dolor. Pulvinar sapien et ligula ullamcorper. ",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Suspendisse ultrices gravida dictum fusce ut placerat.",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Proin nibh nisl condimentum id venenatis",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Nibh ipsum consequat nisl vel pretium lectus. Amet purus gravida quis blandit turpis. Aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod. ",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Diam in arcu cursus euismod quis viverra nibh. Interdum varius sit amet mattis.",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Nec dui nunc mattis enim ut tellus elementum sagittis vitae.",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "In ante metus dictum at tempor commodo ullamcorper a. Consequat nisl vel pretium lectus quam id leo in. Sit amet cursus sit amet dictum sit amet justo donec.",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Leo in vitae turpis massa sed elementum tempus egestas. A pellentesque sit amet porttitor eget dolor. Pulvinar sapien et ligula ullamcorper. ",
                    sender = User(name = "Metallica", id = UUID.randomUUID(), profilePic = null),
                    createdAt = LocalDateTime.now()
                ))

            userMessageList.add(
                UserMessage(
                    message = "Suspendisse ultrices gravida dictum fusce ut placerat.",
                    sender = selfProfile,
                    createdAt = LocalDateTime.now()
                ))

            return userMessageList
        }

    }

}

class DummmySelfProfile {

    companion object {

        fun dummySelfUser(): User {
            val user = User(name ="bugra", profilePic = null, id = UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c"))

            return user
        }


    }
}