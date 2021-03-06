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
name|tools
operator|.
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaClass
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

begin_class
specifier|public
class|class
name|WrapperTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetWrapperBeanClassFromQName
parameter_list|()
block|{
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
literal|"sayHi"
argument_list|)
decl_stmt|;
name|Wrapper
name|wrapper
init|=
operator|new
name|Wrapper
argument_list|()
decl_stmt|;
name|wrapper
operator|.
name|setName
argument_list|(
name|qname
argument_list|)
expr_stmt|;
name|JavaClass
name|jClass
init|=
name|wrapper
operator|.
name|getWrapperBeanClass
argument_list|(
name|qname
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf"
argument_list|,
name|jClass
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SayHi"
argument_list|,
name|jClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://cxf.apache.org"
argument_list|,
name|jClass
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetWrapperBeanClassFromMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pkgName
init|=
literal|"org.apache.cxf.tools.fortest.classnoanno.docwrapped"
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|stockClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|pkgName
operator|+
literal|".Stock"
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|stockClass
operator|.
name|getMethod
argument_list|(
literal|"getPrice"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Wrapper
name|wrapper
init|=
operator|new
name|Wrapper
argument_list|()
decl_stmt|;
name|wrapper
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|JavaClass
name|jClass
init|=
name|wrapper
operator|.
name|getWrapperBeanClass
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jClass
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|jClass
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|jClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|wrapper
operator|=
operator|new
name|RequestWrapper
argument_list|()
expr_stmt|;
name|jClass
operator|=
name|wrapper
operator|.
name|getWrapperBeanClass
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetPrice"
argument_list|,
name|jClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pkgName
operator|+
literal|".jaxws"
argument_list|,
name|jClass
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|wrapper
operator|=
operator|new
name|ResponseWrapper
argument_list|()
expr_stmt|;
name|jClass
operator|=
name|wrapper
operator|.
name|getWrapperBeanClass
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetPriceResponse"
argument_list|,
name|jClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pkgName
operator|+
literal|".jaxws"
argument_list|,
name|jClass
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsWrapperBeanClassNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pkgName
init|=
literal|"org.apache.cxf.tools.fortest.classnoanno.docwrapped"
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|stockClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|pkgName
operator|+
literal|".Stock"
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|stockClass
operator|.
name|getMethod
argument_list|(
literal|"getPrice"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Wrapper
name|wrapper
init|=
operator|new
name|RequestWrapper
argument_list|()
decl_stmt|;
name|wrapper
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wrapper
operator|.
name|isWrapperAbsent
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wrapper
operator|.
name|isToDifferentPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|wrapper
operator|.
name|isWrapperBeanClassNotExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pkgName
operator|+
literal|".jaxws"
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetPrice"
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|pkgName
operator|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc"
expr_stmt|;
name|stockClass
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|pkgName
operator|+
literal|".Stock"
argument_list|)
expr_stmt|;
name|method
operator|=
name|stockClass
operator|.
name|getMethod
argument_list|(
literal|"getPrice"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|wrapper
operator|=
operator|new
name|RequestWrapper
argument_list|()
expr_stmt|;
name|wrapper
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|wrapper
operator|.
name|isWrapperAbsent
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wrapper
operator|.
name|isToDifferentPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|wrapper
operator|.
name|isWrapperBeanClassNotExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pkgName
operator|+
literal|".jaxws"
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetPrice"
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|pkgName
operator|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc"
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|pkgName
operator|+
literal|".Greeter"
argument_list|)
decl_stmt|;
name|method
operator|=
name|clz
operator|.
name|getMethod
argument_list|(
literal|"sayHi"
argument_list|)
expr_stmt|;
name|wrapper
operator|=
operator|new
name|RequestWrapper
argument_list|()
expr_stmt|;
name|wrapper
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|wrapper
operator|.
name|isWrapperAbsent
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wrapper
operator|.
name|isToDifferentPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|wrapper
operator|.
name|isWrapperBeanClassNotExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pkgName
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SayHi"
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|wrapper
operator|=
operator|new
name|ResponseWrapper
argument_list|()
expr_stmt|;
name|wrapper
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pkgName
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SayHiResponse"
argument_list|,
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

