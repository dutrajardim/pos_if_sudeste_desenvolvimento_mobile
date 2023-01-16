package com.example.ifbot

data class MessageData(
    val from: String,
    val chat: String,
    val to: String,
    val type: String,
    val createdAt: Long,
    val text: String
)