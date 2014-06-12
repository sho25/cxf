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
name|transport
operator|.
name|websocket
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
name|PrintWriter
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
name|Locale
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
name|TreeMap
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
name|servlet
operator|.
name|ServletOutputStream
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
name|Cookie
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
name|HttpServletResponse
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WebSocketVirtualServletResponse
implements|implements
name|HttpServletResponse
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
name|WebSocketVirtualServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|WebSocketServletHolder
name|webSocketHolder
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseHeaders
decl_stmt|;
specifier|private
name|ServletOutputStream
name|outputStream
decl_stmt|;
specifier|public
name|WebSocketVirtualServletResponse
parameter_list|(
name|WebSocketServletHolder
name|websocket
parameter_list|)
block|{
name|this
operator|.
name|webSocketHolder
operator|=
name|websocket
expr_stmt|;
name|this
operator|.
name|responseHeaders
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|this
operator|.
name|outputStream
operator|=
name|createOutputStream
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|flushBuffer
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"flushBuffer()"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getBufferSize()"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getCharacterEncoding()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getContentType()"
argument_list|)
expr_stmt|;
return|return
name|responseHeaders
operator|.
name|get
argument_list|(
literal|"Content-Type"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getLocale"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServletOutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|outputStream
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrintWriter
name|getWriter
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getWriter()"
argument_list|)
expr_stmt|;
return|return
operator|new
name|PrintWriter
argument_list|(
name|getOutputStream
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isCommitted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|reset
parameter_list|()
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|resetBuffer
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"resetBuffer()"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setBufferSize({0})"
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setCharacterEncoding
parameter_list|(
name|String
name|charset
parameter_list|)
block|{
comment|// TODO
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setCharacterEncoding({0})"
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setContentLength
parameter_list|(
name|int
name|len
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setContentLength({0})"
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
literal|"Content-Length"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setContentType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setContentType({0})"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLocale
parameter_list|(
name|Locale
name|loc
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setLocale({0})"
argument_list|,
name|loc
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addCookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"addCookie({0})"
argument_list|,
name|cookie
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addDateHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|date
parameter_list|)
block|{
comment|// TODO
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"addDateHeader({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|date
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"addHeader({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|value
block|}
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addIntHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"addIntHeader({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|value
block|}
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|containsHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"containsHeader({0})"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|responseHeaders
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|encodeRedirectURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"encodeRedirectURL({0})"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|encodeRedirectUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"encodeRedirectUrl({0})"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|encodeURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"encodeURL({0})"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|encodeUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"encodeUrl({0})"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getHeader({0})"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getHeaderNames
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getHeaderNames()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getHeaders
parameter_list|(
name|String
name|name
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getHeaders({0})"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getStatus
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"getStatus()"
argument_list|)
expr_stmt|;
name|String
name|v
init|=
name|responseHeaders
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|)
decl_stmt|;
return|return
name|v
operator|==
literal|null
condition|?
literal|200
else|:
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|)
throws|throws
name|IOException
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"sendError{0}"
argument_list|,
name|sc
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|sc
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"sendError({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|sc
block|,
name|msg
block|}
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|sc
argument_list|)
argument_list|)
expr_stmt|;
name|responseHeaders
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SM_KEY
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sendRedirect
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|IOException
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"sendRedirect({0})"
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDateHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|date
parameter_list|)
block|{
comment|// ignore
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setDateHeader({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|date
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setHeader({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|value
block|}
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setIntHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setIntHeader({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|value
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setStatus({0})"
argument_list|,
name|sc
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|sc
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|sm
parameter_list|)
block|{
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"setStatus({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|sc
block|,
name|sm
block|}
argument_list|)
expr_stmt|;
block|}
name|responseHeaders
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|sc
argument_list|)
argument_list|)
expr_stmt|;
name|responseHeaders
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SM_KEY
argument_list|,
name|sm
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ServletOutputStream
name|createOutputStream
parameter_list|()
block|{
return|return
operator|new
name|ServletOutputStream
argument_list|()
block|{
comment|//REVISIT
comment|// This output buffering is needed as the server side websocket does
comment|// not support the fragment transmission mode when sending back a large data.
comment|// And this buffering is only used for the response for the initial service innovation.
comment|// For the subsequently pushed data to the socket are sent back
comment|// unbuffered as individual websocket messages.
comment|// the things to consider :
comment|// - provide a size limit if we are use this buffering
comment|// - add a chunking mode in the cxf websocket's binding.
specifier|private
name|InternalByteArrayOutputStream
name|buffer
init|=
operator|new
name|InternalByteArrayOutputStream
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
literal|1
index|]
decl_stmt|;
name|data
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
name|b
expr_stmt|;
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|responseHeaders
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|FLUSHED_KEY
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// buffer the data until it gets flushed
name|buffer
operator|.
name|write
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// unbuffered write to the socket
name|data
operator|=
name|WebSocketUtils
operator|.
name|buildResponse
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|webSocketHolder
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|responseHeaders
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|FLUSHED_KEY
argument_list|)
operator|==
literal|null
condition|)
block|{
name|byte
index|[]
name|data
init|=
name|WebSocketUtils
operator|.
name|buildResponse
argument_list|(
name|responseHeaders
argument_list|,
name|buffer
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|webSocketHolder
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|responseHeaders
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|FLUSHED_KEY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
specifier|private
specifier|static
class|class
name|InternalByteArrayOutputStream
extends|extends
name|ByteArrayOutputStream
block|{
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
block|}
block|}
end_class

end_unit

