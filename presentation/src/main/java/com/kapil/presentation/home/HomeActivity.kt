package com.kapil.presentation.home

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.kapil.presentation.R
import com.kapil.presentation.events.EventsFragment
import com.kapil.presentation.jobs.JobsFragment
import com.kapil.presentation.offers.OffersFragment
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onResume() {
        super.onResume()
        handleNavigation(navView.selectedItemId)
        navView.setOnNavigationItemSelectedListener { handleNavigation(it.itemId) }
    }

    override fun onPause() {
        super.onPause()
        navView.setOnNavigationItemSelectedListener(null)
    }

    private fun handleNavigation(itemId: Int) = when (itemId) {
        R.id.navigation_my_jobs -> {
            createOrShowFragment { JobsFragment() }.also {
                supportActionBar?.title = getString(it)
            }
            true
        }
        R.id.navigation_my_events -> {
            createOrShowFragment { EventsFragment() }.also {
                supportActionBar?.title = getString(it)
            }
            true
        }
        R.id.navigation_my_offers -> {
            createOrShowFragment { OffersFragment() }.also {
                supportActionBar?.title = getString(it)
            }
            true
        }
        else -> false
    }

    /**
     * Creates a fragment of type T which extends from BottomNavFragment, only if it is not
     * already created.
     *
     * @return Title resource id of the fragment.
     */
    @StringRes
    private inline fun <reified T : BottomNavFragment> createOrShowFragment(factory: () -> T): Int {
        val mFragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = mFragmentManager.beginTransaction()

        mFragmentManager.primaryNavigationFragment?.let { fragmentTransaction.hide(it) }

        val fragment = mFragmentManager.findFragmentByTag(T::class.simpleName)?.also {
            fragmentTransaction.show(it)
        } ?: factory().also {
            fragmentTransaction.add(R.id.fragment_container_view, it, T::class.simpleName)
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commit()

        return (fragment as BottomNavFragment).getTitle()
    }
}
