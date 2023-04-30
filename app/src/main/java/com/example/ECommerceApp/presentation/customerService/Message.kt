package com.example.ECommerceApp.presentation.customerService

data class Message(
    val message: String,
    val sentBy: String,
) {
    companion object {
        const val SENT_BY_USER = "USER"
        const val SENT_BY_BOT = "BOT"
    }
}
