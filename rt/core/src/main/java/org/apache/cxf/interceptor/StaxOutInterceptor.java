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
name|OutputStream
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|XMLOutputFactory
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
name|XMLStreamWriter
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
name|i18n
operator|.
name|BundleUtils
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
name|StaxOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FORCE_START_DOCUMENT
init|=
literal|"org.apache.cxf.stax.force-start-document"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|StaxOutInterceptor
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
name|XMLOutputFactory
argument_list|>
name|factories
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|XMLOutputFactory
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|StaxOutEndingInterceptor
name|ending
init|=
operator|new
name|StaxOutEndingInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|StaxOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|==
literal|null
operator|||
name|writer
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
comment|// assert os != null;
comment|// TODO: where does encoding constant go?
name|String
name|encoding
init|=
name|getEncoding
argument_list|(
name|message
argument_list|)
decl_stmt|;
try|try
block|{
name|writer
operator|=
name|getXMLOutputFactory
argument_list|(
name|message
argument_list|)
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|FORCE_START_DOCUMENT
argument_list|)
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartDocument
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
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
name|BUNDLE
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
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
comment|// Add a final interceptor to write end elements
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ending
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getEncoding
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|encoding
operator|==
literal|null
operator|&&
name|ex
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoding
operator|=
operator|(
name|String
operator|)
name|ex
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
literal|"UTF-8"
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
return|return
name|encoding
return|;
block|}
specifier|public
specifier|static
name|XMLOutputFactory
name|getXMLOutputFactory
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
name|XMLOutputFactory
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
name|XMLOutputFactory
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|AbstractOutDatabindingInterceptor
operator|.
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|FORCE_START_DOCUMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
operator|(
name|XMLOutputFactory
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
name|XMLOutputFactory
name|xif
init|=
operator|(
name|XMLOutputFactory
operator|)
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
name|BUNDLE
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
name|XMLOutputFactory
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
name|m
operator|.
name|put
argument_list|(
name|AbstractOutDatabindingInterceptor
operator|.
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|FORCE_START_DOCUMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|xif
return|;
block|}
else|else
block|{
return|return
name|StaxUtils
operator|.
name|getXMLOutputFactory
argument_list|()
return|;
block|}
block|}
specifier|public
class|class
name|StaxOutEndingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|StaxOutEndingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM_ENDING
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|AttachmentOutEndingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
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
throws|throws
name|Fault
block|{
try|try
block|{
name|XMLStreamWriter
name|xtw
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xtw
operator|!=
literal|null
condition|)
block|{
name|xtw
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|xtw
operator|.
name|close
argument_list|()
expr_stmt|;
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
literal|"STAX_WRITE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

