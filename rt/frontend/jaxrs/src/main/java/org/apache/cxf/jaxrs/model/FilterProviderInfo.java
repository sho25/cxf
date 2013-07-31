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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_class
specifier|public
class|class
name|FilterProviderInfo
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ProviderInfo
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|nameBinding
decl_stmt|;
specifier|private
name|int
name|priority
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|supportedContracts
decl_stmt|;
specifier|public
name|FilterProviderInfo
parameter_list|(
name|T
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|String
name|nameBinding
parameter_list|,
name|int
name|priority
parameter_list|,
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|supportedContracts
parameter_list|)
block|{
name|super
argument_list|(
name|provider
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|nameBinding
operator|=
name|Collections
operator|.
name|singleton
argument_list|(
name|nameBinding
argument_list|)
expr_stmt|;
name|this
operator|.
name|priority
operator|=
name|priority
expr_stmt|;
name|this
operator|.
name|supportedContracts
operator|=
name|supportedContracts
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getNameBinding
parameter_list|()
block|{
return|return
name|nameBinding
return|;
block|}
specifier|public
name|int
name|getPriority
parameter_list|()
block|{
return|return
name|priority
return|;
block|}
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getSupportedContracts
parameter_list|()
block|{
return|return
name|supportedContracts
return|;
block|}
block|}
end_class

end_unit

