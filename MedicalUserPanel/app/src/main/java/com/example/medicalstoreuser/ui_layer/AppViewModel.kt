package com.example.medicalstoreuser.ui_layer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalstoreuser.State1
import com.example.medicalstoreuser.data_layer.Dao.UserDao
import com.example.medicalstoreuser.data_layer.Dao.UserEntity
import com.example.medicalstoreuser.data_layer.response.createOrderResponse
import com.example.medicalstoreuser.data_layer.response.getAllOrdersResponse
import com.example.medicalstoreuser.data_layer.response.getAllProductsResponse
import com.example.medicalstoreuser.data_layer.response.getAllProductsResponseItem
import com.example.medicalstoreuser.data_layer.response.getSpecificOrderResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificProductResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificUserResponse
import com.example.medicalstoreuser.repo.Repo
import com.example.medicalstoreuser.user_pref.UserPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repo: Repo ,
    private val userDao: UserDao,
    private val userPreferenceManager: UserPreferenceManager,
) : ViewModel() {

    // stateflow

    private val _signUpUserState = MutableStateFlow(SignUpState())
    val signUpUserState = _signUpUserState.asStateFlow()

    private val _logInResponse = MutableStateFlow(LoginState())
    val loginResponse = _logInResponse.asStateFlow()

    private val _getAllProductsResponse = MutableStateFlow(GetAllProductsState())
    val getAllProductsResponse = _getAllProductsResponse.asStateFlow()

    private val _getSpecificProductResponse = MutableStateFlow(GetSpecificProductState())
    val getSpecificProductResponse = _getSpecificProductResponse.asStateFlow()

    private val _recentlyViewedProducts = MutableStateFlow<List<String>>(emptyList())
    val recentlyViewedProducts: StateFlow<List<String>> = _recentlyViewedProducts.asStateFlow()

    private val _cartAddedResponse = MutableStateFlow<List<String>>(emptyList())
    val cartAddedResponse: StateFlow<List<String>> = _cartAddedResponse.asStateFlow()

    private val _likeAddedResponse = MutableStateFlow<List<String>>(emptyList())
    val likeAddedResponse: StateFlow<List<String>> = _likeAddedResponse.asStateFlow()

    private val _createOrderResponse = MutableStateFlow(CreateOrderState())
    val createOrderResponse = _createOrderResponse.asStateFlow()

    private val _getAllOrdersResponse = MutableStateFlow(GetAllOrdersState())
    val getAllOrdersResponse = _getAllOrdersResponse.asStateFlow()

    private val _getSpecificOrderResponse = MutableStateFlow(GetSpecificOrderState())
    val getSpecificOrderResponse = _getSpecificOrderResponse.asStateFlow()

    private val _getSpecificUserResponse = MutableStateFlow(GetSpecificUserState())
    val getSpecificUserResponse = _getSpecificUserResponse.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _products = MutableStateFlow<List<getAllProductsResponseItem>>(emptyList())

    // Function to create an order
    fun createOrder(userId : String, productId : String, quantity : String, orderDate : String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.createOrderRepo(userId, productId, quantity, orderDate).collect {
                when (it) {
                    is State1.Loading -> {
                        _createOrderResponse.value = CreateOrderState(isLoading = true)
                    }
                    is State1.Success -> {
                        _createOrderResponse.value = CreateOrderState(data = it.data, isLoading = false)
                    }
                    is State1.Error -> {
                        _createOrderResponse.value = CreateOrderState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    // Function to get all orders
    fun getAllOrders(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllOrdersRepo().collect{
                when(it){
                    is State1.Loading -> {
                        _getAllOrdersResponse.value = GetAllOrdersState(isLoading = true)
                    }
                    is State1.Success -> {
                        _getAllOrdersResponse.value = GetAllOrdersState(data = it.data, isLoading = false)
                    }
                    is State1.Error -> {
                        _getAllOrdersResponse.value = GetAllOrdersState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    // Function to get a specific order by order_id
    fun getSpecificOrder(order_id : String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSpecificOrderRepo(order_id).collect {
                when (it) {
                    is State1.Loading -> {
                        _getSpecificOrderResponse.value = GetSpecificOrderState(isLoading = true)
                    }
                    is State1.Success -> {
                        _getSpecificOrderResponse.value = GetSpecificOrderState(data = it.data, isLoading = false)
                    }
                    is State1.Error -> {
                        _getSpecificOrderResponse.value = GetSpecificOrderState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    // Function to get a specific user by user_id
    fun getSpecificUser(user_id : String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSpecificUserRepo(user_id).collect {
                when (it) {
                    is State1.Loading -> {
                        _getSpecificUserResponse.value = GetSpecificUserState(isLoading = true)
                    }
                    is State1.Success -> {
                        _getSpecificUserResponse.value = GetSpecificUserState(data = it.data, isLoading = false)
                    }
                    is State1.Error -> {
                        _getSpecificUserResponse.value = GetSpecificUserState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    // Function to add a product to recently viewed products
    fun addCart(product_id: String) {
        viewModelScope.launch {
            userPreferenceManager.saveProductIdForCart(product_id)
            Log.d("AppViewModel", "Product added to cart: $product_id")
        }
    }

    // Function to remove a product from cart
    fun addLike(product_id: String) {
        viewModelScope.launch {
            val updatedList = _likeAddedResponse.value.toMutableList().apply {
                if (!contains(product_id)) add(product_id)
            }
            userPreferenceManager.saveProductIdForLike(product_id) // Save to UserPreference
            Log.d("ViewModel2", "Updated liked products: $updatedList")
        }
    }

    // Function to remove a product from liked products
    fun removeLike(productId: String) {
        viewModelScope.launch {
            userPreferenceManager.removeLikedProduct(productId) // Save to UserPreference
            Log.d("ViewModel", "Removed liked product: $productId")
        }
    }

    // Function to load liked products from UserPreference
    fun loadLikedProducts() {
        viewModelScope.launch {
            userPreferenceManager.getProductID2s().collect { likedProducts ->
                _likeAddedResponse.value = likedProducts // Update the state
                Log.d("ViewModel1", "Updated liked products: $likedProducts")
            }
        }
    }

    // Function to sign up a user
    fun signUpView(
        name: String,
        email: String,
        phoneNumber: String,
        address: String,
        password: String,
        pinCode: String
    ) {
        // Input validation
        if (name.isBlank() || email.isBlank() || phoneNumber.isBlank() || address.isBlank() || password.isBlank() || pinCode.isBlank()) {
            _signUpUserState.value = SignUpState(error = "All fields are required", isLoading = false)
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _signUpUserState.value = SignUpState(error = "Invalid email format", isLoading = false)
            return
        }
        if (password.length < 6) {
            _signUpUserState.value = SignUpState(error = "Password must be at least 6 characters", isLoading = false)
            return
        }
        if (phoneNumber.length != 10) {
            _signUpUserState.value = SignUpState(error = "Phone number must be 10 digits", isLoading = false)
            return
        }
        if (pinCode.length != 6) {
            _signUpUserState.value = SignUpState(error = "Pin code must be 6 digits", isLoading = false)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _signUpUserState.value = SignUpState(isLoading = true)
            try {
            // Firebase signup
                val authResult = FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .await()
                val user = authResult.user
                if (user != null) {

            // Sync with Flask API

                    repo.signUpRepo(name, email, phoneNumber, password, pinCode, address).collect { state ->
                        when (state) {
                            is State1.Loading -> {
                                _signUpUserState.value = SignUpState(isLoading = true)
                            }
                            is State1.Success -> {
                                val apiResponse = state.data?.body()
                                if (apiResponse != null) {


            // Save to local Room
                                    userDao.insertUser(
                                        UserEntity(
                                            uid = user.uid,
                                            apiUserId = apiResponse.user_id,
                                            name = name,
                                            email = email,
                                            phoneNumber = phoneNumber,
                                            address = address,
                                            pinCode = pinCode
                                        )
                                    )
                                    userPreferenceManager.saveUserID(apiResponse.user_id ?: user.uid)
                                    _signUpUserState.value = SignUpState(data = user.uid, isLoading = false)
                                } else {
                // Handle null API response
                                    _signUpUserState.value = SignUpState(error = "API response is null", isLoading = false)
                                    // Optionally delete Firebase user if API fails
                                    user.delete().await()
                                }
                            }
                            is State1.Error -> {
                                _signUpUserState.value = SignUpState(error = state.message, isLoading = false)
                // Optionally delete Firebase user if API fails
                                user.delete().await()
                            }
                        }
                    }
                } else {
                    _signUpUserState.value = SignUpState(error = "User creation failed", isLoading = false)
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                _signUpUserState.value = SignUpState(error = "Email already in use", isLoading = false)
            } catch (e: Exception) {
                _signUpUserState.value = SignUpState(error = e.message ?: "Sign Up Failed", isLoading = false)
            }
        }
    }

// Function to log in a user
    fun logInView(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _logInResponse.value = LoginState(isLoading = true)
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = task.result?.user
                            if (user != null) {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val existingUser = userDao.getUser(user.uid)
                                    if (existingUser == null) {
                                        userDao.insertUser(
                                            UserEntity(
                                                uid = user.uid,
                                                name = "N/A",
                                                email = email,
                                                phoneNumber = "N/A",
                                                address = "N/A",
                                                pinCode = "N/A"
                                            )
                                        )
                                    }
                                    userPreferenceManager.saveUserID(user.uid)
                                    _logInResponse.value =
                                        LoginState(data = user.uid, isLoading = false)
                                }
                            } else {
                                _logInResponse.value =
                                    LoginState(error = "Login failed", isLoading = false)
                            }
                        } else {
                            _logInResponse.value = LoginState(
                                error = task.exception?.message ?: "Login Failed",
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                _logInResponse.value =
                    LoginState(error = e.message ?: "Login Failed", isLoading = false)
            }
        }
    }

    // Function to get all products
    fun getAllProducts() {
        viewModelScope.launch() {
            repo.getAllProducts().collect {
                when (it) {
                    is State1.Loading -> {
                        _getAllProductsResponse.value = GetAllProductsState(isLoading = true)
                    }

                    is State1.Success -> {
                        _getAllProductsResponse.value =
                            GetAllProductsState(data = it.data, isLoading = false)
                    }

                    is State1.Error -> {
                        _getAllProductsResponse.value =
                            GetAllProductsState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    // Function to clear login state
    fun clearLogIn() {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            userPreferenceManager.clearUserId()
            Log.d("ViewModel#", "LogIn id cleared")
        }
    }

    // Function to get a specific product by product_id
    fun getSpecificProduct(product_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSpecificProductRepo(product_id).collect {
                when (it) {
                    is State1.Loading -> {
                        _getSpecificProductResponse.value =
                            GetSpecificProductState(isLoading = true)
                    }

                    is State1.Success -> {
                        val productId = it.data?.message()
                        if (productId != null) {
                            userPreferenceManager.saveProductID(productId)
                            Log.d("TAG", "productId saved ")
                        } else {
                            Log.d("TAG", "productId not found ")
                        }
                        _getSpecificProductResponse.value =
                            GetSpecificProductState(data = it.data, isLoading = false)

                    }

                    is State1.Error -> {
                        _getSpecificProductResponse.value =
                            GetSpecificProductState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    // Function to get user from Room database
    fun getUserFromRoom(userId: String): Flow<UserEntity?> {
        return flow {
            emit(userDao.getUser(userId))
        }
    }

    // Function to insert user into Room database
    fun insertUserToRoom(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    init {
        viewModelScope.launch {
            userPreferenceManager.getProductIDs().collect { productIds ->
                _recentlyViewedProducts.value = productIds
            }
        }
        viewModelScope.launch {
            userPreferenceManager.getProductID1s().collect { product ->
                _cartAddedResponse.value = product
                Log.d("AppViewModel", "Cart IDs Updated: $product")
            }
        }
        loadLikedProducts()
        getAllOrders()
        getAllProducts()
    }

    // Function to add a product to recently viewed products
    fun addProduct(productId: String) {
        viewModelScope.launch {
            userPreferenceManager.saveProductID(productId)
        }
    }

    init {
        getAllProducts()
        Log.d("TEST","${userPreferenceManager.userID}")
    }



    @OptIn(FlowPreview::class)
    val products = _searchText
        .debounce(500L) // To limit frequent updates
        .onEach { _isSearching.update { true } }
        .combine(_getAllProductsResponse) { text, state ->
            val allProducts = state.data?.body() ?: emptyList() // Extract product list
            if (text.isBlank()) {
                allProducts
            } else {
                allProducts.filter { it.matchesQuery(text) }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _products.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}

// Extension function to check if a product matches the search query

fun getAllProductsResponseItem.matchesQuery(query: String): Boolean {
    val matchCombinations = listOf(
        this.product_name // Example: Add more fields if necessary
    )
    return matchCombinations.any {
        it.contains(query, ignoreCase = true)
    }
}

// Data classes

data class LoginState(
    val isLoading: Boolean = false,
    val data: String? = null,
    val error: String? = null
)

data class SignUpState(
    val isLoading: Boolean = false,
    val data: String? = null,
    val error: String? = null
)

data class GetAllProductsState(
    val isLoading : Boolean = false,
    val data : Response<getAllProductsResponse>? = null,
    val error : String? = null
)

data class GetSpecificProductState(
    val isLoading : Boolean = false,
    val data : Response<getSpecificProductResponse>? = null,
    val error : String? = null
)

data class CreateOrderState(
    val isLoading : Boolean = false,
    val data : Response<createOrderResponse>? = null,
    val error : String? = null
)

data class GetAllOrdersState(

    val isLoading : Boolean = false,
    val data : Response<getAllOrdersResponse>? = null,
    val error : String? = null
)

data class GetSpecificOrderState(

    val isLoading : Boolean = false,
    val data : Response<getSpecificOrderResponse>? = null,
    val error : String? = null

)

data class GetSpecificUserState(

    val isLoading : Boolean = false,
    val data : Response<getSpecificUserResponse>? = null,
    val error : String? = null

)


