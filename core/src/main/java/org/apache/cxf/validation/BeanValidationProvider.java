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
name|Map
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintViolation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintViolationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ParameterNameProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Validation
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
name|javax
operator|.
name|validation
operator|.
name|ValidationProviderResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ValidatorFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|executable
operator|.
name|ExecutableValidator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|spi
operator|.
name|ValidationProvider
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_class
specifier|public
class|class
name|BeanValidationProvider
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|BeanValidationProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ValidatorFactory
name|factory
decl_stmt|;
specifier|public
name|BeanValidationProvider
parameter_list|()
block|{
try|try
block|{
name|factory
operator|=
name|Validation
operator|.
name|buildDefaultValidatorFactory
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ValidationException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Bean Validation provider can not be found, no validation will be performed"
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|BeanValidationProvider
parameter_list|(
name|ParameterNameProvider
name|parameterNameProvider
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|ValidationConfiguration
argument_list|(
name|parameterNameProvider
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BeanValidationProvider
parameter_list|(
name|ValidationConfiguration
name|cfg
parameter_list|)
block|{
try|try
block|{
name|Configuration
argument_list|<
name|?
argument_list|>
name|factoryCfg
init|=
name|Validation
operator|.
name|byDefaultProvider
argument_list|()
operator|.
name|configure
argument_list|()
decl_stmt|;
name|initFactoryConfig
argument_list|(
name|factoryCfg
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|factory
operator|=
name|factoryCfg
operator|.
name|buildValidatorFactory
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ValidationException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Bean Validation provider can not be found, no validation will be performed"
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|BeanValidationProvider
parameter_list|(
name|ValidatorFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Factory is null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
specifier|public
name|BeanValidationProvider
parameter_list|(
name|ValidationProviderResolver
name|resolver
parameter_list|)
block|{
name|this
argument_list|(
name|resolver
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|Configuration
argument_list|<
name|T
argument_list|>
parameter_list|,
name|U
extends|extends
name|ValidationProvider
argument_list|<
name|T
argument_list|>
parameter_list|>
name|BeanValidationProvider
parameter_list|(
name|ValidationProviderResolver
name|resolver
parameter_list|,
name|Class
argument_list|<
name|U
argument_list|>
name|providerType
parameter_list|)
block|{
name|this
argument_list|(
name|resolver
argument_list|,
name|providerType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|Configuration
argument_list|<
name|T
argument_list|>
parameter_list|,
name|U
extends|extends
name|ValidationProvider
argument_list|<
name|T
argument_list|>
parameter_list|>
name|BeanValidationProvider
parameter_list|(
name|ValidationProviderResolver
name|resolver
parameter_list|,
name|Class
argument_list|<
name|U
argument_list|>
name|providerType
parameter_list|,
name|ValidationConfiguration
name|cfg
parameter_list|)
block|{
try|try
block|{
name|Configuration
argument_list|<
name|?
argument_list|>
name|factoryCfg
init|=
name|providerType
operator|!=
literal|null
condition|?
name|Validation
operator|.
name|byProvider
argument_list|(
name|providerType
argument_list|)
operator|.
name|providerResolver
argument_list|(
name|resolver
argument_list|)
operator|.
name|configure
argument_list|()
else|:
name|Validation
operator|.
name|byDefaultProvider
argument_list|()
operator|.
name|providerResolver
argument_list|(
name|resolver
argument_list|)
operator|.
name|configure
argument_list|()
decl_stmt|;
name|initFactoryConfig
argument_list|(
name|factoryCfg
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|factory
operator|=
name|factoryCfg
operator|.
name|buildValidatorFactory
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ValidationException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Bean Validation provider can not be found, no validation will be performed"
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|private
specifier|static
name|void
name|initFactoryConfig
parameter_list|(
name|Configuration
argument_list|<
name|?
argument_list|>
name|factoryCfg
parameter_list|,
name|ValidationConfiguration
name|cfg
parameter_list|)
block|{
if|if
condition|(
name|cfg
operator|!=
literal|null
condition|)
block|{
name|factoryCfg
operator|.
name|parameterNameProvider
argument_list|(
name|cfg
operator|.
name|getParameterNameProvider
argument_list|()
argument_list|)
expr_stmt|;
name|factoryCfg
operator|.
name|messageInterpolator
argument_list|(
name|cfg
operator|.
name|getMessageInterpolator
argument_list|()
argument_list|)
expr_stmt|;
name|factoryCfg
operator|.
name|traversableResolver
argument_list|(
name|cfg
operator|.
name|getTraversableResolver
argument_list|()
argument_list|)
expr_stmt|;
name|factoryCfg
operator|.
name|constraintValidatorFactory
argument_list|(
name|cfg
operator|.
name|getConstraintValidatorFactory
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|cfg
operator|.
name|getProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|factoryCfg
operator|.
name|addProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|validateParameters
parameter_list|(
specifier|final
name|T
name|instance
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|Object
index|[]
name|arguments
parameter_list|)
block|{
specifier|final
name|ExecutableValidator
name|methodValidator
init|=
name|getExecutableValidator
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|T
argument_list|>
argument_list|>
name|violations
init|=
name|methodValidator
operator|.
name|validateParameters
argument_list|(
name|instance
argument_list|,
name|method
argument_list|,
name|arguments
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|violations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ConstraintViolationException
argument_list|(
name|violations
argument_list|)
throw|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|validateReturnValue
parameter_list|(
specifier|final
name|T
name|instance
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|Object
name|returnValue
parameter_list|)
block|{
specifier|final
name|ExecutableValidator
name|methodValidator
init|=
name|getExecutableValidator
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|T
argument_list|>
argument_list|>
name|violations
init|=
name|methodValidator
operator|.
name|validateReturnValue
argument_list|(
name|instance
argument_list|,
name|method
argument_list|,
name|returnValue
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|violations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResponseConstraintViolationException
argument_list|(
name|violations
argument_list|)
throw|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|validateReturnValue
parameter_list|(
specifier|final
name|T
name|bean
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|T
argument_list|>
argument_list|>
name|violations
init|=
name|doValidateBean
argument_list|(
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|violations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResponseConstraintViolationException
argument_list|(
name|violations
argument_list|)
throw|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|validateBean
parameter_list|(
specifier|final
name|T
name|bean
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|T
argument_list|>
argument_list|>
name|violations
init|=
name|doValidateBean
argument_list|(
name|bean
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|violations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ConstraintViolationException
argument_list|(
name|violations
argument_list|)
throw|;
block|}
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|T
argument_list|>
argument_list|>
name|doValidateBean
parameter_list|(
specifier|final
name|T
name|bean
parameter_list|)
block|{
return|return
name|factory
operator|.
name|getValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|bean
argument_list|)
return|;
block|}
specifier|private
name|ExecutableValidator
name|getExecutableValidator
parameter_list|()
block|{
return|return
name|factory
operator|.
name|getValidator
argument_list|()
operator|.
name|forExecutables
argument_list|()
return|;
block|}
block|}
end_class

end_unit

