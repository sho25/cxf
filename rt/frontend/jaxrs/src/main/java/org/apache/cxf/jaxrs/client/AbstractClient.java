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
name|URI
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
name|text
operator|.
name|SimpleDateFormat
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
name|Date
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
name|Cookie
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
name|EntityTag
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
name|core
operator|.
name|UriBuilder
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
name|impl
operator|.
name|MetadataMap
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
name|UriBuilderImpl
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
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractClient
implements|implements
name|Client
block|{
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestHeaders
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ResponseBuilder
name|responseBuilder
decl_stmt|;
specifier|private
name|URI
name|baseURI
decl_stmt|;
specifier|private
name|UriBuilder
name|currentBuilder
decl_stmt|;
specifier|protected
name|AbstractClient
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|URI
name|currentURI
parameter_list|)
block|{
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
name|this
operator|.
name|currentBuilder
operator|=
operator|new
name|UriBuilderImpl
argument_list|(
name|currentURI
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|)
block|{
name|this
operator|.
name|baseURI
operator|=
name|client
operator|.
name|getCurrentURI
argument_list|()
expr_stmt|;
name|this
operator|.
name|currentBuilder
operator|=
operator|new
name|UriBuilderImpl
argument_list|(
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|inheritHeaders
condition|)
block|{
name|this
operator|.
name|requestHeaders
operator|=
name|client
operator|.
name|getHeaders
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Client
name|header
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
if|if
condition|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|values
operator|.
name|length
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
for|for
control|(
name|Object
name|o
range|:
name|values
control|)
block|{
name|requestHeaders
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|headers
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|requestHeaders
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|accept
parameter_list|(
name|MediaType
modifier|...
name|types
parameter_list|)
block|{
for|for
control|(
name|MediaType
name|mt
range|:
name|types
control|)
block|{
name|requestHeaders
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
name|mt
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|type
parameter_list|(
name|MediaType
name|ct
parameter_list|)
block|{
return|return
name|type
argument_list|(
name|ct
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Client
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|requestHeaders
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|accept
parameter_list|(
name|String
modifier|...
name|types
parameter_list|)
block|{
for|for
control|(
name|String
name|type
range|:
name|types
control|)
block|{
name|requestHeaders
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|cookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
block|{
name|requestHeaders
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|,
name|cookie
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|modified
parameter_list|(
name|Date
name|date
parameter_list|,
name|boolean
name|ifNot
parameter_list|)
block|{
name|SimpleDateFormat
name|dateFormat
init|=
name|HttpUtils
operator|.
name|getHttpDateFormat
argument_list|()
decl_stmt|;
name|String
name|hName
init|=
name|ifNot
condition|?
name|HttpHeaders
operator|.
name|IF_UNMODIFIED_SINCE
else|:
name|HttpHeaders
operator|.
name|IF_MODIFIED_SINCE
decl_stmt|;
name|requestHeaders
operator|.
name|putSingle
argument_list|(
name|hName
argument_list|,
name|dateFormat
operator|.
name|format
argument_list|(
name|date
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|language
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|requestHeaders
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
name|language
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|match
parameter_list|(
name|EntityTag
name|tag
parameter_list|,
name|boolean
name|ifNot
parameter_list|)
block|{
name|String
name|hName
init|=
name|ifNot
condition|?
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
else|:
name|HttpHeaders
operator|.
name|IF_MATCH
decl_stmt|;
name|requestHeaders
operator|.
name|putSingle
argument_list|(
name|hName
argument_list|,
name|tag
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|acceptLanguage
parameter_list|(
name|String
modifier|...
name|languages
parameter_list|)
block|{
for|for
control|(
name|String
name|s
range|:
name|languages
control|)
block|{
name|requestHeaders
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|acceptEncoding
parameter_list|(
name|String
modifier|...
name|encs
parameter_list|)
block|{
for|for
control|(
name|String
name|s
range|:
name|encs
control|)
block|{
name|requestHeaders
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Client
name|encoding
parameter_list|(
name|String
name|enc
parameter_list|)
block|{
name|requestHeaders
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
argument_list|,
name|enc
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|protected
name|List
argument_list|<
name|MediaType
argument_list|>
name|getAccept
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|headers
init|=
name|requestHeaders
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
operator|||
name|headers
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
name|List
argument_list|<
name|MediaType
argument_list|>
name|types
init|=
operator|new
name|ArrayList
argument_list|<
name|MediaType
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|headers
control|)
block|{
name|types
operator|.
name|add
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|types
return|;
block|}
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
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|requestHeaders
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|protected
name|MediaType
name|getType
parameter_list|()
block|{
name|String
name|type
init|=
name|requestHeaders
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
return|return
name|type
operator|==
literal|null
condition|?
literal|null
else|:
name|MediaType
operator|.
name|valueOf
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|URI
name|getBaseURI
parameter_list|()
block|{
return|return
name|baseURI
return|;
block|}
specifier|public
name|URI
name|getCurrentURI
parameter_list|()
block|{
return|return
name|getCurrentBuilder
argument_list|()
operator|.
name|clone
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|UriBuilder
name|getCurrentBuilder
parameter_list|()
block|{
return|return
name|currentBuilder
return|;
block|}
specifier|public
name|Response
name|getResponse
parameter_list|()
block|{
if|if
condition|(
name|responseBuilder
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
name|Response
name|r
init|=
name|responseBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|responseBuilder
operator|=
literal|null
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|public
name|Client
name|reset
parameter_list|()
block|{
name|requestHeaders
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resetResponse
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|protected
name|void
name|resetResponse
parameter_list|()
block|{
name|responseBuilder
operator|=
literal|null
expr_stmt|;
block|}
specifier|protected
name|void
name|resetBaseAddress
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|baseURI
operator|=
name|uri
expr_stmt|;
name|resetCurrentBuilder
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|resetCurrentBuilder
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|currentBuilder
operator|=
operator|new
name|UriBuilderImpl
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ResponseBuilder
name|setResponseBuilder
parameter_list|(
name|HttpURLConnection
name|conn
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|status
init|=
name|conn
operator|.
name|getResponseCode
argument_list|()
decl_stmt|;
name|responseBuilder
operator|=
name|Response
operator|.
name|status
argument_list|(
name|status
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|conn
operator|.
name|getHeaderFields
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|s
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|responseBuilder
operator|.
name|header
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|status
operator|>=
literal|400
condition|)
block|{
try|try
block|{
name|InputStream
name|errorStream
init|=
name|conn
operator|.
name|getErrorStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|errorStream
operator|!=
literal|null
condition|)
block|{
name|responseBuilder
operator|.
name|entity
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|errorStream
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// nothing we can do really
block|}
block|}
return|return
name|responseBuilder
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
specifier|static
name|void
name|writeBody
parameter_list|(
name|Object
name|o
parameter_list|,
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
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|MediaType
name|contentType
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
name|headers
operator|.
name|getFirst
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
decl_stmt|;
name|MessageBodyWriter
name|mbr
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|createMessageBodyWriter
argument_list|(
name|cls
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
name|contentType
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mbr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|mbr
operator|.
name|writeTo
argument_list|(
name|o
argument_list|,
name|cls
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
name|contentType
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
specifier|static
name|Object
name|readBody
parameter_list|(
name|Response
name|r
parameter_list|,
name|HttpURLConnection
name|conn
parameter_list|,
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
parameter_list|)
block|{
try|try
block|{
name|int
name|status
init|=
name|conn
operator|.
name|getResponseCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
argument_list|<
literal|200
operator|||
name|status
operator|==
literal|204
operator|||
name|status
argument_list|>
literal|300
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// won't happen at this stage
block|}
name|MediaType
name|contentType
init|=
name|getResponseContentType
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|MessageBodyReader
name|mbr
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|createMessageBodyReader
argument_list|(
name|cls
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
name|contentType
argument_list|,
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mbr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|mbr
operator|.
name|readFrom
argument_list|(
name|cls
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
name|contentType
argument_list|,
name|r
operator|.
name|getMetadata
argument_list|()
argument_list|,
name|conn
operator|.
name|getInputStream
argument_list|()
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
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
specifier|private
specifier|static
name|MediaType
name|getResponseContentType
parameter_list|(
name|Response
name|r
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|r
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
condition|)
block|{
return|return
name|MediaType
operator|.
name|valueOf
argument_list|(
name|map
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
name|MediaType
operator|.
name|WILDCARD_TYPE
return|;
block|}
specifier|protected
specifier|static
name|HttpURLConnection
name|createHttpConnection
parameter_list|(
name|URI
name|uri
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
try|try
block|{
name|URL
name|url
init|=
name|uri
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|HttpURLConnection
name|connect
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connect
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connect
operator|.
name|setRequestMethod
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
name|connect
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
name|WebApplicationException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|static
name|void
name|setAllHeaders
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|HttpURLConnection
name|conn
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
name|conn
operator|.
name|setRequestProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

