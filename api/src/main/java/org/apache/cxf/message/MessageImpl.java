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
name|message
package|;
end_package

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|IdentityHashMap
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|endpoint
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
name|interceptor
operator|.
name|InterceptorChain
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
name|DelegatingInputStream
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
name|Service
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
name|Destination
import|;
end_import

begin_class
specifier|public
class|class
name|MessageImpl
extends|extends
name|StringMapImpl
implements|implements
name|Message
block|{
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|DEFAULT_CONTENTS
index|[]
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CONTENTS_LENGTH
decl_stmt|;
static|static
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|tmps
index|[]
decl_stmt|;
try|try
block|{
comment|//if SAAJ is there, give it a slot
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"javax.xml.soap.SOAPMessage"
argument_list|)
decl_stmt|;
name|tmps
operator|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|XMLStreamReader
operator|.
name|class
operator|,
name|XMLStreamWriter
operator|.
name|class
operator|,
name|InputStream
operator|.
name|class
operator|,
name|OutputStream
operator|.
name|class
operator|,
name|List
operator|.
name|class
operator|,
name|Exception
operator|.
name|class
operator|,
name|Node
operator|.
name|class
operator|,
name|DelegatingInputStream
operator|.
name|class
operator|,
name|cls
block|}
empty_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|tmps
operator|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|XMLStreamReader
operator|.
name|class
operator|,
name|XMLStreamWriter
operator|.
name|class
operator|,
name|InputStream
operator|.
name|class
operator|,
name|OutputStream
operator|.
name|class
operator|,
name|List
operator|.
name|class
operator|,
name|Exception
operator|.
name|class
operator|,
name|Node
operator|.
name|class
operator|,
name|DelegatingInputStream
operator|.
name|class
block|}
empty_stmt|;
block|}
name|DEFAULT_CONTENTS
operator|=
name|tmps
expr_stmt|;
name|DEFAULT_CONTENTS_LENGTH
operator|=
name|tmps
operator|.
name|length
expr_stmt|;
block|}
specifier|private
name|Exchange
name|exchange
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|InterceptorChain
name|interceptorChain
decl_stmt|;
specifier|private
name|Object
index|[]
name|defaultContents
init|=
operator|new
name|Object
index|[
name|DEFAULT_CONTENTS_LENGTH
index|]
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|contents
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextCache
decl_stmt|;
specifier|public
name|MessageImpl
parameter_list|()
block|{
comment|//nothing
block|}
specifier|public
name|MessageImpl
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|super
argument_list|(
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|instanceof
name|MessageImpl
condition|)
block|{
name|MessageImpl
name|impl
init|=
operator|(
name|MessageImpl
operator|)
name|m
decl_stmt|;
name|exchange
operator|=
name|impl
operator|.
name|getExchange
argument_list|()
expr_stmt|;
name|id
operator|=
name|impl
operator|.
name|id
expr_stmt|;
name|interceptorChain
operator|=
name|impl
operator|.
name|interceptorChain
expr_stmt|;
name|defaultContents
operator|=
name|impl
operator|.
name|defaultContents
expr_stmt|;
name|contents
operator|=
name|impl
operator|.
name|contents
expr_stmt|;
name|contextCache
operator|=
name|impl
operator|.
name|contextCache
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Not a MessageImpl! "
operator|+
name|m
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|getAttachments
parameter_list|()
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|get
argument_list|(
name|ATTACHMENTS
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAttachments
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|put
argument_list|(
name|ATTACHMENTS
argument_list|,
name|attachments
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAttachmentMimeType
parameter_list|()
block|{
comment|//for sub class overriding
return|return
literal|null
return|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|()
block|{
return|return
name|get
argument_list|(
name|Destination
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|InterceptorChain
name|getInterceptorChain
parameter_list|()
block|{
return|return
name|this
operator|.
name|interceptorChain
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|format
parameter_list|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|DEFAULT_CONTENTS_LENGTH
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|DEFAULT_CONTENTS
index|[
name|x
index|]
operator|==
name|format
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|defaultContents
index|[
name|x
index|]
return|;
block|}
block|}
return|return
name|contents
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|T
operator|)
name|contents
operator|.
name|get
argument_list|(
name|format
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|format
parameter_list|,
name|Object
name|content
parameter_list|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|DEFAULT_CONTENTS_LENGTH
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|DEFAULT_CONTENTS
index|[
name|x
index|]
operator|==
name|format
condition|)
block|{
name|defaultContents
index|[
name|x
index|]
operator|=
name|content
expr_stmt|;
return|return;
block|}
block|}
if|if
condition|(
name|contents
operator|==
literal|null
condition|)
block|{
name|contents
operator|=
operator|new
name|IdentityHashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
name|contents
operator|.
name|put
argument_list|(
name|format
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|removeContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|format
parameter_list|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|DEFAULT_CONTENTS_LENGTH
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|DEFAULT_CONTENTS
index|[
name|x
index|]
operator|==
name|format
condition|)
block|{
name|defaultContents
index|[
name|x
index|]
operator|=
literal|null
expr_stmt|;
return|return;
block|}
block|}
if|if
condition|(
name|contents
operator|!=
literal|null
condition|)
block|{
name|contents
operator|.
name|remove
argument_list|(
name|format
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getContentFormats
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|c
decl_stmt|;
if|if
condition|(
name|contents
operator|==
literal|null
condition|)
block|{
name|c
operator|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|c
operator|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|contents
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|DEFAULT_CONTENTS_LENGTH
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|defaultContents
index|[
name|x
index|]
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|add
argument_list|(
name|DEFAULT_CONTENTS
index|[
name|x
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|c
return|;
block|}
specifier|public
name|void
name|setDestination
parameter_list|(
name|Destination
name|d
parameter_list|)
block|{
name|put
argument_list|(
name|Destination
operator|.
name|class
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setExchange
parameter_list|(
name|Exchange
name|e
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|e
expr_stmt|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|i
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|void
name|setInterceptorChain
parameter_list|(
name|InterceptorChain
name|ic
parameter_list|)
block|{
name|this
operator|.
name|interceptorChain
operator|=
name|ic
expr_stmt|;
block|}
specifier|public
name|Object
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|contextCache
operator|!=
literal|null
condition|)
block|{
name|contextCache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getContextualProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|contextCache
operator|==
literal|null
condition|)
block|{
name|calcContextCache
argument_list|()
expr_stmt|;
block|}
return|return
name|contextCache
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|private
name|void
name|calcContextCache
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|o
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|super
operator|.
name|putAll
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|Exchange
name|ex
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
name|Bus
name|b
init|=
name|ex
operator|.
name|getBus
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|o
operator|.
name|putAll
argument_list|(
name|b
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Service
name|sv
init|=
name|ex
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|sv
operator|!=
literal|null
condition|)
block|{
name|o
operator|.
name|putAll
argument_list|(
name|sv
argument_list|)
expr_stmt|;
block|}
name|Endpoint
name|ep
init|=
name|ex
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|ep
operator|!=
literal|null
condition|)
block|{
name|EndpointInfo
name|ei
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|ei
operator|!=
literal|null
condition|)
block|{
name|o
operator|.
name|putAll
argument_list|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|o
operator|.
name|putAll
argument_list|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|o
operator|.
name|putAll
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
block|}
name|o
operator|.
name|putAll
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|o
operator|.
name|putAll
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|contextCache
operator|=
name|o
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|copyContent
parameter_list|(
name|Message
name|m1
parameter_list|,
name|Message
name|m2
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|m1
operator|.
name|getContentFormats
argument_list|()
control|)
block|{
name|m2
operator|.
name|setContent
argument_list|(
name|c
argument_list|,
name|m1
operator|.
name|getContent
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|resetContextCache
parameter_list|()
block|{
if|if
condition|(
name|contextCache
operator|!=
literal|null
condition|)
block|{
name|contextCache
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setContextualProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|v
parameter_list|)
block|{
if|if
condition|(
name|contextCache
operator|!=
literal|null
operator|&&
operator|!
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|contextCache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

