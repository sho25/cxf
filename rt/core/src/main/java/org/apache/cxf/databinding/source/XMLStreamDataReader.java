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
name|databinding
operator|.
name|source
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|interceptor
operator|.
name|Fault
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|DepthXMLStreamReader
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
name|FragmentStreamReader
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
name|W3CDOMStreamReader
import|;
end_import

begin_class
specifier|public
class|class
name|XMLStreamDataReader
implements|implements
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
block|{
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
name|XMLStreamDataReader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Schema
name|schema
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
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
name|read
argument_list|(
literal|null
argument_list|,
name|input
argument_list|,
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Object
name|read
parameter_list|(
specifier|final
name|QName
name|name
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
if|if
condition|(
name|Source
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|&&
name|message
operator|!=
literal|null
condition|)
block|{
comment|//generic Source, find the preferred type
name|String
name|s
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SourceDataBinding
operator|.
name|PREFERRED_FORMAT
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|s
operator|=
literal|"sax"
expr_stmt|;
block|}
if|if
condition|(
literal|"dom"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|type
operator|=
name|DOMSource
operator|.
name|class
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"stream"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|type
operator|=
name|StreamSource
operator|.
name|class
expr_stmt|;
block|}
elseif|else
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
name|type
operator|=
name|SAXSource
operator|.
name|class
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"stax"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
try|try
block|{
name|type
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"javax.xml.transform.stax.StAXSource"
argument_list|,
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|type
operator|=
name|SAXSource
operator|.
name|class
expr_stmt|;
block|}
block|}
else|else
block|{
name|type
operator|=
name|DOMSource
operator|.
name|class
expr_stmt|;
block|}
block|}
try|try
block|{
name|Element
name|dom
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|dom
operator|=
name|validate
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|input
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|dom
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|Object
name|retVal
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|SAXSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|StaxSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|retVal
operator|=
operator|new
name|StaxSource
argument_list|(
name|resetForStreaming
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
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
name|type
argument_list|)
condition|)
block|{
name|retVal
operator|=
operator|new
name|StreamSource
argument_list|(
name|getInputStream
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|XMLStreamReader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|retVal
operator|=
name|resetForStreaming
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Element
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|retVal
operator|=
name|dom
operator|==
literal|null
condition|?
name|read
argument_list|(
name|input
argument_list|)
operator|.
name|getNode
argument_list|()
else|:
name|dom
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Document
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|retVal
operator|=
name|dom
operator|==
literal|null
condition|?
name|read
argument_list|(
name|input
argument_list|)
operator|.
name|getNode
argument_list|()
else|:
name|dom
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|DataSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
specifier|final
name|InputStream
name|ins
init|=
name|getInputStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|retVal
operator|=
operator|new
name|DataSource
argument_list|()
block|{
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|"text/xml"
return|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ins
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
block|}
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"javax.xml.transform.stax.StAXSource"
operator|.
name|equals
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|input
operator|=
name|resetForStreaming
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|createStaxSource
argument_list|(
name|input
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|retVal
operator|=
name|o
expr_stmt|;
block|}
block|}
if|if
condition|(
name|retVal
operator|!=
literal|null
condition|)
block|{
return|return
name|retVal
return|;
block|}
block|}
return|return
name|dom
operator|==
literal|null
condition|?
name|read
argument_list|(
name|input
argument_list|)
else|:
operator|new
name|DOMSource
argument_list|(
name|dom
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"COULD_NOT_READ_XML_STREAM"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"COULD_NOT_READ_XML_STREAM_CAUSED_BY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"COULD_NOT_READ_XML_STREAM_CAUSED_BY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Object
name|createStaxSource
parameter_list|(
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
try|try
block|{
return|return
name|type
operator|.
name|getConstructor
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|input
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|XMLStreamReader
name|resetForStreaming
parameter_list|(
name|XMLStreamReader
name|input
parameter_list|)
throws|throws
name|XMLStreamException
block|{
comment|//Need to mark the message as streaming this so input stream
comment|//is not closed and additional parts are not read and such
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|removeContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|InputStream
name|ins
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|removeContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|input
operator|=
operator|new
name|FragmentStreamReader
argument_list|(
name|input
argument_list|,
literal|true
argument_list|)
block|{
name|boolean
name|closed
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|boolean
name|b
init|=
name|super
operator|.
name|hasNext
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|b
operator|&&
operator|!
name|closed
condition|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|super
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
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
return|return
name|b
return|;
block|}
block|}
expr_stmt|;
block|}
return|return
name|input
return|;
block|}
specifier|private
name|Element
name|validate
parameter_list|(
name|XMLStreamReader
name|input
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
name|SAXException
throws|,
name|IOException
block|{
name|DOMSource
name|ds
init|=
name|read
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|schema
operator|.
name|newValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|Node
name|nd
init|=
name|ds
operator|.
name|getNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|nd
operator|instanceof
name|Document
condition|)
block|{
return|return
operator|(
operator|(
name|Document
operator|)
name|nd
operator|)
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
return|return
operator|(
name|Element
operator|)
name|ds
operator|.
name|getNode
argument_list|()
return|;
block|}
specifier|private
name|InputStream
name|getInputStream
parameter_list|(
name|XMLStreamReader
name|input
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
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
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|DOMSource
name|read
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
block|{
comment|// Use a DOMSource for now, we should really use a StaxSource/SAXSource though for
comment|// performance reasons
try|try
block|{
name|XMLStreamReader
name|reader2
init|=
name|reader
decl_stmt|;
if|if
condition|(
name|reader2
operator|instanceof
name|DepthXMLStreamReader
condition|)
block|{
name|reader2
operator|=
operator|(
operator|(
name|DepthXMLStreamReader
operator|)
name|reader2
operator|)
operator|.
name|getReader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reader2
operator|instanceof
name|W3CDOMStreamReader
condition|)
block|{
name|W3CDOMStreamReader
name|domreader
init|=
operator|(
name|W3CDOMStreamReader
operator|)
name|reader2
decl_stmt|;
name|DOMSource
name|o
init|=
operator|new
name|DOMSource
argument_list|(
name|domreader
operator|.
name|getCurrentElement
argument_list|()
argument_list|)
decl_stmt|;
name|domreader
operator|.
name|consumeFrame
argument_list|()
expr_stmt|;
return|return
name|o
return|;
block|}
else|else
block|{
name|Document
name|document
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|//need to actually consume the END_ELEMENT
name|reader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|DOMSource
argument_list|(
name|document
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"COULD_NOT_READ_XML_STREAM_CAUSED_BY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
block|{
name|schema
operator|=
name|s
expr_stmt|;
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
block|{     }
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
if|if
condition|(
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|prop
argument_list|)
condition|)
block|{
name|message
operator|=
operator|(
name|Message
operator|)
name|value
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

