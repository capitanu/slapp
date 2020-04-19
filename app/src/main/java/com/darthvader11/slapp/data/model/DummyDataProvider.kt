package com.example.messagebox.Model

class DummyDataProvider {

    companion object {

        fun dummyMessagePreview(): List<MessagePreview> {

            val list = ArrayList<MessagePreview>()
            list.add(
                MessagePreview(null, "Metallica", "We are the best")
            )
            list.add(
                MessagePreview(null, "Pink Floyd", "No we are the best")
            )
            list.add(
                MessagePreview(null, "Death Blade", "very much no we are the best and there no way in the whole world to change this so therefore stop calling us")
            )
            list.add(
                MessagePreview(null, "Iron Maiden", "very much no we are the best")
            )
            list.add(
                MessagePreview(null, "Queen", "Hello form the other side")
            )
            list.add(
                MessagePreview(null, "Guns and Roses", "Axl is the best okay")
            )
            list.add(
                MessagePreview(null, "Panther Sabbath", "We conquer")
            )
            return list
        }
    }
}