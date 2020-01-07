package com.dicoding.made.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {

    private var myactivity: BaseActivity? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) myactivity = context
    }

    override fun onDetach() {
        super.onDetach()
        myactivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext[Job]?.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(getLayoutId(),container,false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    fun showSnackBarFragment(message: Int) = myactivity?.showSnackBar(message)

    abstract fun getLayoutId(): Int
    abstract fun setupFragment()
}