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
name|local
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
name|PipedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|AbstractWrappedOutputStream
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
name|CachedOutputStream
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|AbstractConduit
import|;
end_import

begin_class
specifier|public
class|class
name|LocalConduit
extends|extends
name|AbstractConduit
block|{
specifier|public
specifier|static
specifier|final
name|String
name|IN_CONDUIT
init|=
name|LocalConduit
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".inConduit"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESPONSE_CONDUIT
init|=
name|LocalConduit
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".inConduit"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IN_EXCHANGE
init|=
name|LocalConduit
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".inExchange"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DIRECT_DISPATCH
init|=
name|LocalConduit
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".directDispatch"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_FILTER_PROPERTIES
init|=
name|LocalTransportFactory
operator|.
name|MESSAGE_FILTER_PROPERTIES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LocalConduit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|LocalDestination
name|destination
decl_stmt|;
specifier|private
name|LocalTransportFactory
name|transportFactory
decl_stmt|;
specifier|public
name|LocalConduit
parameter_list|(
name|LocalTransportFactory
name|transportFactory
parameter_list|,
name|LocalDestination
name|destination
parameter_list|)
block|{
name|super
argument_list|(
name|destination
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
name|this
operator|.
name|transportFactory
operator|=
name|transportFactory
expr_stmt|;
block|}
specifier|public
name|void
name|prepare
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|DIRECT_DISPATCH
argument_list|)
argument_list|)
condition|)
block|{
name|dispatchViaPipe
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// prepare the stream here
name|CachedOutputStream
name|stream
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|DIRECT_DISPATCH
argument_list|)
argument_list|)
operator|&&
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|)
argument_list|)
condition|)
block|{
name|dispatchDirect
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|close
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|dispatchDirect
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|destination
operator|.
name|getMessageObserver
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Local destination does not have a MessageObserver on address "
operator|+
name|destination
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
throw|;
block|}
name|MessageImpl
name|copy
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|copy
operator|.
name|put
argument_list|(
name|IN_CONDUIT
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|copy
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|transportFactory
operator|.
name|copy
argument_list|(
name|message
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|MessageImpl
operator|.
name|copyContent
argument_list|(
name|message
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|stream
init|=
operator|(
name|CachedOutputStream
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
name|copy
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|stream
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create a new incoming exchange and store the original exchange for the response
name|ExchangeImpl
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|copy
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|IN_EXCHANGE
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|destination
operator|.
name|getMessageObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|dispatchViaPipe
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|LocalConduit
name|conduit
init|=
name|this
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|destination
operator|.
name|getMessageObserver
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Local destination does not have a MessageObserver on address "
operator|+
name|destination
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
throw|;
block|}
name|AbstractWrappedOutputStream
name|cout
init|=
operator|new
name|AbstractWrappedOutputStream
argument_list|()
block|{
specifier|protected
name|void
name|onFirstWrite
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|PipedInputStream
name|stream
init|=
operator|new
name|PipedInputStream
argument_list|()
decl_stmt|;
name|wrappedStream
operator|=
operator|new
name|PipedOutputStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
specifier|final
name|Runnable
name|receiver
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|MessageImpl
name|inMsg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|transportFactory
operator|.
name|copy
argument_list|(
name|message
argument_list|,
name|inMsg
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|stream
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|put
argument_list|(
name|IN_CONDUIT
argument_list|,
name|conduit
argument_list|)
expr_stmt|;
name|ExchangeImpl
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|IN_EXCHANGE
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|destination
operator|.
name|getMessageObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
operator|new
name|Thread
argument_list|(
name|receiver
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|cout
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
end_class

end_unit

