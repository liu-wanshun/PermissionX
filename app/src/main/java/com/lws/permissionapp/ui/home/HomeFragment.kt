package com.lws.permissionapp.ui.home

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lws.permissionapp.CustomCallback
import com.lws.permissionapp.databinding.FragmentHomeBinding
import com.lws.permissionx.PermissionRequester
import com.lws.permissionx.PermissionX

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button1.setOnClickListener {
            PermissionX.init(this)
                .permission(Manifest.permission.CAMERA)
                .onRequestRationale("解释请求权限的原因")
                .request { result: Boolean ->
                    Log.e(
                        "ssss",
                        "onResult: $result"
                    )
                }
        }


        binding.button2.setOnClickListener {
            PermissionX.init(this)
                .permission(Manifest.permission.CAMERA)
                .onRequestRationale("解释请求权限的原因")
                .transform { permissionRequester: PermissionRequester<String?, Boolean?>? ->
                    CustomCallback(
                        permissionRequester
                    )
                }
                .request { allGranted: Boolean, denyForever: Boolean ->
                    Log.e(
                        "ssss",
                        "onResult: $allGranted  $denyForever"
                    )
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}