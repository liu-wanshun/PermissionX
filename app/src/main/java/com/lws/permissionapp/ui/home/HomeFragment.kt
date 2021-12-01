package com.lws.permissionapp.ui.home

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lws.permissionapp.CustomCallback
import com.lws.permissionapp.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionX.getDefaultConfig()
            //设置权限解释弹窗AlertDialog主题(可选,默认为宿主Activity的主题中的AlertDialogTheme)
            .setAlertDialogTheme(R.style.MyAlertDialogTheme)
    }


    var isInRequest = false
    override fun onStart() {
        super.onStart()
        if (testDialog != null || isInRequest) {
            return
        }
        isInRequest = true
        PermissionX.init(this)
            .permission(Manifest.permission.CAMERA)
            .onRequestRationale("解释请求权限的原因,ON_PAUSE时自动Dismiss")
            .request {
                isInRequest = false
                if (it) {
                    testDialog = null
                    Log.e("TAG", "PermissionX: 得到权限")
                } else {

                    if (testDialog == null) {
                        testDialog = TestDialog()
                        testDialog!!.show(childFragmentManager, "ss")
                    }

                }
            }
    }


    var testDialog: TestDialog? = null

    class TestDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(requireContext(),R.style.MyAlertDialogTheme)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setTitle("标题")
                .setMessage("缺少权限")
                .setNegativeButton("退出") { dialog, which ->
                    requireActivity().finish()
                }.create()
        }

        override fun onStart() {
            super.onStart()
            dialog?.window?.setGravity(Gravity.BOTTOM)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}