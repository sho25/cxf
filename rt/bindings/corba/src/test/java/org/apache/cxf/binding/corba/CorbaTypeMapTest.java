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
name|binding
operator|.
name|corba
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaType
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
name|CorbaTypeMapTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCorbaTypeMap
parameter_list|()
throws|throws
name|Exception
block|{
name|CorbaTypeMap
name|typeMap
init|=
operator|new
name|CorbaTypeMap
argument_list|(
literal|"http://yoko.apache.org/ComplexTypes"
argument_list|)
decl_stmt|;
name|String
name|targetNamespace
init|=
name|typeMap
operator|.
name|getTargetNamespace
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|targetNamespace
argument_list|,
literal|"http://yoko.apache.org/ComplexTypes"
argument_list|)
expr_stmt|;
name|QName
name|type
init|=
operator|new
name|QName
argument_list|(
literal|"http://yoko.apache.org/ComplexTypes"
argument_list|,
literal|"xsd1:Test.MultiPart.Colour"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|CorbaType
name|corbaTypeImpl
init|=
operator|new
name|CorbaType
argument_list|()
decl_stmt|;
name|corbaTypeImpl
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|corbaTypeImpl
operator|.
name|setName
argument_list|(
literal|"Test.MultiPart.Colour"
argument_list|)
expr_stmt|;
name|typeMap
operator|.
name|addType
argument_list|(
literal|"Test.MultiPart.Colour"
argument_list|,
name|corbaTypeImpl
argument_list|)
expr_stmt|;
name|CorbaType
name|corbatype
init|=
name|typeMap
operator|.
name|getType
argument_list|(
literal|"Test.MultiPart.Colour"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|corbatype
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Test.MultiPart.Colour"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|corbatype
operator|.
name|getType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
literal|"xsd1:Test.MultiPart.Colour"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

