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
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|GenericType
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
name|sse
operator|.
name|OutboundSseEvent
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|OutboundSseEventImpl
implements|implements
name|OutboundSseEvent
block|{
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
specifier|final
name|String
name|comment
decl_stmt|;
specifier|private
specifier|final
name|long
name|reconnectDelay
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
specifier|private
specifier|final
name|Type
name|genericType
decl_stmt|;
specifier|private
specifier|final
name|MediaType
name|mediaType
decl_stmt|;
specifier|private
specifier|final
name|Object
name|data
decl_stmt|;
specifier|public
specifier|static
class|class
name|BuilderImpl
implements|implements
name|Builder
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|comment
decl_stmt|;
specifier|private
name|long
name|reconnectDelay
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
specifier|private
name|Type
name|genericType
decl_stmt|;
specifier|private
name|MediaType
name|mediaType
init|=
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
decl_stmt|;
specifier|private
name|Object
name|data
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Builder
name|id
parameter_list|(
name|String
name|newId
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|newId
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|name
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|newName
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|reconnectDelay
parameter_list|(
name|long
name|milliseconds
parameter_list|)
block|{
name|this
operator|.
name|reconnectDelay
operator|=
name|milliseconds
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|mediaType
parameter_list|(
name|MediaType
name|newMediaType
parameter_list|)
block|{
name|this
operator|.
name|mediaType
operator|=
name|newMediaType
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|comment
parameter_list|(
name|String
name|newComment
parameter_list|)
block|{
name|this
operator|.
name|comment
operator|=
name|newComment
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|Builder
name|data
parameter_list|(
name|Class
name|newType
parameter_list|,
name|Object
name|newData
parameter_list|)
block|{
if|if
condition|(
name|newType
operator|==
literal|null
operator|||
name|newData
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameters 'type' and 'data' must not be null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|type
operator|=
name|newType
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|newData
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|Builder
name|data
parameter_list|(
name|GenericType
name|newType
parameter_list|,
name|Object
name|newData
parameter_list|)
block|{
if|if
condition|(
name|newType
operator|==
literal|null
operator|||
name|newData
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameters 'type' and 'data' must not be null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|genericType
operator|=
name|newType
operator|.
name|getType
argument_list|()
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|newData
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Builder
name|data
parameter_list|(
name|Object
name|newData
parameter_list|)
block|{
if|if
condition|(
name|newData
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'data' must not be null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|type
operator|=
name|newData
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|newData
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|OutboundSseEvent
name|build
parameter_list|()
block|{
return|return
operator|new
name|OutboundSseEventImpl
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|comment
argument_list|,
name|reconnectDelay
argument_list|,
name|type
argument_list|,
name|genericType
argument_list|,
name|mediaType
argument_list|,
name|data
argument_list|)
return|;
block|}
block|}
comment|//CHECKSTYLE:OFF
specifier|private
name|OutboundSseEventImpl
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|comment
parameter_list|,
name|long
name|reconnectDelay
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
name|MediaType
name|mediaType
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|comment
operator|=
name|comment
expr_stmt|;
name|this
operator|.
name|reconnectDelay
operator|=
name|reconnectDelay
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|genericType
operator|=
name|genericType
expr_stmt|;
name|this
operator|.
name|mediaType
operator|=
name|mediaType
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
comment|//CHECKSTYLE:ON
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getComment
parameter_list|()
block|{
return|return
name|comment
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getReconnectDelay
parameter_list|()
block|{
return|return
name|reconnectDelay
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReconnectDelaySet
parameter_list|()
block|{
return|return
name|reconnectDelay
operator|!=
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
specifier|public
name|Type
name|getGenericType
parameter_list|()
block|{
return|return
name|genericType
return|;
block|}
annotation|@
name|Override
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
return|return
name|mediaType
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
block|}
end_class

end_unit

