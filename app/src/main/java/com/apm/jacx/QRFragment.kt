package com.apm.jacx

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.apm.jacx.util.Util
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "routeName"

/**
 * A simple [Fragment] subclass.
 * Use the [QRFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QRFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var routeName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routeName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_r, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {

            val qrImageView = view.findViewById<ImageView>(R.id.qrImageView)

            val pin = routeName?.let { Util.generatePIN(it) }
            val bitmap = pin?.let { generateQRCode(it) }
            qrImageView.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

    @Throws(WriterException::class)
    private fun generateQRCode(myString: String): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            myString,
            BarcodeFormat.QR_CODE,
            1000,
            1000,
            null
        )

        val width: Int = bitMatrix.width
        val height: Int = bitMatrix.height
        val pixels = IntArray(width * height)

        // Configurar los colores para el código QR
        val whiteColor = Color.WHITE
        val blackColor = Color.BLACK

        // Generar el bitmap del código QR
        for (y in 0 until height) {
            val offset: Int = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix[x, y]) blackColor else whiteColor
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

        return bitmap
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QRFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            QRFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}