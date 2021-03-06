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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
class|class
name|ResourceBean
implements|implements
name|Serializable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_VALUE_STRING
init|=
literal|"DEFAULT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LEVEL
init|=
literal|"log.level"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_MONITOR_POLL_INTERVAL
init|=
literal|"120"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EJB_SERVICE_PROPERTIES_URL
init|=
literal|"ejb.service.properties.url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MONITOR_EJB_SERVICE_PROPERTIES
init|=
literal|"monitor.ejb.service.properties"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MONITOR_POLL_INTERVAL
init|=
literal|"monitor.poll.interval"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EJB_SERVANT_BASE_URL
init|=
literal|"ejb.servant.base.url"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2373577203597864072L
decl_stmt|;
static|static
block|{
comment|// first use of log, default init if necessary
name|LoggerHelper
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
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
name|ResourceBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Properties
name|pluginProps
decl_stmt|;
specifier|public
name|ResourceBean
parameter_list|()
block|{
name|pluginProps
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ResourceBean
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|pluginProps
operator|=
name|props
expr_stmt|;
block|}
specifier|public
name|void
name|setDisableConsoleLogging
parameter_list|(
name|boolean
name|disable
parameter_list|)
block|{
if|if
condition|(
name|disable
condition|)
block|{
name|LoggerHelper
operator|.
name|disableConsoleLogging
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Properties
name|getPluginProps
parameter_list|()
block|{
return|return
name|pluginProps
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|propName
parameter_list|,
name|String
name|propValue
parameter_list|)
block|{
if|if
condition|(
operator|!
name|DEFAULT_VALUE_STRING
operator|.
name|equals
argument_list|(
name|propValue
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"SETTING_PROPERTY"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|propName
block|,
name|propValue
block|}
argument_list|)
expr_stmt|;
name|getPluginProps
argument_list|()
operator|.
name|setProperty
argument_list|(
name|propName
argument_list|,
name|propValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG_LEVEL
operator|.
name|equals
argument_list|(
name|propName
argument_list|)
condition|)
block|{
name|LoggerHelper
operator|.
name|setLogLevel
argument_list|(
name|propValue
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|URL
name|getPropsURL
parameter_list|(
name|String
name|propsUrl
parameter_list|)
throws|throws
name|ResourceException
block|{
name|URL
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|propsUrl
operator|!=
literal|null
condition|)
block|{
name|ret
operator|=
name|createURL
argument_list|(
name|propsUrl
argument_list|,
literal|"Unable to construct URL from URL string, value="
operator|+
name|propsUrl
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|protected
name|URL
name|createURL
parameter_list|(
name|String
name|spec
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|ResourceAdapterInternalException
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|spec
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|mue
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|,
name|mue
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|validateURLString
parameter_list|(
name|String
name|spec
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|ResourceAdapterInternalException
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
name|createURL
argument_list|(
name|spec
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Validated url="
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

