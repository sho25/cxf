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
name|io
operator|.
name|StringWriter
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|helpers
operator|.
name|XMLUtils
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
name|io
operator|.
name|CachedOutputStream
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
name|InterfaceInfo
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
specifier|protected
name|boolean
name|prettyLogging
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
name|AbstractLoggingInterceptor
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|Logger
name|getLogger
parameter_list|()
function_decl|;
name|Logger
name|getMessageLogger
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|EndpointInfo
name|endpoint
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getService
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|getLogger
argument_list|()
return|;
block|}
name|String
name|serviceName
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|iface
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|String
name|portName
init|=
name|endpoint
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|portTypeName
init|=
name|iface
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|logName
init|=
literal|"org.apache.cxf.services."
operator|+
name|serviceName
operator|+
literal|"."
operator|+
name|portName
operator|+
literal|"."
operator|+
name|portTypeName
decl_stmt|;
name|Logger
name|logger
init|=
name|endpoint
operator|.
name|getProperty
argument_list|(
literal|"MessageLogger"
argument_list|,
name|Logger
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|,
name|logName
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setProperty
argument_list|(
literal|"MessageLogger"
argument_list|,
name|logger
argument_list|)
expr_stmt|;
block|}
return|return
name|logger
return|;
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
name|getLogger
argument_list|()
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
specifier|public
name|void
name|setPrettyLogging
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|prettyLogging
operator|=
name|flag
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPrettyLogging
parameter_list|()
block|{
return|return
name|prettyLogging
return|;
block|}
specifier|protected
name|void
name|writePayload
parameter_list|(
name|StringBuilder
name|builder
parameter_list|,
name|CachedOutputStream
name|cos
parameter_list|,
name|String
name|encoding
parameter_list|,
name|String
name|contentType
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Just transform the XML message when the cos has content
if|if
condition|(
name|isPrettyLogging
argument_list|()
operator|&&
operator|(
name|contentType
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"xml"
argument_list|)
operator|>=
literal|0
operator|)
operator|&&
name|cos
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Transformer
name|serializer
init|=
name|XMLUtils
operator|.
name|newTransformer
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// Setup indenting to "pretty print"
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|StringWriter
name|swriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|transform
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|cos
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|swriter
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|swriter
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|length
argument_list|()
operator|<
name|limit
operator|||
name|limit
operator|==
operator|-
literal|1
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|swriter
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|swriter
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|limit
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|cos
operator|.
name|writeCacheTo
argument_list|(
name|builder
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cos
operator|.
name|writeCacheTo
argument_list|(
name|builder
argument_list|,
name|encoding
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Transform the string before display. The implementation in this class       * does nothing. Override this method if you wish to change the contents of the       * logged message before it is delivered to the output.       * For example, you can use this to mask out sensitive information.      * @param originalLogString the raw log message.      * @return transformed data      */
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
name|Logger
name|logger
parameter_list|,
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
comment|// Flushing the writer to make sure the message is written
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|logger
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

