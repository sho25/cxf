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
name|jaxrs
operator|.
name|ext
operator|.
name|logging
operator|.
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|Validate
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
name|jaxrs
operator|.
name|ext
operator|.
name|logging
operator|.
name|LogLevel
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAtomBean
block|{
specifier|private
name|List
argument_list|<
name|LoggerLevel
argument_list|>
name|loggers
init|=
operator|new
name|ArrayList
argument_list|<
name|LoggerLevel
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|initialized
decl_stmt|;
comment|/**      * Creates unconfigured and uninitialized bean. To configure setters must be used, then {@link #init()}      * must be called.      */
specifier|public
name|AbstractAtomBean
parameter_list|()
block|{
name|initSingleLogger
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initSingleLogger
parameter_list|()
block|{
name|loggers
operator|=
operator|new
name|ArrayList
argument_list|<
name|LoggerLevel
argument_list|>
argument_list|()
expr_stmt|;
name|loggers
operator|.
name|add
argument_list|(
operator|new
name|LoggerLevel
argument_list|(
literal|""
argument_list|,
literal|"INFO"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set one or more loggers and levels descriptor.<br>      * Parsed input syntax is:      *       *<pre>      * loggers   :=&lt;logger&gt;(&lt;separator&gt;&lt;logger&gt;)*      * logger    :=&lt;name&gt;[&quot;:&quot;&lt;level&gt;]      * separator :=&quot;,&quot; |&quot;&quot; |&quot;\n&quot;      *</pre>      *       * Examples:      *<p>      * Two loggers and two levels:<br>      *<tt>org.apache.cxf:INFO, org.apache.cxf.jaxrs:DEBUG</tt>      *<p>      * Three loggers, first with default "INFO" level:<br>      *<tt>org.apache.cxf, org.apache.cxf.jaxrs:DEBUG, namedLogger:ERROR</tt><br>      *<p>      * One logger with default "INFO" level:<br>      *<tt>org.apache.cxf</tt><br>      */
specifier|public
name|void
name|setLoggers
parameter_list|(
name|String
name|loggers
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|loggers
argument_list|,
literal|"loggers is null"
argument_list|)
expr_stmt|;
name|parseLoggers
argument_list|(
name|loggers
argument_list|)
expr_stmt|;
block|}
comment|/**      * Name of logger to associate with ATOM push handler; empty string for root logger.      */
specifier|public
name|void
name|setLogger
parameter_list|(
name|String
name|logger
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|logger
argument_list|,
literal|"logger is null"
argument_list|)
expr_stmt|;
if|if
condition|(
name|loggers
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|initSingleLogger
argument_list|()
expr_stmt|;
block|}
name|loggers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
block|}
comment|/**      * Name of level that logger will use publishing log events to ATOM push handler; empty string for default      * "INFO" level.      */
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|level
argument_list|,
literal|"level is null"
argument_list|)
expr_stmt|;
if|if
condition|(
name|loggers
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|initSingleLogger
argument_list|()
expr_stmt|;
block|}
name|loggers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
block|}
comment|/**      * Initializes bean; creates ATOM handler based on current properties state, and attaches handler to      * logger(s).      */
specifier|public
name|void
name|init
parameter_list|()
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
name|Handler
name|h
init|=
name|createHandler
argument_list|()
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
name|loggers
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Logger
name|l
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractAtomBean
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|loggers
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getLogger
argument_list|()
argument_list|)
decl_stmt|;
name|l
operator|.
name|addHandler
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|l
operator|.
name|setLevel
argument_list|(
name|LogLevel
operator|.
name|toJUL
argument_list|(
name|LogLevel
operator|.
name|valueOf
argument_list|(
name|loggers
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getLevel
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|Handler
name|createHandler
parameter_list|()
function_decl|;
specifier|protected
name|void
name|checkInit
parameter_list|()
block|{
if|if
condition|(
name|initialized
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Bean is already initialized"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|parseLoggers
parameter_list|(
name|String
name|param
parameter_list|)
block|{
name|loggers
operator|=
operator|new
name|ArrayList
argument_list|<
name|LoggerLevel
argument_list|>
argument_list|()
expr_stmt|;
name|StringTokenizer
name|st1
init|=
operator|new
name|StringTokenizer
argument_list|(
name|param
argument_list|,
literal|", \t\n\r\f"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st1
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|tok
init|=
name|st1
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|tok
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|loggers
operator|.
name|add
argument_list|(
operator|new
name|LoggerLevel
argument_list|(
name|tok
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
argument_list|,
name|tok
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|,
name|tok
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|loggers
operator|.
name|add
argument_list|(
operator|new
name|LoggerLevel
argument_list|(
name|tok
argument_list|,
literal|"INFO"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|List
argument_list|<
name|LoggerLevel
argument_list|>
name|getLoggers
parameter_list|()
block|{
return|return
name|loggers
return|;
block|}
specifier|protected
specifier|static
class|class
name|LoggerLevel
block|{
specifier|private
name|String
name|logger
decl_stmt|;
specifier|private
name|String
name|level
decl_stmt|;
specifier|public
name|LoggerLevel
parameter_list|(
name|String
name|logger
parameter_list|,
name|String
name|level
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
specifier|public
name|String
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
specifier|public
name|void
name|setLogger
parameter_list|(
name|String
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
specifier|public
name|String
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

