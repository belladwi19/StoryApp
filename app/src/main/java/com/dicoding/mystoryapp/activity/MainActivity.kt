package com.dicoding.mystoryapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.commit
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.fragment.SettingStoryFragment
import com.dicoding.mystoryapp.fragment.StoryFragment
import com.dicoding.mystoryapp.util.clearDirectory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("story_app")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.app_bar)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                ALLOW_PERMISSION,
                CODE_ACCESS_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_ACCESS_PERMISSION) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                   R.string.no_permission,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDirectory(application)
    }

    private fun allPermissionsGranted() = ALLOW_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onOptionsItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.navigation_home -> {
                val intentActivity = Intent(this, MainActivity::class.java)
                startActivity(intentActivity)
            }

            R.id.navigation_new_story -> {
                supportFragmentManager.commit {
                    replace(R.id.activity_main, StoryFragment(), StoryFragment::class.java.simpleName)
                }
            }

            R.id.navigation_setting -> {
                supportFragmentManager.commit {
                    replace(R.id.activity_main, SettingStoryFragment(), SettingStoryFragment::class.java.simpleName)
                }
            }
        }
        return super.onOptionsItemSelected(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val DETAIL = "DETAIL"
        private const val CODE_ACCESS_PERMISSION = 10
        private val ALLOW_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }
}