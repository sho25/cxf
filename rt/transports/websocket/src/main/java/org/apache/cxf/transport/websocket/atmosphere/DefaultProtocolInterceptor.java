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
operator|.
name|atmosphere
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|transport
operator|.
name|websocket
operator|.
name|InvalidPathException
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
name|transport
operator|.
name|websocket
operator|.
name|WebSocketConstants
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
name|transport
operator|.
name|websocket
operator|.
name|WebSocketUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|config
operator|.
name|service
operator|.
name|AtmosphereInterceptorService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AsyncIOInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AsyncIOInterceptorAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AsyncIOWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereFramework
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereInterceptorAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereInterceptorWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|FrameworkConfig
import|;
end_import

begin_comment
comment|/**  * DefaultProtocolInterceptor provides the default CXF's WebSocket protocol that uses.  *   */
end_comment

begin_class
annotation|@
name|AtmosphereInterceptorService
specifier|public
class|class
name|DefaultProtocolInterceptor
extends|extends
name|AtmosphereInterceptorAdapter
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
name|DefaultProtocolInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_DISPATCHED
init|=
literal|"request.dispatched"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESPONSE_PARENT
init|=
literal|"response.parent"
decl_stmt|;
specifier|private
specifier|final
name|AsyncIOInterceptor
name|interceptor
init|=
operator|new
name|Interceptor
argument_list|()
decl_stmt|;
specifier|private
name|Pattern
name|includedheaders
decl_stmt|;
specifier|private
name|Pattern
name|excludedheaders
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|AtmosphereConfig
name|config
parameter_list|)
block|{
name|super
operator|.
name|configure
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|String
name|p
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"org.apache.cxf.transport.websocket.atmosphere.transport.includedheaders"
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|includedheaders
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|p
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"org.apache.cxf.transport.websocket.atmosphere.transport.excludedheaders"
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|excludedheaders
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|p
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DefaultProtocolInterceptor
name|includedheaders
parameter_list|(
name|String
name|p
parameter_list|)
block|{
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|includedheaders
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|p
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|void
name|setIncludedheaders
parameter_list|(
name|Pattern
name|includedheaders
parameter_list|)
block|{
name|this
operator|.
name|includedheaders
operator|=
name|includedheaders
expr_stmt|;
block|}
specifier|public
name|DefaultProtocolInterceptor
name|excludedheaders
parameter_list|(
name|String
name|p
parameter_list|)
block|{
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|excludedheaders
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|p
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|void
name|setExcludedheaders
parameter_list|(
name|Pattern
name|excludedheaders
parameter_list|)
block|{
name|this
operator|.
name|excludedheaders
operator|=
name|excludedheaders
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
specifier|public
name|Action
name|inspect
parameter_list|(
specifier|final
name|AtmosphereResource
name|r
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"inspect"
argument_list|)
expr_stmt|;
name|AtmosphereRequest
name|request
init|=
name|r
operator|.
name|getRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|request
operator|.
name|getAttribute
argument_list|(
name|REQUEST_DISPATCHED
argument_list|)
operator|==
literal|null
condition|)
block|{
name|AtmosphereResponse
name|response
init|=
literal|null
decl_stmt|;
name|AtmosphereFramework
name|framework
init|=
name|r
operator|.
name|getAtmosphereConfig
argument_list|()
operator|.
name|framework
argument_list|()
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|data
init|=
name|WebSocketUtils
operator|.
name|readBody
argument_list|(
name|request
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|Action
operator|.
name|CANCELLED
return|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"inspecting data {0}"
argument_list|,
operator|new
name|String
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|AtmosphereRequest
name|ar
init|=
name|createAtmosphereRequest
argument_list|(
name|request
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|response
operator|=
operator|new
name|WrappedAtmosphereResponse
argument_list|(
name|r
operator|.
name|getResponse
argument_list|()
argument_list|,
name|ar
argument_list|)
expr_stmt|;
name|ar
operator|.
name|attributes
argument_list|()
operator|.
name|put
argument_list|(
name|REQUEST_DISPATCHED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|String
name|refid
init|=
name|ar
operator|.
name|getHeader
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|refid
operator|!=
literal|null
condition|)
block|{
name|ar
operator|.
name|attributes
argument_list|()
operator|.
name|put
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|,
name|refid
argument_list|)
expr_stmt|;
block|}
comment|// This is a new request, we must clean the Websocket AtmosphereResource.
name|request
operator|.
name|removeAttribute
argument_list|(
name|FrameworkConfig
operator|.
name|INJECTED_ATMOSPHERE_RESOURCE
argument_list|)
expr_stmt|;
name|response
operator|.
name|request
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|attachWriter
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|Action
name|action
init|=
name|framework
operator|.
name|doCometSupport
argument_list|(
name|ar
argument_list|,
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|.
name|type
argument_list|()
operator|==
name|Action
operator|.
name|TYPE
operator|.
name|SUSPEND
condition|)
block|{
name|ar
operator|.
name|destroyable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|response
operator|.
name|destroyable
argument_list|(
literal|false
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Error during request dispatching"
argument_list|,
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
name|response
operator|=
operator|new
name|WrappedAtmosphereResponse
argument_list|(
name|r
operator|.
name|getResponse
argument_list|()
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|instanceof
name|InvalidPathException
condition|)
block|{
name|response
operator|.
name|setIntHeader
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
literal|400
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|.
name|setIntHeader
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
literal|500
argument_list|)
expr_stmt|;
block|}
name|OutputStream
name|out
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|createResponse
argument_list|(
name|response
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|Action
operator|.
name|CANCELLED
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Error during protocol processing"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|Action
operator|.
name|CONTINUE
return|;
block|}
block|}
else|else
block|{
name|request
operator|.
name|destroyable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|Action
operator|.
name|CONTINUE
return|;
block|}
specifier|private
name|void
name|attachWriter
parameter_list|(
specifier|final
name|AtmosphereResource
name|r
parameter_list|)
block|{
name|AtmosphereResponse
name|res
init|=
name|r
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|AsyncIOWriter
name|writer
init|=
name|res
operator|.
name|getAsyncIOWriter
argument_list|()
decl_stmt|;
if|if
condition|(
name|writer
operator|instanceof
name|AtmosphereInterceptorWriter
condition|)
block|{
comment|//REVIST need a better way to add a custom filter at the first entry and not at the last as
comment|// e.g. interceptor(AsyncIOInterceptor interceptor, int position)
name|Deque
argument_list|<
name|AsyncIOInterceptor
argument_list|>
name|filters
init|=
name|AtmosphereInterceptorWriter
operator|.
name|class
operator|.
name|cast
argument_list|(
name|writer
argument_list|)
operator|.
name|filters
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|filters
operator|.
name|contains
argument_list|(
name|interceptor
argument_list|)
condition|)
block|{
name|filters
operator|.
name|addFirst
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Creates a virtual request using the specified parent request and the actual data.      *       * @param r      * @param data      * @return      * @throws IOException      */
specifier|protected
name|AtmosphereRequest
name|createAtmosphereRequest
parameter_list|(
name|AtmosphereRequest
name|r
parameter_list|,
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|AtmosphereRequest
operator|.
name|Builder
name|b
init|=
operator|new
name|AtmosphereRequest
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hdrs
init|=
name|WebSocketUtils
operator|.
name|readHeaders
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|hdrs
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|URI_KEY
argument_list|)
decl_stmt|;
name|String
name|origin
init|=
name|r
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|startsWith
argument_list|(
name|origin
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"invalid path: {0} not within {1}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|path
block|,
name|origin
block|}
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|InvalidPathException
argument_list|()
throw|;
block|}
name|String
name|requestURI
init|=
name|path
decl_stmt|;
name|String
name|requestURL
init|=
name|r
operator|.
name|getRequestURL
argument_list|()
operator|+
name|requestURI
operator|.
name|substring
argument_list|(
name|r
operator|.
name|getRequestURI
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|contentType
init|=
name|hdrs
operator|.
name|get
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
name|String
name|method
init|=
name|hdrs
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|METHOD_KEY
argument_list|)
decl_stmt|;
name|b
operator|.
name|pathInfo
argument_list|(
name|path
argument_list|)
operator|.
name|contentType
argument_list|(
name|contentType
argument_list|)
operator|.
name|headers
argument_list|(
name|hdrs
argument_list|)
operator|.
name|method
argument_list|(
name|method
argument_list|)
operator|.
name|requestURI
argument_list|(
name|requestURI
argument_list|)
operator|.
name|requestURL
argument_list|(
name|requestURL
argument_list|)
operator|.
name|request
argument_list|(
name|r
argument_list|)
expr_stmt|;
comment|// add the body only if it is present
name|byte
index|[]
name|body
init|=
name|WebSocketUtils
operator|.
name|readBody
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|body
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Creates a response data based on the specified payload.      *       * @param response      * @param payload      * @param parent      * @return      */
specifier|protected
name|byte
index|[]
name|createResponse
parameter_list|(
name|AtmosphereResponse
name|response
parameter_list|,
name|byte
index|[]
name|payload
parameter_list|,
name|boolean
name|parent
parameter_list|)
block|{
name|AtmosphereRequest
name|request
init|=
name|response
operator|.
name|request
argument_list|()
decl_stmt|;
name|String
name|refid
init|=
operator|(
name|String
operator|)
name|request
operator|.
name|getAttribute
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|refid
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|addHeader
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
name|refid
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
name|refid
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parent
condition|)
block|{
comment|// include the status code and content-type and those matched headers
name|String
name|sc
init|=
name|response
operator|.
name|getHeader
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|==
literal|null
condition|)
block|{
name|sc
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|put
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
name|sc
argument_list|)
expr_stmt|;
if|if
condition|(
name|payload
operator|!=
literal|null
operator|&&
name|payload
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
name|response
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hv
range|:
name|response
operator|.
name|headers
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
literal|"Content-Type"
operator|.
name|equalsIgnoreCase
argument_list|(
name|hv
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|includedheaders
operator|!=
literal|null
operator|&&
name|includedheaders
operator|.
name|matcher
argument_list|(
name|hv
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
operator|&&
operator|!
operator|(
name|excludedheaders
operator|!=
literal|null
operator|&&
name|excludedheaders
operator|.
name|matcher
argument_list|(
name|hv
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
operator|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|hv
operator|.
name|getKey
argument_list|()
argument_list|,
name|hv
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|WebSocketUtils
operator|.
name|buildResponse
argument_list|(
name|headers
argument_list|,
name|payload
argument_list|,
literal|0
argument_list|,
name|payload
operator|==
literal|null
condition|?
literal|0
else|:
name|payload
operator|.
name|length
argument_list|)
return|;
block|}
specifier|private
specifier|final
class|class
name|Interceptor
extends|extends
name|AsyncIOInterceptorAdapter
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
name|byte
index|[]
name|transformPayload
parameter_list|(
name|AtmosphereResponse
name|response
parameter_list|,
name|byte
index|[]
name|responseDraft
parameter_list|,
name|byte
index|[]
name|data
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
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"transformPayload with draft={0}"
argument_list|,
operator|new
name|String
argument_list|(
name|responseDraft
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|AtmosphereRequest
name|request
init|=
name|response
operator|.
name|request
argument_list|()
decl_stmt|;
if|if
condition|(
name|request
operator|.
name|attributes
argument_list|()
operator|.
name|get
argument_list|(
name|RESPONSE_PARENT
argument_list|)
operator|==
literal|null
condition|)
block|{
name|request
operator|.
name|attributes
argument_list|()
operator|.
name|put
argument_list|(
name|RESPONSE_PARENT
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
return|return
name|createResponse
argument_list|(
name|response
argument_list|,
name|responseDraft
argument_list|,
literal|true
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createResponse
argument_list|(
name|response
argument_list|,
name|responseDraft
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|error
parameter_list|(
name|AtmosphereResponse
name|response
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|reasonPhrase
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
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"status={0}"
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setStatus
argument_list|(
name|statusCode
argument_list|,
name|reasonPhrase
argument_list|)
expr_stmt|;
return|return
name|createResponse
argument_list|(
name|response
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
comment|// a workaround to flush the header data upon close when no write operation occurs
specifier|private
class|class
name|WrappedAtmosphereResponse
extends|extends
name|AtmosphereResponse
block|{
specifier|final
name|AtmosphereResponse
name|response
decl_stmt|;
name|ServletOutputStream
name|sout
decl_stmt|;
name|WrappedAtmosphereResponse
parameter_list|(
name|AtmosphereResponse
name|resp
parameter_list|,
name|AtmosphereRequest
name|req
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|(
name|HttpServletResponse
operator|)
name|resp
operator|.
name|getResponse
argument_list|()
argument_list|,
literal|null
argument_list|,
name|req
argument_list|,
name|resp
operator|.
name|isDestroyable
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|=
name|resp
expr_stmt|;
name|response
operator|.
name|request
argument_list|(
name|req
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|sout
operator|==
literal|null
condition|)
block|{
name|sout
operator|=
operator|new
name|BufferedServletOutputStream
argument_list|(
name|super
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sout
return|;
block|}
specifier|private
specifier|final
class|class
name|BufferedServletOutputStream
extends|extends
name|ServletOutputStream
block|{
specifier|final
name|ServletOutputStream
name|delegate
decl_stmt|;
name|CachedOutputStream
name|out
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|BufferedServletOutputStream
parameter_list|(
name|ServletOutputStream
name|d
parameter_list|)
block|{
name|delegate
operator|=
name|d
expr_stmt|;
block|}
name|OutputStream
name|getOut
parameter_list|()
block|{
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
name|out
operator|=
operator|new
name|CachedOutputStream
argument_list|()
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
name|void
name|send
parameter_list|(
name|boolean
name|complete
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|response
operator|.
name|getStatus
argument_list|()
operator|>=
literal|400
condition|)
block|{
name|int
name|i
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
name|response
operator|.
name|setStatus
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|response
operator|.
name|addIntHeader
argument_list|(
name|WebSocketUtils
operator|.
name|SC_KEY
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|lockOutputStream
argument_list|()
expr_stmt|;
name|out
operator|.
name|writeCacheTo
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|IOException
block|{
name|getOut
argument_list|()
operator|.
name|write
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|send
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
name|send
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|getOut
argument_list|()
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|getOut
argument_list|()
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

