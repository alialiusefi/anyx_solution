package com.anymind.sales.resolver

import com.anymind.sales.exception.BadRequestException
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import javax.validation.ConstraintViolationException


@Component
class CustomExceptionResolver: DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        when (ex) {
            is BadRequestException -> {
                return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.message)
                    .path(env.executionStepInfo.path)
                    .location(env.field.sourceLocation)
                    .build()
            }
            is ConstraintViolationException -> {
                return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.message)
                    .path(env.executionStepInfo.path)
                    .location(env.field.sourceLocation)
                    .build()
            }
        }
        return null
    }
}