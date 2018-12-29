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
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|model
operator|.
name|WrapperBeanClass
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

begin_class
specifier|public
class|class
name|WrapperBeanAnnotatorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAnnotate
parameter_list|()
block|{
name|String
name|pkgName
init|=
literal|"org.apache.cxf.tools.fortest.withannotation.doc.jaxws"
decl_stmt|;
name|WrapperBeanClass
name|clz
init|=
operator|new
name|WrapperBeanClass
argument_list|()
decl_stmt|;
name|clz
operator|.
name|setFullClassName
argument_list|(
name|pkgName
operator|+
literal|".SayHi"
argument_list|)
expr_stmt|;
name|clz
operator|.
name|setElementName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
argument_list|,
literal|"sayHi"
argument_list|)
argument_list|)
expr_stmt|;
name|clz
operator|.
name|annotate
argument_list|(
operator|new
name|WrapperBeanAnnotator
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JAnnotation
argument_list|>
name|annotations
init|=
name|clz
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
name|String
name|expectedNamespace
init|=
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
decl_stmt|;
name|JAnnotation
name|rootElementAnnotation
init|=
name|annotations
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlRootElement(name = \"sayHi\", "
operator|+
literal|"namespace = \""
operator|+
name|expectedNamespace
operator|+
literal|"\")"
argument_list|,
name|rootElementAnnotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JAnnotation
name|xmlTypeAnnotation
init|=
name|annotations
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlType(name = \"sayHi\", "
operator|+
literal|"namespace = \""
operator|+
name|expectedNamespace
operator|+
literal|"\")"
argument_list|,
name|xmlTypeAnnotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JAnnotation
name|accessorTypeAnnotation
init|=
name|annotations
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlAccessorType(XmlAccessType.FIELD)"
argument_list|,
name|accessorTypeAnnotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|WrapperBeanClass
name|resWrapperClass
init|=
operator|new
name|WrapperBeanClass
argument_list|()
decl_stmt|;
name|resWrapperClass
operator|.
name|setFullClassName
argument_list|(
name|pkgName
operator|+
literal|".SayHiResponse"
argument_list|)
expr_stmt|;
name|resWrapperClass
operator|.
name|setElementName
argument_list|(
operator|new
name|QName
argument_list|(
name|expectedNamespace
argument_list|,
literal|"sayHiResponse"
argument_list|)
argument_list|)
expr_stmt|;
name|resWrapperClass
operator|.
name|annotate
argument_list|(
operator|new
name|WrapperBeanAnnotator
argument_list|()
argument_list|)
expr_stmt|;
name|annotations
operator|=
name|resWrapperClass
operator|.
name|getAnnotations
argument_list|()
expr_stmt|;
name|rootElementAnnotation
operator|=
name|annotations
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlRootElement(name = \"sayHiResponse\", "
operator|+
literal|"namespace = \""
operator|+
name|expectedNamespace
operator|+
literal|"\")"
argument_list|,
name|rootElementAnnotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|accessorTypeAnnotation
operator|=
name|annotations
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlAccessorType(XmlAccessType.FIELD)"
argument_list|,
name|accessorTypeAnnotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|xmlTypeAnnotation
operator|=
name|annotations
operator|.
name|get
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlType(name = \"sayHiResponse\", "
operator|+
literal|"namespace = \""
operator|+
name|expectedNamespace
operator|+
literal|"\")"
argument_list|,
name|xmlTypeAnnotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

