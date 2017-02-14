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
name|util
operator|.
name|List
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
name|helpers
operator|.
name|CastUtils
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
name|AtmosphereInterceptor
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
name|HeaderConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|interceptor
operator|.
name|CacheHeadersInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|interceptor
operator|.
name|HeartbeatInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|interceptor
operator|.
name|JavaScriptProtocol
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|interceptor
operator|.
name|SSEAtmosphereInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|util
operator|.
name|Utils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AtmosphereUtils
block|{
specifier|private
name|AtmosphereUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|addInterceptors
parameter_list|(
name|AtmosphereFramework
name|framework
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|Object
name|ais
init|=
name|bus
operator|.
name|getProperty
argument_list|(
literal|"atmosphere.interceptors"
argument_list|)
decl_stmt|;
comment|// pre-install those atmosphere default interceptors before the custom interceptors.
name|framework
operator|.
name|interceptor
argument_list|(
operator|new
name|CacheHeadersInterceptor
argument_list|()
argument_list|)
operator|.
name|interceptor
argument_list|(
operator|new
name|HeartbeatInterceptor
argument_list|()
argument_list|)
operator|.
name|interceptor
argument_list|(
operator|new
name|SSEAtmosphereInterceptor
argument_list|()
argument_list|)
operator|.
name|interceptor
argument_list|(
operator|new
name|JavaScriptProtocol
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
operator|||
name|ais
operator|instanceof
name|AtmosphereInterceptor
condition|)
block|{
name|framework
operator|.
name|interceptor
argument_list|(
name|ais
operator|==
literal|null
condition|?
operator|new
name|DefaultProtocolInterceptor
argument_list|()
else|:
operator|(
name|AtmosphereInterceptor
operator|)
name|ais
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|ais
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|List
argument_list|<
name|AtmosphereInterceptor
argument_list|>
name|icps
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ais
argument_list|)
decl_stmt|;
comment|// add the custom interceptors
for|for
control|(
name|AtmosphereInterceptor
name|icp
operator|:
name|icps
control|)
block|{
name|framework
operator|.
name|interceptor
argument_list|(
name|icp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_function
specifier|public
specifier|static
name|boolean
name|useAtmosphere
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
name|Utils
operator|.
name|webSocketEnabled
argument_list|(
name|req
argument_list|)
operator|||
name|req
operator|.
name|getParameter
argument_list|(
name|HeaderConfig
operator|.
name|X_ATMOSPHERE_TRANSPORT
argument_list|)
operator|!=
literal|null
return|;
block|}
end_function

unit|}
end_unit

