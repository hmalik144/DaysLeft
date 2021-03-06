package com.appttude.h_mal.days_left.ui.main.tools

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.appttude.h_mal.days_left.BuildConfig
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.utils.navigateTo
import com.appttude.h_mal.days_left.utils.safeFirebaseResult
import com.appttude.h_mal.days_left.utils.showToast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_tools.*
import java.io.File
import java.util.*

class FragmentTools : Fragment() {

    private val TAG = "FragmentTools"

    val mFunctions = FirebaseFunctions.getInstance()
    val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tools, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compile.setOnClickListener(onClickListener)
        summary_button.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        requestPermissions().let {
            if (it) {
                if (view.id == R.id.compile){
                    writeToExcel().addOnCompleteListener(complete)
                }else if(view.id == R.id.summary_button){
                    writeToExcelVisa().addOnCompleteListener(complete)
                }
            }
        }

    }

    private val complete = OnCompleteListener<String>{ task ->
        task.safeFirebaseResult({
            val savePath = Environment.getExternalStorageDirectory().toString() + "/DaysLeftTemp"
            val file = File(savePath)
            if (!file.exists()) {
                file.mkdirs()
            }
            it ?: return@safeFirebaseResult
            val fbstore = storage.reference.child(it)
            val strings = it.split("/").toTypedArray()
            val myFile = File(savePath, strings.last())

            fbstore.getFile(myFile).safeFirebaseResult({
                // Local temp file has been created
                val data =
                    FileProvider.getUriForFile(
                        requireContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        myFile
                    )

                activity?.grantUriPermission(
                    activity?.packageName,
                    data,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val intent1 = Intent(Intent.ACTION_VIEW)
                    .setDataAndType(data, "application/vnd.ms-excel")
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                try {
                    activity?.startActivity(intent1)
                } catch (e: ActivityNotFoundException) {
                    context?.showToast("No Application Available to View Excel")
                }
            },{

            },{

            })
        },{
            context?.showToast(it)
        },{
        })

//        if (!task.isSuccessful) {
//
//
//            val e = task.exception
//            if (e is FirebaseFunctionsException) {
//                val ffe = e as FirebaseFunctionsException?
//                val code = ffe!!.code
//                val details = ffe.details
//            }
//
//            Log.w(TAG, "addMessage:onFailure", e)
//            Toast.makeText(context, "An error occurred.", Toast.LENGTH_SHORT).show()
//
//        }else{
//            // [START_EXCLUDE]
//
//            val result = task.result as String
//            Log.i(TAG, "onComplete: $result")
//
//            val strings = result.split("/").toTypedArray()
//
//            val fbstore = storage.reference.child(result)
//
//            val savePath = Environment.getExternalStorageDirectory().toString() + "/DaysLeftTemp"
//            val file = File(savePath)
//            if (!file.exists()) {
//                file.mkdirs()
//            }
//
//            val myFile = File(savePath, strings.last())
//
//            fbstore.getFile(myFile).addOnSuccessListener {
//                // Local temp file has been created
//                val data =
//                    FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", myFile)
//                activity?.grantUriPermission(
//                    activity?.getPackageName(),
//                    data,
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION
//                )
//                val intent1 = Intent(Intent.ACTION_VIEW)
//                    .setDataAndType(data, "application/vnd.ms-excel")
//                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//                try {
//                    activity?.startActivity(intent1)
//                } catch (e: ActivityNotFoundException) {
//                    Toast.makeText(activity, "No Application Available to View Excel", Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//
//            }
//        }
    }

    fun writeToExcel(): Task<String> {
        // Create the arguments to the callable function.
        val data = HashMap<String, Any>()
        data["push"] = true

        return mFunctions
            .getHttpsCallable("writeFireToExcel")
            .call(data)
            .continueWith{ task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.

                val result = task.result?.data as String

                Log.i(TAG, "then: " + result)

                result
            }
    }

    fun writeToExcelVisa(): Task<String> {
        // Create the arguments to the callable function.
        val data = HashMap<String, Any>()
        data["push"] = true

        return mFunctions
            .getHttpsCallable("writeFireToExcelVisa")
            .call(data)
            .continueWith{ task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                //todo: change to file
                val result = task.result?.data as String

                Log.i(TAG, "then: " + result)

                result
            }
    }

    fun requestPermissions() : Boolean{
        if (checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context,"not granted",Toast.LENGTH_SHORT).show()

            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                443)

            return false
        }else{

            return true
        }


    }
}
