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
name|transport
operator|.
name|http
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PushbackInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
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
name|ArrayList
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
name|bus
operator|.
name|CXFBusImpl
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|helpers
operator|.
name|CastUtils
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
name|io
operator|.
name|AbstractThresholdOutputStream
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
name|Exchange
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
name|message
operator|.
name|MessageImpl
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|transport
operator|.
name|MessageObserver
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
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|IMocksControl
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
name|Assert
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|HTTPConduitURLEasyMockTest
extends|extends
name|Assert
block|{
specifier|private
enum|enum
name|ResponseStyle
block|{
name|NONE
block|,
name|BACK_CHANNEL
block|,
name|DECOUPLED
block|}
empty_stmt|;
specifier|private
enum|enum
name|ResponseDelimiter
block|{
name|LENGTH
block|,
name|CHUNKED
block|,
name|EOF
block|}
empty_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NOWHERE
init|=
literal|"http://nada.nothing.nowhere.null/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PAYLOAD
init|=
literal|"message payload"
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|private
name|HttpURLConnectionFactory
name|connectionFactory
decl_stmt|;
specifier|private
name|HttpURLConnection
name|connection
decl_stmt|;
specifier|private
name|Proxy
name|proxy
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|MessageObserver
name|observer
decl_stmt|;
specifier|private
name|OutputStream
name|os
decl_stmt|;
specifier|private
name|InputStream
name|is
decl_stmt|;
comment|/**      * This is an extension to the HTTPConduit that replaces      * the dynamic assignment of the HttpURLConnectionFactory,      * and we just use the EasyMocked version for this test.      */
specifier|private
class|class
name|HTTPTestConduit
extends|extends
name|HTTPConduit
block|{
name|HTTPTestConduit
parameter_list|(
name|Bus
name|associatedBus
parameter_list|,
name|EndpointInfo
name|endpoint
parameter_list|,
name|EndpointReferenceType
name|epr
parameter_list|,
name|HttpURLConnectionFactory
name|testFactory
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|associatedBus
argument_list|,
name|endpoint
argument_list|,
name|epr
argument_list|)
expr_stmt|;
name|connectionFactory
operator|=
name|testFactory
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|HttpURLConnectionFactory
name|retrieveConnectionFactory
parameter_list|(
name|String
name|s
parameter_list|)
block|{
comment|// do nothing. i.e do not change the connectionFactory field.
return|return
name|connectionFactory
return|;
block|}
block|}
comment|/**      * @throws java.lang.Exception      */
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{     }
comment|/**      * @throws java.lang.Exception      */
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// avoid intermittent spurious failures on EasyMock detecting finalize
comment|// calls by mocking up only class data members (no local variables)
comment|// and explicitly making available for GC post-verify
name|connectionFactory
operator|=
literal|null
expr_stmt|;
name|connection
operator|=
literal|null
expr_stmt|;
name|proxy
operator|=
literal|null
expr_stmt|;
name|inMessage
operator|=
literal|null
expr_stmt|;
name|observer
operator|=
literal|null
expr_stmt|;
name|os
operator|=
literal|null
expr_stmt|;
name|is
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSend
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
name|setUpConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|finalVerify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendWithHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
name|setUpConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/xml;charset=utf8"
argument_list|)
expr_stmt|;
name|setUpHeaders
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|true
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|finalVerify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendHttpConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
name|setUpConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|finalVerify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendHttpConnectionAutoRedirect
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
name|setUpConduit
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|finalVerify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendHttpGetConnectionAutoRedirect
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
name|setUpConduit
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|"GET"
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|finalVerify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendHttpGetConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
name|setUpConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
literal|"GET"
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|finalVerify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendOnewayChunkedEmptyPartialResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
name|setUpConduit
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
name|ResponseStyle
operator|.
name|NONE
argument_list|,
name|ResponseDelimiter
operator|.
name|CHUNKED
argument_list|,
literal|true
argument_list|,
comment|// empty response
literal|"POST"
argument_list|)
expr_stmt|;
name|finalVerify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|setUpHeaders
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|contentTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|contentTypes
operator|.
name|add
argument_list|(
literal|"text/xml;charset=utf8"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"content-type"
argument_list|,
name|contentTypes
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acceptTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|acceptTypes
operator|.
name|add
argument_list|(
literal|"text/xml;charset=utf8"
argument_list|)
expr_stmt|;
name|acceptTypes
operator|.
name|add
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Accept"
argument_list|,
name|acceptTypes
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|AuthorizationPolicy
name|authPolicy
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|authPolicy
operator|.
name|setUserName
argument_list|(
literal|"BJ"
argument_list|)
expr_stmt|;
name|authPolicy
operator|.
name|setPassword
argument_list|(
literal|"value"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|,
name|authPolicy
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setUpOneway
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|isOneWay
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|isSynchronous
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|HTTPConduit
name|setUpConduit
parameter_list|(
name|boolean
name|send
parameter_list|,
name|boolean
name|autoRedirect
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|setUpConduit
argument_list|(
name|send
argument_list|,
name|autoRedirect
argument_list|,
literal|"POST"
argument_list|)
return|;
block|}
specifier|private
name|HTTPConduit
name|setUpConduit
parameter_list|(
name|boolean
name|send
parameter_list|,
name|boolean
name|autoRedirect
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|Exception
block|{
name|endpointInfo
operator|=
operator|new
name|EndpointInfo
argument_list|()
expr_stmt|;
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|NOWHERE
operator|+
literal|"bar/foo"
argument_list|)
expr_stmt|;
name|connectionFactory
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpURLConnectionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|send
condition|)
block|{
comment|//proxy = control.createMock(Proxy.class);
name|proxy
operator|=
literal|null
expr_stmt|;
name|connection
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpURLConnection
operator|.
name|class
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|createConnection
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|proxy
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
operator|new
name|URL
argument_list|(
name|NOWHERE
operator|+
literal|"bar/foo"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|connection
operator|.
name|setRequestMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|autoRedirect
operator|&&
literal|"POST"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|connection
operator|.
name|setChunkedStreamingMode
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
name|connection
operator|.
name|getRequestMethod
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|method
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|connection
operator|.
name|setInstanceFollowRedirects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|times
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setConnectTimeout
argument_list|(
literal|303030
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|connection
operator|.
name|setReadTimeout
argument_list|(
literal|404040
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|connection
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
name|CXFBusImpl
name|bus
init|=
operator|new
name|CXFBusImpl
argument_list|()
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HTTPConduit
name|conduit
init|=
operator|new
name|HTTPTestConduit
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
literal|null
argument_list|,
name|connectionFactory
argument_list|)
decl_stmt|;
name|conduit
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
if|if
condition|(
name|send
condition|)
block|{
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setConnectionTimeout
argument_list|(
literal|303030
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|404040
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setAutoRedirect
argument_list|(
name|autoRedirect
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|autoRedirect
condition|)
block|{
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setAllowChunking
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|setChunkingThreshold
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
name|observer
operator|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|inMessage
operator|=
name|m
expr_stmt|;
block|}
block|}
expr_stmt|;
name|conduit
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
return|return
name|conduit
return|;
block|}
specifier|private
name|void
name|verifySentMessage
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifySentMessage
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|expectHeaders
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
name|expectHeaders
argument_list|,
name|ResponseStyle
operator|.
name|BACK_CHANNEL
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifySentMessage
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|expectHeaders
parameter_list|,
name|ResponseStyle
name|style
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
name|expectHeaders
argument_list|,
name|style
argument_list|,
name|ResponseDelimiter
operator|.
name|LENGTH
argument_list|,
literal|false
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifySentMessage
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|ResponseStyle
name|style
parameter_list|,
name|ResponseDelimiter
name|delimiter
parameter_list|,
name|boolean
name|emptyResponse
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|verifySentMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
name|style
argument_list|,
name|delimiter
argument_list|,
name|emptyResponse
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifySentMessage
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|expectHeaders
parameter_list|,
name|ResponseStyle
name|style
parameter_list|,
name|ResponseDelimiter
name|delimiter
parameter_list|,
name|boolean
name|emptyResponse
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|OutputStream
name|wrappedOS
init|=
name|verifyRequestHeaders
argument_list|(
name|message
argument_list|,
name|expectHeaders
argument_list|,
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|PAYLOAD
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|PAYLOAD
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|style
operator|==
name|ResponseStyle
operator|.
name|NONE
condition|)
block|{
name|setUpOneway
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|getRequestMethod
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|method
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|verifyHandleResponse
argument_list|(
name|style
argument_list|,
name|delimiter
argument_list|,
name|conduit
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|wrappedOS
operator|.
name|flush
argument_list|()
expr_stmt|;
name|wrappedOS
operator|.
name|flush
argument_list|()
expr_stmt|;
name|wrappedOS
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"expected in message"
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|headerMap
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response headers"
argument_list|,
name|headerMap
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Integer
name|expectedResponseCode
init|=
name|style
operator|==
name|ResponseStyle
operator|.
name|BACK_CHANNEL
condition|?
name|HttpURLConnection
operator|.
name|HTTP_OK
else|:
name|HttpURLConnection
operator|.
name|HTTP_ACCEPTED
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected response code"
argument_list|,
name|expectedResponseCode
argument_list|,
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|emptyResponse
condition|)
block|{
name|assertTrue
argument_list|(
literal|"unexpected content formats"
argument_list|,
name|inMessage
operator|.
name|getContentFormats
argument_list|()
operator|.
name|contains
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|content
init|=
name|inMessage
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|content
operator|instanceof
name|PushbackInputStream
operator|)
condition|)
block|{
name|assertSame
argument_list|(
literal|"unexpected content"
argument_list|,
name|is
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
name|finalVerify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|OutputStream
name|verifyRequestHeaders
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|expectHeaders
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected request headers set"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected output stream format"
argument_list|,
name|message
operator|.
name|getContentFormats
argument_list|()
operator|.
name|contains
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|connection
operator|.
name|getRequestMethod
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|method
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|os
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|connection
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|HTTPConduit
operator|.
name|KEY_HTTP_CONNECTION
argument_list|,
name|connection
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectHeaders
condition|)
block|{
name|connection
operator|.
name|setRequestProperty
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"Authorization"
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"Basic Qko6dmFsdWU="
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"text/xml;charset=utf8"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"Accept"
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"text/xml;charset=utf8,text/plain"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
name|connection
operator|.
name|getRequestProperties
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|AbstractThresholdOutputStream
name|wrappedOS
init|=
operator|(
name|AbstractThresholdOutputStream
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected output stream"
argument_list|,
name|wrappedOS
argument_list|)
expr_stmt|;
name|wrappedOS
operator|.
name|write
argument_list|(
name|PAYLOAD
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|wrappedOS
operator|.
name|unBuffer
argument_list|()
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
return|return
name|wrappedOS
return|;
block|}
specifier|private
name|void
name|verifyHandleResponse
parameter_list|(
name|ResponseStyle
name|style
parameter_list|,
name|ResponseDelimiter
name|delimiter
parameter_list|,
name|HTTPConduit
name|conduit
parameter_list|)
throws|throws
name|IOException
block|{
name|verifyHandleResponse
argument_list|(
name|style
argument_list|,
name|delimiter
argument_list|,
literal|false
argument_list|,
name|conduit
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyHandleResponse
parameter_list|(
name|ResponseStyle
name|style
parameter_list|,
name|ResponseDelimiter
name|delimiter
parameter_list|,
name|boolean
name|emptyResponse
parameter_list|,
name|HTTPConduit
name|conduit
parameter_list|)
throws|throws
name|IOException
block|{
name|connection
operator|.
name|getHeaderFields
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|int
name|responseCode
init|=
name|style
operator|==
name|ResponseStyle
operator|.
name|BACK_CHANNEL
condition|?
name|HttpURLConnection
operator|.
name|HTTP_OK
else|:
name|HttpURLConnection
operator|.
name|HTTP_ACCEPTED
decl_stmt|;
if|if
condition|(
name|conduit
operator|.
name|getClient
argument_list|()
operator|.
name|isAutoRedirect
argument_list|()
condition|)
block|{
name|connection
operator|.
name|getResponseCode
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|301
argument_list|)
operator|.
name|once
argument_list|()
operator|.
name|andReturn
argument_list|(
name|responseCode
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|connection
operator|.
name|getURL
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
operator|new
name|URL
argument_list|(
name|NOWHERE
operator|+
literal|"bar/foo/redirect"
argument_list|)
argument_list|)
operator|.
name|once
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|connection
operator|.
name|getResponseCode
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|responseCode
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
name|is
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|is
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|style
condition|)
block|{
case|case
name|NONE
case|:
case|case
name|DECOUPLED
case|:
name|connection
operator|.
name|getContentLength
argument_list|()
expr_stmt|;
if|if
condition|(
name|delimiter
operator|==
name|ResponseDelimiter
operator|.
name|CHUNKED
operator|||
name|delimiter
operator|==
name|ResponseDelimiter
operator|.
name|EOF
condition|)
block|{
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
operator|-
literal|1
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
if|if
condition|(
name|delimiter
operator|==
name|ResponseDelimiter
operator|.
name|CHUNKED
condition|)
block|{
name|connection
operator|.
name|getHeaderField
argument_list|(
literal|"Transfer-Encoding"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"chunked"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|delimiter
operator|==
name|ResponseDelimiter
operator|.
name|EOF
condition|)
block|{
name|connection
operator|.
name|getHeaderField
argument_list|(
literal|"Connection"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"close"
argument_list|)
expr_stmt|;
block|}
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|emptyResponse
condition|?
operator|-
literal|1
else|:
operator|(
name|int
operator|)
literal|'<'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|123
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|emptyResponse
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
name|BACK_CHANNEL
case|:
name|connection
operator|.
name|getErrorStream
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
specifier|private
name|void
name|finalVerify
parameter_list|()
block|{
if|if
condition|(
name|control
operator|!=
literal|null
condition|)
block|{
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

