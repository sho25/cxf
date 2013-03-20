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
name|common
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|Endpoint
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
name|frontend
operator|.
name|ClientProxy
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
name|SecurityConstants
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
name|tokenstore
operator|.
name|SecurityToken
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
name|tokenstore
operator|.
name|TokenStore
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
name|trust
operator|.
name|STSClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|example
operator|.
name|contract
operator|.
name|doubleit
operator|.
name|DoubleItPortType
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|TokenTestUtils
block|{
specifier|private
name|TokenTestUtils
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|void
name|verifyToken
parameter_list|(
name|DoubleItPortType
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|Endpoint
name|ep
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|id
init|=
operator|(
name|String
operator|)
name|ep
operator|.
name|get
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
decl_stmt|;
name|TokenStore
name|store
init|=
operator|(
name|TokenStore
operator|)
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
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
name|tokenstore
operator|.
name|SecurityToken
name|tok
init|=
name|store
operator|.
name|getToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tok
argument_list|)
expr_stmt|;
name|STSClient
name|sts
init|=
operator|(
name|STSClient
operator|)
name|ep
operator|.
name|get
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|validTokens
init|=
name|sts
operator|.
name|validateSecurityToken
argument_list|(
name|tok
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validTokens
operator|!=
literal|null
operator|&&
operator|!
name|validTokens
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|//mess with the token a bit to force it to fail to validate
name|Element
name|e
init|=
name|tok
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|Element
name|e2
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|e
argument_list|,
name|e
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"Conditions"
argument_list|)
decl_stmt|;
name|String
name|nb
init|=
name|e2
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"NotBefore"
argument_list|)
decl_stmt|;
name|String
name|noa
init|=
name|e2
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"NotOnOrAfter"
argument_list|)
decl_stmt|;
name|nb
operator|=
literal|"2010"
operator|+
name|nb
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|noa
operator|=
literal|"2010"
operator|+
name|noa
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|e2
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"NotBefore"
argument_list|,
name|nb
argument_list|)
expr_stmt|;
name|e2
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"NotOnOrAfter"
argument_list|,
name|noa
argument_list|)
expr_stmt|;
try|try
block|{
name|sts
operator|.
name|validateSecurityToken
argument_list|(
name|tok
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an invalid token"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
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
name|trust
operator|.
name|TrustException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
specifier|static
name|void
name|updateSTSPort
parameter_list|(
name|BindingProvider
name|p
parameter_list|,
name|String
name|port
parameter_list|)
block|{
name|STSClient
name|stsClient
init|=
operator|(
name|STSClient
operator|)
name|p
operator|.
name|getRequestContext
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|stsClient
operator|!=
literal|null
condition|)
block|{
name|String
name|location
init|=
name|stsClient
operator|.
name|getWsdlLocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|location
operator|.
name|contains
argument_list|(
literal|"8080"
argument_list|)
condition|)
block|{
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
name|location
operator|.
name|replace
argument_list|(
literal|"8080"
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|location
operator|.
name|contains
argument_list|(
literal|"8443"
argument_list|)
condition|)
block|{
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
name|location
operator|.
name|replace
argument_list|(
literal|"8443"
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

