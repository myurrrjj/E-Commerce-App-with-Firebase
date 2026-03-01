package com.example.koshi.data

import com.example.koshi.screens.cartScreen.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

interface CartRepository{
    val cartItems : StateFlow<List<CartItem>>
    fun addItem(item: CartItem)
    fun removeItem(item: CartItem)
    fun updateQuantity(item : CartItem, newQuantity : Int)
    fun clearCart()
}
@Singleton
class OfflineCartRepository @Inject constructor(): CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
   override val cartItems = _cartItems.asStateFlow()

    override fun addItem(item: CartItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.id == item.id }
            if (existingItem != null) {
                currentList.map {
                    if (it.id == item.id) {
                        when (it) {
                            is CartItem.PlantItem -> it.copy(quantity = it.quantity + 1)
                            is CartItem.ServiceItem -> it.copy(quantity = it.quantity + 1)
                        }
                    } else it
                }
            } else {
                currentList + item
            }
        }
    }

    override fun removeItem(item: CartItem) {
        _cartItems.update { currentList ->
            currentList.filterNot { it.id == item.id }
        }

    }
   override fun updateQuantity(item: CartItem, newQuantity: Int) {
        _cartItems.update { currentList ->
            if (newQuantity <= 0) {
                currentList.filterNot { it.id == item.id }
            } else {
                currentList.map {
                    if (it.id == item.id) {
                        when (it) {
                            is CartItem.PlantItem -> it.copy(quantity = newQuantity)
                            is CartItem.ServiceItem -> it.copy(quantity = newQuantity)
                        }
                    } else {
                        it
                    }
                }
            }
        }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }


}