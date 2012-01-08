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
name|Provider
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
name|XMLStreamException
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
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|Document
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
name|xml
operator|.
name|XMLSource
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
name|staxutils
operator|.
name|StaxSource
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
annotation|@
name|Provider
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|,
literal|"text/html"
block|}
argument_list|)
specifier|public
class|class
name|SourceProvider
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PREFERRED_FORMAT
init|=
literal|"source-preferred-format"
decl_stmt|;
annotation|@
name|Context
specifier|private
name|MessageContext
name|context
decl_stmt|;
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
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Document
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
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
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|XMLSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Document
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|Object
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|source
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|m
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
name|Class
argument_list|<
name|?
argument_list|>
name|theSource
init|=
name|source
decl_stmt|;
if|if
condition|(
name|theSource
operator|==
name|Source
operator|.
name|class
condition|)
block|{
name|String
name|s
init|=
name|getPreferredSource
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"sax"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
operator|||
literal|"cxf.stax"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|theSource
operator|=
name|SAXSource
operator|.
name|class
expr_stmt|;
block|}
block|}
if|if
condition|(
name|DOMSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
operator|||
name|Document
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
condition|)
block|{
name|boolean
name|docRequired
init|=
name|Document
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|getReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
try|try
block|{
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
return|return
name|docRequired
condition|?
name|doc
else|:
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|ioex
init|=
operator|new
name|IOException
argument_list|(
literal|"Problem creating a Source object"
argument_list|)
decl_stmt|;
name|ioex
operator|.
name|setStackTrace
argument_list|(
name|e
operator|.
name|getStackTrace
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ioex
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|SAXSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
operator|||
name|StaxSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
condition|)
block|{
return|return
operator|new
name|StaxSource
argument_list|(
name|getReader
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|StreamSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
operator|||
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
condition|)
block|{
return|return
operator|new
name|StreamSource
argument_list|(
name|getRealStream
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|XMLSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|theSource
argument_list|)
condition|)
block|{
return|return
operator|new
name|XMLSource
argument_list|(
name|getRealStream
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unrecognized source"
argument_list|)
throw|;
block|}
specifier|protected
name|XMLStreamReader
name|getReader
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|XMLStreamReader
name|reader
init|=
name|getReaderFromMessage
argument_list|()
decl_stmt|;
return|return
name|reader
operator|==
literal|null
condition|?
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
else|:
name|reader
return|;
block|}
specifier|protected
name|InputStream
name|getRealStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|XMLStreamReader
name|reader
init|=
name|getReaderFromMessage
argument_list|()
decl_stmt|;
return|return
name|reader
operator|==
literal|null
condition|?
name|is
else|:
name|getStreamFromReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
specifier|private
name|InputStream
name|getStreamFromReader
parameter_list|(
name|XMLStreamReader
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|CachedOutputStream
name|out
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|out
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|getInputStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"XMLStreamException:"
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|XMLStreamReader
name|getReaderFromMessage
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
return|return
name|mc
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Object
name|source
parameter_list|,
name|Class
argument_list|<
name|?
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
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|encoding
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
name|XMLStreamReader
name|reader
init|=
name|source
operator|instanceof
name|Source
condition|?
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|(
name|Source
operator|)
name|source
argument_list|)
else|:
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|(
name|Document
operator|)
name|source
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
try|try
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
try|try
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|Object
name|source
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
name|mt
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|protected
name|String
name|getPreferredSource
parameter_list|()
block|{
name|MessageContext
name|mc
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|mc
operator|.
name|getContextualProperty
argument_list|(
name|PREFERRED_FORMAT
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|"sax"
return|;
block|}
block|}
specifier|protected
name|MessageContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

