package vn.edu.stu.tranthanhsang.healthy_app.features.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.user.CreateProfileUseCase
import vn.edu.stu.tranthanhsang.healthy_app.features.user.uistate.CreateProfileUiState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val createProfileUseCase: CreateProfileUseCase
) : ViewModel() {
    private var _profileUiState = MutableLiveData<CreateProfileUiState>()
    val profileUiState: LiveData<CreateProfileUiState> get() = _profileUiState

    private var fullName: String = ""
    private var gender : Boolean = false
    private var yearOfBirth: Int = 0
    private var weight: Int = 0
    private var height: Int = 0

    fun updateFullName(name: String){
        this.fullName = name
    }

    fun updateYearOfBirth(year: Int){
        this.yearOfBirth = year
    }
    fun updateWeight(weight: Int){
        this.weight = weight
    }
    fun updateHeight(height: Int) {
        this.height = height
    }
    fun updateGender(gender: Boolean) {
        this.gender = gender
    }

    fun createProfile(){
        _profileUiState.value = CreateProfileUiState.Initial
        viewModelScope.launch(Dispatchers.IO) {
            _profileUiState.postValue(CreateProfileUiState.Loading)

            val uiState = when(val result = createProfileUseCase(fullName, yearOfBirth, height, weight, gender)){
                is Result.Success -> CreateProfileUiState.Success(result.data)
                is Result.Error -> CreateProfileUiState.Error(result.message)
                is Result.Loading -> CreateProfileUiState.Loading
            }
            _profileUiState.postValue(uiState)
        }
    }
}