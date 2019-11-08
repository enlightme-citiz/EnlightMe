package developer.android.com.enlightme

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import developer.android.com.enlightme.Debate.*
import developer.android.com.enlightme.Debate.ConcurentOp.InsertArg
import developer.android.com.enlightme.databinding.ActivityMainBinding
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
    JoinDebateFragment.OnFragmentInteractionListener,
    ItemBtListFragment.OnFragmentInteractionListener,
    ProvideUserNameFragment.OnFragmentInteractionListener,
    NewArgDialogFragment.NoticeDialogListener,
    ProvideUserNameFragment.NoticeDialogListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var debateViewModel: DebateViewModel
    private lateinit var joinDebateViewModel: JoinDebateViewModel
    lateinit var debateFragment: DebateFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        debateViewModel = this.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        }
        joinDebateViewModel = this.run {
            ViewModelProviders.of(this).get(JoinDebateViewModel::class.java)
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
        if(debateViewModel.edit_arg_pos >= 0){
            debateFragment.modArgument(debateViewModel.temp_side,
                debateViewModel.temp_debate_entity, debateViewModel.edit_arg_pos)
        }else{
            val place : Int
            if(debateViewModel.temp_side == 1){
                place = debateViewModel.debate.value?.get_debate_entity()?.side_1_entity?.size ?: -1
            }else{
                place = debateViewModel.debate.value?.get_debate_entity()?.side_2_entity?.size ?: -1
            }
            val currDebate = debateViewModel.debate.value?.get_debate_entity()
            if (currDebate != null){
                val operation = InsertArg(currDebate, debateViewModel.temp_debate_entity, place, debateViewModel.temp_side)
                currDebate.manageUserUpdate(listOf(operation), this,
                    joinDebateViewModel.listEndpointId, joinDebateViewModel.myEndpointId)
            }

        }
        debateViewModel.temp_side = 0
        debateViewModel.temp_debate_entity = DebateEntity()
    }
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // User touched the dialog's negative button
    }

}
