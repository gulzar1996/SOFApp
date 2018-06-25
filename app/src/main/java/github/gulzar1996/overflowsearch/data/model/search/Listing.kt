package github.gulzar1996.overflowsearch.data.model.search

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import github.gulzar1996.overflowsearch.NetworkState

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class Listing<T>(
        val pagedList: LiveData<PagedList<T>>,
        val networkState: LiveData<NetworkState>,
        val refreshState: LiveData<NetworkState>,
        val refresh: () -> Unit,
        val retry: () -> Unit)