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
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Phase
import|;
end_import

begin_class
specifier|public
class|class
name|BeanValidationOutInterceptor
extends|extends
name|AbstractValidationInterceptor
block|{
specifier|private
name|boolean
name|enforceOnlyBeanConstraints
decl_stmt|;
specifier|public
name|BeanValidationOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_MARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BeanValidationOutInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleValidation
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|Object
name|resourceInstance
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
block|{
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Object
name|entity
init|=
name|unwrapEntity
argument_list|(
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|BeanValidationProvider
name|theProvider
init|=
name|getOutProvider
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|isEnforceOnlyBeanConstraints
argument_list|()
condition|)
block|{
name|theProvider
operator|.
name|validateReturnValue
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|theProvider
operator|.
name|validateReturnValue
argument_list|(
name|resourceInstance
argument_list|,
name|method
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Object
name|unwrapEntity
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
return|return
name|entity
return|;
block|}
specifier|protected
name|BeanValidationProvider
name|getOutProvider
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|BeanValidationProvider
name|provider
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BeanValidationProvider
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|provider
operator|==
literal|null
condition|?
name|getProvider
argument_list|(
name|message
argument_list|)
else|:
name|provider
return|;
block|}
specifier|public
name|boolean
name|isEnforceOnlyBeanConstraints
parameter_list|()
block|{
return|return
name|enforceOnlyBeanConstraints
return|;
block|}
specifier|public
name|void
name|setEnforceOnlyBeanConstraints
parameter_list|(
name|boolean
name|enforceOnlyBeanConstraints
parameter_list|)
block|{
name|this
operator|.
name|enforceOnlyBeanConstraints
operator|=
name|enforceOnlyBeanConstraints
expr_stmt|;
block|}
block|}
end_class

end_unit

