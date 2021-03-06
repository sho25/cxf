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
name|jaxrs
operator|.
name|client
operator|.
name|spec
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
name|OutputStream
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ProcessingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|interceptor
operator|.
name|AbstractOutDatabindingInterceptor
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
name|jaxrs
operator|.
name|client
operator|.
name|ClientProviderFactory
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
name|jaxrs
operator|.
name|model
operator|.
name|ProviderInfo
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|InjectionUtils
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
name|message
operator|.
name|MessageImpl
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
name|transport
operator|.
name|MessageObserver
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
name|ProxyOutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|ClientRequestFilterInterceptor
extends|extends
name|AbstractOutDatabindingInterceptor
block|{
specifier|public
name|ClientRequestFilterInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|outMessage
parameter_list|)
throws|throws
name|Fault
block|{
name|ClientProviderFactory
name|pf
init|=
name|ClientProviderFactory
operator|.
name|getInstance
argument_list|(
name|outMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|pf
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// create an empty proxy output stream that the filter can interact with
comment|// and save a reference for later
name|ProxyOutputStream
name|pos
init|=
operator|new
name|ProxyOutputStream
argument_list|()
decl_stmt|;
name|outMessage
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|pos
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|setContent
argument_list|(
name|ProxyOutputStream
operator|.
name|class
argument_list|,
name|pos
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProviderInfo
argument_list|<
name|ClientRequestFilter
argument_list|>
argument_list|>
name|filters
init|=
name|pf
operator|.
name|getClientRequestFilters
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|filters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|outMessage
operator|.
name|getExchange
argument_list|()
decl_stmt|;
specifier|final
name|ClientRequestContext
name|context
init|=
operator|new
name|ClientRequestContextImpl
argument_list|(
name|outMessage
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|ProviderInfo
argument_list|<
name|ClientRequestFilter
argument_list|>
name|filter
range|:
name|filters
control|)
block|{
name|InjectionUtils
operator|.
name|injectContexts
argument_list|(
name|filter
operator|.
name|getProvider
argument_list|()
argument_list|,
name|filter
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
try|try
block|{
name|filter
operator|.
name|getProvider
argument_list|()
operator|.
name|filter
argument_list|(
name|context
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|HttpUtils
operator|.
name|convertHeaderValuesToString
argument_list|(
name|headers
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|outMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|outMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|response
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|MessageObserver
name|observer
init|=
name|exchange
operator|.
name|get
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
decl_stmt|;
name|observer
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProcessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

