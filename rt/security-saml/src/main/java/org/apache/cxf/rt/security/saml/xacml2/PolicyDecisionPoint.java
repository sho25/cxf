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
name|ResponseType
import|;
end_import

begin_comment
comment|/**  * An interface that describes a PolicyDecisionPoint (PDP) that supports XACML 2.0 using OpenSAML  */
end_comment

begin_interface
specifier|public
interface|interface
name|PolicyDecisionPoint
block|{
comment|/**      * Evaluate an XACML Request and return a Response      * @param request an XACML Request as a RequestType      * @return the XACML Response as a ResponseType      */
name|ResponseType
name|evaluate
parameter_list|(
name|RequestType
name|request
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

