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
name|Encoded
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
name|Provider
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
name|MessageContext
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
name|utils
operator|.
name|AnnotationUtils
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
name|ExceptionUtils
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
name|FormUtils
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
name|multipart
operator|.
name|AttachmentUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_class
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/x-www-form-urlencoded"
block|,
literal|"multipart/form-data"
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
block|{
literal|"application/x-www-form-urlencoded"
block|,
literal|"multipart/form-data"
block|}
argument_list|)
annotation|@
name|Provider
specifier|public
class|class
name|FormEncodingProvider
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|FormValidator
name|validator
decl_stmt|;
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|String
name|attachmentDir
decl_stmt|;
specifier|private
name|String
name|attachmentThreshold
decl_stmt|;
specifier|private
name|String
name|attachmentMaxSize
decl_stmt|;
specifier|private
name|boolean
name|expectEncoded
decl_stmt|;
specifier|public
name|FormEncodingProvider
parameter_list|()
block|{              }
specifier|public
name|FormEncodingProvider
parameter_list|(
name|boolean
name|expectEncoded
parameter_list|)
block|{
name|this
operator|.
name|expectEncoded
operator|=
name|expectEncoded
expr_stmt|;
block|}
specifier|public
name|void
name|setExpectedEncoded
parameter_list|(
name|boolean
name|expect
parameter_list|)
block|{
name|this
operator|.
name|expectEncoded
operator|=
name|expect
expr_stmt|;
block|}
specifier|public
name|void
name|setAttachmentDirectory
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|attachmentDir
operator|=
name|dir
expr_stmt|;
block|}
specifier|public
name|void
name|setAttachmentThreshold
parameter_list|(
name|String
name|threshold
parameter_list|)
block|{
name|attachmentThreshold
operator|=
name|threshold
expr_stmt|;
block|}
specifier|public
name|void
name|setAttachmentMaxSize
parameter_list|(
name|String
name|maxSize
parameter_list|)
block|{
name|attachmentMaxSize
operator|=
name|maxSize
expr_stmt|;
block|}
specifier|public
name|void
name|setValidator
parameter_list|(
name|FormValidator
name|formValidator
parameter_list|)
block|{
name|validator
operator|=
name|formValidator
expr_stmt|;
block|}
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
name|mt
parameter_list|)
block|{
return|return
name|isSupported
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mt
argument_list|)
return|;
block|}
specifier|public
name|T
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
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
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
if|if
condition|(
name|mt
operator|.
name|isCompatible
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA_TYPE
argument_list|)
condition|)
block|{
name|MultipartBody
name|body
init|=
name|AttachmentUtils
operator|.
name|getMultipartBody
argument_list|(
name|mc
argument_list|)
decl_stmt|;
if|if
condition|(
name|MultipartBody
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|body
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Attachment
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|body
operator|.
name|getRootAttachment
argument_list|()
argument_list|)
return|;
block|}
block|}
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|createMap
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|populateMap
argument_list|(
name|params
argument_list|,
name|annotations
argument_list|,
name|is
argument_list|,
name|mt
argument_list|,
operator|!
name|keepEncoded
argument_list|(
name|annotations
argument_list|)
argument_list|)
expr_stmt|;
name|validateMap
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|persistParamsOnMessage
argument_list|(
name|params
argument_list|)
expr_stmt|;
return|return
name|getFormObject
argument_list|(
name|clazz
argument_list|,
name|params
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toBadRequestException
argument_list|(
name|e
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|boolean
name|keepEncoded
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
return|return
name|AnnotationUtils
operator|.
name|getAnnotation
argument_list|(
name|anns
argument_list|,
name|Encoded
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|||
name|expectEncoded
return|;
block|}
specifier|protected
name|void
name|persistParamsOnMessage
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|FormUtils
operator|.
name|FORM_PARAM_MAP
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createMap
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|clazz
operator|==
name|MultivaluedMap
operator|.
name|class
operator|||
name|clazz
operator|==
name|Form
operator|.
name|class
condition|)
block|{
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
return|;
block|}
return|return
operator|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|clazz
operator|.
name|newInstance
argument_list|()
return|;
block|}
specifier|private
name|T
name|getFormObject
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|Form
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|?
operator|new
name|Form
argument_list|(
name|params
argument_list|)
else|:
name|params
argument_list|)
return|;
block|}
comment|/**      * Retrieve map of parameters from the passed in message      *      * @param message      * @return a Map of parameters.      */
specifier|protected
name|void
name|populateMap
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|boolean
name|decode
parameter_list|)
block|{
if|if
condition|(
name|mt
operator|.
name|isCompatible
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA_TYPE
argument_list|)
condition|)
block|{
name|MultipartBody
name|body
init|=
name|AttachmentUtils
operator|.
name|getMultipartBody
argument_list|(
name|mc
argument_list|,
name|attachmentDir
argument_list|,
name|attachmentThreshold
argument_list|,
name|attachmentMaxSize
argument_list|)
decl_stmt|;
name|FormUtils
operator|.
name|populateMapFromMultipart
argument_list|(
name|params
argument_list|,
name|body
argument_list|,
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|decode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|enc
init|=
name|HttpUtils
operator|.
name|getEncoding
argument_list|(
name|mt
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|Object
name|servletRequest
init|=
name|mc
operator|!=
literal|null
condition|?
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|servletRequest
operator|==
literal|null
condition|)
block|{
name|FormUtils
operator|.
name|populateMapFromString
argument_list|(
name|params
argument_list|,
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|FormUtils
operator|.
name|readBody
argument_list|(
name|is
argument_list|,
name|enc
argument_list|)
argument_list|,
name|enc
argument_list|,
name|decode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|FormUtils
operator|.
name|populateMapFromString
argument_list|(
name|params
argument_list|,
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|FormUtils
operator|.
name|readBody
argument_list|(
name|is
argument_list|,
name|enc
argument_list|)
argument_list|,
name|enc
argument_list|,
name|decode
argument_list|,
operator|(
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
operator|)
name|servletRequest
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|validateMap
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|validate
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
block|}
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
name|mt
parameter_list|)
block|{
return|return
name|isSupported
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|,
name|mt
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isSupported
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
name|mt
parameter_list|)
block|{
return|return
operator|(
name|MultivaluedMap
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Form
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|)
operator|||
operator|(
name|mt
operator|.
name|getType
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"multipart"
argument_list|)
operator|&&
name|mt
operator|.
name|isCompatible
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA_TYPE
argument_list|)
operator|&&
operator|(
name|MultivaluedMap
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Form
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|)
operator|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|writeTo
parameter_list|(
name|T
name|obj
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
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
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
call|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
call|)
argument_list|(
name|obj
operator|instanceof
name|Form
condition|?
operator|(
operator|(
name|Form
operator|)
name|obj
operator|)
operator|.
name|asMap
argument_list|()
else|:
name|obj
argument_list|)
decl_stmt|;
name|boolean
name|encoded
init|=
name|keepEncoded
argument_list|(
name|anns
argument_list|)
decl_stmt|;
name|String
name|enc
init|=
name|HttpUtils
operator|.
name|getSetEncoding
argument_list|(
name|mt
argument_list|,
name|headers
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|FormUtils
operator|.
name|writeMapToOutputStream
argument_list|(
name|map
argument_list|,
name|os
argument_list|,
name|enc
argument_list|,
name|encoded
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

