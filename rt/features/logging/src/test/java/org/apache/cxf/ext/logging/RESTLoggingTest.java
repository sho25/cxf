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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
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
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|RESTLoggingTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_URI
init|=
literal|"http://localhost:5679/testrest"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_URI_BINARY
init|=
literal|"http://localhost:5680/testrest"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSlf4j
parameter_list|()
throws|throws
name|IOException
block|{
name|LoggingFeature
name|loggingFeature
init|=
operator|new
name|LoggingFeature
argument_list|()
decl_stmt|;
name|Server
name|server
init|=
name|createService
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|WebClient
name|client
init|=
name|createClient
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|client
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"test1"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary
parameter_list|()
throws|throws
name|IOException
block|{
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
name|Server
name|server
init|=
name|createServiceBinary
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|WebClient
name|client
init|=
name|createClientBinary
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|client
operator|.
name|get
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|loggingFeature
operator|.
name|setLogBinary
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|client
operator|.
name|get
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|assertLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertLogged
argument_list|(
name|sender
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertLogged
parameter_list|(
name|LogEvent
name|event
parameter_list|)
block|{
name|Assert
operator|.
name|assertNotEquals
argument_list|(
name|AbstractLoggingInterceptor
operator|.
name|CONTENT_SUPPRESSED
argument_list|,
name|event
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertNotLogged
parameter_list|(
name|LogEvent
name|event
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|AbstractLoggingInterceptor
operator|.
name|CONTENT_SUPPRESSED
argument_list|,
name|event
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|WebClient
name|createClient
parameter_list|(
name|LoggingFeature
name|loggingFeature
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|SERVICE_URI
operator|+
literal|"/test1"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setFeatures
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|loggingFeature
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
specifier|private
name|Server
name|createService
parameter_list|(
name|LoggingFeature
name|loggingFeature
parameter_list|)
block|{
name|JAXRSServerFactoryBean
name|factory
init|=
operator|new
name|JAXRSServerFactoryBean
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
name|loggingFeature
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|TestServiceRest
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
specifier|private
name|WebClient
name|createClientBinary
parameter_list|(
name|LoggingFeature
name|loggingFeature
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|SERVICE_URI_BINARY
operator|+
literal|"/test1"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setFeatures
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|loggingFeature
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
specifier|private
name|Server
name|createServiceBinary
parameter_list|(
name|LoggingFeature
name|loggingFeature
parameter_list|)
block|{
name|JAXRSServerFactoryBean
name|factory
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|SERVICE_URI_BINARY
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
name|loggingFeature
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|TestServiceRestBinary
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
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
name|LoggingFeature
name|loggingFeature
init|=
operator|new
name|LoggingFeature
argument_list|()
decl_stmt|;
name|loggingFeature
operator|.
name|setLogBinary
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|Server
name|server
init|=
name|createService
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|WebClient
name|client
init|=
name|createClient
argument_list|(
name|loggingFeature
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|client
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"test1"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|server
operator|.
name|destroy
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
operator|+
literal|"/test1"
argument_list|,
name|requestOut
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
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
name|assertNull
argument_list|(
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
literal|"GET"
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
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|requestOut
operator|.
name|getPayload
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
operator|+
literal|"/test1"
argument_list|,
name|requestIn
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
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
name|assertNull
argument_list|(
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
literal|"GET"
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
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|requestIn
operator|.
name|getPayload
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
literal|"application/octet-stream"
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
name|assertNull
argument_list|(
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
name|assertEquals
argument_list|(
literal|"test1"
argument_list|,
name|responseOut
operator|.
name|getPayload
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
comment|// Not yet available
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
literal|"application/octet-stream"
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
literal|"ISO-8859-1"
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
name|assertEquals
argument_list|(
literal|"test1"
argument_list|,
name|responseIn
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

