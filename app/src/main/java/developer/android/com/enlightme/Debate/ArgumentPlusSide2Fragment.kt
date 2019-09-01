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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: DebateViewModel
    private lateinit var binding: FragmentArgumentPlusSide2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArgumentPlusSide2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArgumentPlusSide2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onClick(v: View) {
        viewModel.temp_side = 2
        val newArgDialogueFragment = NewArgDialogFragment()
        val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
        newArgDialogueFragment.show(fm, "newArgument")
    }
}
