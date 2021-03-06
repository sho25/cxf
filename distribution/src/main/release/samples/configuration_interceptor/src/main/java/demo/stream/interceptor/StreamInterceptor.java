begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|stream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapPreProtocolOutInterceptor
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

begin_class
specifier|public
class|class
name|StreamInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|StreamInterceptor
parameter_list|()
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
name|SoapPreProtocolOutInterceptor
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
comment|//TODO
name|boolean
name|isOutbound
init|=
literal|false
decl_stmt|;
name|isOutbound
operator|=
name|message
operator|==
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|||
name|message
operator|==
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutFaultMessage
argument_list|()
expr_stmt|;
if|if
condition|(
name|isOutbound
condition|)
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
name|CachedStream
name|cs
init|=
operator|new
name|CachedStream
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|cs
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|message
argument_list|)
expr_stmt|;
try|try
block|{
name|cs
operator|.
name|flush
argument_list|()
expr_stmt|;
name|CachedOutputStream
name|csnew
init|=
operator|(
name|CachedOutputStream
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|GZIPOutputStream
name|zipOutput
init|=
operator|new
name|GZIPOutputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|CachedOutputStream
operator|.
name|copyStream
argument_list|(
name|csnew
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|zipOutput
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
name|cs
operator|.
name|close
argument_list|()
expr_stmt|;
name|zipOutput
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|ioe
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
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
name|GZIPInputStream
name|zipInput
init|=
operator|new
name|GZIPInputStream
argument_list|(
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
name|zipInput
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|ioe
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{     }
specifier|private
class|class
name|CachedStream
extends|extends
name|CachedOutputStream
block|{
name|CachedStream
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doFlush
parameter_list|()
throws|throws
name|IOException
block|{
name|currentStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doClose
parameter_list|()
throws|throws
name|IOException
block|{         }
specifier|protected
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{         }
block|}
block|}
end_class

end_unit

