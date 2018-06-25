package github.gulzar1996.overflowsearch.ui.question.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.gulzar1996.overflowsearch.R
import github.gulzar1996.overflowsearch.data.model.search.Question
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionViewHolder(view: View, val itemClick: (String) -> Unit) : RecyclerView.ViewHolder(view) {


    fun bindTo(question: Question?) {
        itemView.titleq.text = question?.title
        itemView.score.text = question?.score.toString()
        itemView.author.text = question?.authorName
        itemView.comments.text = question?.answerCount.toString()
        itemView.source.text = question?.link.toString()
        itemView.timestamp.text = question?.creationDate.toString()

        itemView.setOnClickListener {
            itemClick(question?.questionId.toString())
        }

    }

    companion object {
        fun create(parent: ViewGroup, itemClick: (String) -> Unit): QuestionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_question, parent, false)
            return QuestionViewHolder(view, itemClick)
        }
    }
}