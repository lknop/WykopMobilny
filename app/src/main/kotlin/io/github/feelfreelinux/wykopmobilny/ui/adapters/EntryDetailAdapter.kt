package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.CommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class EntryDetailAdapter @Inject constructor(val userManagerApi: UserManagerApi,
                                             val settingsPreferencesApi: SettingsPreferencesApi,
                                             val entryPresenterFactory: EntryPresenterFactory,
                                             val commentPresenterFactory: CommentPresenterFactory) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val ENTRY_HOLDER = 0
        private const val COMMENT_HOLDER = 1
    }
    lateinit var addReceiverListener: (Author) -> Unit
    var entry: Entry? = null
    var commentId : Int? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder?.itemViewType == ENTRY_HOLDER) {
            (holder as EntryViewHolder).bindView(entry!!, false)
        } else {
            val comment = entry!!.comments[position - 1]
            val entryAuthor = entry?.author
            val commentAuthor = comment.author
            (holder as CommentViewHolder).bindView(comment, entryAuthor == commentAuthor, commentId)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ENTRY_HOLDER
        else COMMENT_HOLDER
    }

    override fun getItemCount(): Int {
        entry?.comments?.let {
            return it.size + 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ENTRY_HOLDER -> EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false), userManagerApi, settingsPreferencesApi, entryPresenterFactory.create())
            else -> CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false), addReceiverListener, settingsPreferencesApi, userManagerApi, commentPresenterFactory.create())
        }
    }
}
