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
name|transport
operator|.
name|http
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|PolicyAssertion
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
name|builder
operator|.
name|jaxb
operator|.
name|JaxbAssertion
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
name|builder
operator|.
name|jaxb
operator|.
name|JaxbAssertionBuilder
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
name|Constants
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
name|PolicyComponent
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|HTTPClientAssertionBuilder
extends|extends
name|JaxbAssertionBuilder
argument_list|<
name|HTTPClientPolicy
argument_list|>
block|{
specifier|public
name|HTTPClientAssertionBuilder
parameter_list|()
throws|throws
name|JAXBException
block|{
name|super
argument_list|(
name|HTTPClientPolicy
operator|.
name|class
argument_list|,
name|PolicyUtils
operator|.
name|HTTPCLIENTPOLICY_ASSERTION_QNAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PolicyAssertion
name|buildCompatible
parameter_list|(
name|PolicyAssertion
name|a
parameter_list|,
name|PolicyAssertion
name|b
parameter_list|)
block|{
if|if
condition|(
name|PolicyUtils
operator|.
name|HTTPCLIENTPOLICY_ASSERTION_QNAME
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|PolicyUtils
operator|.
name|HTTPCLIENTPOLICY_ASSERTION_QNAME
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|HTTPClientPolicy
name|compatible
init|=
name|PolicyUtils
operator|.
name|intersect
argument_list|(
name|JaxbAssertion
operator|.
name|cast
argument_list|(
name|a
argument_list|,
name|HTTPClientPolicy
operator|.
name|class
argument_list|)
operator|.
name|getData
argument_list|()
argument_list|,
name|JaxbAssertion
operator|.
name|cast
argument_list|(
name|b
argument_list|,
name|HTTPClientPolicy
operator|.
name|class
argument_list|)
operator|.
name|getData
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|compatible
condition|)
block|{
return|return
literal|null
return|;
block|}
name|JaxbAssertion
argument_list|<
name|HTTPClientPolicy
argument_list|>
name|ca
init|=
operator|new
name|JaxbAssertion
argument_list|<
name|HTTPClientPolicy
argument_list|>
argument_list|(
name|PolicyUtils
operator|.
name|HTTPCLIENTPOLICY_ASSERTION_QNAME
argument_list|,
name|a
operator|.
name|isOptional
argument_list|()
operator|&&
name|b
operator|.
name|isOptional
argument_list|()
argument_list|)
decl_stmt|;
name|ca
operator|.
name|setData
argument_list|(
name|compatible
argument_list|)
expr_stmt|;
return|return
name|ca
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|JaxbAssertion
argument_list|<
name|HTTPClientPolicy
argument_list|>
name|buildAssertion
parameter_list|()
block|{
return|return
operator|new
name|HTTPClientPolicyAssertion
argument_list|()
return|;
block|}
class|class
name|HTTPClientPolicyAssertion
extends|extends
name|JaxbAssertion
argument_list|<
name|HTTPClientPolicy
argument_list|>
block|{
name|HTTPClientPolicyAssertion
parameter_list|()
block|{
name|super
argument_list|(
name|PolicyUtils
operator|.
name|HTTPCLIENTPOLICY_ASSERTION_QNAME
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equal
parameter_list|(
name|PolicyComponent
name|policyComponent
parameter_list|)
block|{
if|if
condition|(
name|policyComponent
operator|.
name|getType
argument_list|()
operator|!=
name|Constants
operator|.
name|TYPE_ASSERTION
operator|||
operator|!
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|PolicyAssertion
operator|)
name|policyComponent
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|JaxbAssertion
argument_list|<
name|HTTPClientPolicy
argument_list|>
name|other
init|=
name|JaxbAssertion
operator|.
name|cast
argument_list|(
operator|(
name|PolicyAssertion
operator|)
name|policyComponent
argument_list|)
decl_stmt|;
return|return
name|PolicyUtils
operator|.
name|equals
argument_list|(
name|this
operator|.
name|getData
argument_list|()
argument_list|,
name|other
operator|.
name|getData
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|PolicyAssertion
name|cloneMandatory
parameter_list|()
block|{
name|HTTPClientPolicyAssertion
name|a
init|=
operator|new
name|HTTPClientPolicyAssertion
argument_list|()
decl_stmt|;
name|a
operator|.
name|setData
argument_list|(
name|getData
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|a
return|;
block|}
block|}
block|}
end_class

end_unit

