package com.lws.permissionapp.ui.home

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lws.permissionapp.R
import com.lws.permissionapp.databinding.FragmentHomeBinding
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
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PermissionX.defaultConfig.apply {
            //设置权限解释弹窗AlertDialog主题(可选,默认为宿主Activity的主题中的AlertDialogTheme)
            alertDialogTheme = R.style.MyAlertDialogTheme
            //设置权限解释弹窗位置(可选,默认为Gravity.CENTER)
            gravity = Gravity.BOTTOM
        }
        binding.button1.setOnClickListener {
            PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA)
                .onRequestRationale("解释请求权限的原因")
                .onDeniedRationale("拒绝了权限，进行解释，同意后将再次请求权限") {
                    Log.e("ssss", "不认可拒绝解释，可以退出")
                }
                .onDeniedForeverRationale("永久拒绝了权限，进行解释，同意将跳转设置界面让用户自己开启权限") {
                    Log.e("ssss", "不认可永久拒绝解释,可以退出")
                }
                .request { result ->
                    Log.e("ssss", "onResult: $result")
                }
        }


        binding.button2.setOnClickListener {
            AlertDialog.Builder(requireContext(), R.style.MyAlertDialogTheme)
                .setMessage("MMMMMMMMMM")
                .setNeutralButton("npnpnpnp", DialogInterface.OnClickListener { dialog, which -> })
                .setNegativeButton("nnnnnn", DialogInterface.OnClickListener { dialog, which ->

                }).setPositiveButton("PPPPP", DialogInterface.OnClickListener { dialog, which -> })
                .show().window?.setGravity(Gravity.BOTTOM)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}