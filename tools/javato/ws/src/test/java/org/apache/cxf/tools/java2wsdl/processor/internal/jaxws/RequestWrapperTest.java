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
name|java
operator|.
name|util
operator|.
name|List
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
name|jaxws
operator|.
name|JaxwsServiceBuilder
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
name|MessageInfo
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
name|OperationInfo
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
name|ServiceInfo
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
name|JavaField
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
name|JavaMethod
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
name|fortest
operator|.
name|withannotation
operator|.
name|doc
operator|.
name|GreeterArray
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
name|fortest
operator|.
name|xmllist
operator|.
name|AddNumbersPortType
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|RequestWrapperTest
block|{
name|JaxwsServiceBuilder
name|builder
init|=
operator|new
name|JaxwsServiceBuilder
argument_list|()
decl_stmt|;
specifier|private
name|OperationInfo
name|getOperation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|String
name|opName
parameter_list|)
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationInfo
name|op
range|:
name|serviceInfo
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|op
operator|.
name|getUnwrappedOperation
argument_list|()
operator|!=
literal|null
operator|&&
name|op
operator|.
name|hasInput
argument_list|()
operator|&&
name|opName
operator|.
name|equals
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|op
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildRequestFields
parameter_list|()
block|{
comment|// Test String[]
name|Class
argument_list|<
name|?
argument_list|>
name|testingClass
init|=
name|GreeterArray
operator|.
name|class
decl_stmt|;
name|OperationInfo
name|opInfo
init|=
name|getOperation
argument_list|(
name|testingClass
argument_list|,
literal|"sayStringArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|opInfo
argument_list|)
expr_stmt|;
name|RequestWrapper
name|requestWrapper
init|=
operator|new
name|RequestWrapper
argument_list|()
decl_stmt|;
name|MessageInfo
name|message
init|=
name|opInfo
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|Method
name|method
init|=
operator|(
name|Method
operator|)
name|opInfo
operator|.
name|getProperty
argument_list|(
literal|"operation.method"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|JavaField
argument_list|>
name|fields
init|=
name|requestWrapper
operator|.
name|buildFields
argument_list|(
name|method
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JavaField
name|field
init|=
name|fields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"arg0"
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String[]"
argument_list|,
name|field
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test int[]
name|opInfo
operator|=
name|getOperation
argument_list|(
name|testingClass
argument_list|,
literal|"sayIntArray"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|opInfo
argument_list|)
expr_stmt|;
name|message
operator|=
name|opInfo
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getInput
argument_list|()
expr_stmt|;
name|method
operator|=
operator|(
name|Method
operator|)
name|opInfo
operator|.
name|getProperty
argument_list|(
literal|"operation.method"
argument_list|)
expr_stmt|;
name|fields
operator|=
name|requestWrapper
operator|.
name|buildFields
argument_list|(
name|method
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|field
operator|=
name|fields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg0"
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"int[]"
argument_list|,
name|field
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test TestDataBean[]
name|opInfo
operator|=
name|getOperation
argument_list|(
name|testingClass
argument_list|,
literal|"sayTestDataBeanArray"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|opInfo
argument_list|)
expr_stmt|;
name|message
operator|=
name|opInfo
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getInput
argument_list|()
expr_stmt|;
name|method
operator|=
operator|(
name|Method
operator|)
name|opInfo
operator|.
name|getProperty
argument_list|(
literal|"operation.method"
argument_list|)
expr_stmt|;
name|fields
operator|=
name|requestWrapper
operator|.
name|buildFields
argument_list|(
name|method
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|field
operator|=
name|fields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg0"
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.TestDataBean[]"
argument_list|,
name|field
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
name|testNoAnnotationNoClass
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
name|testingClass
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
name|OperationInfo
name|opInfo
init|=
name|getOperation
argument_list|(
name|testingClass
argument_list|,
literal|"getPrice"
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
name|setOperationInfo
argument_list|(
name|opInfo
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
name|JavaClass
name|jClass
init|=
name|wrapper
operator|.
name|buildWrapperBeanClass
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|jClass
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JavaField
argument_list|>
name|jFields
init|=
name|jClass
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jFields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg0"
argument_list|,
name|jFields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|jFields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|jMethods
init|=
name|jClass
operator|.
name|getMethods
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|jMethods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JavaMethod
name|jMethod
init|=
name|jMethods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getArg0"
argument_list|,
name|jMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|jMethod
operator|.
name|getParameterListWithoutAnnotation
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|jMethod
operator|=
name|jMethods
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"setArg0"
argument_list|,
name|jMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jMethod
operator|.
name|getParameterListWithoutAnnotation
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String newArg0"
argument_list|,
name|jMethod
operator|.
name|getParameterListWithoutAnnotation
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithAnnotationNoClass
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pkgName
init|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc"
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|testingClass
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
name|OperationInfo
name|opInfo
init|=
name|getOperation
argument_list|(
name|testingClass
argument_list|,
literal|"getPrice"
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
name|setOperationInfo
argument_list|(
name|opInfo
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithAnnotationWithClass
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pkgName
init|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc"
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|testingClass
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
name|OperationInfo
name|opInfo
init|=
name|getOperation
argument_list|(
name|testingClass
argument_list|,
literal|"sayHi"
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
name|setOperationInfo
argument_list|(
name|opInfo
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF1752
parameter_list|()
throws|throws
name|Exception
block|{
name|OperationInfo
name|opInfo
init|=
name|getOperation
argument_list|(
name|AddNumbersPortType
operator|.
name|class
argument_list|,
literal|"testCXF1752"
argument_list|)
decl_stmt|;
name|RequestWrapper
name|wrapper
init|=
operator|new
name|RequestWrapper
argument_list|()
decl_stmt|;
name|wrapper
operator|.
name|setOperationInfo
argument_list|(
name|opInfo
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|buildWrapperBeanClass
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|JavaField
argument_list|>
name|fields
init|=
name|wrapper
operator|.
name|getJavaClass
argument_list|()
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.util.List<java.lang.Long>"
argument_list|,
name|fields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"byte[]"
argument_list|,
name|fields
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.tools.fortest.xmllist.AddNumbersPortType.UserObject[]"
argument_list|,
name|fields
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.util.List<org.apache.cxf.tools.fortest.xmllist.AddNumbersPortType.UserObject>"
argument_list|,
name|fields
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

