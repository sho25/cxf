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
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|ConsoleHandler
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
name|Handler
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
name|Log4jLogger
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

begin_class
specifier|public
specifier|final
class|class
name|LoggerHelper
block|{
specifier|public
specifier|static
specifier|final
name|Level
name|DEFAULT_LOG_LEVEL
init|=
name|Level
operator|.
name|WARNING
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONSOLE_HANDLER
init|=
literal|"ConsoleHandler"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WRITER_HANDLER
init|=
literal|"WriterHandler"
decl_stmt|;
specifier|private
specifier|static
name|String
name|rootLoggerName
init|=
literal|"org.apache.cxf"
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|initComplete
decl_stmt|;
specifier|private
specifier|static
name|Level
name|currentLogLevel
init|=
name|Level
operator|.
name|WARNING
decl_stmt|;
specifier|private
name|LoggerHelper
parameter_list|()
block|{
comment|//do nothing here
block|}
specifier|public
specifier|static
name|void
name|initializeLoggingOnWriter
parameter_list|(
specifier|final
name|Writer
name|writer
parameter_list|)
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|writer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.jboss"
argument_list|)
condition|)
block|{
comment|// jboss writer will redirect to log4j which will cause an
comment|// infinite loop if we install an appender over this writer.
comment|// Continue logging via log4j and ignore this writer.
name|LogUtils
operator|.
name|setLoggerClass
argument_list|(
name|Log4jLogger
operator|.
name|class
argument_list|)
expr_stmt|;
return|return;
block|}
name|Logger
name|cxfLogger
init|=
name|getRootCXFLogger
argument_list|()
decl_stmt|;
comment|// test if the stream handler were setted
if|if
condition|(
name|getHandler
argument_list|(
name|cxfLogger
argument_list|,
name|WRITER_HANDLER
argument_list|)
operator|==
literal|null
condition|)
block|{
specifier|final
name|WriterHandler
name|handler
init|=
operator|new
name|WriterHandler
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|cxfLogger
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|deleteLoggingOnWriter
parameter_list|()
block|{
name|Logger
name|cxfLogger
init|=
name|getRootCXFLogger
argument_list|()
decl_stmt|;
name|Handler
name|handler
init|=
name|getHandler
argument_list|(
name|cxfLogger
argument_list|,
name|WRITER_HANDLER
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|cxfLogger
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
name|enableConsoleLogging
argument_list|()
expr_stmt|;
block|}
comment|// true if log output is already going somewhere
specifier|public
specifier|static
name|boolean
name|loggerInitialisedOutsideConnector
parameter_list|()
block|{
specifier|final
name|Handler
index|[]
name|handlers
init|=
name|getConsoleLogger
argument_list|()
operator|.
name|getHandlers
argument_list|()
decl_stmt|;
comment|//NOPMD
return|return
name|handlers
operator|!=
literal|null
operator|&&
name|handlers
operator|.
name|length
operator|>
literal|0
return|;
block|}
specifier|static
name|Handler
name|getHandler
parameter_list|(
name|Logger
name|log
parameter_list|,
name|String
name|handlerName
parameter_list|)
block|{
name|Handler
index|[]
name|handlers
init|=
name|log
operator|.
name|getHandlers
argument_list|()
decl_stmt|;
name|Handler
name|result
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|handlers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|handlers
index|[
name|i
index|]
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|handlerName
argument_list|)
condition|)
block|{
name|result
operator|=
name|handlers
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|void
name|disableConsoleLogging
parameter_list|()
block|{
specifier|final
name|Handler
name|handler
init|=
name|getHandler
argument_list|(
name|getConsoleLogger
argument_list|()
argument_list|,
name|CONSOLE_HANDLER
argument_list|)
decl_stmt|;
comment|//NOPMD
name|getConsoleLogger
argument_list|()
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
comment|//NOPMD
block|}
specifier|public
specifier|static
name|void
name|enableConsoleLogging
parameter_list|()
block|{
if|if
condition|(
name|getHandler
argument_list|(
name|getConsoleLogger
argument_list|()
argument_list|,
name|CONSOLE_HANDLER
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|//NOPMD
specifier|final
name|Handler
name|console
init|=
operator|new
name|ConsoleHandler
argument_list|()
decl_stmt|;
name|getConsoleLogger
argument_list|()
operator|.
name|addHandler
argument_list|(
name|console
argument_list|)
expr_stmt|;
comment|//NOPMD
block|}
block|}
specifier|public
specifier|static
name|void
name|setLogLevel
parameter_list|(
name|String
name|logLevel
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
try|try
block|{
name|currentLogLevel
operator|=
name|Level
operator|.
name|parse
argument_list|(
name|logLevel
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|currentLogLevel
operator|=
name|DEFAULT_LOG_LEVEL
expr_stmt|;
block|}
name|getRootCXFLogger
argument_list|()
operator|.
name|setLevel
argument_list|(
name|currentLogLevel
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getLogLevel
parameter_list|()
block|{
return|return
name|currentLogLevel
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Logger
name|getRootCXFLogger
parameter_list|()
block|{
return|return
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|LoggerHelper
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|getRootLoggerName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Logger
name|getConsoleLogger
parameter_list|()
block|{
return|return
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|LoggerHelper
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|""
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
operator|!
name|initComplete
condition|)
block|{
name|initComplete
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|loggerInitialisedOutsideConnector
argument_list|()
condition|)
block|{
name|enableConsoleLogging
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|String
name|getRootLoggerName
parameter_list|()
block|{
return|return
name|rootLoggerName
return|;
block|}
specifier|public
specifier|static
name|void
name|setRootLoggerName
parameter_list|(
name|String
name|loggerName
parameter_list|)
block|{
name|LoggerHelper
operator|.
name|rootLoggerName
operator|=
name|loggerName
expr_stmt|;
block|}
block|}
end_class

end_unit

