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
name|provider
operator|.
name|json
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|Produces
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
name|Context
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
name|MessageBodyReader
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
name|Providers
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
name|AbstractConfigurableProvider
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
name|PrimitiveTextProvider
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

begin_class
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
class|class
name|SimpleTypeJsonProvider
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
implements|,
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Context
specifier|private
name|Providers
name|providers
decl_stmt|;
specifier|private
name|boolean
name|supportSimpleTypesOnly
decl_stmt|;
specifier|private
name|PrimitiveTextProvider
argument_list|<
name|T
argument_list|>
name|primitiveHelper
init|=
operator|new
name|PrimitiveTextProvider
argument_list|<>
argument_list|()
decl_stmt|;
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
operator|!
name|supportSimpleTypesOnly
operator|||
name|primitiveHelper
operator|.
name|isReadable
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getSize
parameter_list|(
name|T
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
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|T
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
operator|!
name|supportSimpleTypesOnly
operator|&&
operator|!
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|type
argument_list|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
name|next
init|=
operator|(
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
operator|)
name|providers
operator|.
name|getMessageBodyWriter
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|)
decl_stmt|;
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|put
argument_list|(
name|ProviderFactory
operator|.
name|ACTIVE_JAXRS_PROVIDER_KEY
argument_list|,
name|this
argument_list|)
expr_stmt|;
try|try
block|{
name|next
operator|.
name|writeTo
argument_list|(
name|t
argument_list|,
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|put
argument_list|(
name|ProviderFactory
operator|.
name|ACTIVE_JAXRS_PROVIDER_KEY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|os
operator|.
name|write
argument_list|(
name|StringUtils
operator|.
name|toBytesASCII
argument_list|(
literal|"{\""
operator|+
name|type
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"\":"
argument_list|)
argument_list|)
expr_stmt|;
name|writeQuote
argument_list|(
name|os
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|primitiveHelper
operator|.
name|writeTo
argument_list|(
name|t
argument_list|,
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|writeQuote
argument_list|(
name|os
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|StringUtils
operator|.
name|toBytesASCII
argument_list|(
literal|"}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeQuote
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|type
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|StringUtils
operator|.
name|toBytesASCII
argument_list|(
literal|"\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setSupportSimpleTypesOnly
parameter_list|(
name|boolean
name|supportSimpleTypesOnly
parameter_list|)
block|{
name|this
operator|.
name|supportSimpleTypesOnly
operator|=
name|supportSimpleTypesOnly
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReadable
parameter_list|(
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
name|isWriteable
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|T
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
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
if|if
condition|(
operator|!
name|supportSimpleTypesOnly
operator|&&
operator|!
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
name|next
init|=
name|providers
operator|.
name|getMessageBodyReader
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|)
decl_stmt|;
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|put
argument_list|(
name|ProviderFactory
operator|.
name|ACTIVE_JAXRS_PROVIDER_KEY
argument_list|,
name|this
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|next
operator|.
name|readFrom
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|,
name|headers
argument_list|,
name|is
argument_list|)
return|;
block|}
finally|finally
block|{
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|put
argument_list|(
name|ProviderFactory
operator|.
name|ACTIVE_JAXRS_PROVIDER_KEY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|data
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|is
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|data
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|data
operator|=
name|data
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|data
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|data
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|data
operator|=
name|data
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|data
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|primitiveHelper
operator|.
name|readFrom
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mediaType
argument_list|,
name|headers
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|data
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

