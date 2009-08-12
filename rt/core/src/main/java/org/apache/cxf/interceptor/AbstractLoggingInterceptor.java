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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

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
name|net
operator|.
name|URI
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
name|message
operator|.
name|Message
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
name|phase
operator|.
name|AbstractPhaseInterceptor
import|;
end_import

begin_comment
comment|/**  * A simple logging handler which outputs the bytes of the message to the  * Logger.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractLoggingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LoggingInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|int
name|limit
init|=
literal|100
operator|*
literal|1024
decl_stmt|;
specifier|protected
name|PrintWriter
name|writer
decl_stmt|;
specifier|public
name|AbstractLoggingInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutputLocation
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|==
literal|null
operator|||
literal|"<logger>"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|writer
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"<stdout>"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|writer
operator|=
operator|new
name|PrintWriter
argument_list|(
name|System
operator|.
name|out
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"<stderr>"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|writer
operator|=
operator|new
name|PrintWriter
argument_list|(
name|System
operator|.
name|err
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|writer
operator|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|,
literal|true
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Error configuring log location "
operator|+
name|s
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setPrintWriter
parameter_list|(
name|PrintWriter
name|w
parameter_list|)
block|{
name|writer
operator|=
name|w
expr_stmt|;
block|}
specifier|public
name|PrintWriter
name|getPrintWriter
parameter_list|()
block|{
return|return
name|writer
return|;
block|}
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|lim
parameter_list|)
block|{
name|limit
operator|=
name|lim
expr_stmt|;
block|}
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
comment|/**      * Transform the string before display. The implementation in this class       * does nothing. Override this method if you want to change the contents of the       * logged message before it is delivered to the output.       * For example, you can use this to mask out sensitive information.      * @param originalLogString the raw log message.      * @return transformed data      */
specifier|protected
name|String
name|transform
parameter_list|(
name|String
name|originalLogString
parameter_list|)
block|{
return|return
name|originalLogString
return|;
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|message
operator|=
name|transform
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

