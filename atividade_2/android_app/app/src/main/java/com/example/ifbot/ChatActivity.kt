package com.example.ifbot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify

class ChatActivity : AppCompatActivity() {

    private lateinit var messageListAdapter: MessageListAdapter
    private val messageList = mutableSetOf<MessageData>()

    private lateinit var backButtom: ImageButton
    private lateinit var refreshButton: ImageButton
    private lateinit var sendMessageButton: ImageButton
    private lateinit var tagTextView: TextView
    private lateinit var messageEditText: EditText
    private lateinit var messageListRecyclerView: RecyclerView

    lateinit var tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        backButtom = findViewById(R.id.btn_back)
        refreshButton = findViewById(R.id.btn_refresh)
        sendMessageButton = findViewById(R.id.btn_send_message)
        messageEditText = findViewById(R.id.et_message)
        tagTextView = findViewById(R.id.tv_tag)

        backButtom.setOnClickListener { finish() }
        refreshButton.setOnClickListener { updateMessageList() }
        sendMessageButton.setOnClickListener { sendMessage() }

        tag = intent.getStringExtra("tag")!!
        messageListAdapter = MessageListAdapter(messageList, tag)
        messageListRecyclerView = findViewById(R.id.rv_message_list)
        messageListRecyclerView.adapter = messageListAdapter
        messageListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        tagTextView.text = tag
        updateMessageList()
    }

    private fun sendMessage() {
        val message: String = messageEditText.text.toString()
        val options = RestOptions.builder()
            .addPath("/bots/$tag/messages")
            .addBody("{ \"question\": \"$message\"}".toByteArray())
            .build()

        Amplify.API.post(
            options,
            {
                updateMessageList()
                this.runOnUiThread { messageEditText.text.clear() }
            },
            { Log.e("Api.message", "POST failed", it) }
        )
    }

    private fun updateMessageList() {
        val options = RestOptions.builder().addPath("/bots/$tag/messages").build()
        Amplify.API.get(options,
            {
                try {
                    val data = it.data.asJSONObject().getJSONArray("data")
                    for (i in 1..data.length()) {
                        val messageJson = data.getJSONObject(i - 1)
                        val message = MessageData(
                            from=messageJson.getString("From"),
                            chat=messageJson.getString("Chat"),
                            to=messageJson.getString("To"),
                            type=messageJson.getString("Type"),
                            createdAt=messageJson.getLong("CreatedAt"),
                            text=messageJson.getString("Text")
                        )
                        messageList.add(message)
                    }
                    this.runOnUiThread {messageListAdapter.notifyDataSetChanged()}
                } catch (e: Exception) { }
            },
            { Log.e("Api.messages", "GET failed", it)}
        )
    }
}