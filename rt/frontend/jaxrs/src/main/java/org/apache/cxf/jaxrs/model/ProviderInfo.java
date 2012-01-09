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

begin_class
specifier|public
class|class
name|ProviderInfo
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractResourceInfo
block|{
specifier|private
name|T
name|provider
decl_stmt|;
specifier|public
name|ProviderInfo
parameter_list|(
name|T
name|provider
parameter_list|)
block|{
name|super
argument_list|(
name|provider
operator|.
name|getClass
argument_list|()
argument_list|,
name|provider
operator|.
name|getClass
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|T
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|ProviderInfo
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|provider
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ProviderInfo
argument_list|<
name|?
argument_list|>
operator|)
name|obj
operator|)
operator|.
name|getProvider
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|provider
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

