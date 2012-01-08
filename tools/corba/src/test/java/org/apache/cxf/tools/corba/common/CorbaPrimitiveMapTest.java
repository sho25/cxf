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
name|corba
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CorbaTypeImpl
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaPrimitiveMapTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testMap
parameter_list|()
block|{
name|Map
argument_list|<
name|QName
argument_list|,
name|CorbaTypeImpl
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|CorbaTypeImpl
argument_list|>
argument_list|()
decl_stmt|;
name|QName
name|corbaName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/bindings/corba"
argument_list|,
literal|"string"
argument_list|,
literal|"corba"
argument_list|)
decl_stmt|;
name|QName
name|typeName
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|,
literal|"string"
argument_list|)
decl_stmt|;
name|CorbaTypeImpl
name|corbaTypeImpl
init|=
operator|new
name|CorbaTypeImpl
argument_list|()
decl_stmt|;
name|corbaTypeImpl
operator|.
name|setQName
argument_list|(
name|corbaName
argument_list|)
expr_stmt|;
name|corbaTypeImpl
operator|.
name|setType
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
name|corbaTypeImpl
operator|.
name|setName
argument_list|(
name|corbaName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|typeName
argument_list|,
name|corbaTypeImpl
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|corbaTypeImpl
operator|.
name|getName
argument_list|()
argument_list|,
name|corbaName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|corbaTypeImpl
operator|.
name|getQName
argument_list|()
argument_list|,
name|corbaName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|corbaTypeImpl
operator|.
name|getType
argument_list|()
argument_list|,
name|typeName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|corbaTypeImpl
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

