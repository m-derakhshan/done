package m.derakhshan.done

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import m.derakhshan.done.databinding.ActivityMainBinding
import m.derakhshan.done.main.MainFragment.Companion.isAddTaskOpen
import m.derakhshan.done.main.MainFragment.Companion.setAddTaskVisibility
import m.derakhshan.done.main.MainFragment.Companion.todayTaskCount

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.bottomMenu.selectedItemId = R.id.home
        if (savedInstanceState == null)
            setNavigation()

        todayTaskCount.observe(this, {
            it?.let { count ->
                if (count > 0)
                    binding.bottomMenu.getOrCreateBadge(R.id.home).number = count
                else
                    binding.bottomMenu.removeBadge(R.id.home)
            }
        })

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setNavigation()
    }


    private fun setNavigation() {
        val graphID = listOf(
            R.navigation.home,
            R.navigation.tasks
        )
        binding.bottomMenu.setupWithNavController(
            navGraphIds = graphID,
            fragmentManager = supportFragmentManager,
            containerId = binding.container.id, intent = intent
        )
    }


    override fun onBackPressed() {
        if (isAddTaskOpen) {
            setAddTaskVisibility.value = false
            isAddTaskOpen = false
        } else
            super.onBackPressed()
    }

}