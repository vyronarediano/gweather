/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ced.gweather_core.internal.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ced.commons.clean.interactor.BaseReactiveUseCase
import com.ced.commons.clean.interactor.Failure

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 *
 * @since 1.0.0
 */
abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    private var reactiveUseCases = mutableListOf<BaseReactiveUseCase>()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }

    /**
     * All usecase used in a ViewModel should be added here so all subscription after use can be disposed.
     */
    protected fun addUseCases(block: MutableList<BaseReactiveUseCase>.() -> Unit): MutableList<BaseReactiveUseCase> {
        reactiveUseCases.block()
        return reactiveUseCases
    }

    override fun onCleared() {
        super.onCleared()

        reactiveUseCases.forEach { it.dispose() }
    }
}