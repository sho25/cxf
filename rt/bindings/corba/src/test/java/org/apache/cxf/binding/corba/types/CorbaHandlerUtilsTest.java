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
operator|.
name|types
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
name|Bus
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
name|BusFactory
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
name|BindingFactory
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
name|BindingFactoryManager
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
name|CorbaDestination
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
name|CorbaTypeMap
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
name|TestUtils
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
name|utils
operator|.
name|CorbaUtils
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
name|CorbaConstants
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
name|TypeMappingType
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
name|EndpointInfo
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
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|CorbaHandlerUtilsTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|COMPLEX_TYPES_NAMESPACE_URI
init|=
literal|"http://cxf.apache.org/bindings/corba/ComplexTypes/idl_types"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMPLEX_TYPES_PREFIX
init|=
literal|"corbatm"
decl_stmt|;
specifier|protected
name|EndpointInfo
name|endpointInfo
decl_stmt|;
name|BindingFactory
name|factory
decl_stmt|;
name|CorbaTypeMap
name|typeMap
decl_stmt|;
name|ServiceInfo
name|service
decl_stmt|;
specifier|private
name|ORB
name|orb
decl_stmt|;
specifier|private
name|Bus
name|bus
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
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
name|BindingFactoryManager
name|bfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|factory
operator|=
name|bfm
operator|.
name|getBindingFactory
argument_list|(
literal|"http://cxf.apache.org/bindings/corba"
argument_list|)
expr_stmt|;
name|bfm
operator|.
name|registerBindingFactory
argument_list|(
name|CorbaConstants
operator|.
name|NU_WSDL_CORBA
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|java
operator|.
name|util
operator|.
name|Properties
name|props
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"yoko.orb.id"
argument_list|,
literal|"CXF-CORBA-Server-Binding"
argument_list|)
expr_stmt|;
name|orb
operator|=
name|ORB
operator|.
name|init
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|CorbaDestination
name|destination
init|=
name|getDestination
argument_list|()
decl_stmt|;
name|service
operator|=
name|destination
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getService
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|TypeMappingType
argument_list|>
name|corbaTypes
init|=
name|service
operator|.
name|getDescription
argument_list|()
operator|.
name|getExtensors
argument_list|(
name|TypeMappingType
operator|.
name|class
argument_list|)
decl_stmt|;
name|typeMap
operator|=
name|CorbaUtils
operator|.
name|createCorbaTypeMap
argument_list|(
name|corbaTypes
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CorbaDestination
name|getDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUtils
name|testUtils
init|=
operator|new
name|TestUtils
argument_list|()
decl_stmt|;
return|return
name|testUtils
operator|.
name|getComplexTypesTestDestination
argument_list|()
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|orb
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Do nothing.  Throw an Exception?
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateTypeHandler
parameter_list|()
block|{
name|QName
name|objName
init|=
literal|null
decl_stmt|;
name|QName
name|objIdlType
init|=
literal|null
decl_stmt|;
name|CorbaObjectHandler
name|result
init|=
literal|null
decl_stmt|;
comment|// Test for an array handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestArray"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|createTypeHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaArrayHandler
argument_list|)
expr_stmt|;
comment|// Test for an enum handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestEnum"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|createTypeHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaEnumHandler
argument_list|)
expr_stmt|;
comment|// Test for a fixed handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestFixed"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|createTypeHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaFixedHandler
argument_list|)
expr_stmt|;
comment|// Test for a primitive handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
name|CorbaConstants
operator|.
name|NT_CORBA_BOOLEAN
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|createTypeHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaPrimitiveHandler
argument_list|)
expr_stmt|;
comment|// Test for a sequence handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestSequence"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|createTypeHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaSequenceHandler
argument_list|)
expr_stmt|;
comment|// Test for a struct handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestStruct"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|createTypeHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaStructHandler
argument_list|)
expr_stmt|;
comment|// Test for a union handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestUnion"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|createTypeHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaUnionHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitializeObjectHandler
parameter_list|()
block|{
name|QName
name|objName
init|=
literal|null
decl_stmt|;
name|QName
name|objIdlType
init|=
literal|null
decl_stmt|;
name|CorbaObjectHandler
name|result
init|=
literal|null
decl_stmt|;
comment|// Test for an array handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestArray"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaArrayHandler
argument_list|)
expr_stmt|;
name|CorbaArrayHandler
name|arrayHandler
init|=
operator|(
name|CorbaArrayHandler
operator|)
name|result
decl_stmt|;
comment|// WSDL defines the array to have 5 elements
name|assertTrue
argument_list|(
name|arrayHandler
operator|.
name|getElements
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|5
argument_list|)
expr_stmt|;
comment|// Test for a sequence handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestSequence"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaSequenceHandler
argument_list|)
expr_stmt|;
name|CorbaSequenceHandler
name|seqHandler
init|=
operator|(
name|CorbaSequenceHandler
operator|)
name|result
decl_stmt|;
comment|// This is an unbounded sequence so make sure there are no elements and the template
comment|// element has been set.
name|assertTrue
argument_list|(
name|seqHandler
operator|.
name|getElements
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|seqHandler
operator|.
name|getTemplateElement
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test for a bounded sequence handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestBoundedSequence"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaSequenceHandler
argument_list|)
expr_stmt|;
name|CorbaSequenceHandler
name|boundedSeqHandler
init|=
operator|(
name|CorbaSequenceHandler
operator|)
name|result
decl_stmt|;
comment|// This is a bounded sequence with WSDL defining 5 elements.
name|assertTrue
argument_list|(
name|boundedSeqHandler
operator|.
name|getElements
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|5
argument_list|)
expr_stmt|;
comment|// Test for a struct handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestStruct"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaStructHandler
argument_list|)
expr_stmt|;
name|CorbaStructHandler
name|structHandler
init|=
operator|(
name|CorbaStructHandler
operator|)
name|result
decl_stmt|;
comment|// The WSDL defines this struct as having three members
name|assertTrue
argument_list|(
name|structHandler
operator|.
name|getMembers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|3
argument_list|)
expr_stmt|;
comment|// Test for a union handler
name|objName
operator|=
operator|new
name|QName
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|objIdlType
operator|=
operator|new
name|QName
argument_list|(
name|COMPLEX_TYPES_NAMESPACE_URI
argument_list|,
literal|"TestUnion"
argument_list|,
name|COMPLEX_TYPES_PREFIX
argument_list|)
expr_stmt|;
name|result
operator|=
name|CorbaHandlerUtils
operator|.
name|initializeObjectHandler
argument_list|(
name|orb
argument_list|,
name|objName
argument_list|,
name|objIdlType
argument_list|,
name|typeMap
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|CorbaUnionHandler
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

