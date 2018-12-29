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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|ResponseWrapperTest
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
name|testBuildFields
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
name|ResponseWrapper
name|responseWrapper
init|=
operator|new
name|ResponseWrapper
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
name|getOutput
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
name|JavaField
name|field
init|=
name|responseWrapper
operator|.
name|buildFields
argument_list|(
name|method
argument_list|,
name|message
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"_return"
argument_list|,
name|field
operator|.
name|getParaName
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
name|getOutput
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
name|field
operator|=
name|responseWrapper
operator|.
name|buildFields
argument_list|(
name|method
argument_list|,
name|message
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"_return"
argument_list|,
name|field
operator|.
name|getParaName
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
name|getOutput
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
name|field
operator|=
name|responseWrapper
operator|.
name|buildFields
argument_list|(
name|method
argument_list|,
name|message
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"_return"
argument_list|,
name|field
operator|.
name|getParaName
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
name|ResponseWrapper
argument_list|()
decl_stmt|;
name|wrapper
operator|.
name|setOperationInfo
argument_list|(
name|opInfo
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

