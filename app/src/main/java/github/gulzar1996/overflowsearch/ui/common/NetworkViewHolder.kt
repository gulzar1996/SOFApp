package github.gulzar1996.overflowsearch.ui.common

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.gulzar1996.overflowsearch.NetworkState
import github.gulzar1996.overflowsearch.R
import github.gulzar1996.overflowsearch.R.id.*
import github.gulzar1996.overflowsearch.Status
import kotlinx.android.synthetic.main.item_networkstate.view.*

class NetworkViewHolder(view: View,
                        private val retryCallback: () -> Unit) : RecyclerView.ViewHolder(view) {

    init {
        itemView.retryLoadingButton.setOnClickListener { retryCallback() }
    }


    fun bindTo(networkState: NetworkState?) {

        Log.d("NetworkViewholder", networkState.toString())
        itemView.loadingProgressBar.visibility = toVisbility(networkState?.status == Status.RUNNING)
        itemView.retryLoadingButton.visibility = toVisbility(networkState?.status == Status.FAILED)
        itemView.errorMessageTextView.visibility = toVisbility(networkState?.msg != null)
        itemView.errorMessageTextView.text = networkState?.msg
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_networkstate, parent, false)
            return NetworkViewHolder(view, retryCallback)
        }
    }

    fun toVisbility(constraint : Boolean): Int {
        return if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}

