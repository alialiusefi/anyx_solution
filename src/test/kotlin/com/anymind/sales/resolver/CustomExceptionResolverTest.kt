//package com.anymind.sales.resolver
//
//import com.anymind.sales.exception.BadRequestException
//import graphql.execution.ExecutionStepInfo
//import graphql.execution.ResultPath
//import graphql.language.SourceLocation
//import graphql.language.StringValue
//import graphql.schema.DataFetchingEnvironment
//import graphql.schema.DataFetchingEnvironmentImpl
//import io.mockk.every
//import io.mockk.mockk
//import org.hibernate.validator.internal.engine.ConstraintViolationImpl
//import org.junit.jupiter.api.assertThrows
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.Arguments
//import org.junit.jupiter.params.provider.MethodSource
//import java.util.stream.Stream
//import javax.validation.ConstraintViolation
//import javax.validation.ConstraintViolationException
//
//class CustomExceptionResolverTest {
//
//    @ParameterizedTest
//    @MethodSource("getParamTestArguments")
//    fun shouldReturnValidGraphQLErrors(throwable: Throwable, environment: DataFetchingEnvironment) {
//        val resolver = CustomExceptionResolver()
//
//        asser
//    }
//
//    companion object {
//        @JvmStatic
//        fun getParamTestArguments(): Stream<Arguments> {
//            val constraintViolation = mockk<ConstraintViolation<Any>>()
//            every { constraintViolation.message } returns "Incorrect data"
//            val dataFetchingEnvironment = mockk<DataFetchingEnvironment>()
//            every { dataFetchingEnvironment.executionStepInfo.path } returns ResultPath.rootPath()
//            every { dataFetchingEnvironment.field.sourceLocation } returns SourceLocation(1,1)
//
//            return Stream.of(
//                Arguments.of(
//                    BadRequestException("Incorrect data provided!"),
//                    dataFetchingEnvironment
//                ),
//                Arguments.of(
//                    ConstraintViolationException("Constraint Violation", setOf(constraintViolation)),
//                    dataFetchingEnvironment
//                ),
//                Arguments.of(
//                    RuntimeException(),
//                    dataFetchingEnvironment
//                )
//            )
//        }
//    }
//}
