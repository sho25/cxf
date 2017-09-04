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
name|systest
operator|.
name|jaxrs
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
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequestWrapper
import|;
end_import

begin_class
specifier|public
class|class
name|XForwardedServletFilter
implements|implements
name|Filter
block|{
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|resp
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|HttpServletRequest
name|httpReq
init|=
operator|(
name|HttpServletRequest
operator|)
name|req
decl_stmt|;
if|if
condition|(
name|httpReq
operator|.
name|getHeader
argument_list|(
literal|"USE_XFORWARDED"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|httpReq
operator|=
operator|new
name|HttpServletRequestXForwardedFilter
argument_list|(
name|httpReq
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|doFilter
argument_list|(
name|httpReq
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|arg0
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|private
specifier|static
class|class
name|HttpServletRequestXForwardedFilter
extends|extends
name|HttpServletRequestWrapper
block|{
name|HttpServletRequestXForwardedFilter
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|super
argument_list|(
name|request
argument_list|)
expr_stmt|;
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
literal|"X-Forwarded-For"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"199.0.0.1"
return|;
block|}
elseif|else
if|if
condition|(
literal|"X-Forwarded-Proto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"https"
return|;
block|}
elseif|else
if|if
condition|(
literal|"X-Forwarded-Prefix"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"/reverse"
return|;
block|}
elseif|else
if|if
condition|(
literal|"X-Forwarded-Port"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"8090"
return|;
block|}
elseif|else
if|if
condition|(
literal|"X-Forwarded-Host"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"external"
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

