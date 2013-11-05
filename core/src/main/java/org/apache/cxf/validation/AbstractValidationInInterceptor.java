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
specifier|abstract
class|class
name|AbstractValidationInInterceptor
extends|extends
name|AbstractValidationInterceptor
block|{
specifier|public
name|AbstractValidationInInterceptor
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
name|AbstractValidationInInterceptor
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
name|ValidationProvider
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
name|arguments
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
name|ValidationProvider
operator|.
name|class
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

