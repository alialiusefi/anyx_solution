package com.anymind.sales.resolver

import com.anymind.sales.exception.BadRequestException
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import mu.KLogging
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import javax.validation.ConstraintViolationException


@Component
class CustomExceptionResolver : DataFetcherExceptionResolverAdapter() {

    companion object : KLogging()

    override fun resolveToMultipleErrors(ex: Throwable, env: DataFetchingEnvironment): List<GraphQLError> {
        when (ex) {
            is ConstraintViolationException -> {
                val graphQlErrors = ex.constraintViolations.map {
                    GraphqlErrorBuilder.newError()
                        .errorType(ErrorType.BAD_REQUEST)
                        .message(it.message)
                        .path(env.executionStepInfo.path)
                        .location(env.field.sourceLocation)
                        .build()
                }
                return graphQlErrors
            }

            is BadRequestException -> {
                return listOf(
                    GraphqlErrorBuilder.newError()
                        .errorType(ErrorType.BAD_REQUEST)
                        .message(ex.message)
                        .path(env.executionStepInfo.path)
                        .location(env.field.sourceLocation)
                        .build()
                )
            }

            else -> {
                logger.error(ex) { ex.message }
                return listOf(
                    GraphqlErrorBuilder.newError()
                        .errorType(ErrorType.INTERNAL_ERROR)
                        .message(ex.message)
                        .path(env.executionStepInfo.path)
                        .location(env.field.sourceLocation)
                        .build()
                )
            }
        }
    }
}
