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
name|impl
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
name|net
operator|.
name|URI
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
name|container
operator|.
name|ContainerRequestContext
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
name|MultivaluedMap
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
name|Request
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
name|SecurityContext
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
name|UriInfo
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
name|IOUtils
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
name|ExceptionUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|ContainerRequestContextImpl
extends|extends
name|AbstractRequestContextImpl
implements|implements
name|ContainerRequestContext
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENDPOINT_ADDRESS_PROPERTY
init|=
literal|"org.apache.cxf.transport.endpoint.address"
decl_stmt|;
specifier|private
name|boolean
name|preMatch
decl_stmt|;
specifier|public
name|ContainerRequestContextImpl
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|preMatch
parameter_list|,
name|boolean
name|responseContext
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|preMatch
operator|=
name|preMatch
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getEntityStream
parameter_list|()
block|{
return|return
name|m
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Request
name|getRequest
parameter_list|()
block|{
return|return
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SecurityContext
name|getSecurityContext
parameter_list|()
block|{
name|SecurityContext
name|sc
init|=
name|m
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|sc
operator|==
literal|null
condition|?
operator|new
name|SecurityContextImpl
argument_list|(
name|m
argument_list|)
else|:
name|sc
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriInfo
name|getUriInfo
parameter_list|()
block|{
return|return
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasEntity
parameter_list|()
block|{
name|InputStream
name|is
init|=
name|getEntityStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Is Content-Length is explicitly set to 0 ?
if|if
condition|(
name|HttpUtils
operator|.
name|isPayloadEmpty
argument_list|(
name|getHeaders
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
return|return
operator|!
name|IOUtils
operator|.
name|isEmpty
argument_list|(
name|getEntityStream
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEntityStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|checkContext
argument_list|()
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
block|{
name|h
operator|=
literal|null
expr_stmt|;
return|return
name|HttpUtils
operator|.
name|getModifiableStringHeaders
argument_list|(
name|m
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRequestUri
parameter_list|(
name|URI
name|requestUri
parameter_list|)
throws|throws
name|IllegalStateException
block|{
if|if
condition|(
name|requestUri
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|String
name|baseUriString
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|requestUriString
init|=
name|requestUri
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|requestUriString
operator|.
name|startsWith
argument_list|(
name|baseUriString
argument_list|)
condition|)
block|{
name|setRequestUri
argument_list|(
name|requestUri
argument_list|,
name|URI
operator|.
name|create
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|requestUriString
operator|=
name|requestUriString
operator|.
name|substring
argument_list|(
name|baseUriString
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|requestUriString
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|requestUriString
operator|=
literal|"/"
expr_stmt|;
block|}
name|requestUri
operator|=
name|URI
operator|.
name|create
argument_list|(
name|requestUriString
argument_list|)
expr_stmt|;
block|}
block|}
name|doSetRequestUri
argument_list|(
name|requestUri
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doSetRequestUri
parameter_list|(
name|URI
name|requestUri
parameter_list|)
throws|throws
name|IllegalStateException
block|{
name|checkNotPreMatch
argument_list|()
expr_stmt|;
name|HttpUtils
operator|.
name|resetRequestURI
argument_list|(
name|m
argument_list|,
name|requestUri
operator|.
name|getRawPath
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|query
init|=
name|requestUri
operator|.
name|getRawQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRequestUri
parameter_list|(
name|URI
name|baseUri
parameter_list|,
name|URI
name|requestUri
parameter_list|)
throws|throws
name|IllegalStateException
block|{
name|doSetRequestUri
argument_list|(
name|requestUri
argument_list|)
expr_stmt|;
name|Object
name|servletRequest
init|=
name|m
operator|.
name|get
argument_list|(
literal|"HTTP.REQUEST"
argument_list|)
decl_stmt|;
if|if
condition|(
name|servletRequest
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
operator|)
name|servletRequest
operator|)
operator|.
name|setAttribute
argument_list|(
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|baseUri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSecurityContext
parameter_list|(
name|SecurityContext
name|sc
parameter_list|)
block|{
name|checkContext
argument_list|()
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|sc
argument_list|)
expr_stmt|;
if|if
condition|(
name|sc
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|security
operator|.
name|SecurityContext
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|security
operator|.
name|SecurityContext
operator|.
name|class
argument_list|,
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|security
operator|.
name|SecurityContext
operator|)
name|sc
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkNotPreMatch
parameter_list|()
block|{
if|if
condition|(
operator|!
name|preMatch
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
throws|throws
name|IllegalStateException
block|{
name|checkNotPreMatch
argument_list|()
expr_stmt|;
name|super
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

