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
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|jaxrs
operator|.
name|impl
operator|.
name|tl
operator|.
name|ThreadLocalProxy
import|;
end_import

begin_class
specifier|public
class|class
name|ApplicationInfo
extends|extends
name|ProviderInfo
argument_list|<
name|Application
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|overridingProps
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|public
name|ApplicationInfo
parameter_list|(
name|Application
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|this
argument_list|(
name|provider
argument_list|,
literal|null
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ApplicationInfo
parameter_list|(
name|Application
name|provider
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ThreadLocalProxy
argument_list|<
name|?
argument_list|>
argument_list|>
name|constructorProxies
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|provider
argument_list|,
name|constructorProxies
argument_list|,
name|bus
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|appProps
init|=
name|super
operator|.
name|getProvider
argument_list|()
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|overridingProps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|appProps
return|;
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|appProps
argument_list|)
decl_stmt|;
name|props
operator|.
name|putAll
argument_list|(
name|overridingProps
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
block|}
specifier|public
name|void
name|setOverridingProps
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|overridingProps
parameter_list|)
block|{
name|this
operator|.
name|overridingProps
operator|=
name|overridingProps
expr_stmt|;
block|}
block|}
end_class

end_unit

