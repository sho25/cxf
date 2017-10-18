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
name|operation
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|helpers
operator|.
name|DOMUtils
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestedSecurityTokenType
import|;
end_import

begin_comment
comment|/**  * The default implementation of TokenWrapper. For DOM Elements it just set the token directly on the  * RSTT. If it's a String (as per the case of JWT Tokens), it puts a "TokenWrapper" wrapper around the  * token.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultTokenWrapper
implements|implements
name|TokenWrapper
block|{
comment|/**      * Wrap the Token parameter and set it on the RequestedSecurityTokenType parameter      */
specifier|public
name|void
name|wrapToken
parameter_list|(
name|Object
name|token
parameter_list|,
name|RequestedSecurityTokenType
name|requestedTokenType
parameter_list|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|String
condition|)
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|getEmptyDocument
argument_list|()
decl_stmt|;
name|Element
name|tokenWrapper
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
literal|null
argument_list|,
literal|"TokenWrapper"
argument_list|)
decl_stmt|;
name|tokenWrapper
operator|.
name|setTextContent
argument_list|(
operator|(
name|String
operator|)
name|token
argument_list|)
expr_stmt|;
name|requestedTokenType
operator|.
name|setAny
argument_list|(
name|tokenWrapper
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestedTokenType
operator|.
name|setAny
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

