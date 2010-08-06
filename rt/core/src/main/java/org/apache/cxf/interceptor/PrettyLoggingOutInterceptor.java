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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|Level
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
name|transform
operator|.
name|OutputKeys
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
name|Transformer
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
name|StreamResult
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
name|helpers
operator|.
name|XMLUtils
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
name|CacheAndWriteOutputStream
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
name|io
operator|.
name|CachedOutputStreamCallback
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PrettyLoggingOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
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
name|PrettyLoggingOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PrintWriter
name|writer
decl_stmt|;
specifier|public
name|PrettyLoggingOutInterceptor
parameter_list|(
name|PrintWriter
name|w
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|=
name|w
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
specifier|final
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
if|if
condition|(
name|os
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|ALL
argument_list|)
condition|)
block|{
return|return;
block|}
comment|//     Write the output while caching it for the log message
specifier|final
name|CacheAndWriteOutputStream
name|newOut
init|=
operator|new
name|CacheAndWriteOutputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|newOut
argument_list|)
expr_stmt|;
name|newOut
operator|.
name|registerCallback
argument_list|(
operator|new
name|LoggingCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
class|class
name|LoggingCallback
implements|implements
name|CachedOutputStreamCallback
block|{
specifier|public
name|void
name|onFlush
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|)
block|{          }
specifier|public
name|void
name|onClose
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|)
block|{
try|try
block|{
name|Transformer
name|serializer
init|=
name|XMLUtils
operator|.
name|newTransformer
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|//Setup indenting to "pretty print"
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|transform
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|cos
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|StringWriter
name|swriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|transform
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|cos
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|swriter
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|swriter
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

