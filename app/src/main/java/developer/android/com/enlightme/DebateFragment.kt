package developer.android.com.enlightme

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import developer.android.com.enlightme.databinding.FragmentDebateBinding
import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.android.synthetic.main.fragment_main.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import developer.android.com.enlightme.databinding.FragmentArgumentPlusSide1Binding
import developer.android.com.enlightme.databinding.FragmentArgumentPlusSide2Binding
import kotlinx.android.synthetic.main.fragment_argument_plus_side1.*
import kotlinx.android.synthetic.main.fragment_argument_side1.view.*
import kotlinx.android.synthetic.main.fragment_argument_side2.view.*


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
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentDebateBinding
    //Liste of side 1 argument (ArgumentSide1Fragment)
    private var side1ArgList = mutableListOf<ArgumentSide1Fragment>()
    //Liste of side 2 argument (ArgumentSide2Fragment)
    private var side2ArgList = mutableListOf<ArgumentSide2Fragment>()
    // Attribut to hold the plus buttons fragments
    private lateinit var addArg1Frag: ArgumentPlusSide1Fragment
    private lateinit var addArg2Frag: ArgumentPlusSide2Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_debate, container, false)
        setHasOptionsMenu(true)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.side1.text = viewModel.debate.value?.debateEntity?.side_1.toString()
        binding.side2.text = viewModel.debate.value?.debateEntity?.side_2.toString()
        binding.debateQuestion.setText(viewModel.debate.value?.debateEntity?.title.toString())
        this.populate_arguments()
        val debateFragmentObj = this.fragmentManager?.findFragmentById(this.id)
        activity?.run {
            if(this is MainActivity){
                if(debateFragmentObj is DebateFragment){
                    this.debateFragment = debateFragmentObj
                }else{
                    throw IllegalArgumentException("debateFragment should be of class DebateFragment.")
                }
            }
        }
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
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val fragTransaction = fragmentManager?.beginTransaction()
        // Filling side 1 with arguments
        var top_elmt_id = R.id.side_1_arg_container
        for((iarg_1, args_1) in viewModel.debate.value?.debateEntity?.side_1_entity?.withIndex() ?: listOf<DebateEntity>().withIndex()){
            val arg_frag = ArgumentSide1Fragment().apply {
                arguments = Bundle().apply {
                    putString("title", args_1.title)
                    putString("description", args_1.description)
                }
            }
            side1ArgList.add(arg_frag)
            // Constraining wit the previous element
            val constraintSet = ConstraintSet()
            if (iarg_1 == 0){
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
            }else{
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
            }
            Log.i("DebateFragment", top_elmt_id.toString())
            // constraintSet.applyTo(const_layout.findViewById(top_elmt_id))
            fragTransaction?.add(R.id.side_1_arg_container, arg_frag)
            top_elmt_id = arg_frag.id
        }
        //Adding the button to enable adding argument
        addArg1Frag = ArgumentPlusSide1Fragment()
        val constraintSet1 = ConstraintSet()
        if (viewModel.debate.value?.debateEntity?.side_1_entity?.isEmpty() ?: true){
            constraintSet1.connect(addArg1Frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
        }else{
            constraintSet1.connect(addArg1Frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
        }
        fragTransaction?.add(R.id.side_1_arg_container, addArg1Frag)

        // Filling side 2 with arguments
        top_elmt_id = R.id.side_2_arg_container
        for((iarg_2, args_2) in viewModel.debate.value?.debateEntity?.side_2_entity?.withIndex() ?: listOf<DebateEntity>().withIndex()){
            val arg_frag = ArgumentSide2Fragment().apply {
                arguments = Bundle().apply {
                    putString("title", args_2.title)
                    putString("description", args_2.description)
                }
            }
            side2ArgList.add(arg_frag)
            // Constraining wit the previous element
            val constraintSet = ConstraintSet()
            if (iarg_2 == 0){
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
            }else{
                constraintSet.connect(arg_frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
            }
            //constraintSet.applyTo(const_layout.findViewById(top_elmt_id))
            fragTransaction?.add(R.id.side_1_arg_container, arg_frag)
            top_elmt_id = arg_frag.id
        }

        //Adding the button to enable adding argument
        addArg2Frag = ArgumentPlusSide2Fragment()
        
        val constraintSet2 = ConstraintSet()
        if (viewModel.debate.value?.debateEntity?.side_2_entity?.isEmpty() ?: true){
            constraintSet2.connect(addArg2Frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
        }else{
            constraintSet2.connect(addArg2Frag.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
        }
        fragTransaction?.add(R.id.side_2_arg_container, addArg2Frag)?.commit()
    }

    // Add one argument to one side
    fun addArgument(side: Int, debateEntity: DebateEntity){
        val constraintSet = ConstraintSet()
        val fragTransaction = fragmentManager?.beginTransaction()
        val newArgFrag: Fragment
        when(side){
            1 -> {
                val place = viewModel.debate.value?.debateEntity?.side_1_entity?.size ?: -1
                viewModel.debate.value?.debateEntity?.side_1_entity?.add(debateEntity)
                newArgFrag = ArgumentSide1Fragment.newInstance(debateEntity.title, debateEntity.description, place)
                if (side1ArgList.isEmpty()){
                    //Constraints with the top element
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, binding.side1ArgContainer.id, ConstraintSet.TOP, 10)
                }else{
                    //Constraints with the top element
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, side1ArgList[-1].id, ConstraintSet.BOTTOM, 5)
                }
                // Constraining the plus button
                val plusButton = binding.side1ArgContainer.getChildAt(binding.side1ArgContainer.getChildCount()-1)
                constraintSet.connect(plusButton.id, ConstraintSet.TOP, newArgFrag.id, ConstraintSet.BOTTOM, 5)
                fragTransaction?.add(R.id.side_1_arg_container, newArgFrag)?.commit()
            }
            2 -> {
                viewModel.debate.value?.debateEntity?.side_2_entity?.add(debateEntity)
                newArgFrag = ArgumentSide2Fragment.newInstance(debateEntity.title, debateEntity.description)
                if (side2ArgList.isEmpty()){
                    //Constraints with the top element
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, binding.side2ArgContainer.id, ConstraintSet.TOP, 10)
                }else{
                    //Constraints with the top element
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, side2ArgList[-1].id, ConstraintSet.BOTTOM, 5)
                }
                // Constraining the plus button
                val plusButton = binding.side2ArgContainer.getChildAt(binding.side2ArgContainer.getChildCount()-1)
                constraintSet.connect(plusButton.id, ConstraintSet.TOP, newArgFrag.id, ConstraintSet.BOTTOM, 5)
                fragTransaction?.add(R.id.side_2_arg_container, newArgFrag)?.commit()
            }
            else -> {
                //Log.i("temp_side", side.toString())
                throw IllegalArgumentException("side should be either 1 or 2.")
            }
        }
    }
    fun modArgument(side: Int, debateEntity: DebateEntity, edit_arg_pos: Int){
        when(side) {
            1 -> {
                viewModel.debate.value?.debateEntity?.side_1_entity?.set(edit_arg_pos, debateEntity)
                Log.i("DebateFragment", edit_arg_pos.toString())
                Log.i("DebateFragment", binding.side1ArgContainer.toString())
                Log.i("DebateFragment", binding.side1ArgContainer.childCount.toString())
                val argView = binding.side1ArgContainer.getChildAt(edit_arg_pos+1)
                argView.findViewById<TextView>(R.id.argument_side1_text).text = debateEntity.title
                argView.findViewById<TextView>(R.id.argument_side1_description).text = debateEntity.description
            }
            2 -> {
                viewModel.debate.value?.debateEntity?.side_2_entity?.set(edit_arg_pos, debateEntity)
                val argView = binding.side1ArgContainer.getChildAt(edit_arg_pos)
                argView.argument_side2_text.text = debateEntity.title
                argView.argument_side2_description.text = debateEntity.description
            }
            else -> {
                //Log.i("temp_side", side.toString())
                throw IllegalArgumentException("side should be either 1 or 2.")
            }
        }
        // Redraw the UI or the argument table.
    }

}
