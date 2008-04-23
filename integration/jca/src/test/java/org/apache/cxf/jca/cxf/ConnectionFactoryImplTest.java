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
name|io
operator|.
name|Serializable
import|;
end_import

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
name|naming
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|Referenceable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|ResourceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ConnectionManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ManagedConnectionFactory
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
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
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

begin_class
specifier|public
class|class
name|ConnectionFactoryImplTest
extends|extends
name|Assert
block|{
name|ManagedConnectionFactory
name|mockConnectionFactory
decl_stmt|;
name|ConnectionManager
name|mockConnectionManager
decl_stmt|;
name|CXFConnectionRequestInfo
name|param
decl_stmt|;
name|ConnectionFactoryImpl
name|cf
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
name|mockConnectionFactory
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ManagedConnectionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockConnectionManager
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|param
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|()
expr_stmt|;
name|param
operator|.
name|setInterface
argument_list|(
name|Runnable
operator|.
name|class
argument_list|)
expr_stmt|;
name|cf
operator|=
operator|new
name|ConnectionFactoryImpl
argument_list|(
name|mockConnectionFactory
argument_list|,
name|mockConnectionManager
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstanceOfSerializable
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Instance of Serializable"
argument_list|,
name|cf
operator|instanceof
name|Serializable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstanceOfReferencable
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Instance of Referenceable"
argument_list|,
name|cf
operator|instanceof
name|Referenceable
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"No ref set"
argument_list|,
name|cf
operator|.
name|getReference
argument_list|()
argument_list|)
expr_stmt|;
name|Reference
name|ref
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Reference
operator|.
name|class
argument_list|)
decl_stmt|;
name|cf
operator|.
name|setReference
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got back what was set"
argument_list|,
name|ref
argument_list|,
name|cf
operator|.
name|getReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnectionReturnsConnectionWithRightManager
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|CXFConnectionRequestInfo
name|reqInfo
init|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Runnable
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
literal|""
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|mockConnectionManager
operator|.
name|allocateConnection
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|mockConnectionFactory
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|reqInfo
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|param
operator|.
name|setWsdlLocation
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|setPortName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|cf
operator|.
name|getConnection
argument_list|(
name|param
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Got the result (the passed in ConnectionRequestInfo) from out mock manager"
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnectionWithNoPortReturnsConnectionWithRightManager
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|CXFConnectionRequestInfo
name|reqInfo
init|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Runnable
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
literal|""
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mockConnectionManager
operator|.
name|allocateConnection
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|mockConnectionFactory
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|reqInfo
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|param
operator|.
name|setWsdlLocation
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|cf
operator|.
name|getConnection
argument_list|(
name|param
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Got the result (the passed in ConnectionRequestInfo) from out mock manager"
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnectionWithNoWsdlLocationReturnsConnectionWithRightManager
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|CXFConnectionRequestInfo
name|reqInfo
init|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|mockConnectionManager
operator|.
name|allocateConnection
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|mockConnectionFactory
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|reqInfo
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|param
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|setPortName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|cf
operator|.
name|getConnection
argument_list|(
name|param
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Got the result (the passed in ConnectionRequestInfo) from out mock manager"
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnectionWithNoWsdlLocationAndNoPortReturnsConnectionWithRightManager
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|reset
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|CXFConnectionRequestInfo
name|reqInfo
init|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mockConnectionManager
operator|.
name|allocateConnection
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|mockConnectionFactory
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|reqInfo
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|cf
operator|=
operator|new
name|ConnectionFactoryImpl
argument_list|(
name|mockConnectionFactory
argument_list|,
name|mockConnectionManager
argument_list|)
expr_stmt|;
name|param
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|cf
operator|.
name|getConnection
argument_list|(
name|param
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Got the result (the passed in ConnectionRequestInfo) from out mock manager"
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnectionWithNonInterface
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|param
operator|.
name|setInterface
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|param
operator|.
name|setWsdlLocation
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file:/tmp/foo"
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|setPortName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|cf
operator|.
name|getConnection
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect exception on using of none interface class"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceException
name|re
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnectionWithNoInterface
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|param
operator|.
name|setInterface
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|cf
operator|.
name|getConnection
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect exception of no interface here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceException
name|re
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

