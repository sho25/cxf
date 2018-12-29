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
name|jca
operator|.
name|cxf
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

begin_class
specifier|public
class|class
name|CXFConnectionRequestInfoTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCXFConnectionRequestInfoEquals
parameter_list|()
throws|throws
name|Exception
block|{
name|CXFConnectionRequestInfo
name|cr1
init|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
decl_stmt|;
name|CXFConnectionRequestInfo
name|cr2
init|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Checking equals "
argument_list|,
name|cr1
operator|.
name|equals
argument_list|(
name|cr2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking hashcodes "
argument_list|,
name|cr1
operator|.
name|hashCode
argument_list|()
operator|==
name|cr2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|cr1
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|cr2
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking equals with null parameters "
argument_list|,
name|cr1
operator|.
name|equals
argument_list|(
name|cr2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking hashcodes  with null parameters "
argument_list|,
name|cr1
operator|.
name|hashCode
argument_list|()
operator|==
name|cr2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|cr1
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|cr2
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking that objects are not equals "
argument_list|,
operator|!
name|cr1
operator|.
name|equals
argument_list|(
name|cr2
argument_list|)
argument_list|)
expr_stmt|;
name|cr1
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foox"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|cr2
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking that objects are not equal "
argument_list|,
operator|!
name|cr1
operator|.
name|equals
argument_list|(
name|cr2
argument_list|)
argument_list|)
expr_stmt|;
name|cr1
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|cr2
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"servicex"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking that objects are not equal "
argument_list|,
operator|!
name|cr1
operator|.
name|equals
argument_list|(
name|cr2
argument_list|)
argument_list|)
expr_stmt|;
name|cr1
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|cr2
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPortx"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking that objects are not equal "
argument_list|,
operator|!
name|cr1
operator|.
name|equals
argument_list|(
name|cr2
argument_list|)
argument_list|)
expr_stmt|;
name|cr1
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|cr2
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"fooPort"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Checking that objects are not equal "
argument_list|,
operator|!
name|cr1
operator|.
name|equals
argument_list|(
name|cr2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

