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
name|rt
operator|.
name|security
operator|.
name|saml
operator|.
name|xacml2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|opensaml
operator|.
name|xacml
operator|.
name|ctx
operator|.
name|RequestType
import|;
end_import

begin_comment
comment|/**  * This interface defines a way to create an XACML 2.0 Request using OpenSAML  */
end_comment

begin_interface
specifier|public
interface|interface
name|XACMLRequestBuilder
block|{
comment|/**      * Create an XACML Request given a Principal, list of roles and Message.      *       * @param principal The principal to insert into the Subject of the Request      * @param roles The list of roles associated with the principal      * @param message The Message from which to retrieve the resource      * @return An OpenSAML RequestType object      * @throws Exception      */
name|RequestType
name|createRequest
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

