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
name|feature
operator|.
name|transform
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
name|Reader
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
name|interceptor
operator|.
name|StaxInInterceptor
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
comment|/** Class provides XSLT transformation of incoming message.  * Actually it breaks streaming (can be fixed in further versions when XSLT engine supports XML stream)  */
end_comment

begin_class
specifier|public
class|class
name|XSLTInInterceptor
extends|extends
name|AbstractXSLTInterceptor
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
name|XSLTInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|XSLTInInterceptor
parameter_list|(
name|String
name|xsltPath
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|,
name|StaxInInterceptor
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|xsltPath
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XSLTInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|before
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|after
parameter_list|,
name|String
name|xsltPath
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|,
name|before
argument_list|,
name|after
argument_list|,
name|xsltPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|checkContextProperty
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// 1. Try to get and transform XMLStreamReader message content
name|XMLStreamReader
name|xReader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xReader
operator|!=
literal|null
condition|)
block|{
name|transformXReader
argument_list|(
name|message
argument_list|,
name|xReader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// 2. Try to get and transform InputStream message content
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
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|transformIS
argument_list|(
name|message
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// 3. Try to get and transform Reader message content (actually used for JMS TextMessage)
name|Reader
name|reader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|transformReader
argument_list|(
name|message
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|transformXReader
parameter_list|(
name|Message
name|message
parameter_list|,
name|XMLStreamReader
name|xReader
parameter_list|)
block|{
name|CachedOutputStream
name|cachedOS
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
name|xReader
argument_list|,
name|cachedOS
argument_list|)
expr_stmt|;
name|InputStream
name|transformedIS
init|=
name|XSLTUtils
operator|.
name|transform
argument_list|(
name|getXSLTTemplate
argument_list|()
argument_list|,
name|cachedOS
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|transformedReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|transformedIS
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|transformedReader
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
literal|"STAX_COPY"
argument_list|,
name|LOG
argument_list|,
name|e
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
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"GET_CACHED_INPUT_STREAM"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|xReader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
try|try
block|{
name|cachedOS
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
name|LOG
operator|.
name|warning
argument_list|(
literal|"Cannot close stream after transformation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|transformIS
parameter_list|(
name|Message
name|message
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|transformedIS
init|=
name|XSLTUtils
operator|.
name|transform
argument_list|(
name|getXSLTTemplate
argument_list|()
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|transformedIS
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|is
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
name|LOG
operator|.
name|warning
argument_list|(
literal|"Cannot close stream after transformation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|transformReader
parameter_list|(
name|Message
name|message
parameter_list|,
name|Reader
name|reader
parameter_list|)
block|{
try|try
block|{
name|Reader
name|transformedReader
init|=
name|XSLTUtils
operator|.
name|transform
argument_list|(
name|getXSLTTemplate
argument_list|()
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|,
name|transformedReader
argument_list|)
expr_stmt|;
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
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Cannot close stream after transformation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

