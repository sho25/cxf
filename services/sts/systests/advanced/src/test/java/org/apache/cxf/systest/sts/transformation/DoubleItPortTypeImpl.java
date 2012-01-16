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
name|transformation
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
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|WebServiceContext
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
name|handler
operator|.
name|MessageContext
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|feature
operator|.
name|Features
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
name|CastUtils
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
name|WSConstants
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
name|WSSecurityEngineResult
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
name|handler
operator|.
name|WSHandlerConstants
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
name|handler
operator|.
name|WSHandlerResult
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
name|saml
operator|.
name|ext
operator|.
name|AssertionWrapper
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
name|util
operator|.
name|WSSecurityUtil
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

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|endpointInterface
operator|=
literal|"org.example.contract.doubleit.DoubleItPortType"
argument_list|)
annotation|@
name|Features
argument_list|(
name|classes
operator|=
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|feature
operator|.
name|LoggingFeature
operator|.
name|class
argument_list|)
specifier|public
class|class
name|DoubleItPortTypeImpl
implements|implements
name|DoubleItPortType
block|{
annotation|@
name|Resource
name|WebServiceContext
name|wsc
decl_stmt|;
specifier|public
name|int
name|doubleIt
parameter_list|(
name|int
name|numberToDouble
parameter_list|)
block|{
comment|//
comment|// Get the transformed SAML Assertion from the STS and check it
comment|//
name|MessageContext
name|context
init|=
name|wsc
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|handlerResults
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|context
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
name|WSSecurityEngineResult
name|actionResult
init|=
name|WSSecurityUtil
operator|.
name|fetchActionResult
argument_list|(
name|handlerResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResults
argument_list|()
argument_list|,
name|WSConstants
operator|.
name|UT
argument_list|)
decl_stmt|;
name|AssertionWrapper
name|assertion
init|=
operator|(
name|AssertionWrapper
operator|)
name|actionResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TRANSFORMED_TOKEN
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|assertion
operator|!=
literal|null
operator|&&
literal|"DoubleItSTSIssuer"
operator|.
name|equals
argument_list|(
name|assertion
operator|.
name|getIssuerString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|numberToDouble
operator|*
literal|2
return|;
block|}
block|}
end_class

end_unit

