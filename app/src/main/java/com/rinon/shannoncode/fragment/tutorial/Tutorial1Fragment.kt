package com.rinon.shannoncode.fragment.tutorial

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R

class Tutorial1Fragment : Fragment() {

    companion object {
        fun newInstance(): Tutorial1Fragment {
            return Tutorial1Fragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tutorial_1, container, false)
    }
}