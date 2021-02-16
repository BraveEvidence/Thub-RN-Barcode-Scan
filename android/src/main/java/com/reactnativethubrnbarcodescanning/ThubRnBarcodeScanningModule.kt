package com.reactnativethubrnbarcodescanning

import android.net.Uri
import com.facebook.react.bridge.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.io.File
import java.io.IOException
import java.lang.StringBuilder


class ThubRnBarcodeScanningModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  private var barcodeScanningSuccessCallback: Callback? = null
  private var barcodeScanningFailureCallback: Callback? = null
  private val options = BarcodeScannerOptions.Builder()
    .setBarcodeFormats(
      Barcode.FORMAT_QR_CODE,
      Barcode.FORMAT_AZTEC, Barcode.FORMAT_CODE_39,
      Barcode.FORMAT_CODE_128, Barcode.FORMAT_CODE_93, Barcode.FORMAT_CODABAR, Barcode.FORMAT_EAN_13, Barcode.FORMAT_EAN_8,
      Barcode.FORMAT_ITF, Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E, Barcode.FORMAT_DATA_MATRIX, Barcode.FORMAT_PDF417,
    )
    .build()

  override fun getName(): String {
    return "ThubRnBarcodeScanning"
  }

  @ReactMethod
  fun scanBarcode(uri: String, successCallback: Callback, failureCallback: Callback) {
    barcodeScanningSuccessCallback = successCallback
    barcodeScanningFailureCallback = failureCallback

    val image: InputImage
    try {
      val file = File(uri)
      image = InputImage.fromFilePath(reactContext, Uri.parse(file.toString()))
      val scanner = BarcodeScanning.getClient(options)
      scanner.process(image)
        .addOnSuccessListener { barcodes ->
          if (barcodes.size > 0) {
            val barcode = barcodes[0]
//            for (barcode in barcodes) {
//            val bounds = barcode.boundingBox
//            val corners = barcode.cornerPoints

//              val rawValue: String? = barcode?.rawValue


            when (barcode.valueType) {
              Barcode.TYPE_WIFI -> {
                val ssid = barcode.wifi?.ssid
                val password = barcode.wifi?.password
                val type = barcode.wifi?.encryptionType
                barcodeScanningSuccessCallback?.invoke("$ssid $password $type")
              }
              Barcode.TYPE_URL -> {
                val title = barcode.url?.title
                val url = barcode.url?.url
                barcodeScanningSuccessCallback?.invoke("$title $url ")
              }
              Barcode.TYPE_UNKNOWN -> {
                barcodeScanningFailureCallback?.invoke("Unknown barcode type")
              }
              Barcode.TYPE_CONTACT_INFO -> {
                val title = barcode.contactInfo?.title
                val address = barcode.contactInfo?.addresses
                barcodeScanningSuccessCallback?.invoke("$title $address ")
              }
              Barcode.TYPE_EMAIL -> {
                val title = barcode.email?.address
                barcodeScanningSuccessCallback?.invoke("$title")
              }
              Barcode.TYPE_ISBN -> {
                val title = barcode.rawValue
                barcodeScanningSuccessCallback?.invoke(title?.toString() ?: "")
              }
              Barcode.TYPE_PHONE -> {
                val title = barcode.phone?.number
                barcodeScanningSuccessCallback?.invoke("$title")
              }
              Barcode.TYPE_PRODUCT -> {
                val title = barcode.rawValue
                barcodeScanningSuccessCallback?.invoke(title?.toString() ?: "")
              }
              Barcode.TYPE_SMS -> {
                val title = barcode.sms?.message
                barcodeScanningSuccessCallback?.invoke("$title")
              }
              Barcode.TYPE_TEXT -> {
                val title = barcode.rawValue
                barcodeScanningSuccessCallback?.invoke(title?.toString() ?: "")
              }
              Barcode.TYPE_GEO -> {
                val title = barcode.geoPoint?.lat
                barcodeScanningSuccessCallback?.invoke("$title")
              }
              Barcode.TYPE_CALENDAR_EVENT -> {
                val title = barcode.calendarEvent?.description
                barcodeScanningSuccessCallback?.invoke("$title")
              }
              Barcode.TYPE_DRIVER_LICENSE -> {
                val title = barcode.driverLicense?.firstName
                barcodeScanningSuccessCallback?.invoke("$title")
              }
              else -> {
                val title = barcode.rawValue
                barcodeScanningSuccessCallback?.invoke(title?.toString() ?: "")
              }
            }
//            }
          } else {
            barcodeScanningFailureCallback?.invoke("No barcode detected")
          }

        }
        .addOnFailureListener {
          barcodeScanningFailureCallback?.invoke(it.message.toString())
        }

    } catch (e: IOException) {
      e.printStackTrace()
      barcodeScanningFailureCallback?.invoke(e.message.toString())
    }
  }


}
