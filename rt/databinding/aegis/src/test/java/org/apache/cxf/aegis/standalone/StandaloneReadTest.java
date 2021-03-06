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
name|aegis
operator|.
name|standalone
package|;
end_package

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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|aegis
operator|.
name|AegisContext
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
name|aegis
operator|.
name|AegisReader
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
name|aegis
operator|.
name|services
operator|.
name|SimpleBean
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
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
name|TestUtilities
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|StandaloneReadTest
block|{
specifier|private
name|AegisContext
name|context
decl_stmt|;
specifier|private
name|TestUtilities
name|testUtilities
decl_stmt|;
specifier|private
interface|interface
name|ListStringInterface
block|{
name|List
argument_list|<
name|String
argument_list|>
name|method
parameter_list|()
function_decl|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|testUtilities
operator|=
operator|new
name|TestUtilities
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicTypeRead
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|AegisContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|XMLStreamReader
name|streamReader
init|=
name|testUtilities
operator|.
name|getResourceAsXMLStreamReader
argument_list|(
literal|"stringElement.xml"
argument_list|)
decl_stmt|;
name|AegisReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|reader
init|=
name|context
operator|.
name|createXMLStreamReader
argument_list|()
decl_stmt|;
name|Object
name|something
init|=
name|reader
operator|.
name|read
argument_list|(
name|streamReader
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ball-of-yarn"
argument_list|,
name|something
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionReadNoXsiType
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|AegisContext
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|roots
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|listStringType
init|=
name|ListStringInterface
operator|.
name|class
operator|.
name|getMethods
argument_list|()
index|[
literal|0
index|]
operator|.
name|getGenericReturnType
argument_list|()
decl_stmt|;
name|roots
operator|.
name|add
argument_list|(
name|listStringType
argument_list|)
expr_stmt|;
name|context
operator|.
name|setRootClasses
argument_list|(
name|roots
argument_list|)
expr_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|XMLStreamReader
name|streamReader
init|=
name|testUtilities
operator|.
name|getResourceAsXMLStreamReader
argument_list|(
literal|"topLevelList.xml"
argument_list|)
decl_stmt|;
name|AegisReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|reader
init|=
name|context
operator|.
name|createXMLStreamReader
argument_list|()
decl_stmt|;
comment|// until I fix type mapping to use java.lang.reflect.Type instead of
comment|// Class, I need to do the following
name|QName
name|magicTypeQName
init|=
operator|new
name|QName
argument_list|(
literal|"urn:org.apache.cxf.aegis.types"
argument_list|,
literal|"ArrayOfString"
argument_list|)
decl_stmt|;
name|AegisType
name|aegisRegisteredType
init|=
name|context
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|magicTypeQName
argument_list|)
decl_stmt|;
name|Object
name|something
init|=
name|reader
operator|.
name|read
argument_list|(
name|streamReader
argument_list|,
name|aegisRegisteredType
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|correctAnswer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|correctAnswer
operator|.
name|add
argument_list|(
literal|"cat"
argument_list|)
expr_stmt|;
name|correctAnswer
operator|.
name|add
argument_list|(
literal|"dog"
argument_list|)
expr_stmt|;
name|correctAnswer
operator|.
name|add
argument_list|(
literal|"hailstorm"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|correctAnswer
argument_list|,
name|something
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionReadXsiType
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|AegisContext
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|roots
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|listStringType
init|=
name|ListStringInterface
operator|.
name|class
operator|.
name|getMethods
argument_list|()
index|[
literal|0
index|]
operator|.
name|getGenericReturnType
argument_list|()
decl_stmt|;
name|roots
operator|.
name|add
argument_list|(
name|listStringType
argument_list|)
expr_stmt|;
name|context
operator|.
name|setRootClasses
argument_list|(
name|roots
argument_list|)
expr_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|XMLStreamReader
name|streamReader
init|=
name|testUtilities
operator|.
name|getResourceAsXMLStreamReader
argument_list|(
literal|"topLevelListWithXsiType.xml"
argument_list|)
decl_stmt|;
name|AegisReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|reader
init|=
name|context
operator|.
name|createXMLStreamReader
argument_list|()
decl_stmt|;
name|Object
name|something
init|=
name|reader
operator|.
name|read
argument_list|(
name|streamReader
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|correctAnswer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|correctAnswer
operator|.
name|add
argument_list|(
literal|"cat"
argument_list|)
expr_stmt|;
name|correctAnswer
operator|.
name|add
argument_list|(
literal|"dog"
argument_list|)
expr_stmt|;
name|correctAnswer
operator|.
name|add
argument_list|(
literal|"hailstorm"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|correctAnswer
argument_list|,
name|something
argument_list|)
expr_stmt|;
block|}
comment|// test using a .aegis.xml
annotation|@
name|Test
specifier|public
name|void
name|testSimpleBeanRead
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|AegisContext
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|rootClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|rootClasses
operator|.
name|add
argument_list|(
name|SimpleBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|setRootClasses
argument_list|(
name|rootClasses
argument_list|)
expr_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|XMLStreamReader
name|streamReader
init|=
name|testUtilities
operator|.
name|getResourceAsXMLStreamReader
argument_list|(
literal|"simpleBean1.xml"
argument_list|)
decl_stmt|;
name|AegisReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|reader
init|=
name|context
operator|.
name|createXMLStreamReader
argument_list|()
decl_stmt|;
name|Object
name|something
init|=
name|reader
operator|.
name|read
argument_list|(
name|streamReader
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|something
operator|instanceof
name|SimpleBean
argument_list|)
expr_stmt|;
name|SimpleBean
name|simpleBean
init|=
operator|(
name|SimpleBean
operator|)
name|something
decl_stmt|;
name|assertEquals
argument_list|(
literal|"howdy"
argument_list|,
name|simpleBean
operator|.
name|getHowdy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

