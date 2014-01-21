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
name|javax
operator|.
name|validation
operator|.
name|ValidationException
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
name|BeanValidationInInterceptor
extends|extends
name|AbstractValidationInterceptor
block|{
specifier|public
name|BeanValidationInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BeanValidationInInterceptor
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
specifier|protected
name|Object
name|getServiceObject
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|super
operator|.
name|getServiceObject
argument_list|(
name|message
argument_list|)
argument_list|,
literal|"SERVICE_OBJECT_NULL"
argument_list|)
return|;
block|}
specifier|protected
name|Method
name|getServiceMethod
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|(
name|Method
operator|)
name|checkNotNull
argument_list|(
name|super
operator|.
name|getServiceMethod
argument_list|(
name|message
argument_list|)
argument_list|,
literal|"SERVICE_METHOD_NULL"
argument_list|)
return|;
block|}
specifier|private
name|Object
name|checkNotNull
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
name|name
argument_list|,
name|BUNDLE
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ValidationException
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|object
return|;
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
operator|>
literal|0
condition|)
block|{
name|BeanValidationProvider
name|provider
init|=
name|getProvider
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|provider
operator|.
name|validateParameters
argument_list|(
name|resourceInstance
argument_list|,
name|method
argument_list|,
name|unwrapArgs
argument_list|(
name|arguments
argument_list|)
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|BeanValidationProvider
operator|.
name|class
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|unwrapArgs
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
block|{
return|return
name|arguments
return|;
block|}
block|}
end_class

end_unit

