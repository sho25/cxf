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
name|FaultBeanTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testTransform
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|faultClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.cxf.tools.fortest.cxf523.DBServiceFault"
argument_list|)
decl_stmt|;
name|FaultBean
name|bean
init|=
operator|new
name|FaultBean
argument_list|()
decl_stmt|;
name|WrapperBeanClass
name|beanClass
init|=
name|bean
operator|.
name|transform
argument_list|(
name|faultClass
argument_list|,
literal|"org.apache.cxf.tools.fortest.cxf523.jaxws"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|beanClass
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DBServiceFaultBean"
argument_list|,
name|beanClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.tools.fortest.cxf523.jaxws"
argument_list|,
name|beanClass
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|beanClass
operator|.
name|getFields
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JavaField
name|field
init|=
name|beanClass
operator|.
name|getFields
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"message"
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|field
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|qname
init|=
name|beanClass
operator|.
name|getElementName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"DBServiceFault"
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://cxf523.fortest.tools.cxf.apache.org/"
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

