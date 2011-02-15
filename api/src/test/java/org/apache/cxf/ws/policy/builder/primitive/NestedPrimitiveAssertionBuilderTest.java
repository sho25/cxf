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
name|builder
operator|.
name|primitive
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|Bus
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
name|classextension
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
name|classextension
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|NestedPrimitiveAssertionBuilderTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NAMESPACE
init|=
literal|"http://www.w3.org/2007/01/addressing/metadata"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|TEST_NAME1
init|=
operator|new
name|QName
argument_list|(
name|TEST_NAMESPACE
argument_list|,
literal|"Addressing"
argument_list|)
decl_stmt|;
specifier|private
name|NestedPrimitiveAssertionBuilder
name|npab
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|PolicyBuilder
name|builder
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
name|npab
operator|=
operator|new
name|NestedPrimitiveAssertionBuilder
argument_list|()
expr_stmt|;
name|npab
operator|.
name|setKnownElements
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|TEST_NAME1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
name|npab
operator|.
name|setPolicyBuilder
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleBuildOlderNs
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"<wsam:Addressing wsp:Optional=\"true\""
operator|+
literal|" xmlns:wsp=\"http://www.w3.org/2006/07/ws-policy\""
operator|+
literal|" xmlns:wsam=\"http://www.w3.org/2007/01/addressing/metadata\" />"
decl_stmt|;
name|npab
operator|.
name|build
argument_list|(
name|getElement
argument_list|(
name|data
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildDefaultNs
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"<wsam:Addressing wsp:Optional=\"true\""
operator|+
literal|" xmlns:wsp=\"http://www.w3.org/ns/ws-policy\""
operator|+
literal|" xmlns:wsam=\"http://www.w3.org/2007/01/addressing/metadata\">"
operator|+
literal|"<wsp:Policy/></wsam:Addressing>"
decl_stmt|;
name|Policy
name|nested
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
name|nested
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|NestedPrimitiveAssertion
name|npc
init|=
operator|(
name|NestedPrimitiveAssertion
operator|)
name|npab
operator|.
name|build
argument_list|(
name|getElement
argument_list|(
name|data
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TEST_NAME1
argument_list|,
name|npc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|nested
argument_list|,
name|npc
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|npc
operator|.
name|isOptional
argument_list|()
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
name|testBuildOlderNs
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"<wsam:Addressing wsp:Optional=\"true\""
operator|+
literal|" xmlns:wsp=\"http://www.w3.org/2006/07/ws-policy\""
operator|+
literal|" xmlns:wsam=\"http://www.w3.org/2007/01/addressing/metadata\">"
operator|+
literal|"<wsp:Policy/></wsam:Addressing>"
decl_stmt|;
name|Policy
name|nested
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
name|nested
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|npab
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|NestedPrimitiveAssertion
name|npc
init|=
operator|(
name|NestedPrimitiveAssertion
operator|)
name|npab
operator|.
name|build
argument_list|(
name|getElement
argument_list|(
name|data
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TEST_NAME1
argument_list|,
name|npc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|nested
argument_list|,
name|npc
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|npc
operator|.
name|isOptional
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
name|Element
name|getElement
parameter_list|(
name|String
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|is
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
block|}
end_class

end_unit

