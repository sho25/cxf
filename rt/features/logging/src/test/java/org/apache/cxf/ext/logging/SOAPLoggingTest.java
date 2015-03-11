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
name|ext
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|jws
operator|.
name|WebService
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
name|Endpoint
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|EventType
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|LogEvent
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
name|Feature
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
name|JaxWsProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|SOAPLoggingTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_URI
init|=
literal|"http://localhost:5678/test"
decl_stmt|;
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.ext.logging.TestService"
argument_list|)
specifier|public
specifier|final
class|class
name|TestServiceImplementation
implements|implements
name|TestService
block|{
annotation|@
name|Override
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
return|return
name|msg
return|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSlf4j
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|TestService
name|serviceImpl
init|=
operator|new
name|TestServiceImplementation
argument_list|()
decl_stmt|;
name|LoggingFeature
name|loggingFeature
init|=
operator|new
name|LoggingFeature
argument_list|()
decl_stmt|;
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|SERVICE_URI
argument_list|,
name|serviceImpl
argument_list|,
name|loggingFeature
argument_list|)
decl_stmt|;
name|TestService
name|client
init|=
name|createTestClient
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|client
operator|.
name|echo
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvents
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|TestService
name|serviceImpl
init|=
operator|new
name|TestServiceImplementation
argument_list|()
decl_stmt|;
name|LoggingFeature
name|loggingFeature
init|=
operator|new
name|LoggingFeature
argument_list|()
decl_stmt|;
name|TestEventSender
name|sender
init|=
operator|new
name|TestEventSender
argument_list|()
decl_stmt|;
name|loggingFeature
operator|.
name|setSender
argument_list|(
name|sender
argument_list|)
expr_stmt|;
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|SERVICE_URI
argument_list|,
name|serviceImpl
argument_list|,
name|loggingFeature
argument_list|)
decl_stmt|;
name|TestService
name|client
init|=
name|createTestClient
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|client
operator|.
name|echo
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|LogEvent
argument_list|>
name|events
init|=
name|sender
operator|.
name|getEvents
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|checkRequestOut
argument_list|(
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|checkRequestIn
argument_list|(
name|events
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|checkResponseOut
argument_list|(
name|events
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|checkResponseIn
argument_list|(
name|events
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkRequestOut
parameter_list|(
name|LogEvent
name|requestOut
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|SERVICE_URI
argument_list|,
name|requestOut
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|requestOut
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|EventType
operator|.
name|REQ_OUT
argument_list|,
name|requestOut
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|requestOut
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|requestOut
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"POST"
argument_list|,
name|requestOut
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|requestOut
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|requestOut
operator|.
name|getPayload
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<arg0>test</arg0>"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServicePort"
argument_list|,
name|requestOut
operator|.
name|getPortName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestService"
argument_list|,
name|requestOut
operator|.
name|getPortTypeName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServiceService"
argument_list|,
name|requestOut
operator|.
name|getServiceName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkRequestIn
parameter_list|(
name|LogEvent
name|requestIn
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|SERVICE_URI
argument_list|,
name|requestIn
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"text/xml; charset=UTF-8"
argument_list|,
name|requestIn
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|EventType
operator|.
name|REQ_IN
argument_list|,
name|requestIn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|requestIn
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|requestIn
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"POST"
argument_list|,
name|requestIn
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|requestIn
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|requestIn
operator|.
name|getPayload
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<arg0>test</arg0>"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServiceImplementationPort"
argument_list|,
name|requestIn
operator|.
name|getPortName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestService"
argument_list|,
name|requestIn
operator|.
name|getPortTypeName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServiceImplementationService"
argument_list|,
name|requestIn
operator|.
name|getServiceName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkResponseOut
parameter_list|(
name|LogEvent
name|responseOut
parameter_list|)
block|{
comment|// Not yet available
name|Assert
operator|.
name|assertNull
argument_list|(
name|responseOut
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"text/xml"
argument_list|,
name|responseOut
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|EventType
operator|.
name|RESP_OUT
argument_list|,
name|responseOut
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|responseOut
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|responseOut
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Not yet available
name|Assert
operator|.
name|assertNull
argument_list|(
name|responseOut
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|responseOut
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|responseOut
operator|.
name|getPayload
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<return>test</return>"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServiceImplementationPort"
argument_list|,
name|responseOut
operator|.
name|getPortName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestService"
argument_list|,
name|responseOut
operator|.
name|getPortTypeName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServiceImplementationService"
argument_list|,
name|responseOut
operator|.
name|getServiceName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkResponseIn
parameter_list|(
name|LogEvent
name|responseIn
parameter_list|)
block|{
name|Assert
operator|.
name|assertNull
argument_list|(
name|responseIn
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"text/xml; charset=UTF-8"
argument_list|,
name|responseIn
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|EventType
operator|.
name|RESP_IN
argument_list|,
name|responseIn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|responseIn
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|responseIn
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Not yet available
name|Assert
operator|.
name|assertNull
argument_list|(
name|responseIn
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|responseIn
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|responseIn
operator|.
name|getPayload
argument_list|()
operator|.
name|contains
argument_list|(
literal|"<return>test</return>"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServicePort"
argument_list|,
name|responseIn
operator|.
name|getPortName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestService"
argument_list|,
name|responseIn
operator|.
name|getPortTypeName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestServiceService"
argument_list|,
name|responseIn
operator|.
name|getServiceName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TestService
name|createTestClient
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|SERVICE_URI
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeatures
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|feature
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|(
name|TestService
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

