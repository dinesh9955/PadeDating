package com.padedatingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.padedatingapp.R
import com.padedatingapp.model.Doc
import com.padedatingapp.ui.main.fragments.BuyGiftCardsListFragment
import com.padedatingapp.ui.main.fragments.BuyGiftCardsListFragmentDirections
import java.util.*

class GiftCardListAdapter(internal var context: Context, internal var list: List<Doc>, val listener: BuyGiftCardsListFragment) :
        RecyclerView.Adapter<GiftCardListAdapter.BookMarkListViewHolder>() {

    var TAG = "GiftCardListAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_premium_packst, parent, false)
        return BookMarkListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list?.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookMarkListViewHolder, position: Int) {
        //  Glide.with(context as BaseActivity).load(list!![position]?.subTypeImage)
        //  .apply(RequestOptions().placeholder(R.mipmap.ic_placehoder)).into(holder.ivBookmarkWorkoutImage)
        holder.rootLayout1.background = getDrawable(context, position)!!
        holder.cardNameTextView.text = list[position].cardName
        holder.costTextView.text = "$"+list[position].cost

        var months : String = ""
        if(!list[position].validTill.equals("")){

            var calendar2 = Calendar.getInstance()
            calendar2.set(list[position].validTill.split("-")[0].toInt(), list[position].validTill.split("-")[1].toInt(), list[position].validTill.split("-")[2].toInt())

            var calendar = Calendar.getInstance()
          //  val thisYear = calendar[Calendar.YEAR]
            val startDate = Date(calendar[Calendar.YEAR], calendar[Calendar.MONTH]+1, calendar[Calendar.DAY_OF_WEEK])
            val endDate = Date(calendar2[Calendar.YEAR], calendar2[Calendar.MONTH], calendar2[Calendar.DAY_OF_WEEK])
            val difInDays = ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt()


            if(difInDays >= 31 ){
                months = ""+difInDays / 30 +"Months"
                if(months.contains("-")){
                    months = "Expired"
                }
            }else{
                months = ""+difInDays+"Days"
                if(months.contains("-")){
                    months = "Expired"
                }
            }



            Log.e(TAG, "daysDiff "+months)

        }

        holder.validTillTextView.text = ""+months



        holder.rootLayout1.setOnClickListener(View.OnClickListener {
            listener.onItemClick(list[position])
          //  ((BuyGiftCardsListFragment).context.cofindNavController().navigate(BuyGiftCardsListFragmentDirections.actionToBuy())
        })

    }


    class BookMarkListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var rootLayout1 : ConstraintLayout
         var cardNameTextView: TextView
         var costTextView: TextView
         var validTillTextView: TextView

         init {
             rootLayout1 = itemView.findViewById(R.id.clItemPacks)
             cardNameTextView = itemView.findViewById(R.id.tvPackName)
             costTextView = itemView.findViewById(R.id.tvPackPrice)
             validTillTextView = itemView.findViewById(R.id.tvPackValidity)
         }
    }


    fun getDrawable(context: Context, pos: Int): Drawable? =
            when (pos % 5) {
                0 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_saphire)
                1 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_golden)
                2 -> ContextCompat.getDrawable(context, R.drawable.pattern_bg_purple)
                3 ->   ContextCompat.getDrawable(context, R.drawable.pattern_bg_blue)
                else ->  ContextCompat.getDrawable(context, R.drawable.pattern_bg_green)
            }


    interface OnItemClickListener {
        fun onItemClick(model: Doc)
    }


    fun updateData(arrayList2: ArrayList<Doc>) {
        list = arrayList2
        notifyDataSetChanged()
    }


}