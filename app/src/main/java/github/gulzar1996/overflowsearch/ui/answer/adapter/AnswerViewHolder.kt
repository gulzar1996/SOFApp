package github.gulzar1996.overflowsearch.ui.answer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.gulzar1996.overflowsearch.R
import github.gulzar1996.overflowsearch.data.model.answer.Answer
import kotlinx.android.synthetic.main.item_answer.view.*

class AnswerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindTo(answer: Answer?) {
        itemView.titleq.text = answer?.body
        itemView.score.text = answer?.score.toString()
        itemView.author.text = answer?.authorName
        itemView.tag_divider.visibility = View.GONE

    }

    companion object {
        fun create(parent: ViewGroup): AnswerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_answer, parent, false)
            return AnswerViewHolder(view)
        }
    }
}