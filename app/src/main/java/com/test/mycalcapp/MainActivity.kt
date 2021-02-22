package com.test.mycalcapp


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.test.mycalcapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val tag = "MainActivity"
    private var isBoundToCalculatorService = false
    private lateinit var calculatorService: CalcService
    private val calculatorServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            /*
             * This is called when the connection with the service has been
             * established, giving us the object we can use to interact with
             * the service.
             */
            logMessage("Connected to CalcService")
            isBoundToCalculatorService = true
            val binder = iBinder as CalcService.LocalBinder
            calculatorService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // This is called when the connection with the service has been disconnected
            logMessage("Disconnected from CalculatorService")
            isBoundToCalculatorService = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home"
    }

    override fun onStart() {
        super.onStart()
        Intent(this, CalcService::class.java).also { intent ->
            bindService(intent, calculatorServiceConnection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onStop() {
        super.onStop()
        if (isBoundToCalculatorService) {
            unbindService(calculatorServiceConnection)
        }

    }

    fun onClick(view: View?) {

        val operand1 = binding.operandOneEditText.text.toString().toInt()
        val operand2 = binding.operandTwoEditText.text.toString().toInt()

        if (isBoundToCalculatorService) {
            binding.resultTextView.setText(
                when (view?.id) {
                    R.id.addButton -> calculatorService.add(operand1, operand2).toString()
                    R.id.subtractButton -> calculatorService.subtract(operand1, operand2).toString()
                    R.id.multiplyButton -> calculatorService.multiply(operand1, operand2).toString()
                    R.id.divideButton -> calculatorService.divide(operand1, operand2).toString()
                    else -> "Unknown value"
                }
            )
        } else {
            logMessage("Not bound to CalculatorService. This should not happen!")
        }

    }

    private fun logMessage(message: String) {
        Log.d(tag, message)
    }
}