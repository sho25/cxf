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
name|cdi
package|;
end_package

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
name|lifecycle
operator|.
name|ResourceProvider
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
name|SingletonResourceProvider
implements|implements
name|ResourceProvider
block|{
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
specifier|private
name|Object
name|instance
decl_stmt|;
name|SingletonResourceProvider
parameter_list|(
specifier|final
name|Lifecycle
name|lifecycle
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|instance
operator|=
name|lifecycle
operator|.
name|create
argument_list|()
expr_stmt|;
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getInstance
parameter_list|(
specifier|final
name|Message
name|m
parameter_list|)
block|{
return|return
name|instance
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|releaseInstance
parameter_list|(
specifier|final
name|Message
name|m
parameter_list|,
specifier|final
name|Object
name|o
parameter_list|)
block|{
comment|// no-op
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResourceClass
parameter_list|()
block|{
return|return
name|clazz
return|;
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
block|}
end_class

end_unit

