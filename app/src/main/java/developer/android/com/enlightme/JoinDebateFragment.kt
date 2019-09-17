package developer.android.com.enlightme

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import developer.android.com.enlightme.P2PClasses.P2P
import developer.android.com.enlightme.databinding.FragmentJoinDebateBinding

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [JoinDebateFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [JoinDebateFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class JoinDebateFragment : Fragment() {
    private lateinit var binding: FragmentJoinDebateBinding
    private var listener: OnFragmentInteractionListener? = null
    var listNetFrag = mutableListOf<ItemBtListFragment>()
    lateinit var p2p: P2P
    private lateinit var viewModel: JoinDebateViewModel

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            if(viewModel.listNet[info.endpointName] == null){
                //Update listNet map
                viewModel.listNet.plusAssign(Pair(info.endpointName, mutableListOf(endpointId)))
                val fragTransaction = fragmentManager?.beginTransaction()
                val newNetworkItem = ItemBtListFragment.newInstance(info.endpointName, 1)
                //Positioning the network entry in the list
                val constraintSet = ConstraintSet()
                if(listNetFrag.isEmpty()){
                    constraintSet.connect(newNetworkItem.id,
                        ConstraintSet.TOP, binding.listNetwork.id,
                        ConstraintSet.TOP, 10)
                }else{
                    constraintSet.connect(newNetworkItem.id,
                        ConstraintSet.TOP, listNetFrag[listNetFrag.size-1].id,
                        ConstraintSet.BOTTOM, 10)
                }
                fragTransaction?.add(binding.listNetwork.id, newNetworkItem)?.commit()
                listNetFrag.add(listNetFrag.lastIndex+1, newNetworkItem)
            }else {
                if (!(viewModel.listNet[info.endpointName]?.contains(endpointId) ?: false)) {
                    viewModel.listNet[info.endpointName]?.add(viewModel.listNet[info.endpointName]!!.lastIndex+1,
                        endpointId)
                    Log.i("JoinDebateFragment", "updated viewModel.listNet[info.endpointName]")
                    Log.i("JoinDebateFragment", viewModel.listNet[info.endpointName].toString())
                    for (frag in listNetFrag){
                        if (frag.name_bt_network == info.endpointName){
                            frag.nb_attendees += 1
                        }
                    }
                }
            }
        }
        override fun onEndpointLost(endpointId: String) {
            //get the network name(s) by going through the list
            var list_name = mutableListOf<String>()
            for ((name, list_id) in viewModel.listNet){
                if (list_id.contains(endpointId)){
                    list_name.add(name)
                    //remove the endopintId where it appear
                    list_id.remove(endpointId)
                }
            }
            //update corresponding fragments
            var ind_frag = listNetFrag.size-1
            for (frag in listNetFrag.reversed()){
                if (frag.name_bt_network in list_name){
                    if (frag.nb_attendees > 1){
                        frag.nb_attendees -= 1
                    }else{
                        // we remove the entry if the network has completely vanished
                        if(listNetFrag.size > 1) {
                            val constraintSet = ConstraintSet()
                            val previous_frag = listNetFrag[ind_frag + 1]
                            if (ind_frag == 0) {
                                constraintSet.connect(
                                    previous_frag.id,
                                    ConstraintSet.TOP, binding.listNetwork.id,
                                    ConstraintSet.TOP, 10
                                )
                            } else {
                                constraintSet.connect(
                                    previous_frag.id,
                                    ConstraintSet.TOP, listNetFrag[ind_frag - 1].id,
                                    ConstraintSet.TOP, 10
                                )
                            }
                        }
                        //remove the entry
                        fragmentManager?.beginTransaction()?.remove(frag)?.commit()
                    }
                }
                ind_frag -= 1
            }
        }
    }

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
            ViewModelProviders.of(this).get(JoinDebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_join_debate, container, false)
        p2p = P2P(requireContext(), endpointDiscoveryCallback)
        p2p.listAvailablePeers()
        if(viewModel.userName == ""){
            val provideUserNameFragment = ProvideUserNameFragment()
            val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
            provideUserNameFragment.show(fm, "provideUserName")
        }
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JoinDebateFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JoinDebateFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
