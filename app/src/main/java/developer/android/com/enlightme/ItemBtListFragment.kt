package developer.android.com.enlightme

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import developer.android.com.enlightme.databinding.FragmentItemBtListBinding
import developer.android.com.enlightme.databinding.FragmentJoinDebateBinding


// TODO: Rename parameter arguments, choose names that match
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
class ItemBtListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var name_bt_network: String? = null
    var nb_attendees: Int = 0
    private lateinit var binding: FragmentItemBtListBinding
    private var listener: OnFragmentInteractionListener? = null

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
        // Inflate the layout for this fragment
        // Add nb_attendees and name_bt_network to the fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_item_bt_list, container, false)
        binding.nameBtNetwork.text = name_bt_network
        binding.nbAttendees.text = nb_attendees.toString()
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
         * @return A new instance of fragment itemBtListFragment.
         */
        // TODO: Rename and change types and number of parameters
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
