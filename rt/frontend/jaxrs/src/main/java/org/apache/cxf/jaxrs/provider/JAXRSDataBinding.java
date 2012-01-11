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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|databinding
operator|.
name|AbstractDataBinding
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
name|databinding
operator|.
name|DataReader
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
name|databinding
operator|.
name|DataWriter
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
name|message
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
name|invoker
operator|.
name|MethodDispatcher
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
name|BindingOperationInfo
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
name|MessagePartInfo
import|;
end_import

begin_comment
comment|/**  * CXF DataBinding implementation wrapping JAX-RS providers  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSDataBinding
extends|extends
name|AbstractDataBinding
block|{
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|SUPPORTED_READER_FORMATS
index|[]
init|=
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
block|}
empty_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|SUPPORTED_WRITER_FORMATS
index|[]
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|XMLStreamWriter
operator|.
name|class
block|}
empty_stmt|;
specifier|private
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|xmlReader
decl_stmt|;
specifier|private
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|xmlWriter
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|setProvider
parameter_list|(
name|Object
name|provider
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|provider
operator|instanceof
name|MessageBodyWriter
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The provider must implement javax.ws.rs.ext.MessageBodyWriter"
argument_list|)
throw|;
block|}
name|xmlWriter
operator|=
operator|(
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
operator|)
name|provider
expr_stmt|;
if|if
condition|(
name|provider
operator|instanceof
name|MessageBodyReader
condition|)
block|{
name|xmlReader
operator|=
operator|(
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
operator|)
name|provider
expr_stmt|;
block|}
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
name|DataReader
argument_list|<
name|T
argument_list|>
name|createReader
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|xmlReader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"javax.ws.rs.ext.MessageBodyReader reference is uninitialized"
argument_list|)
throw|;
block|}
return|return
operator|(
name|DataReader
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|MessageBodyDataReader
argument_list|()
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
name|DataWriter
argument_list|<
name|T
argument_list|>
name|createWriter
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
operator|(
name|DataWriter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|MessageBodyDataWriter
argument_list|()
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedReaderFormats
parameter_list|()
block|{
return|return
name|SUPPORTED_READER_FORMATS
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedWriterFormats
parameter_list|()
block|{
return|return
name|SUPPORTED_WRITER_FORMATS
return|;
block|}
specifier|public
name|void
name|initialize
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
comment|// Check how to deal with individual parts if needed, build a single JAXBContext, etc
block|}
comment|// TODO: The method containing the actual annotations have to retrieved
specifier|protected
name|Method
name|getTargetMethod
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|BindingOperationInfo
name|bop
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|MethodDispatcher
name|md
init|=
operator|(
name|MethodDispatcher
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|md
operator|.
name|getMethod
argument_list|(
name|bop
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getWriteHeaders
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
class|class
name|MessageBodyDataWriter
implements|implements
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
block|{
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|)
block|{
name|write
argument_list|(
name|obj
argument_list|,
literal|null
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|)
block|{
try|try
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Method
name|method
init|=
name|getTargetMethod
argument_list|(
name|message
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
name|getWriteHeaders
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|xmlWriter
operator|.
name|writeTo
argument_list|(
name|obj
argument_list|,
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|method
operator|.
name|getGenericReturnType
argument_list|()
argument_list|,
name|method
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
name|headers
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|message
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
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
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
comment|// complete
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
block|{
comment|// complete
block|}
block|}
specifier|private
class|class
name|MessageBodyDataReader
implements|implements
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
block|{
specifier|public
name|Object
name|read
parameter_list|(
name|XMLStreamReader
name|input
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|XMLStreamReader
name|input
parameter_list|)
block|{
return|return
name|doRead
argument_list|(
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|input
argument_list|)
return|;
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|QName
name|elementQName
parameter_list|,
name|XMLStreamReader
name|input
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|doRead
argument_list|(
name|type
argument_list|,
name|input
argument_list|)
return|;
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
name|T
name|read
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
throws|throws
name|WebApplicationException
throws|,
name|IOException
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Method
name|method
init|=
name|getTargetMethod
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
name|reader
init|=
operator|(
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
operator|)
name|xmlReader
decl_stmt|;
return|return
name|reader
operator|.
name|readFrom
argument_list|(
name|cls
argument_list|,
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|MediaType
operator|.
name|APPLICATION_ATOM_XML_TYPE
argument_list|,
name|getHeaders
argument_list|(
name|message
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Object
name|doRead
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|XMLStreamReader
name|input
parameter_list|)
block|{
try|try
block|{
return|return
name|read
argument_list|(
name|cls
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
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
comment|// complete
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|prop
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// complete
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
block|{
comment|// complete
block|}
block|}
empty_stmt|;
block|}
end_class

end_unit

