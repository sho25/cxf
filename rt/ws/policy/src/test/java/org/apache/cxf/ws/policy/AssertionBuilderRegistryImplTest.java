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
package|;
end_package

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
name|builders
operator|.
name|PrimitiveAssertion
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
name|After
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
name|assertEquals
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
name|assertFalse
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|AssertionBuilderRegistryImplTest
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
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
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
name|testBuildUnknownAssertion
parameter_list|()
block|{
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|builder
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|AssertionBuilderRegistryImpl
name|reg
init|=
operator|new
name|AssertionBuilderRegistryImpl
argument_list|()
block|{
specifier|protected
name|void
name|loadDynamic
parameter_list|()
block|{
comment|//nothing
block|}
block|}
decl_stmt|;
name|reg
operator|.
name|setIgnoreUnknownAssertions
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Element
index|[]
name|elems
init|=
operator|new
name|Element
index|[
literal|11
index|]
decl_stmt|;
name|QName
index|[]
name|qnames
init|=
operator|new
name|QName
index|[
literal|11
index|]
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
literal|11
condition|;
name|i
operator|++
control|)
block|{
name|qnames
index|[
name|i
index|]
operator|=
operator|new
name|QName
argument_list|(
literal|"http://my.company.com"
argument_list|,
literal|"type"
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|elems
index|[
name|i
index|]
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Element
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|elems
index|[
name|i
index|]
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|qnames
index|[
name|i
index|]
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|elems
index|[
name|i
index|]
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|qnames
index|[
name|i
index|]
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|reg
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|reg
operator|.
name|isIgnoreUnknownAssertions
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|reg
operator|.
name|build
argument_list|(
name|elems
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"NO_ASSERTIONBUILDER_EXC"
argument_list|,
name|ex
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|reg
operator|.
name|setIgnoreUnknownAssertions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reg
operator|.
name|isIgnoreUnknownAssertions
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|Assertion
name|assertion
init|=
name|reg
operator|.
name|build
argument_list|(
name|elems
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not a PrimitiveAsertion: "
operator|+
name|assertion
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|assertion
operator|instanceof
name|PrimitiveAssertion
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|9
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|assertTrue
argument_list|(
name|reg
operator|.
name|build
argument_list|(
name|elems
index|[
name|i
index|]
argument_list|)
operator|instanceof
name|PrimitiveAssertion
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|reg
operator|.
name|build
argument_list|(
name|elems
index|[
literal|10
index|]
argument_list|)
operator|instanceof
name|PrimitiveAssertion
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

