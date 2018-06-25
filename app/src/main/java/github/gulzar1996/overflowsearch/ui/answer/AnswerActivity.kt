package github.gulzar1996.overflowsearch.ui.answer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import dagger.android.AndroidInjection
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.NetworkState
import github.gulzar1996.overflowsearch.R
import github.gulzar1996.overflowsearch.data.local.answer.IAnswerRepository
import github.gulzar1996.overflowsearch.ui.answer.adapter.AnswerAdapter
import github.gulzar1996.overflowsearch.ui.answer.viewmodel.AnswerViewModel
import github.gulzar1996.overflowsearch.ui.answer.viewmodel.AnswerViewModelFactory
import kotlinx.android.synthetic.main.activity_answer.*
import kotlinx.android.synthetic.main.item_answer.*
import kotlinx.android.synthetic.main.item_answer.view.*
import javax.inject.Inject

class AnswerActivity : AppCompatActivity() {

    lateinit var viewModel: AnswerViewModel

    @Inject
    lateinit var answerRepository: IAnswerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)
        viewModel = getVM()
        initAdapter()
        initSwipeToRefresh()
        initUI()

    }

    private fun initUI() {
        val questionId = intent.extras.get(Const.QUESTION_ID)
        viewModel.showAnswers(questionId = questionId as String)
        viewModel.showQuestion(questionId = questionId as String)

        viewModel.question.observe(this, Observer {
            Log.d("AnswerActivity", it.toString())
            titleq.text = it?.title
            score.visibility = View.GONE
            tag.visibility = View.GONE
            author.text = it?.authorName
            comments.visibility = View.GONE
            source.text = it?.link.toString()
            timestamp.visibility = View.GONE
            tag_divider.visibility = View.GONE
        })

    }


    private fun initAdapter() {
        val adapterx = AnswerAdapter {
            viewModel.retry()
        }

        answerList.apply {
            adapter = adapterx
            layoutManager = LinearLayoutManager(this@AnswerActivity)
        }

        viewModel.answers.observe(this, Observer {
            adapterx.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapterx.setNetworkState(it)

            Log.d("Answer Activity", it.toString())
        })

    }

    private fun initSwipeToRefresh() {
        viewModel.refreshState.observe(this, Observer {
            swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }


    private fun getVM() =
            ViewModelProviders.of(this, AnswerViewModelFactory(answerRepository))
                    .get(AnswerViewModel::class.java)


}