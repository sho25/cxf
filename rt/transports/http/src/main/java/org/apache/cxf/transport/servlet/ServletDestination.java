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
name|servlet
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
name|net
operator|.
name|URI
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
name|http
operator|.
name|HttpServletRequest
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
name|Bus
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
name|EndpointInfo
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
name|http
operator|.
name|AbstractHTTPDestination
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
name|http
operator|.
name|DestinationRegistry
import|;
end_import

begin_class
specifier|public
class|class
name|ServletDestination
extends|extends
name|AbstractHTTPDestination
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ServletDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
comment|/**      * Constructor, allowing subsititution of configuration.      *       * @param b the associated Bus      * @param ci the associated conduit initiator      * @param ei the endpoint info of the destination       * @param cfg the configuration      * @throws IOException      */
specifier|public
name|ServletDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
comment|// would add the default port to the address
name|super
argument_list|(
name|b
argument_list|,
name|registry
argument_list|,
name|ei
argument_list|,
name|path
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
specifier|protected
name|Message
name|retrieveFromServlet3Async
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
comment|// It looks current Servlet3 implementation request doesn't pass the isAsyncStart
comment|// status to the redispatched request
try|try
block|{
if|if
condition|(
name|req
operator|.
name|isAsyncSupported
argument_list|()
condition|)
block|{
return|return
operator|(
name|Message
operator|)
name|req
operator|.
name|getAttribute
argument_list|(
name|CXF_CONTINUATION_MESSAGE
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// the request may not implement the Servlet3 API
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|String
name|getBasePath
parameter_list|(
name|String
name|contextPath
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|address
init|=
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|==
literal|null
condition|)
block|{
return|return
name|contextPath
return|;
block|}
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
return|return
name|URI
operator|.
name|create
argument_list|(
name|address
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
return|return
name|contextPath
operator|+
name|address
return|;
block|}
block|}
end_class

end_unit

