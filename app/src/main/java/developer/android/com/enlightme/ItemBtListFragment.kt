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
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import developer.android.com.enlightme.P2PClasses.PayloadByteCallback
import developer.android.com.enlightme.databinding.FragmentItemBtListBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val NAME_BT_NETWORK = "name_bt_network"
private const val NB_ATTENDEES = "nb_attendees"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ItemBtListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ItemBtListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ItemBtListFragment : Fragment(), View.OnClickListener{
    var name_bt_network: String? = null
    var nb_attendees: Int = 0
    private lateinit var binding: FragmentItemBtListBinding
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModelJoinDebate: JoinDebateViewModel
    private lateinit var viewModelDebate: DebateViewModel
    val payloadCallback = PayloadByteCallback(viewModelDebate, viewModelJoinDebate, requireContext())
    val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            Nearby.getConnectionsClient(requireContext()).acceptConnection(endpointId, payloadCallback)
            // Automatically accept the connection on both sides.
            viewModelJoinDebate.listEndpointId.plus(endpointId)
        }
        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when (result.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    Log.i("ItemBtListFragment", "Connection successful")
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    Log.i("ItemBtListFragment", "Connection rejected")
                }
                ConnectionsStatusCodes.STATUS_ERROR -> {
                    Log.i("ItemBtListFragment", "Unknown error")
                }
            }// We're connected! Can now start sending and receiving data.
            // The connection was rejected by one or both sides.
            // The connection broke before it was able to be accepted.
            // Unknown status code
        }
        override fun onDisconnected(endpointId: String) {
            Log.i("ItemBtListFragment", "Disconnected from $endpointId.")
            // We've been disconnected from this endpoint. No more data can be
            // sent or received.
            viewModelJoinDebate.listEndpointId.remove(endpointId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name_bt_network = it.getString(NAME_BT_NETWORK)
            nb_attendees = it.getInt(NB_ATTENDEES)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelJoinDebate = activity?.run {
            ViewModelProviders.of(this).get(JoinDebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        // Add nb_attendees and name_bt_network to the fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_item_bt_list, container, false)
        binding.nameBtNetwork.text = name_bt_network
        binding.nbAttendees.text = nb_attendees.toString()
        binding.root.setOnClickListener(this)
        binding.root.background = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ripple_item_bt_list)
        return binding.root
    }
    fun connectNetwork(){
        Log.i("ItemBtListFragment", viewModelJoinDebate.listNet[name_bt_network].toString())
        //For each endpoint of the network, we request a connection to it.
        var lstEP = (viewModelJoinDebate.listNet[name_bt_network] ?: mutableListOf()).toMutableList()
        for (i in 0..lstEP.size) {
            val ep = lstEP[0]
            val cog = Nearby.getConnectionsClient(requireContext())
            val co = cog.requestConnection(viewModelJoinDebate.userName, ep, connectionLifecycleCallback)
            co.addOnSuccessListener{
                if(lstEP.remove(ep)){
                    Toast.makeText(requireContext(), "Connexion à $ep établie", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Impossible de supprimer $ep", Toast.LENGTH_SHORT).show()
                }
            }
            co.addOnFailureListener{
                Log.i("ItemBtListFragment", it.message)
                if(it.message != "8003: STATUS_ALREADY_CONNECTED_TO_ENDPOINT"){
                    Toast.makeText(requireContext(), "Connexion à $ep échouée. Nouvelle tentative dans 5s.", Toast.LENGTH_SHORT).show()
                    Thread.sleep(5_000)
                    connectNetwork()
                }
            }
        }
    }

    override fun onClick(v: View) {
        //Establishing communication
        connectNetwork()
        // Initiate viewModel to store debate information
        viewModelDebate = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        //Query debate from other's debateViewModel.
        val net = viewModelJoinDebate.listNet[name_bt_network] ?: mutableListOf<String>()
        loop@ for(n in net){
            val payload = Payload.fromBytes(n.toByteArray())
            Nearby.getConnectionsClient(requireContext()).sendPayload(n, payload)
            Thread.sleep(1_000)
            if(viewModelDebate.is_updated == true) break@loop
        }
        //Update it in viewModelDebate
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
         * @return A new instance of fragment itemBtListFragment.
         */
        @JvmStatic
        fun newInstance(name_bt_network: String, nb_attendees: Int) =
            ItemBtListFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME_BT_NETWORK, name_bt_network)
                    putInt(NB_ATTENDEES, nb_attendees)
                }
            }
    }
}
