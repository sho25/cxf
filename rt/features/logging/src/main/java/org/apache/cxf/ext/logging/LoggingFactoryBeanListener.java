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
name|ext
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|annotations
operator|.
name|Logging
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
name|endpoint
operator|.
name|Endpoint
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
name|endpoint
operator|.
name|Server
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|LogEventSender
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|PrintWriterEventSender
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
name|factory
operator|.
name|AbstractServiceFactoryBean
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
name|factory
operator|.
name|FactoryBeanListener
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
name|factory
operator|.
name|FactoryBeanListenerManager
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|LoggingFactoryBeanListener
implements|implements
name|FactoryBeanListener
block|{
specifier|public
name|LoggingFactoryBeanListener
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|FactoryBeanListenerManager
name|m
init|=
name|b
operator|.
name|getExtension
argument_list|(
name|FactoryBeanListenerManager
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|FactoryBeanListener
name|f
range|:
name|m
operator|.
name|getListeners
argument_list|()
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"OldLoggingFactoryBeanListener"
argument_list|)
condition|)
block|{
name|m
operator|.
name|removeListener
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|handleEvent
parameter_list|(
name|Event
name|ev
parameter_list|,
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
switch|switch
condition|(
name|ev
condition|)
block|{
case|case
name|ENDPOINT_SELECTED
case|:
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|args
index|[
literal|2
index|]
decl_stmt|;
name|Endpoint
name|ep
init|=
operator|(
name|Endpoint
operator|)
name|args
index|[
literal|1
index|]
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|getBus
argument_list|()
decl_stmt|;
comment|// To avoid the NPE
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|addLoggingSupport
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Logging
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|SERVER_CREATED
case|:
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|args
index|[
literal|2
index|]
decl_stmt|;
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Server
name|server
init|=
operator|(
name|Server
operator|)
name|args
index|[
literal|0
index|]
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|addLoggingSupport
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|bus
argument_list|,
name|cls
operator|.
name|getAnnotation
argument_list|(
name|Logging
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
default|default:
comment|//do nothing
block|}
block|}
specifier|private
name|LogEventSender
name|createEventSender
parameter_list|(
name|String
name|location
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|location
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
literal|"<stdout>"
operator|.
name|equals
argument_list|(
name|location
argument_list|)
condition|)
block|{
return|return
operator|new
name|PrintWriterEventSender
argument_list|(
name|System
operator|.
name|out
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"<stderr>"
operator|.
name|equals
argument_list|(
name|location
argument_list|)
condition|)
block|{
return|return
operator|new
name|PrintWriterEventSender
argument_list|(
name|System
operator|.
name|err
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|location
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|location
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
name|PrintWriter
name|writer
init|=
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
decl_stmt|;
return|return
operator|new
name|PrintWriterEventSender
argument_list|(
name|writer
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//stick with default
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|addLoggingSupport
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|Logging
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|LoggingFeature
name|lf
init|=
operator|new
name|LoggingFeature
argument_list|()
decl_stmt|;
name|lf
operator|.
name|setPrettyLogging
argument_list|(
name|annotation
operator|.
name|pretty
argument_list|()
argument_list|)
expr_stmt|;
name|lf
operator|.
name|setLimit
argument_list|(
name|annotation
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
name|lf
operator|.
name|setLogBinary
argument_list|(
name|annotation
operator|.
name|showBinary
argument_list|()
argument_list|)
expr_stmt|;
name|LogEventSender
name|les
init|=
name|createEventSender
argument_list|(
name|annotation
operator|.
name|outLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|les
operator|!=
literal|null
condition|)
block|{
name|lf
operator|.
name|setOutSender
argument_list|(
name|les
argument_list|)
expr_stmt|;
block|}
name|les
operator|=
name|createEventSender
argument_list|(
name|annotation
operator|.
name|inLocation
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|les
operator|!=
literal|null
condition|)
block|{
name|lf
operator|.
name|setInSender
argument_list|(
name|les
argument_list|)
expr_stmt|;
block|}
name|lf
operator|.
name|initialize
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

