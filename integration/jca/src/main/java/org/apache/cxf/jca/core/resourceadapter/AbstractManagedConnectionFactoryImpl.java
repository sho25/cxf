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
name|Iterator
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
name|ManagedConnectionFactory
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractManagedConnectionFactoryImpl
extends|extends
name|ResourceBean
implements|implements
name|ManagedConnectionFactory
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1886331424891906960L
decl_stmt|;
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
name|AbstractManagedConnectionFactoryImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PrintWriter
name|printWriter
decl_stmt|;
specifier|public
name|AbstractManagedConnectionFactoryImpl
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|AbstractManagedConnectionFactoryImpl
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|super
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|Object
name|createConnectionFactory
parameter_list|(
name|ConnectionManager
name|connMgr
parameter_list|)
throws|throws
name|ResourceException
function_decl|;
specifier|public
specifier|abstract
name|Object
name|createConnectionFactory
parameter_list|()
throws|throws
name|ResourceException
function_decl|;
specifier|public
specifier|abstract
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
function_decl|;
specifier|public
specifier|abstract
name|void
name|close
parameter_list|()
throws|throws
name|ResourceAdapterInternalException
function_decl|;
specifier|protected
specifier|abstract
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
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|ManagedConnection
name|matchManagedConnections
parameter_list|(
name|Set
name|aMCSet
parameter_list|,
name|Subject
name|subject
parameter_list|,
name|ConnectionRequestInfo
name|crInfo
parameter_list|)
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
literal|"MATCHING_CONNECTIONS"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|Integer
operator|.
name|valueOf
argument_list|(
name|aMCSet
operator|.
name|size
argument_list|()
argument_list|)
block|,
name|crInfo
block|,
name|subject
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|aMCSet
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|AbstractManagedConnectionImpl
name|conn
init|=
operator|(
name|AbstractManagedConnectionImpl
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"MATCH_CONNECTION_AGAINST"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|conn
operator|.
name|getConnectionRequestInfo
argument_list|()
block|,
name|crInfo
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|conn
operator|.
name|isBound
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Match against unbounded, con= "
operator|+
name|conn
operator|+
literal|", info="
operator|+
name|crInfo
argument_list|)
expr_stmt|;
return|return
name|conn
return|;
block|}
if|if
condition|(
name|isMatch
argument_list|(
name|conn
argument_list|,
name|crInfo
argument_list|,
name|subject
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Match against bounded, con= "
operator|+
name|conn
operator|+
literal|", info="
operator|+
name|crInfo
argument_list|)
expr_stmt|;
return|return
name|conn
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|isMatch
parameter_list|(
specifier|final
name|AbstractManagedConnectionImpl
name|candidateConn
parameter_list|,
specifier|final
name|ConnectionRequestInfo
name|crInfo
parameter_list|,
specifier|final
name|Subject
name|subject
parameter_list|)
throws|throws
name|ResourceAdapterInternalException
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
specifier|final
name|ConnectionRequestInfo
name|candidate
init|=
name|candidateConn
operator|.
name|getConnectionRequestInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|candidate
operator|.
name|equals
argument_list|(
name|crInfo
argument_list|)
operator|&&
operator|(
name|subject
operator|==
literal|null
operator|||
name|subject
operator|.
name|equals
argument_list|(
name|candidateConn
operator|.
name|getSubject
argument_list|()
argument_list|)
operator|)
condition|)
block|{
try|try
block|{
name|validateReference
argument_list|(
name|candidateConn
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|result
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|thrown
parameter_list|)
block|{
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
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
name|void
name|setLogWriter
parameter_list|(
specifier|final
name|PrintWriter
name|aPrintWriter
parameter_list|)
throws|throws
name|ResourceException
block|{
if|if
condition|(
name|aPrintWriter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"NULL_LOG_WRITER"
argument_list|)
throw|;
block|}
name|printWriter
operator|=
name|aPrintWriter
expr_stmt|;
name|LoggerHelper
operator|.
name|initializeLoggingOnWriter
argument_list|(
name|printWriter
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

