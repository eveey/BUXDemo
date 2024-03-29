package com.evastos.bux.data.domain.product.details

import com.evastos.bux.data.domain.Repositories
import com.evastos.bux.data.exception.ExceptionMappers
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.rx.mapException
import com.evastos.bux.data.service.ApiService
import io.reactivex.Single
import javax.inject.Inject

class ProductDetailsRepository
@Inject constructor(
    private val apiService: ApiService,
    private val exceptionMapper: ExceptionMappers.Api
) : Repositories.ProductDetailsRepository {

    override fun getProductDetails(productIdentifier: String): Single<ProductDetails> =
            apiService.getProductDetails(productIdentifier)
                    .mapException(exceptionMapper)
}
