package com.example.medicalstoreadmin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalstoreadmin.data.api.GenericResponse
import com.example.medicalstoreadmin.data.response.AddProductResponse
import com.example.medicalstoreadmin.data.response.UpdateProductResponse
import com.example.medicalstoreadmin.data.response.deleteSpecificUserResponse
import com.example.medicalstoreadmin.data.response.getAllOrdersResponse
import com.example.medicalstoreadmin.data.response.getAllProductsResponse
import com.example.medicalstoreadmin.data.response.getAllUsersResponse
import com.example.medicalstoreadmin.data.response.getSpecificProductResponse
import com.example.medicalstoreadmin.data.response.updateOrdersDetailsResponse
import com.example.medicalstoreadmin.data.response.updateUserDetailsResponse
import com.example.medicalstoreadmin.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val repo: Repo) : ViewModel() {

    private val _getAllUsersResponse = MutableStateFlow(GetAllUserState())
    val getAllUsersResponse = _getAllUsersResponse.asStateFlow()

    private val _updateUserDetailsResponse = MutableStateFlow(UpdateUserDetailsState())
    val updateUserDetailsResponse = _updateUserDetailsResponse.asStateFlow()

    private val _deleteSpecificUserResponse = MutableStateFlow(DeleteSpecificUserState())
    val deleteSpecificUserResponse = _deleteSpecificUserResponse.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // for taking a response from the add product API
    private val _addProductResponse = MutableStateFlow(AddProductState())
    val addProductResponse = _addProductResponse.asStateFlow()

    private val _getAllProductsResponse = MutableStateFlow(GetAllProductsState())
    val getAllProductsResponse = _getAllProductsResponse.asStateFlow()

    private val _getAllOrdersResponse = MutableStateFlow(GetAllOrdersState())
    val getAllOrdersResponse = _getAllOrdersResponse.asStateFlow()

    private val _updateOrderDetailsResponse = MutableStateFlow(UpdateOrderDetailsState())
    val updateOrderDetailsResponse = _updateOrderDetailsResponse.asStateFlow()

    private val _getSpecificProductResponse = MutableStateFlow(GetSpecificProductState())
    val getSpecificProductResponse = _getSpecificProductResponse.asStateFlow()

    private val _updateProductResponse = MutableStateFlow(UpdateProductState())
    val updateProductResponse = _updateProductResponse.asStateFlow()

    private val _deleteSpecificProductResponse = MutableStateFlow(DeleteSpecificProductState())
    val deleteSpecificProductResponse = _deleteSpecificProductResponse.asStateFlow()

    private val _deleteSpecificOrderResponse = MutableStateFlow(DeleteSpecificOrderState())
    val deleteSpecificOrderResponse = _deleteSpecificOrderResponse.asStateFlow()

    init {
        getAllUsers()
       // getAllProducts()
       // getAllOrders()
    }

    fun updateOrderDetails(order_id : String , isApproved : Int){
        viewModelScope.launch (Dispatchers.IO){
            repo.updateOrderDetailsRepo(order_id = order_id, isApproved = isApproved)
                .collect{
                    when(it){
                        is State.Loading -> {
                            _updateOrderDetailsResponse.value = UpdateOrderDetailsState(isLoading = true)
                        }
                        is State.Success -> {
                            _updateOrderDetailsResponse.value = UpdateOrderDetailsState(data = it.data,isLoading = false)
                        }
                        is State.Error -> {
                            _updateOrderDetailsResponse.value = UpdateOrderDetailsState(error = it.message,isLoading = false)
                        }
                    }
                }
        }
    }


    fun approveUser(user_id : String, isApproved : Int){

        viewModelScope.launch (Dispatchers.IO){
            repo.updateUserDetailsRepo(
                user_id = user_id,
                isApproved = isApproved
            ).collect {
                when(it){
                    is State.Loading -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(isLoading = true)
                    }
                    is State.Success -> {
                        Log.d("APPROVE_USER", "Response: ${it.data?.body()}")
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(data = it.data,isLoading = false)

                    }
                    is State.Error -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(error = it.message,isLoading = false)
                    }
                }
            }
        }
    }

    fun addProduct(
        product_name: String,
        product_expiry_date: String,
        product_price: String,
        stock: String,
        product_category: String
    ){
        viewModelScope.launch (Dispatchers.IO){
            repo.addProductDetailsRepo(
                product_name = product_name,
                product_expiry_date = product_expiry_date,
                product_price = product_price,
                stock = stock,
                product_category = product_category
            ).collect {
                when(it){
                    is State.Loading -> {
                        _addProductResponse.value = AddProductState(isLoading = true)
                    }
                    is State.Success -> {
                        _addProductResponse.value =
                            AddProductState(data = it.data, isLoading = false)
                    }
                    is State.Error -> {
                        _addProductResponse.value = AddProductState(error = it.message, isLoading = false)
                    }
                }
            }
        }

    }

    fun getAllUsers() {
        viewModelScope.launch {
            repo.getAllUsersRepo().collect { state ->
                when (state) {
                    is State.Loading -> {
                        _getAllUsersResponse.value = GetAllUserState(isLoading = true)
                    }
                    is State.Success -> {
                        _getAllUsersResponse.value = GetAllUserState(data = state.data, isLoading = false)
                    }
                    is State.Error -> {
                        _getAllUsersResponse.value = GetAllUserState(error = state.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun acceptUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateUserDetailsRepo(userId, isApproved = 1).collect { state ->
                when (state) {
                    is State.Loading -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(isLoading = true)
                    }
                    is State.Success -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(data = state.data, isLoading = false)
                        getAllUsers()
                    }
                    is State.Error -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(error = state.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun rejectUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSpecificUserRepo(userId).collect { state ->
                when (state) {
                    is State.Loading -> {
                        _deleteSpecificUserResponse.value = DeleteSpecificUserState(isLoading = true)
                    }
                    is State.Success -> {
                        _deleteSpecificUserResponse.value = DeleteSpecificUserState(data = state.data, isLoading = false)
                        getAllUsers()
                    }
                    is State.Error -> {
                        _deleteSpecificUserResponse.value = DeleteSpecificUserState(error = state.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun blockUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateUserDetailsRepo(userId, isApproved = 0).collect { state ->
                when (state) {
                    is State.Loading -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(isLoading = true)
                    }
                    is State.Success -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(data = state.data, isLoading = false)
                        getAllUsers()
                    }
                    is State.Error -> {
                        _updateUserDetailsResponse.value = UpdateUserDetailsState(error = state.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSpecificUserRepo(userId).collect { state ->
                when (state) {
                    is State.Loading -> {
                        _deleteSpecificUserResponse.value = DeleteSpecificUserState(isLoading = true)
                    }
                    is State.Success -> {
                        _deleteSpecificUserResponse.value = DeleteSpecificUserState(data = state.data, isLoading = false)
                        getAllUsers()
                    }
                    is State.Error -> {
                        _deleteSpecificUserResponse.value = DeleteSpecificUserState(error = state.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }


    fun getAllProducts() {
        viewModelScope.launch (Dispatchers.IO){
            repo.getAllProductsRepo().collect {
                when(it){
                    is State.Loading -> {
                        _getAllProductsResponse.value = GetAllProductsState(isLoading = true)
                    }
                    is State.Success -> {
                        _getAllProductsResponse.value = GetAllProductsState(data = it.data ,isLoading = false)
                    }
                    is State.Error -> {
                        _getAllProductsResponse.value = GetAllProductsState(error = it.message,isLoading = false)
                    }
                }
            }
        }
    }

    fun getAllOrders() {
        viewModelScope.launch (Dispatchers.IO) {
            repo.getAllOrdersRepo().collect {
                when (it) {
                    is State.Loading -> {
                        _getAllOrdersResponse.value = GetAllOrdersState(isLoading = true)
                    }
                    is State.Success -> {
                        _getAllOrdersResponse.value = GetAllOrdersState(data = it.data, isLoading = false)
                    }
                    is State.Error -> {
                        _getAllOrdersResponse.value = GetAllOrdersState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun getSpecificProduct(product_id : String){
        viewModelScope.launch (Dispatchers.IO) {
            repo.getSpecificProduct(product_id = product_id).collect{
                when{
                    it is State.Loading -> {
                        _getSpecificProductResponse.value = GetSpecificProductState(isLoading = true)
                    }
                    it is State.Success -> {
                        _getSpecificProductResponse.value = GetSpecificProductState(data = it.data,isLoading = false)

                    }
                    it is State.Error -> {
                        _getSpecificProductResponse.value = GetSpecificProductState(error = it.message,isLoading = false)
                    }
                }
            }
        }
    }

    fun updateProductDetails(product_id : String , stock : String){
        viewModelScope.launch (Dispatchers.IO) {
            repo.updateProductDetails(product_id = product_id, stock = stock).collect{
                when{
                    it is State.Loading -> {
                        _updateProductResponse.value = UpdateProductState(isLoading = true)
                    }
                    it is State.Success -> {
                        _updateProductResponse.value = UpdateProductState(data = it.data,isLoading = false)
                    }
                    it is State.Error -> {
                        _updateProductResponse.value = UpdateProductState(error = it.message,isLoading = false)
                    }
                }
            }
        }
    }

    fun deleteSpecificUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSpecificUserRepo(userId).collect {
                when (it) {
                    is State.Loading -> {
                        _deleteSpecificUserResponse.value =
                            DeleteSpecificUserState(isLoading = true)
                    }

                    is State.Success -> {
                        _deleteSpecificUserResponse.value =
                            DeleteSpecificUserState(data = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _deleteSpecificUserResponse.value =
                            DeleteSpecificUserState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    // Functions
    fun deleteSpecificProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSpecificProductRepo(productId).collect {
                when (it) {
                    is State.Loading -> {
                        _deleteSpecificProductResponse.value =
                            DeleteSpecificProductState(isLoading = true)
                    }
                    is State.Success -> {
                        _deleteSpecificProductResponse.value =
                            DeleteSpecificProductState(data = it.data, isLoading = false)
                    }
                    is State.Error -> {
                        _deleteSpecificProductResponse.value =
                            DeleteSpecificProductState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun deleteSpecificOrder(OrderId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSpecificOrderRepo(OrderId).collect {
                when (it) {
                    is State.Loading -> {
                        _deleteSpecificOrderResponse.value =
                            DeleteSpecificOrderState(isLoading = true)
                    }
                    is State.Success -> {
                        _deleteSpecificOrderResponse.value =
                            DeleteSpecificOrderState(data = it.data, isLoading = false)
                    }
                    is State.Error -> {
                        _deleteSpecificOrderResponse.value =
                            DeleteSpecificOrderState(error = it.message, isLoading = false)
                    }
                }
            }
        }
    }
}
data class GetAllUserState(
         val isLoading : Boolean = false,
         val data : Response<getAllUsersResponse>? = null,
         val error : String? = null
)

data class UpdateUserDetailsState(
    val isLoading : Boolean = false,
    val data : Response<updateUserDetailsResponse>? = null,
    val error : String? = null
)

data class AddProductState(
    val isLoading : Boolean = false,
    val data : Response<AddProductResponse>? = null,
    val error : String? = null
)

data class GetAllProductsState(
    val isLoading : Boolean = false,
    val data : Response<getAllProductsResponse>? = null,
    val error : String? = null
)

data class GetAllOrdersState(
    val isLoading : Boolean = false,
    val data : Response<getAllOrdersResponse>? = null,
    val error : String? = null
)

data class UpdateOrderDetailsState(
    val isLoading : Boolean = false,
    val data : Response<updateOrdersDetailsResponse>? = null,
    val error : String? = null
)

data class DeleteSpecificUserState(
    val isLoading : Boolean = false,
    val data : Response<deleteSpecificUserResponse>? = null,
    val error : String? = null
)

data class GetSpecificProductState(
    val isLoading : Boolean = false,
    val data : Response<getSpecificProductResponse>? = null,
    val error : String? = null
)

data class UpdateProductState(
    val isLoading : Boolean = false,
    val data : Response<UpdateProductResponse>? = null,
    val error : String? = null
)
data class DeleteAllUsersState(
    val isLoading : Boolean = false,
    val data : Response<GenericResponse>? = null,
    val error : String? = null
)
data class DeleteSpecificProductState(
    val isLoading : Boolean = false,
    val data : Response<GenericResponse>? = null,
    val error : String? = null
)
data class DeleteAllProductsState(
    val isLoading : Boolean = false,
    val data : Response<GenericResponse>? = null,
    val error : String? = null
)
data class DeleteSpecificOrderState(
    val isLoading : Boolean = false,
    val data : Response<GenericResponse>? = null,
    val error : String? = null
)
