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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ArgumentPlusSide1Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ArgumentPlusSide1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ArgumentPlusSide1Fragment : Fragment(), View.OnClickListener{
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: DebateViewModel
    private lateinit var binding: FragmentArgumentPlusSide1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            R.layout.fragment_argument_plus_side1, container, false)
        //Attache click event listener to display the Add argument dialogue box
        binding.argumentPlus1.setOnClickListener(this)
        binding.argumentPlus1.background = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ripple_arg_plus_side_1)
        binding.icAddWhite24dp.background = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ic_add_white_24dp)
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
         * @return A new instance of fragment ArgumentPlusSide1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ArgumentPlusSide1Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onClick(v: View) {
        viewModel.temp_side = 1
        val newArgDialogueFragment = NewArgDialogFragment()
        val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
        newArgDialogueFragment.show(fm, "newArgument")
    }
}
