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
name|metrics
operator|.
name|codahale
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MalformedObjectNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|JmxReporter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|ObjectNameFactory
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|management
operator|.
name|InstrumentationManager
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
name|management
operator|.
name|ManagementConstants
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
name|metrics
operator|.
name|MetricsContext
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
name|metrics
operator|.
name|MetricsProvider
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
name|Service
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
name|BindingOperationInfo
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|CodahaleMetricsProvider
implements|implements
name|MetricsProvider
block|{
specifier|private
specifier|static
specifier|final
name|String
name|QUESTION_MARK
init|=
literal|"?"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ESCAPED_QUESTION_MARK
init|=
literal|"\\?"
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|MetricRegistry
name|registry
decl_stmt|;
comment|/**      *       */
specifier|public
name|CodahaleMetricsProvider
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
name|registry
operator|=
name|b
operator|.
name|getExtension
argument_list|(
name|MetricRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
operator|new
name|MetricRegistry
argument_list|()
expr_stmt|;
name|setupJMXReporter
argument_list|(
name|b
argument_list|,
name|registry
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setupJMXReporter
parameter_list|(
name|Bus
name|b
parameter_list|,
name|MetricRegistry
name|reg
parameter_list|)
block|{
name|InstrumentationManager
name|im
init|=
name|b
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|im
operator|!=
literal|null
condition|)
block|{
name|JmxReporter
name|reporter
init|=
name|JmxReporter
operator|.
name|forRegistry
argument_list|(
name|reg
argument_list|)
operator|.
name|registerWith
argument_list|(
name|im
operator|.
name|getMBeanServer
argument_list|()
argument_list|)
operator|.
name|inDomain
argument_list|(
literal|"org.apache.cxf"
argument_list|)
operator|.
name|createsObjectNamesWith
argument_list|(
operator|new
name|ObjectNameFactory
argument_list|()
block|{
specifier|public
name|ObjectName
name|createName
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|domain
parameter_list|,
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|ObjectName
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|reporter
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|escapePatternChars
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// This can be replaced if really needed with pattern-based matching
if|if
condition|(
name|value
operator|.
name|lastIndexOf
argument_list|(
name|QUESTION_MARK
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|replace
argument_list|(
name|QUESTION_MARK
argument_list|,
name|ESCAPED_QUESTION_MARK
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
name|StringBuilder
name|getBaseServiceName
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|isClient
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.management.service.counter.name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|String
operator|)
name|endpoint
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.management.service.counter.name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Service
name|service
init|=
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
name|String
name|serviceName
init|=
literal|"\""
operator|+
name|escapePatternChars
argument_list|(
name|service
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|+
literal|"\""
decl_stmt|;
name|String
name|portName
init|=
literal|"\""
operator|+
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\""
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
operator|+
literal|"="
operator|+
name|bus
operator|.
name|getId
argument_list|()
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|TYPE_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|"=Metrics"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isClient
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".Client,"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".Server,"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|SERVICE_NAME_PROP
operator|+
literal|"="
operator|+
name|serviceName
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|PORT_NAME_PROP
operator|+
literal|"="
operator|+
name|portName
operator|+
literal|","
argument_list|)
expr_stmt|;
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"Client="
operator|+
name|clientId
operator|+
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buffer
return|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|MetricsContext
name|createEndpointContext
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|isClient
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
name|getBaseServiceName
argument_list|(
name|endpoint
argument_list|,
name|isClient
argument_list|,
name|clientId
argument_list|)
decl_stmt|;
specifier|final
name|String
name|baseName
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|new
name|CodahaleMetricsContext
argument_list|(
name|baseName
argument_list|,
name|registry
argument_list|)
return|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|MetricsContext
name|createOperationContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|boolean
name|asClient
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
name|getBaseServiceName
argument_list|(
name|endpoint
argument_list|,
name|asClient
argument_list|,
name|clientId
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"Operation="
argument_list|)
operator|.
name|append
argument_list|(
name|boi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
return|return
operator|new
name|CodahaleMetricsContext
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|,
name|registry
argument_list|)
return|;
block|}
block|}
end_class

end_unit

