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
name|policy
operator|.
name|attachment
operator|.
name|reference
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|wsdl
operator|.
name|extensions
operator|.
name|UnknownExtensibilityElement
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
name|service
operator|.
name|model
operator|.
name|DescriptionInfo
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
name|PolicyBuilder
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
name|PolicyConstants
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
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|assertNull
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
name|assertSame
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ReferenceResolverTest
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocalServiceModelReferenceResolver
parameter_list|()
block|{
name|DescriptionInfo
name|di
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DescriptionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|PolicyBuilder
name|builder
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|LocalServiceModelReferenceResolver
name|resolver
init|=
operator|new
name|LocalServiceModelReferenceResolver
argument_list|(
name|di
argument_list|,
name|builder
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|UnknownExtensibilityElement
argument_list|>
name|extensions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|di
operator|.
name|getExtensors
argument_list|(
name|UnknownExtensibilityElement
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|extensions
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|resolveReference
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|UnknownExtensibilityElement
name|extension
init|=
name|control
operator|.
name|createMock
argument_list|(
name|UnknownExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|extensions
operator|.
name|add
argument_list|(
name|extension
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|di
operator|.
name|getExtensors
argument_list|(
name|UnknownExtensibilityElement
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|extensions
argument_list|)
expr_stmt|;
name|Element
name|e
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Element
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|extension
operator|.
name|getElement
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|e
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|QName
name|qn
init|=
operator|new
name|QName
argument_list|(
name|Constants
operator|.
name|URI_POLICY_NS
argument_list|,
name|Constants
operator|.
name|ELEM_POLICY
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|extension
operator|.
name|getElementType
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|qn
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|e
operator|.
name|getAttributeNS
argument_list|(
name|PolicyConstants
operator|.
name|WSU_NAMESPACE_URI
argument_list|,
name|PolicyConstants
operator|.
name|WSU_ID_ATTR_NAME
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Policy
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|builder
operator|.
name|getPolicy
argument_list|(
name|e
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|resolver
operator|.
name|resolveReference
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoteReferenceResolverWithOlderNs
parameter_list|()
block|{
name|doTestRemoteResolver
argument_list|(
name|Constants
operator|.
name|URI_POLICY_15_DEPRECATED_NS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoteReferenceResolverWithDefaultNs
parameter_list|()
block|{
name|doTestRemoteResolver
argument_list|(
name|Constants
operator|.
name|URI_POLICY_NS
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestRemoteResolver
parameter_list|(
name|String
name|policyNs
parameter_list|)
block|{
name|URL
name|url
init|=
name|ReferenceResolverTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"referring.wsdl"
argument_list|)
decl_stmt|;
name|String
name|baseURI
init|=
name|url
operator|.
name|toString
argument_list|()
decl_stmt|;
name|PolicyBuilder
name|builder
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|RemoteReferenceResolver
name|resolver
init|=
operator|new
name|RemoteReferenceResolver
argument_list|(
name|baseURI
argument_list|,
name|builder
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|resolveReference
argument_list|(
literal|"referred.wsdl#PolicyB"
argument_list|)
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Policy
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|builder
operator|.
name|getPolicy
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|Element
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|resolver
operator|.
name|resolveReference
argument_list|(
literal|"referred.wsdl#PolicyA"
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

