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
name|type
operator|.
name|basic
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
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|AbstractAegisTest
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
name|type
operator|.
name|TypeMapping
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
name|xml
operator|.
name|stax
operator|.
name|ElementReader
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
name|assertNotNull
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

begin_class
specifier|public
class|class
name|DynamicProxyTest
extends|extends
name|AbstractAegisTest
block|{
name|TypeMapping
name|mapping
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|AegisContext
name|context
init|=
operator|new
name|AegisContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|mapping
operator|=
name|context
operator|.
name|getTypeMapping
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDynamicProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|IMyInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface"
argument_list|,
literal|"data"
argument_list|)
argument_list|)
expr_stmt|;
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getResourceAsStream
argument_list|(
literal|"MyInterface.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|IMyInterface
name|data
init|=
operator|(
name|IMyInterface
operator|)
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"junk"
argument_list|,
name|data
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|isUseless
argument_list|()
argument_list|)
expr_stmt|;
name|data
operator|.
name|setName
argument_list|(
literal|"bigjunk"
argument_list|)
expr_stmt|;
name|data
operator|.
name|setUseless
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bigjunk"
argument_list|,
name|data
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|data
operator|.
name|isUseless
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|hashCode
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
argument_list|,
name|data
argument_list|)
expr_stmt|;
comment|// checkstyle isn't smart enough to know we're testing equals....
comment|//        assertFalse(data.equals(null));
comment|//        assertFalse("bigjunk".equals(data));
name|assertNotNull
argument_list|(
name|data
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|data
operator|.
name|getFOO
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|data
operator|.
name|getNonSpecifiedInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDynamicProxyNonStandardGetter
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|IMyInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface"
argument_list|,
literal|"data"
argument_list|)
argument_list|)
expr_stmt|;
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getResourceAsStream
argument_list|(
literal|"MyInterface.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|IMyInterface
name|data
init|=
operator|(
name|IMyInterface
operator|)
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|data
operator|.
name|getNameById
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|IllegalAccessError
operator|.
name|class
operator|+
literal|" should be thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessError
name|e
parameter_list|)
block|{
comment|//          do nothing
block|}
try|try
block|{
name|data
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
name|IllegalAccessError
operator|.
name|class
operator|+
literal|" should be thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessError
name|e
parameter_list|)
block|{
comment|//          do nothing
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDynamicProxyNonStandardSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|IMyInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface"
argument_list|,
literal|"data"
argument_list|)
argument_list|)
expr_stmt|;
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getResourceAsStream
argument_list|(
literal|"MyInterface.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|IMyInterface
name|data
init|=
operator|(
name|IMyInterface
operator|)
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|data
operator|.
name|setNameNoParams
argument_list|()
expr_stmt|;
name|fail
argument_list|(
name|IllegalAccessError
operator|.
name|class
operator|+
literal|" should be thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessError
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
try|try
block|{
name|data
operator|.
name|set
argument_list|()
expr_stmt|;
name|fail
argument_list|(
name|IllegalAccessError
operator|.
name|class
operator|+
literal|" should be thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessError
name|e
parameter_list|)
block|{
comment|//          do nothing
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDynamicProxyNonGetterSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|IMyInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface"
argument_list|,
literal|"data"
argument_list|)
argument_list|)
expr_stmt|;
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getResourceAsStream
argument_list|(
literal|"MyInterface.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|IMyInterface
name|data
init|=
operator|(
name|IMyInterface
operator|)
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|data
operator|.
name|doSomething
argument_list|()
expr_stmt|;
name|fail
argument_list|(
name|IllegalAccessError
operator|.
name|class
operator|+
literal|" should be thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessError
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDynamicProxyMissingAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|IMyInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface"
argument_list|,
literal|"data"
argument_list|)
argument_list|)
expr_stmt|;
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getResourceAsStream
argument_list|(
literal|"MyInterface.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|IMyInterface
name|data
init|=
operator|(
name|IMyInterface
operator|)
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"junk"
argument_list|,
name|data
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|data
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
name|testDynamicProxyNested
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setTypeClass
argument_list|(
name|IMyInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface"
argument_list|,
literal|"myInterface"
argument_list|)
argument_list|)
expr_stmt|;
name|type
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|BeanType
name|type2
init|=
operator|new
name|BeanType
argument_list|()
decl_stmt|;
name|type2
operator|.
name|setTypeClass
argument_list|(
name|IMyInterface2
operator|.
name|class
argument_list|)
expr_stmt|;
name|type2
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface2"
argument_list|,
literal|"myInterface2"
argument_list|)
argument_list|)
expr_stmt|;
name|type2
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|type2
operator|.
name|getTypeInfo
argument_list|()
operator|.
name|mapType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:MyInterface"
argument_list|,
literal|"myInterface"
argument_list|)
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getResourceAsStream
argument_list|(
literal|"MyInterface2.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|IMyInterface2
name|data
init|=
operator|(
name|IMyInterface2
operator|)
name|type2
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
operator|.
name|getMyInterface
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"junk"
argument_list|,
name|data
operator|.
name|getMyInterface
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|getMyInterface
argument_list|()
operator|.
name|isUseless
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
interface|interface
name|IMyInterface
block|{
name|String
name|getName
parameter_list|()
function_decl|;
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|boolean
name|isUseless
parameter_list|()
function_decl|;
name|void
name|setUseless
parameter_list|(
name|boolean
name|useless
parameter_list|)
function_decl|;
name|String
name|getNameById
parameter_list|(
name|int
name|id
parameter_list|)
function_decl|;
name|void
name|setNameNoParams
parameter_list|()
function_decl|;
name|void
name|doSomething
parameter_list|()
function_decl|;
name|String
name|get
parameter_list|()
function_decl|;
name|Integer
name|set
parameter_list|()
function_decl|;
name|String
name|getType
parameter_list|()
function_decl|;
name|String
name|getFOO
parameter_list|()
function_decl|;
name|int
name|getNonSpecifiedInt
parameter_list|()
function_decl|;
block|}
specifier|public
interface|interface
name|IMyInterface2
block|{
name|IMyInterface
name|getMyInterface
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

