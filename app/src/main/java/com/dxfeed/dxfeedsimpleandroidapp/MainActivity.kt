package com.dxfeed.dxfeedsimpleandroidapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.dxfeed.dxfeedsimpleandroidapp.adapters.LogViewAdapter
import com.dxfeed.dxfeedsimpleandroidapp.databinding.ActivityMainBinding
import com.dxfeed.dxfeedsimpleandroidapp.extensions.splitSymbols
import com.dxfeed.dxfeedsimpleandroidapp.tools.QDService
import com.dxfeed.dxfeedsimpleandroidapp.tools.Speedometer
import com.dxfeed.dxfeedsimpleandroidapp.tools.UiLogger

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val uiLogger = UiLogger(500)
    private val speedometer = Speedometer(5000, uiLogger)
    private val qdService = QDService(speedometer, uiLogger)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        findViewById<Button>(R.id.testTnSSubscriptionButton).setOnClickListener {
            val address = findViewById<EditText>(R.id.editTextAddress).text.toString()
            val symbols = findViewById<EditText>(R.id.editTextSymbols).text.toString()
            val timeout =
                findViewById<EditText>(R.id.editTextTimeout).text.toString().toLongOrNull() ?: 0L

            qdService.testTnsSubscription(address, symbols.splitSymbols(), timeout)
        }

        val logView: RecyclerView = findViewById(R.id.logView)

        logView.adapter = uiLogger.adapter
        uiLogger.setView(logView)

        System.setProperty("scheme", "com.dxfeed.api.impl.DXFeedScheme")
        speedometer.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}