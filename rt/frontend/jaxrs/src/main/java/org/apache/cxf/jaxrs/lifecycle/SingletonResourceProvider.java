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
name|lifecycle
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
name|common
operator|.
name|util
operator|.
name|ClassHelper
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
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

begin_comment
comment|/**  * The default singleton resource provider which returns  * the same resource instance per every request  */
end_comment

begin_class
specifier|public
class|class
name|SingletonResourceProvider
implements|implements
name|ResourceProvider
block|{
specifier|private
name|Object
name|resourceInstance
decl_stmt|;
specifier|public
name|SingletonResourceProvider
parameter_list|(
name|Object
name|o
parameter_list|,
name|boolean
name|callPostConstruct
parameter_list|)
block|{
name|resourceInstance
operator|=
name|o
expr_stmt|;
if|if
condition|(
name|callPostConstruct
condition|)
block|{
name|InjectionUtils
operator|.
name|invokeLifeCycleMethod
argument_list|(
name|o
argument_list|,
name|ResourceUtils
operator|.
name|findPostConstructMethod
argument_list|(
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|o
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|SingletonResourceProvider
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|this
argument_list|(
name|o
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Object
name|getInstance
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
name|resourceInstance
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|releaseInstance
parameter_list|(
name|Message
name|m
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
comment|// complete
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResourceClass
parameter_list|()
block|{
return|return
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|resourceInstance
argument_list|)
return|;
block|}
block|}
end_class

end_unit

