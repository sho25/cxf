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
name|jaxrs
operator|.
name|model
operator|.
name|OperationResourceInfo
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
specifier|final
class|class
name|ValidationUtils
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
name|ValidationUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ValidationUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|Object
name|getResourceInstance
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
specifier|final
name|OperationResourceInfo
name|ori
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ResourceProvider
name|resourceProvider
init|=
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
operator|.
name|getResourceProvider
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|resourceProvider
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
name|String
name|error
init|=
literal|"Service object is not a singleton, use a custom invoker to validate"
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|error
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|resourceProvider
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isAnnotatedMethodAvailable
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
specifier|final
name|OperationResourceInfo
name|ori
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// If this is a user-model resource then no validation is possible
return|return
name|ori
operator|.
name|getAnnotatedMethod
argument_list|()
operator|!=
literal|null
return|;
block|}
specifier|public
specifier|static
name|Object
name|getResponseObject
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|Response
condition|?
operator|(
operator|(
name|Response
operator|)
name|o
operator|)
operator|.
name|getEntity
argument_list|()
else|:
name|o
return|;
block|}
block|}
end_class

end_unit

