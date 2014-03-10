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
name|systest
operator|.
name|sts
operator|.
name|deployment
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Level
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
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
name|sts
operator|.
name|claims
operator|.
name|ClaimsParser
import|;
end_import

begin_comment
comment|/**  * A Custom ClaimsParser implementation.  */
end_comment

begin_class
specifier|public
class|class
name|CustomClaimsParser
implements|implements
name|ClaimsParser
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DIALECT
init|=
literal|"http://schemas.mycompany.com/claims"
decl_stmt|;
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
name|CustomClaimsParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Claim
name|parse
parameter_list|(
name|Element
name|claim
parameter_list|)
block|{
return|return
name|parseClaimType
argument_list|(
name|claim
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Claim
name|parseClaimType
parameter_list|(
name|Element
name|claimType
parameter_list|)
block|{
name|String
name|claimLocalName
init|=
name|claimType
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|claimNS
init|=
name|claimType
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"ClaimType"
operator|.
name|equals
argument_list|(
name|claimLocalName
argument_list|)
condition|)
block|{
name|String
name|claimTypeUri
init|=
name|claimType
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Uri"
argument_list|)
decl_stmt|;
name|String
name|claimTypeOptional
init|=
name|claimType
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Optional"
argument_list|)
decl_stmt|;
name|Claim
name|requestClaim
init|=
operator|new
name|Claim
argument_list|()
decl_stmt|;
try|try
block|{
name|requestClaim
operator|.
name|setClaimType
argument_list|(
operator|new
name|URI
argument_list|(
name|claimTypeUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Cannot create URI from the given ClaimType attribute value "
operator|+
name|claimTypeUri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|requestClaim
operator|.
name|setOptional
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|claimTypeOptional
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|requestClaim
return|;
block|}
elseif|else
if|if
condition|(
literal|"ClaimValue"
operator|.
name|equals
argument_list|(
name|claimLocalName
argument_list|)
condition|)
block|{
name|String
name|claimTypeUri
init|=
name|claimType
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Uri"
argument_list|)
decl_stmt|;
name|String
name|claimTypeOptional
init|=
name|claimType
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Optional"
argument_list|)
decl_stmt|;
name|Claim
name|requestClaim
init|=
operator|new
name|Claim
argument_list|()
decl_stmt|;
try|try
block|{
name|requestClaim
operator|.
name|setClaimType
argument_list|(
operator|new
name|URI
argument_list|(
name|claimTypeUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Cannot create URI from the given ClaimTye attribute value "
operator|+
name|claimTypeUri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|Node
name|valueNode
init|=
name|claimType
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|valueNode
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"Value"
operator|.
name|equals
argument_list|(
name|valueNode
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|requestClaim
operator|.
name|addValue
argument_list|(
name|valueNode
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unsupported child element of ClaimValue element "
operator|+
name|valueNode
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No child element of ClaimValue element available"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|requestClaim
operator|.
name|setOptional
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|claimTypeOptional
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|requestClaim
return|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found unknown element: "
operator|+
name|claimLocalName
operator|+
literal|" "
operator|+
name|claimNS
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|/**      * Return the supported dialect of this class      */
specifier|public
name|String
name|getSupportedDialect
parameter_list|()
block|{
return|return
name|DIALECT
return|;
block|}
block|}
end_class

end_unit

