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
name|jaxrs
operator|.
name|sse
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|InternalServerErrorException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|sse
operator|.
name|OutboundSseEvent
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
name|provider
operator|.
name|ServerProviderFactory
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

begin_class
annotation|@
name|Provider
specifier|public
class|class
name|OutboundSseEventBodyWriter
implements|implements
name|MessageBodyWriter
argument_list|<
name|OutboundSseEvent
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_SENT_EVENTS
init|=
literal|"text/event-stream"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|MediaType
name|SERVER_SENT_EVENTS_TYPE
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
name|SERVER_SENT_EVENTS
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|COMMENT
init|=
literal|": "
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|EVENT
init|=
literal|"    "
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|ID
init|=
literal|"id: "
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|RETRY
init|=
literal|"retry: "
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|DATA
init|=
literal|"data: "
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|NEW_LINE
init|=
literal|"\n"
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
specifier|private
name|ServerProviderFactory
name|factory
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|protected
name|OutboundSseEventBodyWriter
parameter_list|()
block|{     }
specifier|public
name|OutboundSseEventBodyWriter
parameter_list|(
specifier|final
name|ServerProviderFactory
name|factory
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|message
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|this
operator|.
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|OutboundSseEvent
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
operator|||
name|SERVER_SENT_EVENTS_TYPE
operator|.
name|isCompatible
argument_list|(
name|mt
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutboundSseEvent
name|p
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
if|if
condition|(
name|p
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|p
operator|.
name|getName
argument_list|()
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|ID
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|p
operator|.
name|getId
argument_list|()
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|getComment
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|COMMENT
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|p
operator|.
name|getComment
argument_list|()
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|getReconnectDelay
argument_list|()
operator|>
literal|0
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|RETRY
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|p
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|getData
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|payloadClass
init|=
name|p
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Type
name|payloadType
init|=
name|p
operator|.
name|getGenericType
argument_list|()
decl_stmt|;
if|if
condition|(
name|payloadType
operator|==
literal|null
condition|)
block|{
name|payloadType
operator|=
name|payloadClass
expr_stmt|;
block|}
if|if
condition|(
name|payloadType
operator|==
literal|null
operator|&&
name|payloadClass
operator|==
literal|null
condition|)
block|{
name|payloadType
operator|=
name|Object
operator|.
name|class
expr_stmt|;
name|payloadClass
operator|=
name|Object
operator|.
name|class
expr_stmt|;
block|}
name|os
operator|.
name|write
argument_list|(
name|DATA
argument_list|)
expr_stmt|;
name|writePayloadTo
argument_list|(
name|payloadClass
argument_list|,
name|payloadType
argument_list|,
name|anns
argument_list|,
name|p
operator|.
name|getMediaType
argument_list|()
argument_list|,
name|headers
argument_list|,
name|p
operator|.
name|getData
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|writePayloadTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Object
name|data
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
name|writer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|factory
operator|!=
literal|null
condition|)
block|{
name|writer
operator|=
name|factory
operator|.
name|createMessageBodyWriter
argument_list|(
name|cls
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InternalServerErrorException
argument_list|(
literal|"No suitable message body writer for class: "
operator|+
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|writer
operator|.
name|writeTo
argument_list|(
operator|(
name|T
operator|)
name|data
argument_list|,
name|cls
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getSize
parameter_list|(
name|OutboundSseEvent
name|t
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mediaType
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

