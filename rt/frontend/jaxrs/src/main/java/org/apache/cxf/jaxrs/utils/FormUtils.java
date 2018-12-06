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
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|HttpMethod
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
name|Form
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|ContentDisposition
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
name|ext
operator|.
name|multipart
operator|.
name|MultipartBody
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
name|provider
operator|.
name|FormEncodingProvider
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
name|MessageUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|FormUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FORM_PARAMS_FROM_HTTP_PARAMS
init|=
literal|"set.form.parameters.from.http.parameters"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FORM_PARAM_MAP
init|=
literal|"org.apache.cxf.form_data"
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
name|FormUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MULTIPART_FORM_DATA_TYPE
init|=
literal|"form-data"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MAX_FORM_PARAM_COUNT
init|=
literal|"maxFormParameterCount"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_DISPOSITION_FILES_PARAM
init|=
literal|"files"
decl_stmt|;
specifier|private
name|FormUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|String
name|formToString
parameter_list|(
name|Form
name|form
parameter_list|)
block|{
try|try
init|(
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|FormUtils
operator|.
name|writeMapToOutputStream
argument_list|(
name|form
operator|.
name|asMap
argument_list|()
argument_list|,
name|bos
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|bos
operator|.
name|toString
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
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
comment|// will not happen
block|}
return|return
literal|""
return|;
block|}
specifier|public
specifier|static
name|void
name|restoreForm
parameter_list|(
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|provider
parameter_list|,
name|Form
name|form
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|CachedOutputStream
name|os
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|writeForm
argument_list|(
name|provider
argument_list|,
name|form
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|os
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeForm
parameter_list|(
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|provider
parameter_list|,
name|Form
name|form
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
name|provider
operator|.
name|writeTo
argument_list|(
name|form
argument_list|,
name|Form
operator|.
name|class
argument_list|,
name|Form
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Form
name|readForm
parameter_list|(
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|provider
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|provider
operator|.
name|readFrom
argument_list|(
name|Form
operator|.
name|class
argument_list|,
name|Form
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|addPropertyToForm
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|map
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
init|=
name|InjectionUtils
operator|.
name|extractValuesFromBean
argument_list|(
name|value
argument_list|,
literal|""
argument_list|)
decl_stmt|;
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
name|Object
argument_list|>
argument_list|>
name|entry
range|:
name|values
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|Object
name|v
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|map
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|v
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
specifier|static
name|String
name|readBody
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
try|try
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|encoding
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
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|void
name|populateMapFromString
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|Message
name|m
parameter_list|,
name|String
name|postBody
parameter_list|,
name|String
name|enc
parameter_list|,
name|boolean
name|decode
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|postBody
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
index|[]
name|parts
init|=
name|postBody
operator|.
name|split
argument_list|(
literal|"&"
argument_list|)
decl_stmt|;
name|checkNumberOfParts
argument_list|(
name|m
argument_list|,
name|parts
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|String
index|[]
name|keyValue
init|=
operator|new
name|String
index|[
literal|2
index|]
decl_stmt|;
name|int
name|index
init|=
name|part
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|keyValue
index|[
literal|0
index|]
operator|=
name|part
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|keyValue
index|[
literal|1
index|]
operator|=
name|index
operator|+
literal|1
operator|<
name|part
operator|.
name|length
argument_list|()
condition|?
name|part
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
else|:
literal|""
expr_stmt|;
block|}
else|else
block|{
name|keyValue
index|[
literal|0
index|]
operator|=
name|part
expr_stmt|;
name|keyValue
index|[
literal|1
index|]
operator|=
literal|""
expr_stmt|;
block|}
name|String
name|name
init|=
name|HttpUtils
operator|.
name|urlDecode
argument_list|(
name|keyValue
index|[
literal|0
index|]
argument_list|,
name|enc
argument_list|)
decl_stmt|;
if|if
condition|(
name|decode
condition|)
block|{
name|params
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|HttpUtils
operator|.
name|urlDecode
argument_list|(
name|keyValue
index|[
literal|1
index|]
argument_list|,
name|enc
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|params
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|keyValue
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|populateMapFromStringOrHttpRequest
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|Message
name|m
parameter_list|,
name|String
name|postBody
parameter_list|,
name|String
name|enc
parameter_list|,
name|boolean
name|decode
parameter_list|)
block|{
name|HttpServletRequest
name|request
init|=
operator|(
name|HttpServletRequest
operator|)
name|m
operator|.
name|get
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_REQUEST
argument_list|)
decl_stmt|;
name|populateMapFromString
argument_list|(
name|params
argument_list|,
name|m
argument_list|,
name|postBody
argument_list|,
name|enc
argument_list|,
name|decode
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|populateMapFromString
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|Message
name|m
parameter_list|,
name|String
name|postBody
parameter_list|,
name|String
name|enc
parameter_list|,
name|boolean
name|decode
parameter_list|,
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
name|request
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|postBody
argument_list|)
condition|)
block|{
name|populateMapFromString
argument_list|(
name|params
argument_list|,
name|m
argument_list|,
name|postBody
argument_list|,
name|enc
argument_list|,
name|decode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|request
operator|!=
literal|null
operator|&&
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|m
argument_list|,
name|FORM_PARAMS_FROM_HTTP_PARAMS
argument_list|,
literal|true
argument_list|)
condition|)
block|{
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|en
init|=
name|request
operator|.
name|getParameterNames
argument_list|()
init|;
name|en
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|paramName
init|=
name|en
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
index|[]
name|values
init|=
name|request
operator|.
name|getParameterValues
argument_list|(
name|paramName
argument_list|)
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
name|HttpUtils
operator|.
name|urlDecode
argument_list|(
name|paramName
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|logRequestParametersIfNeeded
argument_list|(
name|params
argument_list|,
name|enc
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|logRequestParametersIfNeeded
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|params
parameter_list|,
name|String
name|enc
parameter_list|)
block|{
if|if
condition|(
operator|(
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|==
literal|null
operator|)
operator|||
operator|(
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
return|return;
block|}
name|String
name|chain
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|chain
operator|.
name|contains
argument_list|(
literal|"LoggingInInterceptor"
argument_list|)
condition|)
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|writeMapToOutputStream
argument_list|(
name|params
argument_list|,
name|bos
argument_list|,
name|enc
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|bos
operator|.
name|toString
argument_list|(
name|enc
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|writeMapToOutputStream
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|map
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|String
name|enc
parameter_list|,
name|boolean
name|encoded
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Iterator
argument_list|<
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
argument_list|>
name|it
init|=
name|map
operator|.
name|entrySet
argument_list|()
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
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|encoded
condition|)
block|{
name|key
operator|=
name|HttpUtils
operator|.
name|urlEncode
argument_list|(
name|key
argument_list|,
name|enc
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|entryIterator
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|os
operator|.
name|write
argument_list|(
name|key
operator|.
name|getBytes
argument_list|(
name|enc
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|entryIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|encoded
condition|)
block|{
name|value
operator|=
name|HttpUtils
operator|.
name|urlEncode
argument_list|(
name|value
argument_list|,
name|enc
argument_list|)
expr_stmt|;
block|}
name|os
operator|.
name|write
argument_list|(
name|value
operator|.
name|getBytes
argument_list|(
name|enc
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
literal|'&'
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
literal|'&'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|populateMapFromMultipart
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|MultipartBody
name|body
parameter_list|,
name|Message
name|m
parameter_list|,
name|boolean
name|decode
parameter_list|)
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|body
operator|.
name|getAllAttachments
argument_list|()
decl_stmt|;
name|checkNumberOfParts
argument_list|(
name|m
argument_list|,
name|atts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|atts
control|)
block|{
name|ContentDisposition
name|cd
init|=
name|a
operator|.
name|getContentDisposition
argument_list|()
decl_stmt|;
if|if
condition|(
name|cd
operator|!=
literal|null
operator|&&
operator|!
name|MULTIPART_FORM_DATA_TYPE
operator|.
name|equalsIgnoreCase
argument_list|(
name|cd
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|cdName
init|=
name|cd
operator|==
literal|null
condition|?
literal|null
else|:
name|cd
operator|.
name|getParameter
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|contentId
init|=
name|a
operator|.
name|getContentId
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|cdName
argument_list|)
condition|?
name|contentId
else|:
name|cdName
operator|.
name|replace
argument_list|(
literal|"\""
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"'"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toBadRequestException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
if|if
condition|(
name|CONTENT_DISPOSITION_FILES_PARAM
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// this is a reserved name in Content-Disposition for parts containing files
continue|continue;
block|}
try|try
block|{
name|String
name|value
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|HttpUtils
operator|.
name|urlDecode
argument_list|(
name|name
argument_list|)
argument_list|,
name|decode
condition|?
name|HttpUtils
operator|.
name|urlDecode
argument_list|(
name|value
argument_list|)
else|:
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Illegal URL-encoded characters, make sure that no "
operator|+
literal|"@FormParam and @Multipart annotations are mixed up"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toBadRequestException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|checkNumberOfParts
parameter_list|(
name|Message
name|m
parameter_list|,
name|int
name|numberOfParts
parameter_list|)
block|{
if|if
condition|(
name|m
operator|==
literal|null
operator|||
name|m
operator|.
name|getExchange
argument_list|()
operator|==
literal|null
operator|||
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|maxPartsCountProp
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|MAX_FORM_PARAM_COUNT
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxPartsCountProp
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|int
name|maxPartsCount
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|maxPartsCountProp
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxPartsCount
operator|!=
operator|-
literal|1
operator|&&
name|numberOfParts
operator|>=
name|maxPartsCount
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|413
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isFormPostRequest
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
operator|.
name|equals
argument_list|(
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
operator|&&
name|HttpMethod
operator|.
name|POST
operator|.
name|equals
argument_list|(
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

