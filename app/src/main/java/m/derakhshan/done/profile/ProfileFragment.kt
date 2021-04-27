package m.derakhshan.done.profile

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.co.derakhshan.imagepickerlib.ImagePicker
import m.derakhshan.done.Arrange
import m.derakhshan.done.R
import m.derakhshan.done.Utils
import m.derakhshan.done.databinding.FragmentProfileBinding
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {


    //-------------------------(global variables)-----------------------//
    private lateinit var binding: FragmentProfileBinding


    //-------------------------(injection)-----------------------//
    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var arrange: Arrange


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-------------------------(init variables)-----------------------//
        binding.name.setText(utils.userNameFamily)
        /*binding.phone.setText(arrange.persianConverter(utils.userPhone))
        binding.email.setText(utils.userEmail)*/
        binding.picture.setImageURI(Uri.parse(utils.userImage))


        //-------------------------(bindings)-----------------------//
        binding.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(name: Editable?) {
                utils.userNameFamily = name.toString()
            }

        })
        /*binding.phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(name: Editable?) {
                utils.userPhone = name.toString()
            }

        })
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(name: Editable?) {
                utils.userEmail = name.toString()
            }

        })
        binding.phone.setOnFocusChangeListener { _, b ->
            if (!b) binding.phone.setText(arrange.persianConverter(utils.userPhone))
        }*/
        binding.editPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .maxResultSize(1080, 1080)
                .start(startForImage)
        }
        binding.picture.setOnClickListener {
            val uri = Bundle()
            uri.putString("uri", utils.userImage)
            uri.putString("name", utils.userNameFamily)
            it.findNavController().navigate(R.id.action_profileFragment2_to_zoomImageFragment2, uri)
        }
        binding.version.text = arrange.persianConcatenate(first = "نسخه ی ", end = "1.0")
        binding.name.onFocusChangeListener = View.OnFocusChangeListener { _, focus ->
            binding.name.isCursorVisible = focus
        }

    }


    private val startForImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.picture.setImageURI(uri)
                    utils.userImage = uri.toString()
                }


            }
        }


}