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
name|jaxws
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|LinkedList
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
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|handler
operator|.
name|MessageContext
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
name|handler
operator|.
name|MessageContext
operator|.
name|Scope
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
name|attachment
operator|.
name|AttachmentImpl
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
name|context
operator|.
name|WrappedMessageContext
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
name|Attachment
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
name|ExchangeImpl
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

begin_class
specifier|public
class|class
name|ContextPropertiesMappingTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"test address"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_METHOD
init|=
literal|"GET"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HEADER
init|=
literal|"header"
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|message
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|message
operator|.
name|clear
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|ADDRESS
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
name|REQUEST_METHOD
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
name|HEADER
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|clear
argument_list|()
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|ADDRESS
operator|+
literal|"jaxws"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
argument_list|,
name|HEADER
operator|+
literal|"jaxws"
argument_list|)
expr_stmt|;
name|responseContext
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateWebServiceContext
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|putAll
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|MessageContext
name|ctx
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
decl_stmt|;
name|Object
name|requestHeader
init|=
name|ctx
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|HTTP_REQUEST_HEADERS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"the request header should not be null"
argument_list|,
name|requestHeader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the request header"
argument_list|,
name|requestHeader
argument_list|,
name|HEADER
argument_list|)
expr_stmt|;
name|Object
name|responseHeader
init|=
name|ctx
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"the response header should be null"
argument_list|,
name|responseHeader
argument_list|)
expr_stmt|;
name|Object
name|outMessageHeader
init|=
name|outMessage
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
literal|"the outMessage PROTOCOL_HEADERS should be update"
argument_list|,
name|responseHeader
argument_list|,
name|outMessageHeader
argument_list|)
expr_stmt|;
name|Object
name|inAttachments
init|=
name|ctx
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"inbound attachments object must be initialized"
argument_list|,
name|inAttachments
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"inbound attachments must be in a Map"
argument_list|,
name|inAttachments
operator|instanceof
name|Map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"no inbound attachments expected"
argument_list|,
operator|(
operator|(
name|Map
operator|)
name|inAttachments
operator|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|testCreateWebServiceContextWithInAttachments
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
operator|new
name|LinkedList
argument_list|<
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|DataSource
name|source
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|,
literal|"text/xml"
argument_list|)
decl_stmt|;
name|DataHandler
name|handler1
init|=
operator|new
name|DataHandler
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|attachments
operator|.
name|add
argument_list|(
operator|new
name|AttachmentImpl
argument_list|(
literal|"part1"
argument_list|,
name|handler1
argument_list|)
argument_list|)
expr_stmt|;
name|DataHandler
name|handler2
init|=
operator|new
name|DataHandler
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|attachments
operator|.
name|add
argument_list|(
operator|new
name|AttachmentImpl
argument_list|(
literal|"part2"
argument_list|,
name|handler2
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|putAll
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
expr_stmt|;
name|MessageContext
name|ctx
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
decl_stmt|;
name|Object
name|inAttachments
init|=
name|ctx
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"inbound attachments object must be initialized"
argument_list|,
name|inAttachments
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"inbound attachments must be in a Map"
argument_list|,
name|inAttachments
operator|instanceof
name|Map
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|dataHandlers
init|=
operator|(
name|Map
operator|)
name|inAttachments
decl_stmt|;
name|assertEquals
argument_list|(
literal|"two inbound attachments expected"
argument_list|,
literal|2
argument_list|,
name|dataHandlers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"part1 attachment is missing"
argument_list|,
name|dataHandlers
operator|.
name|containsKey
argument_list|(
literal|"part1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// should do as it's the same instance
name|assertTrue
argument_list|(
literal|"part1 handler is missing"
argument_list|,
name|dataHandlers
operator|.
name|get
argument_list|(
literal|"part1"
argument_list|)
operator|==
name|handler1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"part2 attachment is missing"
argument_list|,
name|dataHandlers
operator|.
name|containsKey
argument_list|(
literal|"part2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"part2 handler is missing"
argument_list|,
name|dataHandlers
operator|.
name|get
argument_list|(
literal|"part2"
argument_list|)
operator|==
name|handler2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

