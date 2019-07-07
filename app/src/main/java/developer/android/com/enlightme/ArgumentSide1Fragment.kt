package developer.android.com.enlightme

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import developer.android.com.enlightme.objects.DebatEntity
import kotlinx.android.synthetic.main.fragment_argument_side1.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TITLE = "title"
private const val ID_ARG = "idArg"
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
    private var id_arg: Int? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.title = it.getString(TITLE)
            this.id_arg = it.getInt(ID_ARG)
            // put argument title to the body of the argument icon
            argument_side1_text.text = title
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_argument_side1, container, false)
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
        fun newInstance(title: String, id_arg: Int) =
            ArgumentSide1Fragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putInt(ID_ARG, id_arg)
                }
            }
    }
}
