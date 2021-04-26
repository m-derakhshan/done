package m.derakhshan.done

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import m.derakhshan.done.databinding.FragmentZoomImageBinding


class ZoomImageFragment : Fragment() {


    //-------------------------(global variables)-----------------------//
    private var doubleZoomed = true
    private lateinit var binding: FragmentZoomImageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zoom_image, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-------------------------(getting values passed by bundle)-----------------------//
        val uri = arguments?.getString("uri") ?: ""
        binding.title.text = arguments?.getString("name") ?: ""


        //-------------------------(binding functions)-----------------------//
        binding.image.setImageURI(Uri.parse(uri))
        binding.back.setOnClickListener {
            it.findNavController().navigateUp()
        }
        binding.image.setOnClickListener(object : OnDoubleTabZoom() {
            override fun onDoubleClick(v: View?) {
                if (doubleZoomed)
                    binding.zoom.zoomTo(2F, true)
                else
                    binding.zoom.zoomTo(-2F, true)
                doubleZoomed = !doubleZoomed
            }
        })

    }

    override fun onPause() {
        super.onPause()
        doubleZoomed = true
    }


    //-------------------------(recognizing double tap)-----------------------//
    private abstract class OnDoubleTabZoom : View.OnClickListener {

        private val doubleTabTime: Long = 300
        var lastClickTime: Long = 0

        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < doubleTabTime)
                onDoubleClick(v)
            lastClickTime = clickTime
        }

        abstract fun onDoubleClick(v: View?)
    }
}