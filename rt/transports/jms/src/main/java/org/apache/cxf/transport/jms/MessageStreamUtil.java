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
name|jms
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|Message
import|;
end_import

begin_class
specifier|final
class|class
name|MessageStreamUtil
block|{
specifier|private
name|MessageStreamUtil
parameter_list|()
block|{     }
comment|/**      * Set Writer or OutputStream in message that calls the sender on close with      * the content of the stream      *      * @param message where to set the content      * @param isTextPayload decides about stream type true:Writer, false: OutputStream      * @param sender will be called on close      */
specifier|public
specifier|static
name|void
name|prepareStream
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|,
name|boolean
name|isTextPayload
parameter_list|,
specifier|final
name|JMSExchangeSender
name|sender
parameter_list|)
block|{
if|if
condition|(
name|isTextPayload
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|,
operator|new
name|SendingWriter
argument_list|(
name|sender
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SendingOutputStream
name|out
init|=
operator|new
name|SendingOutputStream
argument_list|(
name|sender
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|SendingWriter
extends|extends
name|StringWriter
block|{
specifier|private
specifier|final
name|JMSExchangeSender
name|sender
decl_stmt|;
specifier|private
name|Exchange
name|exchange
decl_stmt|;
specifier|private
name|SendingWriter
parameter_list|(
name|JMSExchangeSender
name|sender
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|sender
operator|.
name|sendExchange
argument_list|(
name|exchange
argument_list|,
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|SendingOutputStream
extends|extends
name|CachedOutputStream
block|{
specifier|private
specifier|final
name|JMSExchangeSender
name|sender
decl_stmt|;
specifier|private
name|Exchange
name|exchange
decl_stmt|;
name|SendingOutputStream
parameter_list|(
name|JMSExchangeSender
name|sender
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doClose
parameter_list|()
throws|throws
name|IOException
block|{
name|this
operator|.
name|sender
operator|.
name|sendExchange
argument_list|(
name|exchange
argument_list|,
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|closeStreams
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|Writer
name|writer
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Reader
name|reader
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

