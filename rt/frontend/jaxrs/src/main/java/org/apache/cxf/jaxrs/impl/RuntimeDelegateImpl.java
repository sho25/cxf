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
name|util
operator|.
name|Date
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Application
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
name|CacheControl
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
name|Cookie
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
name|EntityTag
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
name|Link
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
name|MediaType
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
name|NewCookie
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
operator|.
name|ResponseBuilder
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
name|UriBuilder
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
name|Variant
operator|.
name|VariantListBuilder
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
name|ext
operator|.
name|RuntimeDelegate
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
name|endpoint
operator|.
name|Server
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
name|JAXRSServerFactoryBean
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
name|ResourceUtils
import|;
end_import

begin_class
specifier|public
class|class
name|RuntimeDelegateImpl
extends|extends
name|RuntimeDelegate
block|{
specifier|protected
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|HeaderDelegate
argument_list|<
name|?
argument_list|>
argument_list|>
name|headerProviders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|RuntimeDelegateImpl
parameter_list|()
block|{
name|headerProviders
operator|.
name|put
argument_list|(
name|MediaType
operator|.
name|class
argument_list|,
operator|new
name|MediaTypeHeaderProvider
argument_list|()
argument_list|)
expr_stmt|;
name|headerProviders
operator|.
name|put
argument_list|(
name|CacheControl
operator|.
name|class
argument_list|,
operator|new
name|CacheControlHeaderProvider
argument_list|()
argument_list|)
expr_stmt|;
name|headerProviders
operator|.
name|put
argument_list|(
name|EntityTag
operator|.
name|class
argument_list|,
operator|new
name|EntityTagHeaderProvider
argument_list|()
argument_list|)
expr_stmt|;
name|headerProviders
operator|.
name|put
argument_list|(
name|Cookie
operator|.
name|class
argument_list|,
operator|new
name|CookieHeaderProvider
argument_list|()
argument_list|)
expr_stmt|;
name|headerProviders
operator|.
name|put
argument_list|(
name|NewCookie
operator|.
name|class
argument_list|,
operator|new
name|NewCookieHeaderProvider
argument_list|()
argument_list|)
expr_stmt|;
name|headerProviders
operator|.
name|put
argument_list|(
name|Link
operator|.
name|class
argument_list|,
operator|new
name|LinkHeaderProvider
argument_list|()
argument_list|)
expr_stmt|;
name|headerProviders
operator|.
name|put
argument_list|(
name|Date
operator|.
name|class
argument_list|,
operator|new
name|DateHeaderProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|createInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|ResponseBuilder
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
operator|new
name|ResponseBuilderImpl
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|UriBuilder
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
operator|new
name|UriBuilderImpl
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|VariantListBuilder
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
operator|new
name|VariantListBuilderImpl
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|HeaderDelegate
argument_list|<
name|T
argument_list|>
name|createHeaderDelegate
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"HeaderDelegate type is null"
argument_list|)
throw|;
block|}
return|return
operator|(
name|HeaderDelegate
argument_list|<
name|T
argument_list|>
operator|)
name|headerProviders
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|createResponseBuilder
parameter_list|()
block|{
return|return
operator|new
name|ResponseBuilderImpl
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriBuilder
name|createUriBuilder
parameter_list|()
block|{
return|return
operator|new
name|UriBuilderImpl
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|VariantListBuilder
name|createVariantListBuilder
parameter_list|()
block|{
return|return
operator|new
name|VariantListBuilderImpl
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|createEndpoint
parameter_list|(
name|Application
name|app
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|endpointType
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|UnsupportedOperationException
block|{
if|if
condition|(
name|app
operator|==
literal|null
operator|||
operator|(
operator|!
name|Server
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|endpointType
argument_list|)
operator|&&
operator|!
name|JAXRSServerFactoryBean
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|endpointType
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|JAXRSServerFactoryBean
name|bean
init|=
name|ResourceUtils
operator|.
name|createApplication
argument_list|(
name|app
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|JAXRSServerFactoryBean
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|endpointType
argument_list|)
condition|)
block|{
return|return
name|endpointType
operator|.
name|cast
argument_list|(
name|bean
argument_list|)
return|;
block|}
name|bean
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|endpointType
operator|.
name|cast
argument_list|(
name|server
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Link
operator|.
name|Builder
name|createLinkBuilder
parameter_list|()
block|{
return|return
operator|new
name|LinkBuilderImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit

