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
name|interceptor
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|xml
operator|.
name|stream
operator|.
name|XMLInputFactory
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
name|helpers
operator|.
name|HttpHeaderHelper
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
name|AbstractPhaseInterceptor
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
name|Phase
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

begin_comment
comment|/**  * Creates an XMLStreamReader from the InputStream on the Message.  */
end_comment

begin_class
specifier|public
class|class
name|StaxInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
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
name|StaxInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|Object
argument_list|,
name|XMLInputFactory
argument_list|>
name|factories
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|XMLInputFactory
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|StaxInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StaxInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|isGET
argument_list|(
name|message
argument_list|)
operator|||
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"StaxInInterceptor skipped."
argument_list|)
expr_stmt|;
return|return;
block|}
name|InputStream
name|is
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
name|Reader
name|reader
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|reader
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
return|return;
block|}
block|}
name|String
name|contentType
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|contains
argument_list|(
literal|"text/html"
argument_list|)
operator|&&
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|StringBuilder
name|htmlMessage
init|=
operator|new
name|StringBuilder
argument_list|(
literal|1024
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
name|reader
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|char
name|s
index|[]
init|=
operator|new
name|char
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|i
init|=
name|reader
operator|.
name|read
argument_list|(
name|s
argument_list|)
decl_stmt|;
while|while
condition|(
name|htmlMessage
operator|.
name|length
argument_list|()
operator|<
literal|64536
operator|&&
name|i
operator|>
literal|0
condition|)
block|{
name|htmlMessage
operator|.
name|append
argument_list|(
name|s
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|i
operator|=
name|reader
operator|.
name|read
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
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
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"INVALID_HTML_RESPONSETYPE"
argument_list|,
name|LOG
argument_list|,
literal|"(none)"
argument_list|)
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"INVALID_HTML_RESPONSETYPE"
argument_list|,
name|LOG
argument_list|,
operator|(
name|htmlMessage
operator|==
literal|null
operator|||
name|htmlMessage
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|?
literal|"(none)"
else|:
name|htmlMessage
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
comment|//if contentType is null, this is likely a an empty post/put/delete/similar, lets see if it's
comment|//detectable at all
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|m
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
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
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|contentLen
init|=
name|HttpHeaderHelper
operator|.
name|getHeader
argument_list|(
name|m
argument_list|,
name|HttpHeaderHelper
operator|.
name|CONTENT_LENGTH
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|contentTE
init|=
name|HttpHeaderHelper
operator|.
name|getHeader
argument_list|(
name|m
argument_list|,
name|HttpHeaderHelper
operator|.
name|CONTENT_TRANSFER_ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|contentLen
argument_list|)
operator|||
literal|"0"
operator|.
name|equals
argument_list|(
name|contentLen
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|contentTE
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
block|}
name|String
name|encoding
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xreader
decl_stmt|;
try|try
block|{
name|XMLInputFactory
name|factory
init|=
name|getXMLInputFactory
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|xreader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xreader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
synchronized|synchronized
init|(
name|factory
init|)
block|{
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|xreader
operator|=
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xreader
operator|=
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|xreader
operator|=
name|configureRestrictions
argument_list|(
name|xreader
argument_list|,
name|message
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
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"STREAM_CREATE_EXC"
argument_list|,
name|LOG
argument_list|,
name|encoding
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|xreader
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|StaxInEndingInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|XMLStreamReader
name|configureRestrictions
parameter_list|(
name|XMLStreamReader
name|xreader
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Integer
name|maxChildElements
init|=
name|getInteger
argument_list|(
name|message
argument_list|,
name|StaxUtils
operator|.
name|MAX_CHILD_ELEMENTS
argument_list|)
decl_stmt|;
name|Integer
name|maxElementDepth
init|=
name|getInteger
argument_list|(
name|message
argument_list|,
name|StaxUtils
operator|.
name|MAX_ELEMENT_DEPTH
argument_list|)
decl_stmt|;
name|Integer
name|maxAttributeCount
init|=
name|getInteger
argument_list|(
name|message
argument_list|,
name|StaxUtils
operator|.
name|MAX_ATTRIBUTE_COUNT
argument_list|)
decl_stmt|;
name|Integer
name|maxAttributeSize
init|=
name|getInteger
argument_list|(
name|message
argument_list|,
name|StaxUtils
operator|.
name|MAX_ATTRIBUTE_SIZE
argument_list|)
decl_stmt|;
name|Integer
name|maxTextLength
init|=
name|getInteger
argument_list|(
name|message
argument_list|,
name|StaxUtils
operator|.
name|MAX_TEXT_LENGTH
argument_list|)
decl_stmt|;
name|Long
name|maxElementCount
init|=
name|getLong
argument_list|(
name|message
argument_list|,
name|StaxUtils
operator|.
name|MAX_ELEMENT_COUNT
argument_list|)
decl_stmt|;
name|Long
name|maxXMLCharacters
init|=
name|getLong
argument_list|(
name|message
argument_list|,
name|StaxUtils
operator|.
name|MAX_XML_CHARACTERS
argument_list|)
decl_stmt|;
return|return
name|StaxUtils
operator|.
name|configureReader
argument_list|(
name|xreader
argument_list|,
name|maxChildElements
argument_list|,
name|maxElementDepth
argument_list|,
name|maxAttributeCount
argument_list|,
name|maxAttributeSize
argument_list|,
name|maxTextLength
argument_list|,
name|maxElementCount
argument_list|,
name|maxXMLCharacters
argument_list|)
return|;
block|}
specifier|private
name|Long
name|getLong
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Long
condition|)
block|{
return|return
operator|(
name|Long
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|o
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Integer
name|getInteger
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Integer
condition|)
block|{
return|return
operator|(
name|Integer
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|o
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|XMLInputFactory
name|getXMLInputFactory
parameter_list|(
name|Message
name|m
parameter_list|)
throws|throws
name|Fault
block|{
name|Object
name|o
init|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|XMLInputFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|XMLInputFactory
condition|)
block|{
return|return
operator|(
name|XMLInputFactory
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|XMLInputFactory
name|xif
init|=
name|factories
operator|.
name|get
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|xif
operator|==
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Class
condition|)
block|{
name|cls
operator|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|cls
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|StaxInInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"INVALID_INPUT_FACTORY"
argument_list|,
name|LOG
argument_list|,
name|o
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
name|xif
operator|=
call|(
name|XMLInputFactory
call|)
argument_list|(
name|cls
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|o
argument_list|,
name|xif
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|xif
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

