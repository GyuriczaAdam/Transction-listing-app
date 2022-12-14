package hu.gyuriczaadam.sprintformteszt.presentation.transaction_list_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.gyuriczaadam.sprintformteszt.domain.model.TransactionItem
import hu.gyuriczaadam.sprintformteszt.domain.use_case.TransactionUseCases
import hu.gyuriczaadam.sprintformteszt.util.Constants
import hu.gyuriczaadam.sprintformteszt.util.Resource
import hu.gyuriczaadam.sprintformteszt.util.TransactionTypes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
):ViewModel() {
    private val _state = mutableStateOf(TransactionListState())
    val state:State<TransactionListState> = _state

    private var getTransactionsJob:Job? = null

    init {
        getTransactions()
    }

    fun onEvent(event: TransactionEvent){
        when(event){
            is TransactionEvent.Order -> {
                _state.value = TransactionListState(transactionTypes = event.transactionTypes)
                    when(event.transactionTypes){
                        TransactionTypes.all -> getAllTransactions()
                        TransactionTypes.food -> getTransactionsByQuery(Constants.FOOD_TYPE,event.transactionTypes)
                        TransactionTypes.housing -> getTransactionsByQuery(Constants.HOUSING_TYPE,event.transactionTypes)
                        TransactionTypes.travel -> getTransactionsByQuery(Constants.TRAVEL_TYPE,event.transactionTypes)
                        TransactionTypes.enetertainment -> getTransactionsByQuery(Constants.ENTERTAINMENT_TYPE,event.transactionTypes)
                        TransactionTypes.financial -> getTransactionsByQuery(Constants.FINANCIAL_TYPE,event.transactionTypes)
                        TransactionTypes.healthcare -> getTransactionsByQuery(Constants.HEALTHCARE_TYPE,event.transactionTypes)
                        TransactionTypes.insurance -> getTransactionsByQuery(Constants.INSURANCE_TYPE,event.transactionTypes)
                        TransactionTypes.lifestyle -> getTransactionsByQuery(Constants.LIFESTYLE_TYPE,event.transactionTypes)
                        TransactionTypes.miscellaneous -> getTransactionsByQuery(Constants.MISCELLANEOUS_TYPE,event.transactionTypes)
                        TransactionTypes.utilities -> getTransactionsByQuery(Constants.UTILITIES_TYPE,event.transactionTypes)
                    }
            }
            TransactionEvent.ToggleOrderSection -> {
                _state.value = TransactionListState(isOrderSectionVisible = !state.value.isOrderSectionVisible , transaction = state.value.transaction, transactionTypes = state.value.transactionTypes)
            }
            is TransactionEvent.OnQueryChange -> {
                _state.value = TransactionListState(query = event.query)
            }
            TransactionEvent.OnSearch -> {
                getTransactionsByQuery(state.value.query,state.value.transactionTypes)
            }
            TransactionEvent.OnRefresh -> {
                getAllTransactions()
            }
        }
    }

    private fun getTransactions(){
     val job = transactionUseCases.getTransactionsFromApiUseCase().onEach { result->
         when(result){
             is Resource.Error -> {
                 _state.value = TransactionListState(error = result.message.toString())
             }
             is Resource.Loading -> {
                 _state.value = TransactionListState(isLoading = true)
             }
             is Resource.Success -> {
                 _state.value= TransactionListState(transaction = getAllTransactions())
                }
            }
        }.launchIn(viewModelScope)
        if(job.isCompleted){
            job.cancel()
        }
    }

  private fun getAllTransactions():List<TransactionItem>{
     getTransactionsJob?.cancel()
     getTransactionsJob = transactionUseCases.getAllTransactionsUseCase().onEach { result->
            _state.value = TransactionListState(transaction = result)
        }.launchIn(viewModelScope)
      return _state.value.transaction
    }

    private fun getTransactionsByQuery(query:String,transactionTypes: TransactionTypes){
        getTransactionsJob?.cancel()
        getTransactionsJob =  transactionUseCases.getTransactionByQueryUseCase(query).onEach { result->
            _state.value = TransactionListState(transaction = result , transactionTypes =transactionTypes)
        }.launchIn(viewModelScope)
    }
}