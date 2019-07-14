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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

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
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: DebateViewModel
    // Use this instance of the interface to deliver action events
    internal lateinit var dialogListener: NoticeDialogListener

    // Interface to communicate between this dialog and debateFragment
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_new_arg, container, false)
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
        // Verify that the host activity implements the callback interface
        Log.i("TestInterfacedialog",this.toString())
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dialogListener = this as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
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
         * @return A new instance of fragment NewArgDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewArgDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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
            val dialogView = inflater.inflate(R.layout.fragment_dialog_new_arg_core, null)
            builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.new_arg_dialogue_ok
                ) { dialog, id ->
                    // copy argument title and summary in the temp_debate_entity of DebateViewModel
                    viewModel.temp_debate_entity.title = dialogView.findViewById<EditText>(R.id.new_arg_dialog_title).text.toString()
                    viewModel.temp_debate_entity.description = dialogView.findViewById<EditText>(R.id.new_arg_dialog_description).text.toString()
                }
                .setNegativeButton(R.string.new_arg_dialogue_cancel)
                    { dialog, id ->
                        getDialog()?.cancel()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
