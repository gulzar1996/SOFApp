package github.gulzar1996.overflowsearch.ui.question

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import dagger.android.AndroidInjection
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.NetworkState
import github.gulzar1996.overflowsearch.R
import github.gulzar1996.overflowsearch.data.local.question.IQuestionSearchRepository
import github.gulzar1996.overflowsearch.ui.answer.AnswerActivity
import github.gulzar1996.overflowsearch.ui.question.adapter.QuestionSearchAdapter
import github.gulzar1996.overflowsearch.ui.question.viewmodel.QuestionSearchViewModel
import github.gulzar1996.overflowsearch.ui.question.viewmodel.QuestionSearchViewModelFactory
import kotlinx.android.synthetic.main.activity_question_search.*
import javax.inject.Inject


class QuestionSearchActivity : AppCompatActivity() {

    lateinit var viewModel: QuestionSearchViewModel

    @Inject
    lateinit var questionSearchRepository: IQuestionSearchRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_search)
        viewModel = getVM()
        initAdapter()
        initInput()
        initSwipeToRefresh()
        initNavigation()

    }

    private fun initNavigation() {
        viewModel.navigateTrigger.observe(this, Observer {
            val i = Intent(this@QuestionSearchActivity, AnswerActivity::class.java)
            i.putExtra(Const.QUESTION_ID, it)
            startActivity(i)
        })
    }

    private fun initInput() {
        search.setOnClickListener {
            if (!queryInput.text.isEmpty())

                if (viewModel.showQuestions(questionQuery = queryInput.text.toString().trim())) {
                    questionList.smoothScrollToPosition(0)
                    (questionList.adapter as? QuestionSearchAdapter)?.submitList(null)
                }

        }
    }

    private fun initAdapter() {
        val adapterx = QuestionSearchAdapter({
            viewModel.retry()
        }, { it ->
            viewModel.itemClick(it)
        })

        questionList.apply {
            adapter = adapterx
            layoutManager = LinearLayoutManager(this@QuestionSearchActivity)
        }

        viewModel.questions.observe(this, Observer {
            adapterx.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapterx.setNetworkState(it)

            Log.d("QuestionSearch", it.toString())
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
            ViewModelProviders.of(this, QuestionSearchViewModelFactory(questionSearchRepository))
                    .get(QuestionSearchViewModel::class.java)


}