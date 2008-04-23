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
name|external
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|ws
operator|.
name|policy
operator|.
name|PolicyException
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
name|DomainExpressionBuilderRegistryTest
extends|extends
name|Assert
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
name|testNoBuilder
parameter_list|()
block|{
name|DomainExpressionBuilderRegistry
name|reg
init|=
operator|new
name|DomainExpressionBuilderRegistry
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|DomainExpressionBuilderRegistry
operator|.
name|class
argument_list|,
name|reg
operator|.
name|getRegistrationType
argument_list|()
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
name|e
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"http://a.b.c"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|e
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"x"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|reg
operator|.
name|build
argument_list|(
name|e
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
comment|// expected
block|}
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
name|testBuild
parameter_list|()
block|{
name|DomainExpressionBuilder
name|builder
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DomainExpressionBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|DomainExpressionBuilder
argument_list|>
name|builders
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|DomainExpressionBuilder
argument_list|>
argument_list|()
decl_stmt|;
name|QName
name|qn
init|=
operator|new
name|QName
argument_list|(
literal|"http://a.b.c"
argument_list|,
literal|"x"
argument_list|)
decl_stmt|;
name|builders
operator|.
name|put
argument_list|(
name|qn
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|DomainExpressionBuilderRegistry
name|reg
init|=
operator|new
name|DomainExpressionBuilderRegistry
argument_list|(
name|builders
argument_list|)
decl_stmt|;
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
name|e
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"http://a.b.c"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|e
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"x"
argument_list|)
expr_stmt|;
name|DomainExpression
name|de
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DomainExpression
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
name|build
argument_list|(
name|e
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|de
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|de
argument_list|,
name|reg
operator|.
name|build
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

