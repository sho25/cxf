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
name|client
operator|.
name|spec
package|;
end_package

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
name|net
operator|.
name|URI
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
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Client
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
name|client
operator|.
name|ClientRequestContext
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
name|Configuration
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
name|GenericEntity
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
name|HttpHeaders
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
name|ClientProviderFactory
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
name|impl
operator|.
name|AbstractRequestContextImpl
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
name|utils
operator|.
name|HttpUtils
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
name|utils
operator|.
name|InjectionUtils
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
name|utils
operator|.
name|JAXRSUtils
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
name|MessageContentsList
import|;
end_import

begin_class
specifier|public
class|class
name|ClientRequestContextImpl
extends|extends
name|AbstractRequestContextImpl
implements|implements
name|ClientRequestContext
block|{
specifier|public
name|ClientRequestContextImpl
parameter_list|(
name|Message
name|m
parameter_list|,
name|boolean
name|responseContext
parameter_list|)
block|{
name|super
argument_list|(
name|m
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasEntity
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|mt
init|=
name|HttpUtils
operator|.
name|getModifiableHeaders
argument_list|(
name|m
argument_list|)
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
return|return
name|mt
operator|instanceof
name|MediaType
condition|?
operator|(
name|MediaType
operator|)
name|mt
else|:
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|mt
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
operator|(
name|Client
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|Client
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
name|ClientProviderFactory
name|cpf
init|=
name|ClientProviderFactory
operator|.
name|getInstance
argument_list|(
name|m
argument_list|)
decl_stmt|;
return|return
name|cpf
operator|.
name|getDynamicConfiguration
argument_list|()
return|;
block|}
specifier|private
name|Object
name|getMessageContent
parameter_list|()
block|{
name|MessageContentsList
name|objs
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|==
literal|null
operator|||
name|objs
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getEntity
parameter_list|()
block|{
return|return
name|getMessageContent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Annotation
index|[]
name|getEntityAnnotations
parameter_list|()
block|{
name|Annotation
index|[]
name|anns
init|=
operator|(
name|Annotation
index|[]
operator|)
name|m
operator|.
name|get
argument_list|(
name|Annotation
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|anns
operator|==
literal|null
condition|?
operator|new
name|Annotation
index|[]
block|{}
else|:
name|anns
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getEntityClass
parameter_list|()
block|{
name|Object
name|entity
init|=
name|getEntity
argument_list|()
decl_stmt|;
return|return
name|entity
operator|==
literal|null
condition|?
literal|null
else|:
name|entity
operator|.
name|getClass
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Type
name|getEntityType
parameter_list|()
block|{
name|Type
name|t
init|=
name|m
operator|.
name|get
argument_list|(
name|Type
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|t
operator|!=
literal|null
condition|?
name|t
else|:
name|getEntityClass
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|OutputStream
name|getEntityStream
parameter_list|()
block|{
return|return
name|m
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasEntity
parameter_list|()
block|{
return|return
name|getEntity
argument_list|()
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEntity
parameter_list|(
name|Object
name|entity
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
if|if
condition|(
name|mt
operator|!=
literal|null
condition|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|getHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|mt
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|mt
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|anns
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Annotation
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|anns
argument_list|)
expr_stmt|;
block|}
name|doSetEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEntity
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|doSetEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doSetEntity
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|Object
name|actualEntity
init|=
name|InjectionUtils
operator|.
name|getEntity
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|actualEntity
operator|==
literal|null
condition|?
operator|new
name|MessageContentsList
argument_list|()
else|:
operator|new
name|MessageContentsList
argument_list|(
name|actualEntity
argument_list|)
argument_list|)
expr_stmt|;
name|Type
name|type
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|GenericEntity
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|entity
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|type
operator|=
operator|(
operator|(
name|GenericEntity
argument_list|<
name|?
argument_list|>
operator|)
name|entity
operator|)
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|entity
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Type
operator|.
name|class
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
name|String
name|requestURI
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
decl_stmt|;
return|return
name|requestURI
operator|==
literal|null
condition|?
literal|null
else|:
name|URI
operator|.
name|create
argument_list|(
name|requestURI
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEntityStream
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
name|m
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|requestURI
parameter_list|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|requestURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|,
name|requestURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
name|h
operator|=
literal|null
expr_stmt|;
return|return
name|HttpUtils
operator|.
name|getModifiableHeaders
argument_list|(
name|m
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getStringHeaders
parameter_list|()
block|{
name|h
operator|=
literal|null
expr_stmt|;
return|return
name|HttpUtils
operator|.
name|getModifiableStringHeaders
argument_list|(
name|m
argument_list|)
return|;
block|}
block|}
end_class

end_unit

