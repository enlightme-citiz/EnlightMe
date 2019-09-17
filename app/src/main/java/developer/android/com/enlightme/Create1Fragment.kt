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




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Create1Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Create1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Create1Fragment : Fragment() {
    private lateinit var viewModel: DebateViewModel
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<developer.android.com.enlightme.databinding.FragmentCreate1Binding>(inflater, R.layout.fragment_create1, container, false)
        setHasOptionsMenu(true)
        //Click listener to next view
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.suivantCreate1.setOnClickListener{ view : View ->
            // Checking if all fields are filled
            var toast = false
//            if (TextUtils.isEmpty(binding.debateQuestion.text.toString())){
//                Toast.makeText(activity, getResources().getString(R.string.question_not_filled), Toast.LENGTH_SHORT).show()
//                toast = true
//            }
//            if (TextUtils.isEmpty(binding.introNewDebat.text.toString())){
//                Toast.makeText(activity, getResources().getString(R.string.intro_not_filed), Toast.LENGTH_SHORT).show()
//                toast = true
//            }
            // Saving data and moving to the second part of the form
            if (toast == false) {
                viewModel.debate.value?.debateEntity?.title = binding.debateQuestion.text.toString()
                Log.i("Create1Fragment", binding.debateQuestion.text.toString())
                Log.i("Create1Fragment", viewModel.debate.value?.debateEntity?.title)
                viewModel.debate.value?.debateEntity?.description = binding.introNewDebat.text.toString()
                view.findNavController().navigate(R.id.action_create1Fragment_to_create2Fragment)
            }
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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Create1Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
