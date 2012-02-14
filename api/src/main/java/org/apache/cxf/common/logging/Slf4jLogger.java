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
name|common
operator|.
name|logging
package|;
end_package

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
name|LogRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|spi
operator|.
name|LocationAwareLogger
import|;
end_import

begin_comment
comment|/**  *<p>  * java.util.logging.Logger implementation delegating to SLF4J.  *</p>  *<p>  * Methods {@link java.util.logging.Logger#setParent(Logger)}, {@link java.util.logging.Logger#getParent()},  * {@link java.util.logging.Logger#setUseParentHandlers(boolean)} and  * {@link java.util.logging.Logger#getUseParentHandlers()} are not overrriden.  *</p>  *<p>  * Level mapping inspired by {@link org.slf4j.bridge.SLF4JBridgeHandler}:  *</p>  *   *<pre>  * FINEST  -&gt; TRACE  * FINER   -&gt; DEBUG  * FINE    -&gt; DEBUG  * CONFIG  -&gt; DEBUG  * INFO    -&gt; INFO  * WARN ING -&gt; WARN  * SEVER   -&gt; ERROR  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|Slf4jLogger
extends|extends
name|AbstractDelegatingLogger
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FQCN
init|=
name|AbstractDelegatingLogger
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|org
operator|.
name|slf4j
operator|.
name|Logger
name|logger
decl_stmt|;
specifier|private
name|LocationAwareLogger
name|locationAwareLogger
decl_stmt|;
specifier|public
name|Slf4jLogger
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|resourceBundleName
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|resourceBundleName
argument_list|)
expr_stmt|;
name|logger
operator|=
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|logger
operator|instanceof
name|LocationAwareLogger
condition|)
block|{
name|locationAwareLogger
operator|=
operator|(
name|LocationAwareLogger
operator|)
name|logger
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Level
name|getLevel
parameter_list|()
block|{
name|Level
name|level
decl_stmt|;
comment|// Verify from the wider (trace) to the narrower (error)
if|if
condition|(
name|logger
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|level
operator|=
name|Level
operator|.
name|FINEST
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// map to the lowest between FINER, FINE and CONFIG
name|level
operator|=
name|Level
operator|.
name|FINER
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|level
operator|=
name|Level
operator|.
name|INFO
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logger
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|level
operator|=
name|Level
operator|.
name|WARNING
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logger
operator|.
name|isErrorEnabled
argument_list|()
condition|)
block|{
name|level
operator|=
name|Level
operator|.
name|SEVERE
expr_stmt|;
block|}
else|else
block|{
name|level
operator|=
name|Level
operator|.
name|OFF
expr_stmt|;
block|}
return|return
name|level
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|internalLogFormatted
parameter_list|(
name|String
name|msg
parameter_list|,
name|LogRecord
name|record
parameter_list|)
block|{
name|Level
name|level
init|=
name|record
operator|.
name|getLevel
argument_list|()
decl_stmt|;
name|Throwable
name|t
init|=
name|record
operator|.
name|getThrown
argument_list|()
decl_stmt|;
comment|/*          * As we can not use a "switch ... case" block but only a "if ... else if ..." block, the order of the          * comparisons is important. We first try log level FINE then INFO, WARN, FINER, etc          */
if|if
condition|(
name|Level
operator|.
name|FINE
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|DEBUG_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|INFO
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|INFO_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|WARNING
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|WARN_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|FINER
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|trace
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|DEBUG_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|FINEST
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|trace
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|TRACE_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|ALL
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
comment|// should never occur, all is used to configure java.util.logging
comment|// but not accessible by the API Logger.xxx() API
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|error
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|ERROR_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|SEVERE
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|error
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|ERROR_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|CONFIG
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|locationAwareLogger
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|locationAwareLogger
operator|.
name|log
argument_list|(
literal|null
argument_list|,
name|FQCN
argument_list|,
name|LocationAwareLogger
operator|.
name|DEBUG_INT
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|Level
operator|.
name|OFF
operator|.
name|equals
argument_list|(
name|level
argument_list|)
condition|)
block|{
comment|// don't log
block|}
block|}
block|}
end_class

end_unit

