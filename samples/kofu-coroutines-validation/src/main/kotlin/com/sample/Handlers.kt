package com.sample

import am.ik.yavi.fn.awaitFold
import kotlinx.coroutines.FlowPreview
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait

@FlowPreview
@Suppress("UNUSED_PARAMETER")
class UserHandler {

    suspend fun createApi(request: ServerRequest): ServerResponse =
            request.awaitBody<User>()
                    .validate()
                    .leftMap { mapOf("details" to it.details()) }
                    .awaitFold(badRequest()::bodyAndAwait, ok()::bodyAndAwait)
}