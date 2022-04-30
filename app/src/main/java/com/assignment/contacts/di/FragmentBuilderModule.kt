package com.assignment.contacts.di

import com.assignment.contacts.di.addContact.AddContactViewModelModule
import com.assignment.contacts.di.contactDetails.ContactDetailsViewModelModule
import com.assignment.contacts.di.contactsList.ContactsListViewModelModule
import com.assignment.contacts.ui.addContact.AddContactFragment
import com.assignment.contacts.ui.contactDetails.ContactDetailsFragment
import com.assignment.contacts.ui.contactsList.ContactsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [ContactsListViewModelModule::class])
    abstract fun contributeContactsFragment() : ContactsListFragment

    @ContributesAndroidInjector(modules = [AddContactViewModelModule::class])
    abstract fun contributeAddContactFragment() : AddContactFragment

    @ContributesAndroidInjector(modules = [ContactDetailsViewModelModule::class])
    abstract fun contributeContactDetailsFragment() : ContactDetailsFragment
}