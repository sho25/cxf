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
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|test
operator|.
name|assertions
operator|.
name|foo
operator|.
name|FooType
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
name|primitive
operator|.
name|PrimitiveAssertion
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
name|neethi
operator|.
name|PolicyComponent
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
name|Test
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JaxbAssertionTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testBasic
parameter_list|()
block|{
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
name|assertion
init|=
operator|new
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|assertion
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|assertion
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|assertion
operator|.
name|isOptional
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Constants
operator|.
name|TYPE_ASSERTION
argument_list|,
name|assertion
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|FooType
name|data
init|=
operator|new
name|FooType
argument_list|()
decl_stmt|;
name|data
operator|.
name|setName
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|data
operator|.
name|setNumber
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
literal|"http://cxf.apache.org/test/assertions/foo"
argument_list|,
literal|"FooType"
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|setName
argument_list|(
name|qn
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setData
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setOptional
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|qn
argument_list|,
name|assertion
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|data
argument_list|,
name|assertion
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertion
operator|.
name|isOptional
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Constants
operator|.
name|TYPE_ASSERTION
argument_list|,
name|assertion
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqual
parameter_list|()
block|{
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
name|assertion
init|=
operator|new
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
argument_list|()
decl_stmt|;
name|FooType
name|data
init|=
operator|new
name|FooType
argument_list|()
decl_stmt|;
name|data
operator|.
name|setName
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|data
operator|.
name|setNumber
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
literal|"http://cxf.apache.org/test/assertions/foo"
argument_list|,
literal|"FooType"
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|setName
argument_list|(
name|qn
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setData
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|PolicyComponent
name|pc
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|assertion
operator|.
name|equal
argument_list|(
name|pc
argument_list|)
argument_list|)
expr_stmt|;
name|pc
operator|=
operator|new
name|All
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|assertion
operator|.
name|equal
argument_list|(
name|pc
argument_list|)
argument_list|)
expr_stmt|;
name|pc
operator|=
operator|new
name|ExactlyOne
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|assertion
operator|.
name|equal
argument_list|(
name|pc
argument_list|)
argument_list|)
expr_stmt|;
name|IMocksControl
name|ctrl
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
name|PrimitiveAssertion
name|xpa
init|=
name|ctrl
operator|.
name|createMock
argument_list|(
name|PrimitiveAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|oqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/test/assertions/blah"
argument_list|,
literal|"OtherType"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|xpa
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|oqn
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|xpa
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Constants
operator|.
name|TYPE_ASSERTION
argument_list|)
expr_stmt|;
name|ctrl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|assertion
operator|.
name|equal
argument_list|(
name|xpa
argument_list|)
argument_list|)
expr_stmt|;
name|ctrl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|FooType
name|odata
init|=
operator|new
name|FooType
argument_list|()
decl_stmt|;
name|odata
operator|.
name|setName
argument_list|(
name|data
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|odata
operator|.
name|setNumber
argument_list|(
name|data
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
name|oassertion
init|=
operator|new
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
argument_list|()
decl_stmt|;
name|oassertion
operator|.
name|setData
argument_list|(
name|odata
argument_list|)
expr_stmt|;
name|oassertion
operator|.
name|setName
argument_list|(
name|qn
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|assertion
operator|.
name|equal
argument_list|(
name|oassertion
argument_list|)
argument_list|)
expr_stmt|;
name|oassertion
operator|.
name|setData
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertion
operator|.
name|equal
argument_list|(
name|oassertion
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertion
operator|.
name|equal
argument_list|(
name|assertion
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNormalise
parameter_list|()
block|{
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
name|assertion
init|=
operator|new
name|JaxbAssertion
argument_list|<
name|FooType
argument_list|>
argument_list|()
decl_stmt|;
name|FooType
name|data
init|=
operator|new
name|FooType
argument_list|()
decl_stmt|;
name|data
operator|.
name|setName
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|data
operator|.
name|setNumber
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
literal|"http://cxf.apache.org/test/assertions/foo"
argument_list|,
literal|"FooType"
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|setName
argument_list|(
name|qn
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setData
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|JaxbAssertion
argument_list|<
name|?
argument_list|>
name|normalised
init|=
operator|(
name|JaxbAssertion
argument_list|<
name|?
argument_list|>
operator|)
name|assertion
operator|.
name|normalize
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|normalised
operator|.
name|equal
argument_list|(
name|assertion
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|assertion
operator|.
name|getData
argument_list|()
argument_list|,
name|normalised
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setOptional
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|PolicyComponent
name|pc
init|=
name|assertion
operator|.
name|normalize
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Constants
operator|.
name|TYPE_POLICY
argument_list|,
name|pc
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
operator|(
name|Policy
operator|)
name|pc
decl_stmt|;
name|Iterator
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|alternatives
init|=
name|p
operator|.
name|getAlternatives
argument_list|()
decl_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|List
argument_list|<
name|Assertion
argument_list|>
name|pcs
init|=
name|alternatives
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|pcs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
name|assertion
operator|.
name|equal
argument_list|(
name|pcs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|total
operator|+=
name|pcs
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
operator|!
name|alternatives
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

