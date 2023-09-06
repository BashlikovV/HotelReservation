package by.bashlikovvv.hotelreservation.presentation.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import by.bashlikovvv.domain.model.Item
import by.bashlikovvv.domain.model.TouristInfo
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.ExpandableContentLayoutBinding
import com.google.android.material.textfield.TextInputEditText
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class TouristsInfoAdapter(
    private val context: Context,
    private val updateTouristCallback: (TouristInfo) -> Unit
) {

    fun touristsInfoAdapter(): AdapterDelegate<List<Item>> =
        adapterDelegateViewBinding<TouristInfo, Item, ExpandableContentLayoutBinding>(
            { layoutInflater, parent ->
                ExpandableContentLayoutBinding.inflate(layoutInflater, parent, false)
            }
        ) {

            bind {
                if (item.isEmpty()) return@bind
                if (item.expanded != binding.expandableLayout.isExpanded()) {
                    binding.arrowImageView.callOnClick()
                }
                binding.expandableLayout.addOnClickListener {
                    updateTouristCallback.invoke(item.copy(expanded = it))
                }
                binding.touristCount.text = item.name
                addNameTextChangedListener(binding.expandableLayoutContent, item)
                addSurnameTextChangedListener(binding.expandableLayoutContent, item)
                addBirthDateTextChangedListener(binding.expandableLayoutContent, item)
                addNameCitizenshipTextChangedListener(binding.expandableLayoutContent, item)
                addPassportNumberTextChangedListener(binding.expandableLayoutContent, item)
                addValidityPeriodTextChangedListener(binding.expandableLayoutContent, item)
            }
        }

    private fun addValidityPeriodTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.validityPeriod).apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            background = getBackground(touristInfo.hasError && touristInfo.validityPeriod.isBlank())
            text?.append(touristInfo.validityPeriod)
            addTextChangedListener {
                updateTouristCallback.invoke(touristInfo.copy(validityPeriod = it.toString()))
                background = getBackground(touristInfo.hasError && touristInfo.validityPeriod.isBlank())
            }
        }
    }

    private fun addPassportNumberTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.passportNumber).apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            background = getBackground(touristInfo.hasError && touristInfo.passportNumber.isBlank())
            text?.append(touristInfo.passportNumber)
            addTextChangedListener {
                updateTouristCallback.invoke(touristInfo.copy(passportNumber = it.toString()))
                background = getBackground(touristInfo.hasError && touristInfo.passportNumber.isBlank())
            }
        }
    }

    private fun addNameCitizenshipTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.citizenship).apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            background = getBackground(touristInfo.hasError && touristInfo.citizenship.isBlank())
            text?.append(touristInfo.citizenship)
            addTextChangedListener {
                updateTouristCallback.invoke(touristInfo.copy(citizenship = it.toString()))
                background = getBackground(touristInfo.hasError && touristInfo.citizenship.isBlank())
            }
        }
    }

    private fun addBirthDateTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.date_of_birth).apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            background = getBackground(touristInfo.hasError && touristInfo.dateOfBirth.isBlank())
            text?.append(touristInfo.dateOfBirth)
            addTextChangedListener {
                updateTouristCallback.invoke(touristInfo.copy(dateOfBirth = it.toString()))
                background = getBackground(touristInfo.hasError && touristInfo.dateOfBirth.isBlank())
            }
        }
    }

    private fun addSurnameTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.surname).apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            background = getBackground(touristInfo.hasError && touristInfo.surname.isBlank())
            text?.append(touristInfo.surname)
            addTextChangedListener {
                updateTouristCallback.invoke(touristInfo.copy(surname = it.toString()))
                background = getBackground(touristInfo.hasError && touristInfo.surname.isBlank())
            }
        }
    }

    private fun addNameTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.name).apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            background = getBackground(touristInfo.hasError && touristInfo.inputName.isBlank())
            text?.append(touristInfo.inputName)
            addTextChangedListener {
                updateTouristCallback.invoke(touristInfo.copy(inputName = it.toString()))
                background = getBackground(touristInfo.hasError && touristInfo.inputName.isBlank())
            }
        }
    }

    private fun getBackground(flag: Boolean): Drawable? {
        return if(flag) {
            ResourcesCompat.getDrawable(context.resources, R.drawable.te_error_backgroud, context.resources.newTheme())
        } else {
            ResourcesCompat.getDrawable(context.resources, R.drawable.te_background, context.resources.newTheme())
        }
    }
}