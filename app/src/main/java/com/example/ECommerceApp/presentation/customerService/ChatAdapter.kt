package com.example.ECommerceApp.presentation.customerService

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ECommerceApp.R
import com.example.ECommerceApp.common.util.gone
import com.example.ECommerceApp.common.util.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatAdapter(messageList: List<Message>) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    var messageList : List<Message>

    init {
        this.messageList = messageList
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var LeftChatView : LinearLayout
        var RightChatView : LinearLayout
        var RightTextView : TextView
        var LeftTextView : TextView

        init {
            LeftChatView = itemView.findViewById(R.id.left_chat_view)
            RightChatView = itemView.findViewById(R.id.right_chat_view)
            RightTextView = itemView.findViewById(R.id.right_chat_text_view)
            LeftTextView = itemView.findViewById(R.id.left_chat_text_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val chatView: View = LayoutInflater.from(parent.context).inflate(R.layout.message_item, null)
        return MyViewHolder(chatView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMsg = messageList[position].message
        if (messageList[position].sentBy == Message.SENT_BY_USER){

            holder.LeftChatView.gone()
            holder.RightChatView.visible()
            holder.RightTextView.text = currentMsg

        }else {

            holder.LeftChatView.visible()
            holder.RightChatView.gone()
            holder.LeftTextView.text = currentMsg
            animateNewMessage(holder.LeftTextView , currentMsg , position)

        }
    }

    override fun getItemCount(): Int {
       return messageList.size
    }
    private var lastAnimatedIndex = -1

    private fun animateNewMessage(textView: TextView, message: String, index: Int) {
        if (index <= lastAnimatedIndex) {
            // If the message has already been animated, don't animate it again
            textView.text = message
            return
        }
        lastAnimatedIndex = index
        CoroutineScope(Dispatchers.Main).launch {
            textView.text = ""
            textView.visible()
            for (char in message) {
                textView.append(char.toString())
                delay(20L)
            }
        }
    }
}