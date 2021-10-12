package com.example.permissionapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissionapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val REQUEST_LOCATION_CODE = 2
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        onClickbtn()
    }

    private fun onClickbtn() {
        binding.button.setOnClickListener{
            requestLocationPermission()
        }
    }


    private fun requestLocationPermission(){
        when{
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getLastKnownLocation()
            }
            else -> {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(applicationContext, getText(R.string.start_gps), Toast.LENGTH_LONG).show()
                }
                else {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION_CODE)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_LOCATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first()==PackageManager.PERMISSION_GRANTED){
                    getLastKnownLocation()
                }
            }
            else -> {}
        }
    }


    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener{ task ->
            if (task.isSuccessful && task.result != null){
                Snackbar.make(binding.root,"N: ${task.result.longitude  } E:${task.result.latitude}",Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(binding.root,"Abbiamo scontrato un poblema",Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
