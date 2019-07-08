package developer.android.com.enlightme

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_debate.*
import kotlinx.android.synthetic.main.fragment_main.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DebateFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DebateFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DebateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: DebateViewModel
    private var listener: DebateFragment.OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<developer.android.com.enlightme.databinding.FragmentDebateBinding>(inflater, R.layout.fragment_debate, container, false)
        // Inflate the layout for this fragment
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        Log.i("DebateFragment", viewModel.debate.debateEntity.title)
        Log.i("DebateFragment", viewModel.debate.debateEntity.side_1)
        Log.i("DebateFragment", viewModel.debate.debateEntity.side_2)
        binding.debateQuestion.text = "test question"//viewModel.debate.debateEntity.title
        binding.side1.text = viewModel.debate.debateEntity.side_1
        binding.side2.text = viewModel.debate.debateEntity.side_2
        Log.i("DebateFragment", binding.side1.text.toString())
        Log.i("DebateFragment", binding.side2.text.toString())
        this.populate_arguments()
        return inflater.inflate(R.layout.fragment_debate, container, false)
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
         * @return A new instance of fragment DebateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DebateFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


    fun populate_arguments(){
        viewModel = ViewModelProviders.of(this).get(DebateViewModel::class.java)
        val fragMan = fragmentManager
        val fragTransaction = fragMan?.beginTransaction()
        // Filling side 1 with arguments
        var top_elmt_id = R.id.side_1_arg_container
        for((iarg_1, args_1) in viewModel.debate.debateEntity.side_1_entity.withIndex()){
            val arg_frag = ArgumentSide1Fragment().apply {
                arguments = Bundle().apply {
                    putString("title", args_1.title)
                    putInt("idArg", args_1.idArg)
                }
            }
            // Constraining wit the previous element
            val constraintSet = ConstraintSet()
            if (iarg_1 == 0){
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
            }else{
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
            }
            constraintSet.applyTo(const_layout.findViewById(top_elmt_id))
            fragTransaction?.add(R.id.side_1_arg_container, arg_frag)
            top_elmt_id = arg_frag.id
        }
        //Adding the button to enable adding argument
        val arg_plus_frag_1 = ArgumentPlusSide1Fragment()
        val constraintSet1 = ConstraintSet()
        if (viewModel.debate.debateEntity.side_1_entity.isEmpty()){
            constraintSet1.connect(arg_plus_frag_1.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
        }else{
            constraintSet1.connect(arg_plus_frag_1.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
        }
        fragTransaction?.add(R.id.side_1_arg_container, arg_plus_frag_1)

        // Filling side 2 with arguments
        top_elmt_id = R.id.side_2_arg_container
        for((iarg_2, args_2) in viewModel.debate.debateEntity.side_2_entity.withIndex()){
            val arg_frag = ArgumentSide2Fragment().apply {
                arguments = Bundle().apply {
                    putString("title", args_2.title)
                    putInt("idArg", args_2.idArg)
                }
            }
            // Constraining wit the previous element
            val constraintSet = ConstraintSet()
            if (iarg_2 == 0){
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
            }else{
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
            }
            constraintSet.applyTo(const_layout.findViewById(top_elmt_id))
            fragTransaction?.add(R.id.side_1_arg_container, arg_frag)
            top_elmt_id = arg_frag.id
        }

        //Adding the button to enable adding argument
        val arg_plus_frag_2 = ArgumentPlusSide2Fragment()
        val constraintSet2 = ConstraintSet()
        if (viewModel.debate.debateEntity.side_2_entity.isEmpty()){
            constraintSet2.connect(arg_plus_frag_2.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
        }else{
            constraintSet2.connect(arg_plus_frag_2.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
        }
        fragTransaction?.add(R.id.side_2_arg_container, arg_plus_frag_2)?.commit()
    }
}
