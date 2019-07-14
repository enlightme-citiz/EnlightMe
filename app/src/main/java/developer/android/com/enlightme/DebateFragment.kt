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
import androidx.lifecycle.ViewModelProviders
import developer.android.com.enlightme.databinding.FragmentDebateBinding
import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.android.synthetic.main.fragment_main.*
import androidx.fragment.app.DialogFragment


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
class DebateFragment : Fragment(), NewArgDialogFragment.NoticeDialogListener {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: DebateViewModel
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentDebateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.i("DebateFragment", container.toString())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_debate, container, false)
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        Log.i("test_activity",activity.toString())
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        //Log.i("DebateFragment", viewModel.debate.value?.debateEntity?.title)
        //Log.i("DebateFragment", viewModel.debate.value?.debateEntity?.side_1)
        //Log.i("DebateFragment", viewModel.debate.value?.debateEntity?.side_2)
        // viewModel.debate.observe(this, Observer { newDebate ->
        //     binding.side1.text = newDebate.debateEntity.side_1.toString()
        //     binding.side2.text = newDebate.debateEntity.side_2.toString()
        //     binding.debateQuestion.text = newDebate.debateEntity.title.toString()
        // })
        //Log.i("DebateFragment", binding.side1.text.toString())
        //Log.i("DebateFragment", binding.side2.text.toString())
        binding.side1.text = viewModel.debate.value?.debateEntity?.side_1.toString()
        binding.side2.text = viewModel.debate.value?.debateEntity?.side_2.toString()
        binding.debateQuestion.setText(viewModel.debate.value?.debateEntity?.title.toString())
        //Log.i("DebateFragment", binding.side1.text.toString())
        //Log.i("DebateFragment", binding.side2.text.toString())
        this.populate_arguments()
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
        viewModel = ViewModelProviders.of(this).get(DebateViewModel::class.java)
        val fragMan = fragmentManager
        val fragTransaction = fragMan?.beginTransaction()
        // Filling side 1 with arguments
        var top_elmt_id = R.id.side_1_arg_container
        for((iarg_1, args_1) in viewModel.debate.value?.debateEntity?.side_1_entity?.withIndex() ?: listOf<DebateEntity>().withIndex()){
            val arg_frag = ArgumentSide1Fragment().apply {
                arguments = Bundle().apply {
                    putString("title", args_1.title)
                    putString("description", args_1.description)
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
        if (viewModel.debate.value?.debateEntity?.side_1_entity?.isEmpty() ?: true){
            constraintSet1.connect(arg_plus_frag_1.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
        }else{
            constraintSet1.connect(arg_plus_frag_1.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
        }
        fragTransaction?.add(R.id.side_1_arg_container, arg_plus_frag_1)

        // Filling side 2 with arguments
        top_elmt_id = R.id.side_2_arg_container
        for((iarg_2, args_2) in viewModel.debate.value?.debateEntity?.side_2_entity?.withIndex() ?: listOf<DebateEntity>().withIndex()){
            val arg_frag = ArgumentSide2Fragment().apply {
                arguments = Bundle().apply {
                    putString("title", args_2.title)
                    putString("description", args_2.description)
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
        if (viewModel.debate.value?.debateEntity?.side_2_entity?.isEmpty() ?: true){
            constraintSet2.connect(arg_plus_frag_2.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.TOP, 10)
        }else{
            constraintSet2.connect(arg_plus_frag_2.id, ConstraintSet.TOP, top_elmt_id, ConstraintSet.BOTTOM, 5)
        }
        fragTransaction?.add(R.id.side_2_arg_container, arg_plus_frag_2)?.commit()
        binding.side2ArgContainer.setOnClickListener {
            viewModel.temp_side = 2
            val newArgDialogueFragment = NewArgDialogFragment()
            val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
            newArgDialogueFragment.show(fm, "newArgument")
        }
    }
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        // User touched the dialog's positive button
        viewModel.temp_side = 2
        populate_arguments()
    }
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // User touched the dialog's negative button
    }
    // Add one argument to one side
    fun addArgument(side: Int, debateEntity: DebateEntity){
        val constraintSet = ConstraintSet()
        val fragMan = fragmentManager
        val fragTransaction = fragMan?.beginTransaction()
        val newArgFrag: Fragment
        when(side){
            1 -> {
                viewModel.debate.value?.debateEntity?.side_1_entity?.add(debateEntity)
                newArgFrag = ArgumentSide1Fragment.newInstance(debateEntity.title, debateEntity.description)
                if (viewModel.debate.value?.debateEntity?.side_1_entity?.isEmpty() ?: true){
                    //Constraints with the top element
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, binding.side1ArgContainer.id, ConstraintSet.TOP, 10)
                }else{
                    //Constraints with the top element
                    val child = binding.side1ArgContainer.getChildAt(binding.side1ArgContainer.getChildCount()-1)
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, child.id, ConstraintSet.BOTTOM, 5)
                }
                // Constraining the plus button
                val plusButton = binding.side1ArgContainer.getChildAt(binding.side1ArgContainer.getChildCount())
                constraintSet.connect(plusButton.id, ConstraintSet.TOP, newArgFrag.id, ConstraintSet.BOTTOM, 5)
                fragTransaction?.add(R.id.side_1_arg_container, newArgFrag)?.commit()
            }
            2 -> {
                viewModel.debate.value?.debateEntity?.side_2_entity?.add(debateEntity)
                newArgFrag = ArgumentSide2Fragment.newInstance(debateEntity.title, debateEntity.description)
                if (viewModel.debate.value?.debateEntity?.side_2_entity?.isEmpty() ?: true){
                    //Constraints with the top element
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, binding.side2ArgContainer.id, ConstraintSet.TOP, 10)
                }else{
                    //Constraints with the top element
                    val child = binding.side2ArgContainer.getChildAt(binding.side2ArgContainer.getChildCount()-1)
                    constraintSet.connect(newArgFrag.id, ConstraintSet.TOP, child.id, ConstraintSet.BOTTOM, 5)
                }
                // Constraining the plus button
                val plusButton = binding.side2ArgContainer.getChildAt(binding.side2ArgContainer.getChildCount())
                constraintSet.connect(plusButton.id, ConstraintSet.TOP, newArgFrag.id, ConstraintSet.BOTTOM, 5)
                fragTransaction?.add(R.id.side_2_arg_container, newArgFrag)?.commit()
            }
            else -> throw IllegalArgumentException("side should be either 1 or 2.")
        }
    }
}
