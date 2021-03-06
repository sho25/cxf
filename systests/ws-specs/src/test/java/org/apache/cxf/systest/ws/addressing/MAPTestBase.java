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
name|ws
operator|.
name|addressing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|ProtocolException
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
name|Bus
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
name|BusFactory
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|interceptor
operator|.
name|Interceptor
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
name|jaxws
operator|.
name|ServiceImpl
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
name|jaxws
operator|.
name|support
operator|.
name|ServiceDelegateAccessor
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
name|AbstractClientServerTestBase
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|ws
operator|.
name|addressing
operator|.
name|AttributedURIType
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
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceUtils
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
name|ws
operator|.
name|addressing
operator|.
name|Names
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
name|ws
operator|.
name|addressing
operator|.
name|ReferenceParametersType
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
name|ws
operator|.
name|addressing
operator|.
name|VersionTransformer
operator|.
name|Names200408
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|BadRecordLitFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|NoSuchCodeLitFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  * Tests the addition of WS-Addressing Message Addressing Properties.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|MAPTestBase
extends|extends
name|AbstractClientServerTestBase
implements|implements
name|VerificationCache
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|DECOUPLE_PORT
init|=
name|allocatePort
argument_list|(
name|MAPTestBase
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
name|Bus
name|staticBus
decl_stmt|;
specifier|static
specifier|final
name|String
name|INBOUND_KEY
init|=
literal|"inbound"
decl_stmt|;
specifier|static
specifier|final
name|String
name|OUTBOUND_KEY
init|=
literal|"outbound"
decl_stmt|;
specifier|static
specifier|final
name|QName
name|CUSTOMER_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://example.org/customer"
argument_list|,
literal|"CustomerKey"
argument_list|,
literal|"customer"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|CUSTOMER_KEY
init|=
literal|"Key#123456789"
decl_stmt|;
specifier|private
specifier|static
name|MAPVerifier
name|mapVerifier
decl_stmt|;
specifier|private
specifier|static
name|HeaderVerifier
name|headerVerifier
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPServiceAddressing"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|Object
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|messageIDs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|Greeter
name|greeter
decl_stmt|;
specifier|private
name|String
name|verified
decl_stmt|;
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|shutdownBus
parameter_list|()
throws|throws
name|Exception
block|{
name|staticBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|staticBus
operator|=
literal|null
expr_stmt|;
name|messageIDs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|mapVerifier
operator|=
literal|null
expr_stmt|;
name|headerVerifier
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|String
name|getPort
parameter_list|()
function_decl|;
specifier|private
name|void
name|addInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|chain
parameter_list|,
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
index|[]
name|interceptors
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|interceptors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|interceptors
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|removeInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|chain
parameter_list|,
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
index|[]
name|interceptors
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|interceptors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|chain
operator|.
name|remove
argument_list|(
name|interceptors
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|abstract
name|String
name|getConfigFileName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getAddress
parameter_list|()
function_decl|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|//super.setUp();
if|if
condition|(
name|staticBus
operator|==
literal|null
condition|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|staticBus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|getConfigFileName
argument_list|()
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|staticBus
argument_list|)
expr_stmt|;
block|}
name|messageIDs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|mapVerifier
operator|=
operator|new
name|MAPVerifier
argument_list|()
expr_stmt|;
name|headerVerifier
operator|=
operator|new
name|HeaderVerifier
argument_list|()
expr_stmt|;
name|Interceptor
argument_list|<
name|?
argument_list|>
index|[]
name|interceptors
init|=
block|{
name|mapVerifier
block|,
name|headerVerifier
block|}
decl_stmt|;
name|addInterceptors
argument_list|(
name|staticBus
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|addInterceptors
argument_list|(
name|staticBus
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|addInterceptors
argument_list|(
name|staticBus
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|addInterceptors
argument_list|(
name|staticBus
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|target
init|=
name|EndpointReferenceUtils
operator|.
name|getEndpointReference
argument_list|(
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
name|ReferenceParametersType
name|params
init|=
name|ContextUtils
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createReferenceParametersType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|param
init|=
operator|new
name|JAXBElement
argument_list|<>
argument_list|(
name|CUSTOMER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|CUSTOMER_KEY
argument_list|)
decl_stmt|;
name|params
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|target
operator|.
name|setReferenceParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|greeter
operator|=
name|createGreeter
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|mapVerifier
operator|.
name|verificationCache
operator|=
name|this
expr_stmt|;
name|headerVerifier
operator|.
name|verificationCache
operator|=
name|this
expr_stmt|;
block|}
specifier|public
name|URL
name|getWSDLURL
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
return|;
block|}
specifier|public
name|Greeter
name|createGreeter
parameter_list|(
name|EndpointReferenceType
name|target
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceImpl
name|serviceImpl
init|=
name|ServiceDelegateAccessor
operator|.
name|get
argument_list|(
operator|new
name|SOAPService
argument_list|(
name|getWSDLURL
argument_list|()
argument_list|,
name|SERVICE_NAME
argument_list|)
argument_list|)
decl_stmt|;
name|Greeter
name|g
init|=
name|serviceImpl
operator|.
name|getPort
argument_list|(
name|target
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|g
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|Interceptor
argument_list|<
name|?
argument_list|>
index|[]
name|interceptors
init|=
block|{
name|mapVerifier
block|,
name|headerVerifier
block|}
decl_stmt|;
name|removeInterceptors
argument_list|(
name|staticBus
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|removeInterceptors
argument_list|(
name|staticBus
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|removeInterceptors
argument_list|(
name|staticBus
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|removeInterceptors
argument_list|(
name|staticBus
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
if|if
condition|(
name|greeter
operator|instanceof
name|Closeable
condition|)
block|{
operator|(
operator|(
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|greeter
operator|=
literal|null
expr_stmt|;
name|mapVerifier
operator|=
literal|null
expr_stmt|;
name|headerVerifier
operator|=
literal|null
expr_stmt|;
name|verified
operator|=
literal|null
expr_stmt|;
name|messageIDs
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|//--Tests
annotation|@
name|Test
specifier|public
name|void
name|testImplicitMAPs
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"implicit1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response received from service"
argument_list|,
literal|"Hello implicit1"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|checkVerification
argument_list|()
expr_stmt|;
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"implicit2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response received from service"
argument_list|,
literal|"Hello implicit2"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|checkVerification
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExplicitMAPs
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|msgId
init|=
literal|"urn:uuid:12345-"
operator|+
name|Math
operator|.
name|random
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|AttributedURIType
name|id
init|=
name|ContextUtils
operator|.
name|getAttributedURI
argument_list|(
name|msgId
argument_list|)
decl_stmt|;
name|maps
operator|.
name|setMessageID
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|CLIENT_ADDRESSING_PROPERTIES
argument_list|,
name|maps
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"explicit1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response received from service"
argument_list|,
literal|"Hello explicit1"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|checkVerification
argument_list|()
expr_stmt|;
comment|// the previous addition to the request context impacts
comment|// on all subsequent invocations on this proxy => a duplicate
comment|// message ID fault is expected
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"explicit2"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected ProtocolException on duplicate message ID"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProtocolException
name|pe
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"expected duplicate message ID failure"
argument_list|,
literal|"Duplicate Message ID "
operator|+
name|msgId
argument_list|,
name|pe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|checkVerification
argument_list|()
expr_stmt|;
block|}
comment|// clearing the message ID ensure a duplicate is not sent
name|maps
operator|.
name|setMessageID
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|//maps.setRelatesTo(ContextUtils.getRelatesTo(id.getValue()));
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"explicit3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response received from service"
argument_list|,
literal|"Hello explicit3"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOneway
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"implicit_oneway1"
argument_list|)
expr_stmt|;
name|checkVerification
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplicationFault
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|greeter
operator|.
name|testDocLitFault
argument_list|(
literal|"BadRecordLitFault"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected fault from service"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadRecordLitFault
name|brlf
parameter_list|)
block|{
comment|//checkVerification();
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"intra-fault"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response received from service"
argument_list|,
literal|"Hello intra-fault"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|testDocLitFault
argument_list|(
literal|"NoSuchCodeLitFault"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected NoSuchCodeLitFault"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchCodeLitFault
name|nsclf
parameter_list|)
block|{
comment|//checkVerification();
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testVersioning
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|// expect two MAPs instances versioned with 200408, i.e. for both
comment|// the partial and full responses
name|mapVerifier
operator|.
name|addToExpectedExposedAs
argument_list|(
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|mapVerifier
operator|.
name|addToExpectedExposedAs
argument_list|(
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"versioning1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response received from service"
argument_list|,
literal|"Hello versioning1"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|checkVerification
argument_list|()
expr_stmt|;
name|greeting
operator|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"versioning2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response received from service"
argument_list|,
literal|"Hello versioning2"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|checkVerification
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
comment|//--VerificationCache implementation
specifier|public
name|void
name|put
parameter_list|(
name|String
name|verification
parameter_list|)
block|{
if|if
condition|(
name|verification
operator|!=
literal|null
condition|)
block|{
name|verified
operator|=
name|verified
operator|==
literal|null
condition|?
name|verification
else|:
name|verified
operator|+
literal|"; "
operator|+
name|verification
expr_stmt|;
block|}
block|}
comment|//--Verification methods called by handlers
comment|/**      * Verify presence of expected MAPs.      *      * @param maps the MAPs to verify      * @param checkPoint the check point      * @return null if all expected MAPs present, otherwise an error string.      */
specifier|protected
specifier|static
name|String
name|verifyMAPs
parameter_list|(
name|AddressingProperties
name|maps
parameter_list|,
name|Object
name|checkPoint
parameter_list|)
block|{
if|if
condition|(
name|maps
operator|==
literal|null
condition|)
block|{
return|return
literal|"expected MAPs"
return|;
block|}
comment|//String rt = maps.getReplyTo() != null ? maps.getReplyTo().getAddress().getValue() : "null";
comment|//System.out.println("verifying MAPs: " + maps
comment|//                   + " id: " + maps.getMessageID().getValue()
comment|//                   + " to: " + maps.getTo().getValue()
comment|//                   + " reply to: " + rt);
comment|// MessageID
name|String
name|id
init|=
name|maps
operator|.
name|getMessageID
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|"expected MessageID MAP"
return|;
block|}
if|if
condition|(
operator|!
name|id
operator|.
name|startsWith
argument_list|(
literal|"urn:uuid"
argument_list|)
condition|)
block|{
return|return
literal|"bad URN format in MessageID MAP: "
operator|+
name|id
return|;
block|}
comment|// ensure MessageID is unique for this check point
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|checkPointMessageIDs
init|=
name|messageIDs
operator|.
name|get
argument_list|(
name|checkPoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|checkPointMessageIDs
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|checkPointMessageIDs
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
comment|//return "MessageID MAP duplicate: " + id;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|checkPointMessageIDs
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|messageIDs
operator|.
name|put
argument_list|(
name|checkPoint
argument_list|,
name|checkPointMessageIDs
argument_list|)
expr_stmt|;
block|}
name|checkPointMessageIDs
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|id
argument_list|)
expr_stmt|;
comment|// To
if|if
condition|(
name|maps
operator|.
name|getTo
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|"expected To MAP"
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Verify presence of expected MAP headers.      *      * @param wsaHeaders a list of the wsa:* headers present in the SOAP      * message      * @param parial true if partial response      * @return null if all expected headers present, otherwise an error string.      */
specifier|protected
specifier|static
name|String
name|verifyHeaders
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|wsaHeaders
parameter_list|,
name|boolean
name|partial
parameter_list|,
name|boolean
name|requestLeg
parameter_list|,
name|boolean
name|replyToRequired
parameter_list|)
block|{
comment|//System.out.println("verifying headers: " + wsaHeaders);
name|String
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|wsaHeaders
operator|.
name|contains
argument_list|(
name|Names
operator|.
name|WSA_MESSAGEID_NAME
argument_list|)
condition|)
block|{
name|ret
operator|=
literal|"expected MessageID header"
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|wsaHeaders
operator|.
name|contains
argument_list|(
name|Names
operator|.
name|WSA_TO_NAME
argument_list|)
condition|)
block|{
name|ret
operator|=
literal|"expected To header"
expr_stmt|;
block|}
if|if
condition|(
name|replyToRequired
operator|&&
operator|!
operator|(
name|wsaHeaders
operator|.
name|contains
argument_list|(
name|Names
operator|.
name|WSA_REPLYTO_NAME
argument_list|)
operator|||
name|wsaHeaders
operator|.
name|contains
argument_list|(
name|Names
operator|.
name|WSA_RELATESTO_NAME
argument_list|)
operator|)
condition|)
block|{
name|ret
operator|=
literal|"expected ReplyTo or RelatesTo header"
expr_stmt|;
block|}
comment|/*         if (partial) {             if (!wsaHeaders.contains(Names.WSA_FROM_NAME)) {                 ret = "expected From header";             }         } else {             // REVISIT Action missing from full response             //if (!wsaHeaders.contains(Names.WSA_ACTION_NAME)) {             //    ret = "expected Action header";             //}         }         */
if|if
condition|(
name|requestLeg
operator|&&
operator|!
operator|(
name|wsaHeaders
operator|.
name|contains
argument_list|(
name|CUSTOMER_NAME
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|ret
operator|=
literal|"expected CustomerKey header"
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|void
name|checkVerification
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"MAP/Header verification failed: "
operator|+
name|verified
argument_list|,
name|verified
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

