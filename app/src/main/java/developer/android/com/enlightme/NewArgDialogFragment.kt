package developer.android.com.enlightme

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import developer.android.com.enlightme.Debate.ConcurentOp.DeleteStr
import developer.android.com.enlightme.Debate.ConcurentOp.InsertStr
import developer.android.com.enlightme.databinding.FragmentNewArgDialogBinding
import developer.android.com.enlightme.Objects.DebateEntity

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TITLE = "title"
private const val ARG_DESCRIPTION = "description"
private const val ARG_SIDE = "side"
private const val ARG_PLACE = "place"
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
    private var side:Int = -1
    private var place:Int = -1
    private lateinit var binding: FragmentNewArgDialogBinding
    private lateinit var viewModel: DebateViewModel
    private lateinit var viewModelJoin: JoinDebateViewModel
    // Use this instance of the interface to deliver action events
    internal lateinit var dialogListener: NoticeDialogListener

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("NewArgDialogFragment", "onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            description = it.getString(ARG_DESCRIPTION)
            side = it.getInt(ARG_SIDE)
            place = it.getInt(ARG_PLACE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("NewArgDialogFragment", "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_arg_dialog, container, false)
        binding.newArgDialogTitle.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fire call to change and update in viewModel. Done with the right side and place in
                // this and current_level in debate
                var debate_entity: DebateEntity?
                if(side==1){
                    debate_entity = viewModel.debate.value?.getDebateEntity()?.side_1_entity?.get(place)

                }else{
                    debate_entity = viewModel.debate.value?.getDebateEntity()?.side_2_entity?.get(place)
                }
                debate_entity ?: run{
                    Log.i("NewArgDialogFragment", "Debate entity not found.")
                    throw java.lang.Exception("Debate entity not found.")
                }
                val target = "title"
                val listOperation = listOf(DeleteStr(start, before, target),
                                                      InsertStr(start, s.substring(start,start+count),
                                                          target)
                )
                viewModel.debate.value!!.manageUserUpdate(listOperation, requireContext(), viewModelJoin.listEndpointId,
                    viewModelJoin.myEndpointId, debate_entity.path_to_root)
            }
        })
        binding.newArgDialogDescription.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fire call to change and update in viewModel. Done with the right side and place in
                // this and current_level in debate
                var debate_entity: DebateEntity?
                if(side==1){
                    debate_entity = viewModel.debate.value?.getDebateEntity()?.side_1_entity?.get(place)

                }else{
                    debate_entity = viewModel.debate.value?.getDebateEntity()?.side_2_entity?.get(place)
                }
                debate_entity ?: run{
                    Log.i("NewArgDialogFragment", "Debate entity not found.")
                    throw java.lang.Exception("Debate entity not found.")
                }
                val target = "description"
                val listOperation = listOf(DeleteStr(start, before, target),
                    InsertStr(start, s.substring(start,start+count),
                        target)
                )
                viewModel.debate.value!!.manageUserUpdate(listOperation, requireContext(),
                    viewModelJoin.listEndpointId, viewModelJoin.myEndpointId, debate_entity!!.path_to_root)
            }
        })
        return binding.root
    }

    override fun onAttach(context: Context) {
        Log.i("NewArgDialogFragment", "onAttach")
        super.onAttach(context)
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i("NewArgDialogFragment", "onCreateDialog")
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModelJoin = activity?.run {
            ViewModelProviders.of(this).get(JoinDebateViewModel::class.java)
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
                    Log.i("NewArgDialogFragment", "positivebutton")
                    dialogListener.onDialogPositiveClick(this)
                }
                .setNegativeButton(R.string.new_arg_dialogue_cancel)
                { dialog, id ->
                    getDialog()?.cancel()
                }
            if (title != null) {
                dialogView.findViewById<EditText>(R.id.new_arg_dialog_title)
                    .setText(title.toString(), TextView.BufferType.EDITABLE)
            }
            if (description != null) {
                dialogView.findViewById<EditText>(R.id.new_arg_dialog_description)
                    .setText(description.toString(), TextView.BufferType.EDITABLE)
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
        fun newInstance(title: String, description: String, side: Int = -1, place: Int = -1) =
            NewArgDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DESCRIPTION, description)
                    putInt(ARG_SIDE, side)
                    putInt(ARG_PLACE, place)
                }
            }
    }
}
