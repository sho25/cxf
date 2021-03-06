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
name|rm
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|RM10Constants
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
name|rmp
operator|.
name|v200502
operator|.
name|RMAssertion
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
name|Assertion
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
comment|/**  * Policy assertion builder for WS-RMP 1.0 (submission). Since this version of WS-RMP nests everything as  * direct child elements of the RMAssertion JAXB can be used directly to convert to/from XML.  */
end_comment

begin_class
specifier|public
class|class
name|RM10AssertionBuilder
extends|extends
name|JaxbAssertionBuilder
argument_list|<
name|RMAssertion
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|QName
argument_list|>
name|KNOWN_ELEMENTS
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|RM10Constants
operator|.
name|WSRMP_RMASSERTION_QNAME
argument_list|)
decl_stmt|;
specifier|public
name|RM10AssertionBuilder
parameter_list|()
throws|throws
name|JAXBException
block|{
name|super
argument_list|(
name|RMAssertion
operator|.
name|class
argument_list|,
name|RM10Constants
operator|.
name|WSRMP_RMASSERTION_QNAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|JaxbAssertion
argument_list|<
name|RMAssertion
argument_list|>
name|buildAssertion
parameter_list|()
block|{
return|return
operator|new
name|RMPolicyAssertion
argument_list|()
return|;
block|}
class|class
name|RMPolicyAssertion
extends|extends
name|JaxbAssertion
argument_list|<
name|RMAssertion
argument_list|>
block|{
name|RMPolicyAssertion
parameter_list|()
block|{
name|super
argument_list|(
name|RM10Constants
operator|.
name|WSRMP_RMASSERTION_QNAME
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|RMPolicyAssertion
parameter_list|(
name|boolean
name|opt
parameter_list|)
block|{
name|super
argument_list|(
name|RM10Constants
operator|.
name|WSRMP_RMASSERTION_QNAME
argument_list|,
name|opt
argument_list|)
expr_stmt|;
block|}
name|RMPolicyAssertion
parameter_list|(
name|boolean
name|opt
parameter_list|,
name|boolean
name|ignore
parameter_list|)
block|{
name|super
argument_list|(
name|RM10Constants
operator|.
name|WSRMP_RMASSERTION_QNAME
argument_list|,
name|opt
argument_list|,
name|ignore
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
name|Assertion
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
name|RMAssertion
argument_list|>
name|other
init|=
name|JaxbAssertion
operator|.
name|cast
argument_list|(
operator|(
name|Assertion
operator|)
name|policyComponent
argument_list|)
decl_stmt|;
return|return
name|RMPolicyUtilities
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
name|Assertion
name|clone
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|RMPolicyAssertion
name|a
init|=
operator|new
name|RMPolicyAssertion
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

