package com.example.ECommerceApp.presentation.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ECommerceApp.R
import com.example.ECommerceApp.data.model.Product

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    inner class FoodViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img : ImageView = itemView.findViewById(R.id.item_image)
        val priceET : TextView = itemView.findViewById(R.id.item_price)
        val ratingET : TextView = itemView.findViewById(R.id.item_rating)
        val itemNumET : TextView = itemView.findViewById(R.id.item_number)
        val title : TextView = itemView.findViewById(R.id.title)
        val desc : TextView = itemView.findViewById(R.id.desc)

    }

    // compare two items
     val differCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product,
        ): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product,
        ): Boolean {
            return oldItem == newItem
        }
    }

        //compare two lists
    val differ = AsyncListDiffer(this , differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : FoodViewHolder {
        return FoodViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.food_item ,
            parent ,
            false
        )
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        val item = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(item.thumbnail).into(holder.img)
            holder.priceET.text = item.price.toString()
            holder.ratingET.text = item.rating.toString()
            holder.itemNumET.text = item.stock.toString()
            holder.title.text = item.title
            holder.desc.text = item.description
            setOnClickListener{
                onItemClickListener?.let {
                    it(item)
                }
            }

        }
    }

    override fun getItemCount(): Int {
      return differ.currentList.size
    }

    //to manage item click
    private var onItemClickListener:((Product)->Unit)? = null

    fun OnItemClickListener(listener: (Product)->Unit){
        onItemClickListener = listener
    }

}