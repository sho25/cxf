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
name|http
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
name|security
operator|.
name|AuthenticationException
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
comment|/**  * Translates an AuthenticationException into a 401 response  */
end_comment

begin_class
specifier|public
class|class
name|HttpAuthenticationFaultHandler
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
name|String
name|authenticationType
decl_stmt|;
name|String
name|realm
decl_stmt|;
specifier|public
name|HttpAuthenticationFaultHandler
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|)
expr_stmt|;
name|this
operator|.
name|authenticationType
operator|=
literal|"Basic"
expr_stmt|;
name|this
operator|.
name|realm
operator|=
literal|"CXF service"
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
throws|throws
name|Fault
block|{
comment|// Nothing
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exception
name|ex
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ex
operator|instanceof
name|AuthenticationException
condition|)
block|{
name|HttpServletResponse
name|resp
init|=
operator|(
name|HttpServletResponse
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_RESPONSE
argument_list|)
decl_stmt|;
name|resp
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
name|resp
operator|.
name|setHeader
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
name|authenticationType
operator|+
literal|" realm=\""
operator|+
name|realm
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|resp
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
try|try
block|{
name|resp
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|resp
operator|.
name|getOutputStream
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|setFaultObserver
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|//avoid return soap fault
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO
block|}
block|}
block|}
specifier|public
name|void
name|setAuthenticationType
parameter_list|(
name|String
name|authenticationType
parameter_list|)
block|{
name|this
operator|.
name|authenticationType
operator|=
name|authenticationType
expr_stmt|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
block|}
end_class

end_unit

