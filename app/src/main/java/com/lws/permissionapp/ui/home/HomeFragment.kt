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
import androidx.lifecycle.Lifecycle
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
            //设置权限解释弹窗AlertDialog主题(可选)
            .setAlertDialogTheme(R.style.Theme_Material3_DayNight_Dialog_Alert)
            //设置权限解释弹窗位置(可选)
            .setGravity(Gravity.BOTTOM)
            //设置权限解释自动Dismiss时机(可选)
            .setAutoDismiss(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStart() {
        super.onStart()
        if (testDialog != null) {
            return
        }
        PermissionX.init(this)
            .permission(Manifest.permission.CAMERA)
            .onRequestRationale("解释请求权限的原因,ON_PAUSE时自动Dismiss", Lifecycle.Event.ON_PAUSE)
            .request {
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
            return AlertDialog.Builder(requireContext())
                .setMessage("缺少权限")
                .setNegativeButton("退出") { dialog, which ->
                    requireActivity().finish()
                }.create()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}