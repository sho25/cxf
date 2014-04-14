begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|schema_validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Dispatch
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|Mode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|feature
operator|.
name|LoggingFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|DoSomethingFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|SchemaValidation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|SchemaValidationService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|ComplexStruct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|OccuringStruct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|SomeRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|SomeResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ValidationClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|ValidationServer
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/schema_validation"
argument_list|,
literal|"SchemaValidationService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/schema_validation"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startservers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|ValidationServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO : Change this test so that we test the combinations of
comment|// client and server with schema validation enabled/disabled...
comment|// Only tests client side validation enabled/server side disabled.
annotation|@
name|Test
specifier|public
name|void
name|testSchemaValidationServer
parameter_list|()
throws|throws
name|Exception
block|{
name|SchemaValidation
name|validation
init|=
name|createService
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
literal|"SoapPortValidate"
argument_list|)
decl_stmt|;
name|runSchemaValidationTest
argument_list|(
name|validation
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|validation
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSchemaValidationClient
parameter_list|()
throws|throws
name|Exception
block|{
name|SchemaValidation
name|validation
init|=
name|createService
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
name|runSchemaValidationTest
argument_list|(
name|validation
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|validation
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|runSchemaValidationTest
parameter_list|(
name|SchemaValidation
name|validation
parameter_list|)
block|{
name|ComplexStruct
name|complexStruct
init|=
operator|new
name|ComplexStruct
argument_list|()
decl_stmt|;
name|complexStruct
operator|.
name|setElem1
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
comment|// Don't initialize a member of the structure.
comment|// Client side validation should throw an exception.
comment|// complexStruct.setElem2("two");
name|complexStruct
operator|.
name|setElem3
argument_list|(
literal|3
argument_list|)
expr_stmt|;
try|try
block|{
comment|/*boolean result =*/
name|validation
operator|.
name|setComplexStruct
argument_list|(
name|complexStruct
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Set ComplexStruct should have thrown ProtocolException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
name|String
name|expected
init|=
literal|"'{\"http://apache.org/schema_validation/types\":elem2}' is expected."
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expected
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|OccuringStruct
name|occuringStruct
init|=
operator|new
name|OccuringStruct
argument_list|()
decl_stmt|;
comment|// Populate the list in the wrong order.
comment|// Client side validation should throw an exception.
name|List
argument_list|<
name|Serializable
argument_list|>
name|floatIntStringList
init|=
name|occuringStruct
operator|.
name|getVarFloatAndVarIntAndVarString
argument_list|()
decl_stmt|;
name|floatIntStringList
operator|.
name|add
argument_list|(
operator|new
name|Integer
argument_list|(
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|floatIntStringList
operator|.
name|add
argument_list|(
operator|new
name|Float
argument_list|(
literal|4.2f
argument_list|)
argument_list|)
expr_stmt|;
name|floatIntStringList
operator|.
name|add
argument_list|(
literal|"Goofus and Gallant"
argument_list|)
expr_stmt|;
try|try
block|{
comment|/*boolean result =*/
name|validation
operator|.
name|setOccuringStruct
argument_list|(
name|occuringStruct
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Set OccuringStruct should have thrown ProtocolException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
name|String
name|expected
init|=
literal|"'{\"http://apache.org/schema_validation/types\":varFloat}' is expected."
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expected
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// The server will attempt to return an invalid ComplexStruct
comment|// When validation is disabled on the server side, we'll get the
comment|// exception while unmarshalling the invalid response.
comment|/*complexStruct =*/
name|validation
operator|.
name|getComplexStruct
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Get ComplexStruct should have thrown ProtocolException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
name|String
name|expected
init|=
literal|"'{\"http://apache.org/schema_validation/types\":elem2}' is expected."
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Found message "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expected
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// The server will attempt to return an invalid OccuringStruct
comment|// When validation is disabled on the server side, we'll get the
comment|// exception while unmarshalling the invalid response.
comment|/*occuringStruct =*/
name|validation
operator|.
name|getOccuringStruct
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Get OccuringStruct should have thrown ProtocolException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
name|String
name|expected
init|=
literal|"'{\"http://apache.org/schema_validation/types\":varFloat}' is expected."
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expected
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|SomeRequest
name|req
init|=
operator|new
name|SomeRequest
argument_list|()
decl_stmt|;
name|req
operator|.
name|setId
argument_list|(
literal|"9999999999"
argument_list|)
expr_stmt|;
try|try
block|{
name|validation
operator|.
name|doSomething
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have faulted"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DoSomethingFault
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"1234"
argument_list|,
name|e
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|req
operator|.
name|setId
argument_list|(
literal|"8888888888"
argument_list|)
expr_stmt|;
try|try
block|{
name|validation
operator|.
name|doSomething
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have faulted"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DoSomethingFault
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not have happened"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestFailedSchemaValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailedRequestValidation
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailedRequestSchemaValidationTypeBoth
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailedRequestValidation
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailedSchemaValidationSchemaValidationTypeOut
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailedRequestValidation
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIgnoreRequestSchemaValidationNone
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIgnoredRequestValidation
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIgnoreRequestSchemaValidationResponseOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIgnoredRequestValidation
argument_list|(
name|SchemaValidationType
operator|.
name|IN
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIgnoreRequestSchemaValidationFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIgnoredRequestValidation
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseFailedSchemaValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailureResponseValidation
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseSchemaFailedValidationBoth
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailureResponseValidation
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseSchemaFailedValidationIn
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailureResponseValidation
argument_list|(
name|SchemaValidationType
operator|.
name|IN
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIgnoreResponseSchemaFailedValidationNone
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIgnoredResponseValidation
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIgnoreResponseSchemaFailedValidationFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIgnoredResponseValidation
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIgnoreResponseSchemaFailedValidationOut
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIgnoredResponseValidation
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHeaderValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/schema_validation.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SchemaValidationService
name|service
init|=
operator|new
name|SchemaValidationService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|String
name|smsg
init|=
literal|"<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'><soap:Header>"
operator|+
literal|"<SomeHeader soap:mustUnderstand='1' xmlns='http://apache.org/schema_validation/types'>"
operator|+
literal|"<id>1111111111</id></SomeHeader>"
operator|+
literal|"</soap:Header><soap:Body><SomeRequestWithHeader xmlns='http://apache.org/schema_validation/types'>"
operator|+
literal|"<id>1111111111</id></SomeRequestWithHeader></soap:Body></soap:Envelope>"
decl_stmt|;
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|dsp
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|SchemaValidationService
operator|.
name|SoapPort
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|Mode
operator|.
name|MESSAGE
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|dsp
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|dsp
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|smsg
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SomeResponse
name|execute
parameter_list|(
name|SchemaValidation
name|service
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|SomeRequest
name|request
init|=
operator|new
name|SomeRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|service
operator|.
name|doSomething
argument_list|(
name|request
argument_list|)
return|;
block|}
specifier|private
name|void
name|assertFailureResponseValidation
parameter_list|(
name|Object
name|validationConfig
parameter_list|)
throws|throws
name|Exception
block|{
name|SchemaValidation
name|service
init|=
name|createService
argument_list|(
name|validationConfig
argument_list|)
decl_stmt|;
name|SomeResponse
name|response
init|=
name|execute
argument_list|(
name|service
argument_list|,
literal|"1111111111"
argument_list|)
decl_stmt|;
comment|// valid request
name|assertEquals
argument_list|(
name|response
operator|.
name|getTransactionId
argument_list|()
argument_list|,
literal|"aaaaaaaaaa"
argument_list|)
expr_stmt|;
try|try
block|{
name|execute
argument_list|(
name|service
argument_list|,
literal|"1234567890"
argument_list|)
expr_stmt|;
comment|// valid request, but will result in invalid response
name|fail
argument_list|(
literal|"should catch marshall exception as the invalid incoming message per schema"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Unmarshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"is not facet-valid with respect to pattern"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|service
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|assertIgnoredRequestValidation
parameter_list|(
name|Object
name|validationConfig
parameter_list|)
throws|throws
name|Exception
block|{
name|SchemaValidation
name|service
init|=
name|createService
argument_list|(
name|validationConfig
argument_list|)
decl_stmt|;
comment|// this is an invalid request but validation is turned off.
name|SomeResponse
name|response
init|=
name|execute
argument_list|(
name|service
argument_list|,
literal|"1234567890aaaa"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getTransactionId
argument_list|()
argument_list|,
literal|"aaaaaaaaaa"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|service
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|assertIgnoredResponseValidation
parameter_list|(
name|Object
name|validationConfig
parameter_list|)
throws|throws
name|Exception
block|{
name|SchemaValidation
name|service
init|=
name|createService
argument_list|(
name|validationConfig
argument_list|)
decl_stmt|;
comment|// the request will result in invalid response but validation is turned off
name|SomeResponse
name|response
init|=
name|execute
argument_list|(
name|service
argument_list|,
literal|"1234567890"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getTransactionId
argument_list|()
argument_list|,
literal|"aaaaaaaaaaxxx"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|service
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|assertFailedRequestValidation
parameter_list|(
name|Object
name|validationConfig
parameter_list|)
throws|throws
name|Exception
block|{
name|SchemaValidation
name|service
init|=
name|createService
argument_list|(
name|validationConfig
argument_list|)
decl_stmt|;
name|SomeResponse
name|response
init|=
name|execute
argument_list|(
name|service
argument_list|,
literal|"1111111111"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getTransactionId
argument_list|()
argument_list|,
literal|"aaaaaaaaaa"
argument_list|)
expr_stmt|;
try|try
block|{
name|execute
argument_list|(
name|service
argument_list|,
literal|"1234567890aaa"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should catch marshall exception as the invalid outgoing message per schema"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Marshalling Error"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"is not facet-valid with respect to pattern"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|service
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|SchemaValidation
name|createService
parameter_list|(
name|Object
name|validationConfig
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createService
argument_list|(
name|validationConfig
argument_list|,
literal|"SoapPort"
argument_list|)
return|;
block|}
specifier|private
name|SchemaValidation
name|createService
parameter_list|(
name|Object
name|validationConfig
parameter_list|,
name|String
name|postfix
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/schema_validation.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SchemaValidationService
name|service
init|=
operator|new
name|SchemaValidationService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|SchemaValidation
name|validation
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|SchemaValidation
operator|.
name|class
argument_list|)
decl_stmt|;
name|setAddress
argument_list|(
name|validation
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/"
operator|+
name|postfix
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|validation
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|validationConfig
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|validation
operator|)
operator|.
name|getResponseContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|validationConfig
argument_list|)
expr_stmt|;
operator|new
name|LoggingFeature
argument_list|()
operator|.
name|initialize
argument_list|(
operator|(
name|Client
operator|)
name|validation
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|validation
return|;
block|}
block|}
end_class

end_unit

