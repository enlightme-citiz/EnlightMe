package developer.android.com.enlightme

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import developer.android.com.enlightme.objects.Attendee
import kotlinx.android.synthetic.main.fragment_debate.view.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Create2Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Create2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Create2Fragment : Fragment() {
    private lateinit var viewModel: DebateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.i("Create2Fragment", container.)
        val binding = DataBindingUtil.inflate<developer.android.com.enlightme.databinding.FragmentCreate2Binding>(inflater, R.layout.fragment_create2, container, false)
        setHasOptionsMenu(true)
        //Click listener to next view
        Log.i("test_activity",activity.toString())
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.suivantCreate2.setOnClickListener{ view : View ->
            // Checking if all fields are filled
            var toast = false
//            if (TextUtils.isEmpty(binding.side1.text.toString()) ||
//                TextUtils.isEmpty(binding.side2.text.toString())){
//                Toast.makeText(activity, getResources().getString(R.string.side_not_filed), Toast.LENGTH_SHORT).show()
//                toast = true
//            }
//            if (TextUtils.isEmpty(binding.pseudo.text.toString())){
//                Toast.makeText(activity, getResources().getString(R.string.pseudo_not_filed), Toast.LENGTH_SHORT).show()
//                toast = true
//            }
            // Saving data and moving to the debate
            if (toast == false){
                viewModel.debate.value?.debateEntity?.side_1 = binding.side1.text.toString()
                viewModel.debate.value?.debateEntity?.side_2 = binding.side2.text.toString()
                //Creating attendee
                val user = Attendee()
                user.name = binding.pseudo.text.toString()
                viewModel.debate.value?.listAttendees?.add(user)
                viewModel.attendee.value = user
                view.findNavController().navigate(R.id.action_create2Fragment_to_debateFragment)
            }
        }
        return binding.root
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

    companion object {
        @JvmStatic
        fun newInstance() =
            Create2Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
