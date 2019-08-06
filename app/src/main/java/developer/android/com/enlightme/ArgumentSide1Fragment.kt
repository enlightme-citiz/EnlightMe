package developer.android.com.enlightme

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import developer.android.com.enlightme.databinding.FragmentArgumentSide1Binding
import kotlinx.android.synthetic.main.fragment_argument_side1.*
import android.view.View.OnLongClickListener




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TITLE = "title"
private const val DESCRIPTION = "description"
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ArgumentSide1Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ArgumentSide1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ArgumentSide1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = null
    private var description: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.title = it.getString(TITLE)
            this.description = it.getString(DESCRIPTION)
            // put argument title to the body of the argument icon
            //argument_side1_text.text = title
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentArgumentSide1Binding>(inflater, R.layout.fragment_argument_side1, container, false)
        binding.argumentSide1Text.text = this.title
        //Attache click event listener to display the Add argument dialogue box
        view?.setOnLongClickListener{
            // Long click listener function to edit the argument
            val title = this.title
            val description = this.description
            val newArgDialogueFragment = NewArgDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("description", description)
                }
            }
            val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
            newArgDialogueFragment.show(fm, "editArgument")
            true
        }
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
         * @return A new instance of fragment ArgumentSide1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String, description: String) =
            ArgumentSide1Fragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(DESCRIPTION, description)
                }
            }
    }
}
