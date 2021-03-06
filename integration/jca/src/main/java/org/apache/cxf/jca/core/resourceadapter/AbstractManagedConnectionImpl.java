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
name|*
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|ManagedConnectionMetaData
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|jca
operator|.
name|core
operator|.
name|logging
operator|.
name|LoggerHelper
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
name|jca
operator|.
name|cxf
operator|.
name|CXFManagedConnectionMetaData
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractManagedConnectionImpl
implements|implements
name|ManagedConnection
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractManagedConnectionImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|PrintWriter
name|printWriter
decl_stmt|;
specifier|protected
name|ConnectionRequestInfo
name|crinfo
decl_stmt|;
specifier|protected
name|Subject
name|subject
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|ConnectionEventListener
argument_list|>
name|connectionEventListeners
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|AbstractManagedConnectionFactoryImpl
name|managedConnectionFactory
decl_stmt|;
specifier|public
name|AbstractManagedConnectionImpl
parameter_list|(
name|AbstractManagedConnectionFactoryImpl
name|managedFactory
parameter_list|,
name|ConnectionRequestInfo
name|crInfo
parameter_list|,
name|Subject
name|sj
parameter_list|)
throws|throws
name|ResourceException
block|{
name|this
operator|.
name|managedConnectionFactory
operator|=
name|managedFactory
expr_stmt|;
name|this
operator|.
name|crinfo
operator|=
name|crInfo
expr_stmt|;
name|this
operator|.
name|subject
operator|=
name|sj
expr_stmt|;
block|}
specifier|public
name|void
name|addConnectionEventListener
parameter_list|(
name|ConnectionEventListener
name|aListener
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"ADD_EVENT_LISTENER_CALLED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|this
block|,
name|aListener
block|}
argument_list|)
expr_stmt|;
name|connectionEventListeners
operator|.
name|add
argument_list|(
name|aListener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeConnectionEventListener
parameter_list|(
name|ConnectionEventListener
name|aListener
parameter_list|)
block|{
name|connectionEventListeners
operator|.
name|remove
argument_list|(
name|aListener
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|associateConnection
parameter_list|(
name|Object
name|arg0
parameter_list|)
throws|throws
name|ResourceException
function_decl|;
comment|//public abstract LocalTransaction getLocalTransaction() throws ResourceException;
comment|//public abstract javax.transaction.xa.XAResource getXAResource() throws ResourceException;
specifier|public
specifier|abstract
name|Object
name|getConnection
parameter_list|(
name|Subject
name|aSubject
parameter_list|,
name|ConnectionRequestInfo
name|aCrInfo
parameter_list|)
throws|throws
name|ResourceException
function_decl|;
specifier|public
specifier|abstract
name|boolean
name|isBound
parameter_list|()
function_decl|;
specifier|public
name|void
name|close
parameter_list|(
name|Object
name|closingHandle
parameter_list|)
throws|throws
name|ResourceException
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Closing handle: "
operator|+
name|closingHandle
argument_list|)
expr_stmt|;
name|ConnectionEvent
name|coEvent
init|=
operator|new
name|ConnectionEvent
argument_list|(
name|this
argument_list|,
name|ConnectionEvent
operator|.
name|CONNECTION_CLOSED
argument_list|)
decl_stmt|;
name|coEvent
operator|.
name|setConnectionHandle
argument_list|(
name|closingHandle
argument_list|)
expr_stmt|;
name|sendEvent
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
block|}
comment|// going back in the pool
specifier|public
name|void
name|cleanup
parameter_list|()
throws|throws
name|ResourceException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CLEANUP_CALLED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|this
block|}
argument_list|)
expr_stmt|;
block|}
comment|// beging chucked from the pool
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|ResourceException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"DESTROY_CALLED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|this
block|}
argument_list|)
expr_stmt|;
name|connectionEventListeners
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|LoggerHelper
operator|.
name|deleteLoggingOnWriter
argument_list|()
expr_stmt|;
if|if
condition|(
name|printWriter
operator|!=
literal|null
condition|)
block|{
name|printWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
name|printWriter
return|;
block|}
specifier|public
name|ManagedConnectionMetaData
name|getMetaData
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
operator|new
name|CXFManagedConnectionMetaData
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|aPrintWriter
parameter_list|)
throws|throws
name|ResourceException
block|{
name|printWriter
operator|=
name|aPrintWriter
expr_stmt|;
if|if
condition|(
name|printWriter
operator|!=
literal|null
condition|)
block|{
name|LoggerHelper
operator|.
name|initializeLoggingOnWriter
argument_list|(
name|printWriter
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Subject
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
specifier|public
name|void
name|setSubject
parameter_list|(
name|Subject
name|sj
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|sj
expr_stmt|;
block|}
specifier|protected
name|ConnectionRequestInfo
name|getConnectionRequestInfo
parameter_list|()
block|{
return|return
name|crinfo
return|;
block|}
specifier|protected
name|void
name|setConnectionRequestInfo
parameter_list|(
name|ConnectionRequestInfo
name|info
parameter_list|)
block|{
name|this
operator|.
name|crinfo
operator|=
name|info
expr_stmt|;
block|}
specifier|protected
name|void
name|sendEvent
parameter_list|(
name|ConnectionEvent
name|coEvent
parameter_list|)
block|{
for|for
control|(
name|ConnectionEventListener
name|l
range|:
name|connectionEventListeners
control|)
block|{
name|sendEventToListener
argument_list|(
name|coEvent
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|sendEventToListener
parameter_list|(
name|ConnectionEvent
name|coEvent
parameter_list|,
name|ConnectionEventListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|CONNECTION_CLOSED
condition|)
block|{
name|listener
operator|.
name|connectionClosed
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CONNECTION_CLOSED_EVENT_FIRED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|listener
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_COMMITTED
condition|)
block|{
name|listener
operator|.
name|localTransactionCommitted
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"LOCAL_TX_COMMITTED_EVENT_FIRED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|listener
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_ROLLEDBACK
condition|)
block|{
name|listener
operator|.
name|localTransactionRolledback
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"LOCAL_TX_ROLLEDBACK_EVENT_FIRED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|listener
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_STARTED
condition|)
block|{
name|listener
operator|.
name|localTransactionStarted
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"LOCAL_TX_STARTED_EVENT_FIRED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|listener
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|CONNECTION_ERROR_OCCURRED
condition|)
block|{
name|listener
operator|.
name|connectionErrorOccurred
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CTX_ERROR_OCURRED_EVENT_FIRED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|listener
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|AbstractManagedConnectionFactoryImpl
name|theManagedConnectionFactory
parameter_list|()
block|{
return|return
name|managedConnectionFactory
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"["
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|':'
operator|+
name|hashCode
argument_list|()
operator|+
literal|":ManagedConnection["
operator|+
name|crinfo
operator|+
literal|"]"
return|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|ex
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sendEvent
argument_list|(
operator|new
name|ConnectionEvent
argument_list|(
name|this
argument_list|,
name|ConnectionEvent
operator|.
name|CONNECTION_ERROR_OCCURRED
argument_list|,
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

