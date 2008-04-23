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
name|generator
operator|.
name|wsdl11
operator|.
name|annotator
package|;
end_package

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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JAnnotation
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

begin_class
specifier|public
class|class
name|WrapperBeanFieldAnnotatorTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAnnotate
parameter_list|()
block|{
name|JavaClass
name|clz
init|=
operator|new
name|JavaClass
argument_list|()
decl_stmt|;
name|clz
operator|.
name|setFullClassName
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.jaxws.SayHi"
argument_list|)
expr_stmt|;
name|JavaField
name|reqField
init|=
operator|new
name|JavaField
argument_list|(
literal|"array"
argument_list|,
literal|"String[]"
argument_list|,
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
argument_list|)
decl_stmt|;
name|reqField
operator|.
name|setOwner
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JAnnotation
argument_list|>
name|annotation
init|=
name|reqField
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|annotation
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|reqField
operator|.
name|annotate
argument_list|(
operator|new
name|WrapperBeanFieldAnnotator
argument_list|()
argument_list|)
expr_stmt|;
name|annotation
operator|=
name|reqField
operator|.
name|getAnnotations
argument_list|()
expr_stmt|;
name|String
name|expectedNamespace
init|=
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlElement(name = \"array\", namespace = \""
operator|+
name|expectedNamespace
operator|+
literal|"\")"
argument_list|,
name|annotation
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|clz
operator|.
name|setFullClassName
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.jaxws.SayHiResponse"
argument_list|)
expr_stmt|;
name|JavaField
name|resField
init|=
operator|new
name|JavaField
argument_list|(
literal|"return"
argument_list|,
literal|"String[]"
argument_list|,
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
argument_list|)
decl_stmt|;
name|resField
operator|.
name|setOwner
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|resField
operator|.
name|annotate
argument_list|(
operator|new
name|WrapperBeanFieldAnnotator
argument_list|()
argument_list|)
expr_stmt|;
name|annotation
operator|=
name|resField
operator|.
name|getAnnotations
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlElement(name = \"return\", namespace = \""
operator|+
name|expectedNamespace
operator|+
literal|"\")"
argument_list|,
name|annotation
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

