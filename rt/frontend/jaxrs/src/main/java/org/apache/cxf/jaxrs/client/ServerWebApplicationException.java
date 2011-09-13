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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|util
operator|.
name|Iterator
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
name|core
operator|.
name|Response
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
name|Response
operator|.
name|ResponseBuilder
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
name|MessageBodyReader
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
name|util
operator|.
name|StringUtils
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
name|util
operator|.
name|SystemPropertyAction
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
name|IOUtils
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
name|ProviderFactory
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

begin_comment
comment|/**  * Utility Exception class which makes it easier to get the response status,  * headers and error message if any  */
end_comment

begin_class
specifier|public
class|class
name|ServerWebApplicationException
extends|extends
name|WebApplicationException
block|{
specifier|private
name|String
name|errorMessage
decl_stmt|;
specifier|public
name|ServerWebApplicationException
parameter_list|()
block|{              }
specifier|public
name|ServerWebApplicationException
parameter_list|(
name|Response
name|response
parameter_list|)
block|{
name|super
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerWebApplicationException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|getResponse
parameter_list|()
block|{
name|Response
name|response
init|=
name|super
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|ResponseBuilder
name|rb
init|=
name|Response
operator|.
name|status
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|response
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|header
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|header
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|rb
operator|.
name|header
argument_list|(
name|header
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|rb
operator|.
name|entity
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|getMessage
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|rb
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
name|int
name|getStatus
parameter_list|()
block|{
return|return
name|super
operator|.
name|getResponse
argument_list|()
operator|.
name|getStatus
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
call|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
call|)
argument_list|(
operator|(
name|MultivaluedMap
operator|)
name|super
operator|.
name|getResponse
argument_list|()
operator|.
name|getMetadata
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
if|if
condition|(
name|errorMessage
operator|==
literal|null
condition|)
block|{
name|errorMessage
operator|=
name|readErrorMessage
argument_list|()
expr_stmt|;
block|}
return|return
name|errorMessage
return|;
block|}
specifier|private
name|String
name|readErrorMessage
parameter_list|()
block|{
name|Object
name|entity
init|=
name|super
operator|.
name|getResponse
argument_list|()
operator|.
name|getEntity
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|entity
operator|==
literal|null
condition|?
literal|""
else|:
name|entity
operator|instanceof
name|InputStream
condition|?
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
operator|(
name|InputStream
operator|)
name|entity
argument_list|)
else|:
name|entity
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|lineSep
init|=
name|SystemPropertyAction
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Status : "
operator|+
name|getStatus
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|lineSep
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Headers : "
argument_list|)
operator|.
name|append
argument_list|(
name|lineSep
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|getHeaders
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|header
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|header
operator|+
literal|" :"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|headers
operator|.
name|get
argument_list|(
name|header
argument_list|)
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
name|lineSep
argument_list|)
expr_stmt|;
block|}
name|String
name|message
init|=
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"Error message : "
argument_list|)
operator|.
name|append
argument_list|(
name|lineSep
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|message
argument_list|)
operator|.
name|append
argument_list|(
name|lineSep
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the typed error message       * @param client the client      * @param cls the entity class      * @return the typed entity      */
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
name|toErrorObject
parameter_list|(
name|Client
name|client
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entityCls
parameter_list|)
block|{
name|Response
name|response
init|=
name|getResponse
argument_list|()
decl_stmt|;
try|try
block|{
name|MultivaluedMap
name|headers
init|=
name|response
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
name|Object
name|contentType
init|=
name|headers
operator|.
name|getFirst
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
name|InputStream
name|inputStream
init|=
operator|(
name|InputStream
operator|)
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentType
operator|==
literal|null
operator|||
name|inputStream
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Annotation
index|[]
name|annotations
init|=
operator|new
name|Annotation
index|[]
block|{}
decl_stmt|;
name|MediaType
name|mt
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Endpoint
name|ep
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getConduitSelector
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
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
name|inMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|ep
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
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|inMessage
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
name|ProviderFactory
name|pf
init|=
operator|(
name|ProviderFactory
operator|)
name|ep
operator|.
name|get
argument_list|(
name|ProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|MessageBodyReader
name|reader
init|=
name|pf
operator|.
name|createMessageBodyReader
argument_list|(
name|entityCls
argument_list|,
name|entityCls
argument_list|,
name|annotations
argument_list|,
name|mt
argument_list|,
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|T
operator|)
name|reader
operator|.
name|readFrom
argument_list|(
name|entityCls
argument_list|,
name|entityCls
argument_list|,
name|annotations
argument_list|,
name|mt
argument_list|,
operator|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|headers
argument_list|,
name|inputStream
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ClientWebApplicationException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

