package com.apm.jacx

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.apm.jacx.client.ApiClient
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.model.Route
import com.apm.jacx.util.Util
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JoinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JoinFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startQRScanner()
            } else {
                Toast.makeText(context, "Permiso de la camara denegado", Toast.LENGTH_LONG).show()
            }
        }

    private val qrScannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val intentResult: IntentResult? =
                IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            if (intentResult != null) {
                if (intentResult.contents == null) {
                    // Si se cancela el escaneo
                    // Aquí puedes realizar alguna acción adicional si deseas
                } else {
                    // Si se obtiene la lectura del código QR
                    val qrData = intentResult.contents
                    // Aquí puedes realizar alguna acción con los datos del QR
                    val inputPIN = view?.findViewById<EditText>(R.id.pinInput)
                    inputPIN?.setText(qrData)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.showUpButton()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchByPinButton = view.findViewById<ImageView>(R.id.searchPin)
        searchByPinButton.setOnClickListener { onSearchBtPinButtonClick() }

        val qrButton = view.findViewById<Button>(R.id.qrButton)
        qrButton.setOnClickListener { onQrButtonClick() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.hideUpButton()
        lifecycleScope.cancel() // Cancelar todas las corutinas
    }

    private fun onSearchBtPinButtonClick() {
        val pinEditText = view?.findViewById<EditText>(R.id.pinInput);
        val pin = pinEditText?.text.toString()
        if (pin.isNotBlank()) {
            // Obtenemos todas las rutas y seleccionamos la correcta
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    activity?.let { Util.hideKeyboard(it) }
                    pinEditText?.setText("")
                    pinEditText?.clearFocus()

                    val loginBtn = view?.findViewById<ImageView>(R.id.searchPin)
                    loginBtn?.visibility = View.INVISIBLE
                    val spinner = view?.findViewById<ProgressBar>(R.id.pin_spinner)
                    spinner?.visibility = View.VISIBLE

                    val responseGet = ApiClient.get("/routes")
                    val routesArray = Gson().fromJson(responseGet, Array<Route>::class.java)
                    var routeName = ""
                    routesArray.forEach { el ->
                        val pin2 = Util.generatePIN(el.name)
                        if (pin == pin2) {
                            Log.d("ROUTA ENCONTRADA", el.name)
                            routeName = el.name
                        }
                    }
                    if (routeName.isBlank()) {
                        Toast.makeText(
                            context,
                            "NO SE HA ENCONTRADO NINGUNA RUTA",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        postUserInRoute(routeName)
                    }

                } catch (e: IOException) {
                    // Manejar errores de red aquí
                    Log.d("Error de red", e.toString())
                    Toast.makeText(context, "Datos de acceso incorrectos", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    // Manejar otros errores aquí
                    Log.d("Error en la peticion", e.toString())
                    Toast.makeText(
                        context,
                        "Datos de acceso incorrectos",
                        Toast.LENGTH_LONG
                    ).show()

                } finally {
                     val loginBtn = view?.findViewById<ImageView>(R.id.searchPin)
                     loginBtn?.visibility = View.VISIBLE
                     val spinner = view?.findViewById<ProgressBar>(R.id.pin_spinner)
                     spinner?.visibility = View.INVISIBLE
                }
            }
        }
    }

    private suspend fun postUserInRoute(routeName: String) {
        val userInformation: JsonObject =
            Gson().fromJson(AppPreferences.USER_INFORMATION, JsonObject::class.java)

        val username = userInformation.get("username").asString
        val jsonBody = JSONObject().apply {
            put("username", username)
            put("routeName", routeName)
        }.toString()
        ApiClient.post("/route/user", jsonBody)
        Toast.makeText(context, "Añadido $username a la ruta $routeName", Toast.LENGTH_LONG).show()
    }

    private fun onQrButtonClick() {
        Toast.makeText(context, "Escaneo de QR", Toast.LENGTH_SHORT).show();
        // Verificar y solicitar permiso de cámara si es necesario
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startQRScanner()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startQRScanner() {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea un código QR")
        integrator.setCameraId(0)  // Usar la cámara trasera
        integrator.setBeepEnabled(false)  // Desactivar el sonido de escaneo
        qrScannerLauncher.launch(integrator.createScanIntent())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JoinFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JoinFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}