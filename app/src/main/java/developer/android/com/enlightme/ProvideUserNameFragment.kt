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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import developer.android.com.enlightme.databinding.FragmentNewArgDialogBinding
import developer.android.com.enlightme.databinding.FragmentProvideUserNameBinding


/**
 * A simple [Fragment] subclass.
 *
 */
class ProvideUserNameFragment : DialogFragment() {
    private lateinit var binding: FragmentProvideUserNameBinding
    private lateinit var viewModel: JoinDebateViewModel
    private var listener: OnFragmentInteractionListener? = null
    // Use this instance of the interface to deliver action events
    internal lateinit var dialogListener: NoticeDialogListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_provide_user_name, container, false)
        return binding.root
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("ProvideUserNameFragment", "onResume")
        if (context is ProvideUserNameFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dialogListener = context as ProvideUserNameFragment.NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(JoinDebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        //val container = activity?.findViewById<ViewGroup>(R.id.fragment_provide_user_nameue_core)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = inflater.inflate(R.layout.fragment_provide_user_name, null)
            builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(
                    R.string.provide_user_name_ok
                ) { dialog, id ->
                    // copy argument title and description in the temp_debate_entity of DebateViewModel
                    viewModel.userName =
                        dialogView.findViewById<EditText>(R.id.provide_user_name_entry).text.toString()
                    //dialogListener.onDialogPositiveClick(this)
                }
                .setNegativeButton(R.string.provide_user_name_cancel)
                { dialog, id ->
                    getDialog()?.cancel()
                    view?.findNavController()?.navigate(R.id.action_provideUserNameFragment_to_mainFragment)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    // Interface to communicate between this dialog and debateFragment
    interface NoticeDialogListener {
        // fun onDialogPositiveClick(dialog: DialogFragment)
        // fun onDialogNegativeClick(dialog: DialogFragment)
    }
}
