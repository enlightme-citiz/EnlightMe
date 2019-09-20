package developer.android.com.enlightme.Debate

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import developer.android.com.enlightme.DebateViewModel
import developer.android.com.enlightme.NewArgDialogFragment
import developer.android.com.enlightme.R
import developer.android.com.enlightme.databinding.FragmentArgumentSide2Binding

private const val TITLE = "title"
private const val DESCRIPTION = "description"
private const val PLACE = "place"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ArgumentSide2Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ArgumentSide2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ArgumentSide2Fragment : Fragment() {
    private var title: String? = null
    private var description: String? = null
    private var place: Int = -1
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: DebateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.title = it.getString(TITLE)
            this.description = it.getString(DESCRIPTION)
            this.place = it.getInt(PLACE)
            // put argument title to the body of the argument icon
            //argument_side2_text.text = title
        }
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentArgumentSide2Binding>(
            inflater,
            R.layout.fragment_argument_side2, container, false
        )
        binding.argumentSide2Text.text = this.title
        //Attache click event listener to display the Add argument dialogue box
        binding.root.setOnLongClickListener {
            // Long click listener function to edit the argument
            viewModel.temp_side = 2
            viewModel.edit_arg_pos = this.place
            this.title = viewModel.debate.value?.debateEntity?.side_2_entity?.get(this.place)?.title
            this.description = viewModel.debate.value?.debateEntity?.side_2_entity?.get(this.place)?.description
            // TODO send the position and side of ArgumentSide1 object to the constructor of NewArgDialogFragment
            val newArgDialogueFragment = NewArgDialogFragment.newInstance(
                this.title ?: "",
                this.description ?: ""
            )
            val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
            newArgDialogueFragment.show(fm, "editArgument")
            true
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
        fun newInstance(title: String, description: String, place: Int) =
            ArgumentSide2Fragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(DESCRIPTION, description)
                    putInt(PLACE, place)
                }
            }
    }
}
