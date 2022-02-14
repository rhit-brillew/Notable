package edu.rosehulman.notable.ui.notes

import android.widget.MediaController
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.notable.R
import edu.rosehulman.notable.databinding.FragmentAddTabBinding
import edu.rosehulman.notable.databinding.FragmentNoteDetailBinding
import edu.rosehulman.notable.models.NoteViewModel
import java.io.File
import kotlin.concurrent.fixedRateTimer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var model: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model =
            ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        updateView()
        setHasOptionsMenu(true)

        val videoUri = model.getCurrentNote().videoUrl
        if (videoUri != "") {
            val player = ExoPlayer.Builder(context!!).build()
            binding.videoView.player = player
            val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }

        return binding.root
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.edit_note -> {
            Log.d("NTBLE", "Test")
            findNavController().navigate(R.id.nav_note_edit)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun updateView() {
        binding.detailTitleText.text = model.getCurrentNote().title
        binding.detailDescriptionText.text = model.getCurrentNote().description
        binding.tabText.text = model.getCurrentNote().tab
    }
}