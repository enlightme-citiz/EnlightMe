package developer.android.com.enlightme

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import developer.android.com.enlightme.databinding.FragmentDebateBinding
import developer.android.com.enlightme.databinding.FragmentNewArgDialogBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TITLE = "title"
private const val ARG_DESCRIPTION = "description"
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewArgDialogFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewArgDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NewArgDialogFragment : DialogFragment() {
    private var title: String? = null
    private var description: String? = null
    private lateinit var binding: FragmentNewArgDialogBinding
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: DebateViewModel
    // Use this instance of the interface to deliver action events
    internal lateinit var dialogListener: NoticeDialogListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("NewArgDialogFragment", "onCreate")
        arguments?.let {
            title = it.getString(ARG_TITLE)
            description = it.getString(ARG_DESCRIPTION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("NewArgDialogFragment", "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_arg_dialog, container, false)
        return binding.root
    }

    override fun onResume() {
        Log.i("NewArgDialogFragment", "onResume")
        super.onResume()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("NewArgDialogFragment", "onResume")
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dialogListener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }

    }

    override fun onDetach() {
        super.onDetach()
        Log.i("NewArgDialogFragment", "onResume")
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i("NewArgDialogFragment", "onCreateDialog")
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        //val container = activity?.findViewById<ViewGroup>(R.id.fragment_new_arg_dialogue_core)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = inflater.inflate(R.layout.fragment_new_arg_dialog, null)
            builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(
                    R.string.new_arg_dialogue_ok
                ) { dialog, id ->
                    // copy argument title and description in the temp_debate_entity of DebateViewModel
                    viewModel.temp_debate_entity.title =
                        dialogView.findViewById<EditText>(R.id.new_arg_dialog_title).text.toString()
                    viewModel.temp_debate_entity.description =
                        dialogView.findViewById<EditText>(R.id.new_arg_dialog_description).text.toString()
                    dialogListener.onDialogPositiveClick(this)
                }
                .setNegativeButton(R.string.new_arg_dialogue_cancel)
                { dialog, id ->
                    getDialog()?.cancel()
                }
            Log.i("NewArgDialogFragment", "resume new arg dialog.")
            if (title != null) {
                dialogView.findViewById<EditText>(R.id.new_arg_dialog_title)
                    .setText(title.toString(), TextView.BufferType.EDITABLE)
                Log.i(
                    "NewArgDialogFragment",
                    dialogView.findViewById<EditText>(R.id.new_arg_dialog_title).text.toString()
                )
                //Log.i("NewArgDialogFragment", title)
            }
            if (description != null) {
                dialogView.findViewById<EditText>(R.id.new_arg_dialog_description)
                    .setText(description.toString(), TextView.BufferType.EDITABLE)
                Log.i(
                    "NewArgDialogFragment",
                    dialogView.findViewById<EditText>(R.id.new_arg_dialog_description).text.toString()
                )
                //Log.i("NewArgDialogFragment", description)
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Interface to communicate between this dialog and debateFragment
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, description: String) =
            NewArgDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DESCRIPTION, description)
                }
            }
    }
}
