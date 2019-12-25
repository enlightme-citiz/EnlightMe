package developer.android.com.enlightme.Debate

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import developer.android.com.enlightme.databinding.FragmentArgumentSide1Binding
import androidx.lifecycle.ViewModelProviders
import developer.android.com.enlightme.DebateViewModel
import developer.android.com.enlightme.NewArgDialogFragment
import developer.android.com.enlightme.R


private const val TITLE = "title"
private const val DESCRIPTION = "description"
private const val PLACE = "place"
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
    private var title: String? = null
    private var description: String? = null
    private var place: Int = -1
    private lateinit var viewModel: DebateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.title = it.getString(TITLE)
            this.description = it.getString(DESCRIPTION)
            this.place = it.getInt(PLACE)
            // put argument title to the body of the argument icon
            //argument_side1_text.text = title
        }
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DebateViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        if(place < 0){
            throw Exception("Place should be greater than 0")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentArgumentSide1Binding>(inflater,
            R.layout.fragment_argument_side1, container, false)
        binding.argumentSide1Text.text = this.title
        //Attache click event listener to display the Add argument dialogue box
        binding.root.setOnLongClickListener{
            // Long click listener function to edit the argument
            viewModel.temp_side = 1
            viewModel.edit_arg_pos = this.place
            this.title = viewModel.debate.value?.get_debate_entity()?.side_1_entity?.get(this.place)?.title
            this.description = viewModel.debate.value?.get_debate_entity()?.side_1_entity?.get(this.place)?.description
            val newArgDialogueFragment = NewArgDialogFragment.newInstance(
                this.title ?: "",
                this.description ?: "",
                1,
                this.place
            )
            val fm = activity?.supportFragmentManager ?: throw RuntimeException(context.toString() + " cannot be null")
            newArgDialogueFragment.show(fm, "editArgument")
            true
        }
        return binding.root
    }



    companion object {
        @JvmStatic
        fun newInstance(title: String, description: String, place: Int) =
            ArgumentSide1Fragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(DESCRIPTION, description)
                    putInt(PLACE, place)
                }
            }
    }
}
