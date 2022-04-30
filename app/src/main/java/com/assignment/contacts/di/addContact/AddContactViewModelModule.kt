package com.assignment.contacts.di.addContact

import androidx.lifecycle.ViewModel
import com.assignment.contacts.di.ViewModelKey
import com.assignment.contacts.ui.addContact.AddContactViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddContactViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddContactViewModel::class)
    abstract fun bindAddContactViewModel(addContactViewModel: AddContactViewModel): ViewModel

}