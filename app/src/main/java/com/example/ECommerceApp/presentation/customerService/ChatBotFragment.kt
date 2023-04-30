package com.example.ECommerceApp.presentation.customerService

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ECommerceApp.R
import com.example.ECommerceApp.databinding.FragmentChatBotBinding
import com.example.ECommerceApp.common.util.Resource
import com.example.ECommerceApp.common.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatBotFragment : Fragment(R.layout.fragment_chat_bot) {

    private val messageList = mutableListOf<Message>()
    val viewModel : ChatBotViewModel by viewModels()
    private lateinit var adapter: ChatAdapter
    private val binding: FragmentChatBotBinding by viewBinding(FragmentChatBotBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatAdapter(messageList)
        setupRecyclerView()
        addToChat("hello sir Customer Service with you ,  how can i help you !!" ,
            Message.SENT_BY_BOT)
        with(binding){
            with(viewModel){
                chatSendBtn.setOnClickListener {
                    val msg = Message(binding.chatET.text.toString(), Message.SENT_BY_USER)
                    addToChat(msg.message, msg.sentBy)
                    askChatBot("174851" , "NVnzTLjXTww5fNM0" , "1" , binding.chatET.text.toString())
                    chatET.text?.clear()
                }
                chatBotAnswer.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Success->{
                            CoroutineScope(Dispatchers.Main).launch {
                                onlineText.text = "Typing..."
                               Log.d("length" , it.data!!.cnt.length.toString() )
                                val dly = it.data.cnt.length*21
                                delay(dly.toLong())

                            onlineText.text = "online"
                            }
                            addToChat(it.data!!.cnt , Message.SENT_BY_BOT)
                        }
                        is Resource.Error->{
                            onlineText.text = "online"
                            addToChat("not connected please try again later " , Message.SENT_BY_BOT)
                        }
                        is Resource.Loading ->{
                                onlineText.text = "Typing..."

                        }

                    }
                })
            }
        }

    }

    private fun addToChat(message: String, sentBy: String) {
        requireActivity().runOnUiThread {
            messageList.add(Message(message, sentBy))
            // to auto scroll for last message added
            binding.recyclerView.smoothScrollToPosition(adapter.itemCount)
            adapter.notifyDataSetChanged()

        }
    }


    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        val layoutManger: LinearLayoutManager = LinearLayoutManager(requireContext())
        layoutManger.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManger
    }

    fun animateText(textView: TextView, text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            for (i in text.indices) {
                textView.text = text.substring(0, i + 1)
                delay(100) // delay between each character
            }
        }
    }


}