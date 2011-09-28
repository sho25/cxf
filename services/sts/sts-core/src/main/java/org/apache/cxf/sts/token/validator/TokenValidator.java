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
name|sts
operator|.
name|token
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
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
import|;
end_import

begin_comment
comment|/**  * An interface that can validate a security token.  */
end_comment

begin_interface
specifier|public
interface|interface
name|TokenValidator
block|{
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument.      */
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|)
function_decl|;
comment|/**      * Return true if this TokenValidator implementation is capable of validating the      * ReceivedToken argument in the given realm.      */
name|boolean
name|canHandleToken
parameter_list|(
name|ReceivedToken
name|validateTarget
parameter_list|,
name|String
name|realm
parameter_list|)
function_decl|;
comment|/**      * Validate a Token using the given TokenValidatorParameters.      */
name|TokenValidatorResponse
name|validateToken
parameter_list|(
name|TokenValidatorParameters
name|tokenParameters
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

