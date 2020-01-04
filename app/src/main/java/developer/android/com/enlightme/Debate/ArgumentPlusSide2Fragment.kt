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
import developer.android.com.enlightme.databinding.FragmentArgumentPlusSide2Binding


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ArgumentPlusSide2Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ArgumentPlusSide2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ArgumentPlusSide2Fragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: DebateViewModel
    private lateinit var binding: FragmentArgumentPlusSide2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_argument_plus_side2, container, false)
        binding.argumentPlus2.setOnClickListener(this)
        binding.argumentPlus2.background = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ripple_arg_plus_side_2)
        binding.icAddBlack24dp.background = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ic_add_black_24dp)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArgumentPlusSide2Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onClick(v: View) {
        viewModel.temp_side = 2
        // No position or side send to newArgDialogueFragment here since we deal with a new argument (debate_entity will
        // be created when the dialogue box disappear)
        val newArgDialogueFragment = NewArgDialogFragment()
        val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
        newArgDialogueFragment.show(fm, "newArgument")
    }
}
