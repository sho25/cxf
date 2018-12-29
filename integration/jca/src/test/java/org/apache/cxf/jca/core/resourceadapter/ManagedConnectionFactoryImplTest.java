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
name|core
operator|.
name|resourceadapter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|ConnectionRequestInfo
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
name|ManagedConnection
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
name|ResourceAdapterInternalException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|ManagedConnectionFactoryImplTest
block|{
name|DummyManagedConnectionFactoryImpl
name|mcf
init|=
operator|new
name|DummyManagedConnectionFactoryImpl
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testManagedConnectionFactoryImplInstanceOfResourceBean
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"mcf is not null"
argument_list|,
name|mcf
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"ManagedConnectionFactoryImpl is ResourceBean"
argument_list|,
name|mcf
operator|instanceof
name|ResourceBean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchConnectionSameConnectioRequestInfoNotBound
parameter_list|()
throws|throws
name|Exception
block|{
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
name|Set
argument_list|<
name|AbstractManagedConnectionImpl
argument_list|>
name|connectionSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|ConnectionRequestInfo
name|cri
init|=
operator|new
name|DummyConnectionRequestInfo
argument_list|()
decl_stmt|;
name|DummyManagedConnectionImpl
name|con1
init|=
operator|new
name|DummyManagedConnectionImpl
argument_list|(
name|mcf
argument_list|,
name|cri
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|connectionSet
operator|.
name|add
argument_list|(
name|con1
argument_list|)
expr_stmt|;
name|ManagedConnection
name|mcon
init|=
name|mcf
operator|.
name|matchManagedConnections
argument_list|(
name|connectionSet
argument_list|,
name|subject
argument_list|,
name|cri
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|con1
argument_list|,
name|mcon
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchConnectionSameConnectioRequestInfoBound
parameter_list|()
throws|throws
name|Exception
block|{
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
name|Set
argument_list|<
name|AbstractManagedConnectionImpl
argument_list|>
name|connectionSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|ConnectionRequestInfo
name|cri
init|=
operator|new
name|DummyConnectionRequestInfo
argument_list|()
decl_stmt|;
name|DummyManagedConnectionImpl
name|con1
init|=
operator|new
name|DummyManagedConnectionImpl
argument_list|(
name|mcf
argument_list|,
name|cri
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|con1
operator|.
name|setBound
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connectionSet
operator|.
name|add
argument_list|(
name|con1
argument_list|)
expr_stmt|;
name|ManagedConnection
name|mcon
init|=
name|mcf
operator|.
name|matchManagedConnections
argument_list|(
name|connectionSet
argument_list|,
name|subject
argument_list|,
name|cri
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|con1
argument_list|,
name|mcon
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchConnectionDifferentConnectioRequestInfoNotBound
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionRequestInfo
name|cri1
init|=
operator|new
name|DummyConnectionRequestInfo
argument_list|()
decl_stmt|;
name|ConnectionRequestInfo
name|cri2
init|=
operator|new
name|DummyConnectionRequestInfo
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
name|assertTrue
argument_list|(
literal|"request info object are differnt"
argument_list|,
name|cri1
operator|!=
name|cri2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|AbstractManagedConnectionImpl
argument_list|>
name|connectionSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|DummyManagedConnectionImpl
name|con1
init|=
operator|new
name|DummyManagedConnectionImpl
argument_list|(
name|mcf
argument_list|,
name|cri1
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|connectionSet
operator|.
name|add
argument_list|(
name|con1
argument_list|)
expr_stmt|;
name|ManagedConnection
name|mcon
init|=
name|mcf
operator|.
name|matchManagedConnections
argument_list|(
name|connectionSet
argument_list|,
name|subject
argument_list|,
name|cri2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"incorrect connection returned"
argument_list|,
name|con1
argument_list|,
name|mcon
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchConnectionDifferentConnectioRequestInfoBound
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionRequestInfo
name|cri1
init|=
operator|new
name|DummyConnectionRequestInfo
argument_list|()
decl_stmt|;
name|ConnectionRequestInfo
name|cri2
init|=
operator|new
name|DummyConnectionRequestInfo
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
name|assertTrue
argument_list|(
literal|"request info object are differnt"
argument_list|,
name|cri1
operator|!=
name|cri2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|AbstractManagedConnectionImpl
argument_list|>
name|connectionSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|DummyManagedConnectionImpl
name|con1
init|=
operator|new
name|DummyManagedConnectionImpl
argument_list|(
name|mcf
argument_list|,
name|cri1
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|con1
operator|.
name|setBound
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connectionSet
operator|.
name|add
argument_list|(
name|con1
argument_list|)
expr_stmt|;
name|ManagedConnection
name|mcon
init|=
name|mcf
operator|.
name|matchManagedConnections
argument_list|(
name|connectionSet
argument_list|,
name|subject
argument_list|,
name|cri2
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"should not get a match"
argument_list|,
name|mcon
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchConnectionInvalidatedWithSameConnectioRequestInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
name|Set
argument_list|<
name|AbstractManagedConnectionImpl
argument_list|>
name|connectionSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|ConnectionRequestInfo
name|cri
init|=
operator|new
name|DummyConnectionRequestInfo
argument_list|()
decl_stmt|;
name|DummyManagedConnectionImpl
name|con1
init|=
operator|new
name|DummyManagedConnectionImpl
argument_list|(
name|mcf
argument_list|,
name|cri
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|con1
operator|.
name|setBound
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|con1
operator|.
name|setCon
argument_list|(
name|connectionSet
argument_list|)
expr_stmt|;
name|connectionSet
operator|.
name|add
argument_list|(
name|con1
argument_list|)
expr_stmt|;
name|ManagedConnection
name|mcon
init|=
name|mcf
operator|.
name|matchManagedConnections
argument_list|(
name|connectionSet
argument_list|,
name|subject
argument_list|,
name|cri
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Connection must be null"
argument_list|,
name|mcon
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSetLogWriter
parameter_list|()
throws|throws
name|Exception
block|{
name|PrintWriter
name|writer
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|PrintWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
comment|// the write could be call lots of time
name|EasyMock
operator|.
name|replay
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|mcf
operator|.
name|setLogWriter
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mcf
operator|.
name|getLogWriter
argument_list|()
operator|==
name|writer
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetNullLogWriter
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|mcf
operator|.
name|setLogWriter
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expect ex on null log writer arg"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|//do nothing here
block|}
block|}
block|}
end_class

begin_class
class|class
name|DummyConnectionRequestInfo
implements|implements
name|ConnectionRequestInfo
block|{ }
end_class

begin_class
class|class
name|DummyManagedConnectionFactoryImpl
extends|extends
name|AbstractManagedConnectionFactoryImpl
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|218445259745278972L
decl_stmt|;
name|DummyManagedConnectionFactoryImpl
parameter_list|()
block|{     }
name|DummyManagedConnectionFactoryImpl
parameter_list|(
name|Properties
name|p
parameter_list|)
block|{
name|super
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|createConnectionFactory
parameter_list|(
name|ConnectionManager
name|connMgr
parameter_list|)
throws|throws
name|ResourceException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Object
name|createConnectionFactory
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ManagedConnection
name|createManagedConnection
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|ConnectionRequestInfo
name|connReqInfo
parameter_list|)
throws|throws
name|ResourceException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|ResourceAdapterInternalException
block|{     }
specifier|protected
name|void
name|validateReference
parameter_list|(
name|AbstractManagedConnectionImpl
name|conn
parameter_list|,
name|Subject
name|subject
parameter_list|)
throws|throws
name|ResourceAdapterInternalException
block|{
name|boolean
name|valid
init|=
literal|true
decl_stmt|;
try|try
block|{
if|if
condition|(
name|conn
operator|.
name|getConnection
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|valid
operator|=
literal|false
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ResourceException
name|ignored
parameter_list|)
block|{
comment|// do nothing here
block|}
if|if
condition|(
operator|!
name|valid
condition|)
block|{
throw|throw
operator|new
name|ResourceAdapterInternalException
argument_list|(
literal|"invalid"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setResourceAdapter
parameter_list|(
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapter
name|ra
parameter_list|)
block|{     }
specifier|public
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapter
name|getResourceAdapter
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

