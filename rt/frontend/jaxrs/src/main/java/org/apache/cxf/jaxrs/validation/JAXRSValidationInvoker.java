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
name|ValidationException
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
name|Response
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
name|JAXRSInvoker
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
name|JAXRSUtils
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
name|Exchange
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
name|message
operator|.
name|MessageContentsList
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
name|validation
operator|.
name|ValidationProvider
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSValidationInvoker
extends|extends
name|JAXRSInvoker
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
name|JAXRSValidationInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|volatile
name|ValidationProvider
name|provider
decl_stmt|;
specifier|private
name|boolean
name|validateServiceObject
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|Message
name|message
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ValidationUtils
operator|.
name|isAnnotatedMethodAvailable
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|String
name|error
init|=
literal|"Resource method is not available"
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|error
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ValidationException
argument_list|(
name|error
argument_list|)
throw|;
block|}
name|ValidationProvider
name|theProvider
init|=
name|getProvider
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|isValidateServiceObject
argument_list|()
condition|)
block|{
name|theProvider
operator|.
name|validateBean
argument_list|(
name|serviceObject
argument_list|)
expr_stmt|;
block|}
name|theProvider
operator|.
name|validateParameters
argument_list|(
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|response
init|=
name|super
operator|.
name|invoke
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|MessageContentsList
condition|)
block|{
name|MessageContentsList
name|list
init|=
operator|(
name|MessageContentsList
operator|)
name|response
decl_stmt|;
if|if
condition|(
name|list
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
operator|(
operator|(
name|MessageContentsList
operator|)
name|list
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|instanceof
name|Response
condition|)
block|{
name|theProvider
operator|.
name|validateReturnValue
argument_list|(
name|m
argument_list|,
operator|(
operator|(
name|Response
operator|)
name|entity
operator|)
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|theProvider
operator|.
name|validateReturnValue
argument_list|(
name|serviceObject
argument_list|,
name|m
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|response
return|;
block|}
specifier|protected
name|ValidationProvider
name|getProvider
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|provider
operator|==
literal|null
condition|)
block|{
name|Object
name|prop
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|ValidationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|prop
operator|!=
literal|null
condition|)
block|{
name|provider
operator|=
operator|(
name|ValidationProvider
operator|)
name|prop
expr_stmt|;
block|}
else|else
block|{
name|provider
operator|=
operator|new
name|ValidationProvider
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|provider
return|;
block|}
specifier|public
name|void
name|setProvider
parameter_list|(
name|ValidationProvider
name|provider
parameter_list|)
block|{
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValidateServiceObject
parameter_list|()
block|{
return|return
name|validateServiceObject
return|;
block|}
specifier|public
name|void
name|setValidateServiceObject
parameter_list|(
name|boolean
name|validateServiceObject
parameter_list|)
block|{
name|this
operator|.
name|validateServiceObject
operator|=
name|validateServiceObject
expr_stmt|;
block|}
block|}
end_class

end_unit

