package developer.android.com.enlightme

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import developer.android.com.enlightme.databinding.ActivityMainBinding
import developer.android.com.enlightme.databinding.FragmentDebateBinding
import developer.android.com.enlightme.objects.DebateEntity

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener,
    Create1Fragment.OnFragmentInteractionListener,
    Create2Fragment.OnFragmentInteractionListener,
    DebateFragment.OnFragmentInteractionListener,
    ArgumentPlusSide1Fragment.OnFragmentInteractionListener,
    ArgumentPlusSide2Fragment.OnFragmentInteractionListener,
    ArgumentSide1Fragment.OnFragmentInteractionListener,
    ArgumentSide2Fragment.OnFragmentInteractionListener,
    NewArgDialogFragment.OnFragmentInteractionListener,
    NewArgDialogFragment.NoticeDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: DebateViewModel
    lateinit var debateFragment: DebateFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        viewModel = this.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
    override fun onFragmentInteraction(uri: Uri) {
    }
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        debateFragment.addArgument(viewModel.temp_side, viewModel.temp_debate_entity)
        viewModel.temp_side = 0
        viewModel.temp_debate_entity = DebateEntity()
        // User touched the dialog's positive button
        //this.addArgument(viewModel.temp_side, viewModel.temp_debate_entity)


    }
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // User touched the dialog's negative button
    }

}
