enum class AuthMode {
    LOGIN, SIGN_UP
}

data class AuthUiState(
    val name:String = "",
    val phoneNumber :String= "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val authMode: AuthMode = AuthMode.LOGIN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisteringAsPartner : Boolean = false
)