package io.github.feelfreelinux.wykopmobilny.fragments
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData

class HotFeedFragment : FeedFragment() {
    var period = "24"
    val supportActionBar by lazy{ (activity as AppCompatActivity).supportActionBar }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun loadData(page : Int, resultCallback : (Any) -> Unit) {
        when(period) {
            "6", "12", "24" -> wam.getMikroblogHot(page, period, resultCallback)
            "newest" -> wam.getMikroblogIndex(page, resultCallback)
        }

    }

    companion object {
        fun newInstance(data : WykopApiData) : Fragment {
            val fragmentData = Bundle()
            val fragment = HotFeedFragment()
            fragmentData.putSerializable("wamData", data)
            fragment.arguments = fragmentData
            return fragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Create hot peroid menu
        inflater?.inflate(R.menu.hot_period, menu)
        val item = menu?.findItem(R.id.spinner)
        val spinner = item?.actionView as Spinner
        val adapter = ArrayAdapter<String>(
                supportActionBar?.themedContext,
                R.layout.actionbar_spinner,
                R.id.text1, resources.getStringArray(R.array.hotPeriodSpinner))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position) {
                    0 -> period = "24"
                    1 -> period = "12"
                    2 -> period = "6"
                    3 -> period = "newest"
                }
            }
        }
        spinner.adapter = adapter
        super.onCreateOptionsMenu(menu, inflater)
    }
}