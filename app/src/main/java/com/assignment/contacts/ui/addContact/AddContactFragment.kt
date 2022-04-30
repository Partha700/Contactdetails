package com.assignment.contacts.ui.addContact


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.assignment.contacts.MainActivity

import com.assignment.contacts.R
import com.assignment.contacts.data.Contact
import com.assignment.contacts.databinding.FragmentAddContactBinding
import com.assignment.contacts.di.ViewModelProviderFactory
import com.assignment.contacts.util.OPTIONS
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class AddContactFragment : DaggerFragment() {

    val REQUEST_IMAGE = 100
    var profilePicturePath: String? = null

    private lateinit var  binding: FragmentAddContactBinding

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var viewModel: AddContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_contact, container, false)

        binding.setLifecycleOwner(this.viewLifecycleOwner)

        viewModel = ViewModelProviders.of(this, factory).get(AddContactViewModel::class.java)

        val args = AddContactFragmentArgs.fromBundle(arguments!!)

        if (args.id != -1){
            val contact = viewModel.getContactById(args.id)
            contact.observe(this, Observer {
                binding.contact = it
            })
        }

        binding.firstNameEditText.doAfterTextChanged {
            viewModel.firstName = it.toString()
        }

        binding.lastNameEditText.doAfterTextChanged {
            viewModel.lastName = it.toString()
        }

        binding.phoneNumberEditText.doAfterTextChanged {
            viewModel.phoneNumber = it.toString()
        }

        binding.emailEditText.doAfterTextChanged {
            viewModel.email = it.toString()
        }

        binding.uploadProfilePictureImageView.setOnClickListener{
            selectProfilePicture()
        }

        binding.saveButton.setOnClickListener {
            if (args.id == -1) {
                Timber.d("Hello this contact name is ${viewModel.firstName} ${viewModel.lastName}")
                viewModel.saveContact(Contact(
                    viewModel.firstName,
                    viewModel.lastName,
                    viewModel.phoneNumber,
                    viewModel.email,
                    profilePicturePath))
                it.findNavController().navigate(AddContactFragmentDirections
                    .actionAddContactFragmentToContactsFragment())
            } else {
                Timber.d("Hello this contact name is ${viewModel.firstName}")
                viewModel.updateContact(Contact(
                    viewModel.firstName,
                    viewModel.lastName,
                    viewModel.phoneNumber,
                    viewModel.email,
                    profilePicturePath,
                    args.id))
                Timber.d("Hello image is ${profilePicturePath}")

                it.findNavController().navigate(AddContactFragmentDirections
                    .actionAddContactFragmentToContactsFragment())
            }
        }

        binding.cancelButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            // handling back button
            findNavController().navigate(AddContactFragmentDirections.actionAddContactFragmentToContactsFragment(), OPTIONS)
        }

        return binding.root
    }

    fun selectProfilePicture(){
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(activity!!.packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK){
            val imageUri = data?.data
            Timber.d("URI ${imageUri}")

            profilePicturePath = imageUri.toString()
            Timber.d("image path ***** ${profilePicturePath}")
            binding.uploadProfilePictureImageView.setImageURI(data?.data)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Add New Contact"
    }
}
