package journey.project.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import journey.project.R
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream


class SettingsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    fun bytes2Object(raw: ByteArray?): Any? {
        val bais = ByteArrayInputStream(raw)
        val ois = ObjectInputStream(bais)
        return ois.readObject()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val isChecked = sharedPref.getBoolean("isChecked", true)

        cbSuggestion.isChecked = isChecked

        cbSuggestion.setOnClickListener() {
            with (sharedPref.edit()) {
                putBoolean("isChecked", cbSuggestion.isChecked)
                commit()
            }
        }
    }
}
