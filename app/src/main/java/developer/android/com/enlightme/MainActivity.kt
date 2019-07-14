package developer.android.com.enlightme

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import developer.android.com.enlightme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener,
    Create1Fragment.OnFragmentInteractionListener,
    Create2Fragment.OnFragmentInteractionListener,
    DebateFragment.OnFragmentInteractionListener,
    ArgumentPlusSide1Fragment.OnFragmentInteractionListener,
    ArgumentPlusSide2Fragment.OnFragmentInteractionListener,
    ArgumentSide1Fragment.OnFragmentInteractionListener,
    ArgumentSide2Fragment.OnFragmentInteractionListener,
    NewArgDialogFragment.OnFragmentInteractionListener{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
    override fun onFragmentInteraction(uri: Uri) {
    }
}
