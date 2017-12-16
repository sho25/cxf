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
name|microprofile
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Objects
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
name|Configurable
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
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|RestClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|annotation
operator|.
name|RegisterProvider
import|;
end_import

begin_class
specifier|public
class|class
name|CxfTypeSafeClientBuilder
implements|implements
name|RestClientBuilder
implements|,
name|Configurable
argument_list|<
name|RestClientBuilder
argument_list|>
block|{
specifier|private
name|String
name|baseUri
decl_stmt|;
specifier|private
specifier|final
name|MicroProfileClientConfigurableImpl
argument_list|<
name|RestClientBuilder
argument_list|>
name|configImpl
init|=
operator|new
name|MicroProfileClientConfigurableImpl
argument_list|<>
argument_list|(
name|this
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|baseUrl
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|url
argument_list|)
operator|.
name|toExternalForm
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|build
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
if|if
condition|(
name|baseUri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"baseUrl not set"
argument_list|)
throw|;
block|}
name|RegisterProvider
index|[]
name|providers
init|=
name|aClass
operator|.
name|getAnnotationsByType
argument_list|(
name|RegisterProvider
operator|.
name|class
argument_list|)
decl_stmt|;
name|Configuration
name|config
init|=
name|configImpl
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|providers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RegisterProvider
name|provider
range|:
name|providers
control|)
block|{
if|if
condition|(
operator|!
name|config
operator|.
name|isRegistered
argument_list|(
name|provider
operator|.
name|value
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|provider
operator|.
name|priority
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
name|register
argument_list|(
name|provider
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|register
argument_list|(
name|provider
operator|.
name|value
argument_list|()
argument_list|,
name|provider
operator|.
name|priority
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|MicroProfileClientFactoryBean
name|bean
init|=
operator|new
name|MicroProfileClientFactoryBean
argument_list|(
name|getConfiguration
argument_list|()
argument_list|,
name|baseUri
argument_list|,
name|aClass
argument_list|)
decl_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|aClass
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configImpl
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|property
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|configImpl
operator|.
name|property
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|,
name|int
name|priority
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|,
name|priority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|componentClass
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|componentClass
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|,
name|int
name|priority
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|,
name|priority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|RestClientBuilder
name|register
parameter_list|(
name|Object
name|component
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
name|configImpl
operator|.
name|register
argument_list|(
name|component
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

