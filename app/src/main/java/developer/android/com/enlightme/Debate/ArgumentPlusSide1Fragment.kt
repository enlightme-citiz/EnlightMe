package developer.android.com.enlightme.Debate

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import developer.android.com.enlightme.DebateViewModel
import developer.android.com.enlightme.NewArgDialogFragment
import developer.android.com.enlightme.R
import developer.android.com.enlightme.databinding.FragmentArgumentPlusSide1Binding



class ArgumentPlusSide1Fragment : Fragment(), View.OnClickListener{
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: DebateViewModel
    private lateinit var binding: FragmentArgumentPlusSide1Binding
    // TODO add the debateEntity object as a parameter to constructor. Stringify it if necessary

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_argument_plus_side1, container, false)
        //Attache click event listener to display the Add argument dialogue box
        binding.argumentPlus1.setOnClickListener(this)
        binding.argumentPlus1.background = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ripple_arg_plus_side_1)
        binding.ic_add_white_24dp.background = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ic_add_white_24dp)
        return binding.root
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ArgumentPlusSide1Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onClick(v: View) {
        // TODO create the argument in view modele here (see MainActivity onDialogPositiveClick function)
        viewModel.temp_side = 1
        // No position or side send to newArgDialogueFragment here since we deal with a new argument (debate_entity will
        // be created when the dialogue box disappear)
        val newArgDialogueFragment = NewArgDialogFragment()
        val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
        newArgDialogueFragment.show(fm, "newArgument")
    }
}
