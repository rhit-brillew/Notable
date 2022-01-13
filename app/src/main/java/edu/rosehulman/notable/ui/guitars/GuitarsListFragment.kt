package edu.rosehulman.notable.ui.guitars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.notable.databinding.FragmentGuitarsListBinding

class GuitarsListFragment : Fragment() {

    private lateinit var guitarViewModel: GuitarViewModel
    private var _binding: FragmentGuitarsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        guitarViewModel =
            ViewModelProvider(this).get(GuitarViewModel::class.java)

        _binding = FragmentGuitarsListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        guitarViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}