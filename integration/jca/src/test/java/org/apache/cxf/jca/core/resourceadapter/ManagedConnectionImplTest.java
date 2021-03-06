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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|ConnectionEvent
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
name|ConnectionEventListener
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
name|assertSame
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
name|ManagedConnectionImplTest
block|{
specifier|private
name|DummyManagedConnectionImpl
name|mc
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
name|mc
operator|=
operator|new
name|DummyManagedConnectionImpl
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
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
specifier|final
name|AtomicBoolean
name|closed
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|StringWriter
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|closed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|mc
operator|.
name|setLogWriter
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|writer
argument_list|,
name|mc
operator|.
name|getLogWriter
argument_list|()
argument_list|)
expr_stmt|;
name|mc
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|closed
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetNullLogWriterOk
parameter_list|()
throws|throws
name|Exception
block|{
name|mc
operator|.
name|setLogWriter
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoveConnectionEventListener
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionEvent
name|event
init|=
operator|new
name|ConnectionEvent
argument_list|(
name|mc
argument_list|,
name|ConnectionEvent
operator|.
name|CONNECTION_ERROR_OCCURRED
argument_list|)
decl_stmt|;
name|ConnectionEventListener
name|listener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|mc
operator|.
name|addConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|connectionErrorOccurred
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ConnectionEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|removeConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCleanupDoesNothing
parameter_list|()
throws|throws
name|Exception
block|{
name|mc
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMetaData
parameter_list|()
throws|throws
name|Exception
block|{
name|mc
operator|.
name|getMetaData
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSetSubject
parameter_list|()
block|{
name|Subject
name|s
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|mc
operator|.
name|setSubject
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got back what we set"
argument_list|,
name|s
argument_list|,
name|mc
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSetConnectionRequestInfo
parameter_list|()
block|{
name|ConnectionRequestInfo
name|ri
init|=
operator|new
name|ConnectionRequestInfo
argument_list|()
block|{         }
decl_stmt|;
name|mc
operator|.
name|setConnectionRequestInfo
argument_list|(
name|ri
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got back what we set"
argument_list|,
name|ri
argument_list|,
name|mc
operator|.
name|getConnectionRequestInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClose
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Object
name|o
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ConnectionEventListener
name|listener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|listener
operator|.
name|connectionClosed
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ConnectionEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|addConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|close
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testError
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionEventListener
name|listener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|mc
operator|.
name|addConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|connectionErrorOccurred
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ConnectionEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|setLogWriter
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|mc
operator|.
name|error
argument_list|(
operator|new
name|Exception
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendEventError
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionEvent
name|event
init|=
operator|new
name|ConnectionEvent
argument_list|(
name|mc
argument_list|,
name|ConnectionEvent
operator|.
name|CONNECTION_ERROR_OCCURRED
argument_list|)
decl_stmt|;
name|ConnectionEventListener
name|listener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|mc
operator|.
name|addConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|connectionErrorOccurred
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ConnectionEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendEventTxStarted
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionEvent
name|event
init|=
operator|new
name|ConnectionEvent
argument_list|(
name|mc
argument_list|,
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_STARTED
argument_list|)
decl_stmt|;
name|ConnectionEventListener
name|listener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|mc
operator|.
name|addConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|localTransactionStarted
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ConnectionEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendEventTxCommitted
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionEvent
name|event
init|=
operator|new
name|ConnectionEvent
argument_list|(
name|mc
argument_list|,
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_COMMITTED
argument_list|)
decl_stmt|;
name|ConnectionEventListener
name|listener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|mc
operator|.
name|addConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|localTransactionCommitted
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ConnectionEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSendEventTxRolledBack
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionEvent
name|event
init|=
operator|new
name|ConnectionEvent
argument_list|(
name|mc
argument_list|,
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_ROLLEDBACK
argument_list|)
decl_stmt|;
name|ConnectionEventListener
name|listener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|mc
operator|.
name|addConnectionEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reset
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|localTransactionRolledback
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|ConnectionEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|mc
operator|.
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

