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
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|interceptors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|ws
operator|.
name|policy
operator|.
name|AbstractPolicyInterceptorProvider
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
name|policy
operator|.
name|AssertionInfoMap
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
name|policy
operator|.
name|SP11Constants
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
name|policy
operator|.
name|SP12Constants
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
name|policy
operator|.
name|model
operator|.
name|AlgorithmSuite
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
name|apache
operator|.
name|neethi
operator|.
name|All
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|ExactlyOne
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSConfig
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SpnegoTokenInterceptorProvider
extends|extends
name|AbstractPolicyInterceptorProvider
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SpnegoTokenInterceptorProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3412111025613191505L
decl_stmt|;
specifier|public
name|SpnegoTokenInterceptorProvider
parameter_list|()
block|{
name|super
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|SP11Constants
operator|.
name|SPNEGO_CONTEXT_TOKEN
argument_list|,
name|SP12Constants
operator|.
name|SPNEGO_CONTEXT_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SpnegoContextTokenOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SpnegoContextTokenOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SpnegoContextTokenInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SpnegoContextTokenInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
name|String
name|setupClient
parameter_list|(
name|STSClient
name|client
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|client
operator|.
name|setTrust
argument_list|(
name|NegotiationUtils
operator|.
name|getTrust10
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTrust
argument_list|(
name|NegotiationUtils
operator|.
name|getTrust13
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|NegotiationUtils
operator|.
name|getAddressingPolicy
argument_list|(
name|aim
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|client
operator|.
name|setPolicy
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|client
operator|.
name|setSoap11
argument_list|(
name|message
operator|.
name|getVersion
argument_list|()
operator|==
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setSpnego
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|WSSConfig
name|config
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
name|String
name|context
init|=
name|config
operator|.
name|getIdAllocator
argument_list|()
operator|.
name|createSecureId
argument_list|(
literal|"_"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|client
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|client
operator|.
name|setLocation
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|AlgorithmSuite
name|suite
init|=
name|NegotiationUtils
operator|.
name|getAlgorithmSuite
argument_list|(
name|aim
argument_list|)
decl_stmt|;
if|if
condition|(
name|suite
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|setAlgorithmSuite
argument_list|(
name|suite
argument_list|)
expr_stmt|;
name|int
name|x
init|=
name|suite
operator|.
name|getMaximumSymmetricKeyLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|x
operator|<
literal|256
condition|)
block|{
name|client
operator|.
name|setKeySize
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
init|=
name|client
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|mapSecurityProps
argument_list|(
name|message
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
return|return
name|s
return|;
block|}
specifier|private
specifier|static
name|void
name|mapSecurityProps
parameter_list|(
name|Message
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|)
block|{
for|for
control|(
name|String
name|s
range|:
name|SecurityConstants
operator|.
name|ALL_PROPERTIES
control|)
block|{
name|Object
name|v
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|ctx
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

