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
name|ValidationProvider
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
name|ValidationProvider
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
name|ValidationProvider
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
literal|"Bean Validation provider could be found, no validation will be performed"
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|ValidationProvider
parameter_list|(
name|ValidationProviderResolver
name|resolver
parameter_list|)
block|{
try|try
block|{
name|Configuration
argument_list|<
name|?
argument_list|>
name|cfg
init|=
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
name|factory
operator|=
name|cfg
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
literal|"Bean Validation provider could be found, no validation will be performed"
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|Configuration
argument_list|<
name|T
argument_list|>
parameter_list|>
name|ValidationProvider
parameter_list|(
name|Class
argument_list|<
name|javax
operator|.
name|validation
operator|.
name|spi
operator|.
name|ValidationProvider
argument_list|<
name|T
argument_list|>
argument_list|>
name|providerType
parameter_list|,
name|ValidationProviderResolver
name|resolver
parameter_list|)
block|{
try|try
block|{
name|Configuration
argument_list|<
name|?
argument_list|>
name|cfg
init|=
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
decl_stmt|;
name|factory
operator|=
name|cfg
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
literal|"Bean Validation provider could be found, no validation will be performed"
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|ValidationProvider
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
parameter_list|<
name|T
parameter_list|>
name|void
name|validate
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
name|factory
operator|.
name|getValidator
argument_list|()
operator|.
name|forExecutables
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
name|validate
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
name|factory
operator|.
name|getValidator
argument_list|()
operator|.
name|forExecutables
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
block|}
end_class

end_unit

