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
name|tools
operator|.
name|validator
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_class
specifier|public
class|class
name|ServiceValidator
extends|extends
name|AbstractValidator
block|{
specifier|protected
name|ServiceInfo
name|service
decl_stmt|;
specifier|public
name|void
name|setService
parameter_list|(
specifier|final
name|ServiceInfo
name|s
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"ServiceValidator.isValid should be extended"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

