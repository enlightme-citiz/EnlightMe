package developer.android.com.enlightme.Debate

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import developer.android.com.enlightme.DebateViewModel
import developer.android.com.enlightme.MainActivity
import developer.android.com.enlightme.P2PClasses.P2P
import developer.android.com.enlightme.R
import developer.android.com.enlightme.databinding.FragmentDebateBinding
import developer.android.com.enlightme.objects.DebateEntity


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
    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {}
        override fun onEndpointLost(endpointId: String) {}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_debate, container, false)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.side1.text = viewModel.debate.value?.debateEntity?.side_1.toString()
        binding.side2.text = viewModel.debate.value?.debateEntity?.side_2.toString()
        binding.debateQuestion.setText(viewModel.debate.value?.debateEntity?.title.toString())
        this.populate_arguments()
        // val debateFragmentObj = this.fragmentManager?.findFragmentById(this.id)
        // activity?.run {
        //     if(this is MainActivity){
        //         if(debateFragmentObj is DebateFragment){
        //             this.debateFragment = debateFragmentObj
        //         }else{
        //             throw IllegalArgumentException("debateFragment should be of class DebateFragment.")
        //         }
        //     }
        // }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val p2p = P2P(requireContext(), endpointDiscoveryCallback)
        val userName = viewModel.debate.value?.debateEntity?.title ?: "Sans nom"
        p2p.startAdvertising(userName)
        return super.onOptionsItemSelected(item)
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
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
        //Adding the button to enable adding argument
        addArg1Frag = ArgumentPlusSide1Fragment()
        val constraintSet1 = ConstraintSet()
        constraintSet1.connect(addArg1Frag.id, ConstraintSet.TOP, binding.side1ArgContainer.id, ConstraintSet.TOP, 10)
        fragTransaction?.add(R.id.side_1_arg_container, addArg1Frag)

        //Adding the button to enable adding argument
        addArg2Frag = ArgumentPlusSide2Fragment()
        val constraintSet2 = ConstraintSet()
        constraintSet2.connect(addArg2Frag.id, ConstraintSet.TOP, binding.side2ArgContainer.id, ConstraintSet.TOP, 10)
        fragTransaction?.add(R.id.side_2_arg_container, addArg2Frag)?.commit()

        // Filling side 1 with arguments
        for((iarg_1, args_1) in viewModel.debate.value?.debateEntity?.side_1_entity?.withIndex() ?: listOf<DebateEntity>().withIndex()){
            this.addArgument(1, args_1, iarg_1, addArg1Frag.id)
        }

        // Filling side 2 with arguments
        for((iarg_2, args_2) in viewModel.debate.value?.debateEntity?.side_2_entity?.withIndex() ?: listOf<DebateEntity>().withIndex()){
            this.addArgument(2, args_2, iarg_2, addArg2Frag.id)
        }
    }

    // Add one argument to one side
    fun addArgument(side: Int, debateEntity: DebateEntity, place: Int, idPlusButton: Int? = null){
        val constraintSetArg = ConstraintSet()
        val constraintSetPls = ConstraintSet()
        val fragTransaction = fragmentManager?.beginTransaction()
        val newArgFrag: Fragment
        when(side){
            1 -> {
                newArgFrag =
                    ArgumentSide1Fragment.newInstance(
                        debateEntity.title,
                        debateEntity.description,
                        place
                    )
                if (side1ArgList.isEmpty()){
                    //Constraints with the top element
                    constraintSetArg.connect(newArgFrag.id, ConstraintSet.TOP, binding.side1ArgContainer.id, ConstraintSet.TOP, 10)
                }else{
                    //Constraints with the top element
                    constraintSetArg.connect(newArgFrag.id, ConstraintSet.TOP, side1ArgList[side1ArgList.size-1].id, ConstraintSet.BOTTOM, 5)
                }
                // Constraining the plus button
                val plusId :Int
                if(idPlusButton == null){
                    plusId = binding.side1ArgContainer.getChildAt(binding.side1ArgContainer.childCount-1).id
                }else{
                    plusId = idPlusButton
                }

                constraintSetPls.connect(plusId, ConstraintSet.TOP, newArgFrag.id, ConstraintSet.BOTTOM, 5)
                fragTransaction?.add(R.id.side_1_arg_container, newArgFrag)?.commit()
            }
            2 -> {
                newArgFrag =
                    ArgumentSide2Fragment.newInstance(
                        debateEntity.title,
                        debateEntity.description,
                        place
                    )
                if (side2ArgList.isEmpty()){
                    //Constraints with the top element
                    constraintSetArg.connect(newArgFrag.id, ConstraintSet.TOP, binding.side2ArgContainer.id, ConstraintSet.TOP, 10)
                }else{
                    //Constraints with the top element
                    constraintSetArg.connect(newArgFrag.id, ConstraintSet.TOP, side2ArgList[side2ArgList.size-1].id, ConstraintSet.BOTTOM, 5)
                }
                // Constraining the plus button
                val plusId :Int
                if(idPlusButton == null){
                    plusId = binding.side2ArgContainer.getChildAt(binding.side2ArgContainer.childCount-1).id
                }else{
                    plusId = idPlusButton
                }
                constraintSetPls.connect(plusId, ConstraintSet.TOP, newArgFrag.id, ConstraintSet.BOTTOM, 5)
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
                val argView = binding.side1ArgContainer.getChildAt(edit_arg_pos+1)
                argView.findViewById<TextView>(R.id.argument_side1_text).text = debateEntity.title
                argView.findViewById<TextView>(R.id.argument_side1_description).text = debateEntity.description
            }
            2 -> {
                val argView = binding.side2ArgContainer.getChildAt(edit_arg_pos+1)
                argView.findViewById<TextView>(R.id.argument_side2_text).text = debateEntity.title
                argView.findViewById<TextView>(R.id.argument_side2_description).text = debateEntity.description
            }
            else -> {
                //Log.i("temp_side", side.toString())
                throw IllegalArgumentException("side should be either 1 or 2.")
            }
        }
        // Redraw the UI or the argument table.
    }

}
