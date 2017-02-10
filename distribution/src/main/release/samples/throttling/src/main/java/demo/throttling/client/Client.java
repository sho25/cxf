begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|throttling
operator|.
name|client
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
name|ArrayList
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
name|List
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|bus
operator|.
name|CXFBusFactory
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
name|MetricsFeature
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
name|metrics
operator|.
name|codahale
operator|.
name|CodahaleMetricsProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
implements|implements
name|Runnable
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
specifier|private
specifier|final
name|SOAPService
name|service
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|doStop
decl_stmt|;
specifier|private
name|Client
parameter_list|(
name|String
name|name
parameter_list|,
name|SOAPService
name|service
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|int
name|x
init|=
literal|0
decl_stmt|;
name|boolean
name|exceeded
init|=
literal|false
decl_stmt|;
try|try
init|(
name|Greeter
name|port
init|=
name|service
operator|.
name|getSoapPort
argument_list|(
operator|new
name|MetricsFeature
argument_list|()
argument_list|)
init|)
block|{
name|port
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|MetricsProvider
operator|.
name|CLIENT_ID
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|port
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|USERNAME_PROPERTY
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|port
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|PASSWORD_PROPERTY
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
try|try
block|{
do|do
block|{
if|if
condition|(
name|doStop
condition|)
block|{
break|break;
block|}
name|port
operator|.
name|greetMe
argument_list|(
name|username
operator|+
literal|"-"
operator|+
name|x
argument_list|)
expr_stmt|;
name|x
operator|++
expr_stmt|;
block|}
do|while
condition|(
name|x
operator|<
literal|10000
condition|)
do|;
block|}
catch|catch
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
name|wse
parameter_list|)
block|{
if|if
condition|(
name|wse
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"429"
argument_list|)
condition|)
block|{
comment|//exceeded are allowable number of requests
name|exceeded
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|wse
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|double
name|rate
init|=
name|x
operator|*
literal|1000
operator|/
operator|(
name|end
operator|-
name|start
operator|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|username
operator|+
literal|" finished "
operator|+
name|x
operator|+
literal|" invocations: "
operator|+
name|rate
operator|+
literal|" req/sec "
operator|+
operator|(
name|exceeded
condition|?
literal|"(exceeded max)"
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|//sleep for a few seconds before the client is closed so things can be seen in JMX
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|doStop
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|args
operator|=
operator|new
name|String
index|[]
block|{
name|SOAPService
operator|.
name|WSDL_LOCATION
operator|.
name|toExternalForm
argument_list|()
block|}
expr_stmt|;
block|}
name|URL
name|wsdlURL
decl_stmt|;
name|File
name|wsdlFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|wsdlURL
operator|=
name|wsdlFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|wsdlURL
operator|=
operator|new
name|URL
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"bus.jmx.usePlatformMBeanServer"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"bus.jmx.enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"bus.jmx.createMBServerConnectorFactory"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|Bus
name|b
init|=
operator|new
name|CXFBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|null
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|MetricRegistry
name|registry
init|=
operator|new
name|MetricRegistry
argument_list|()
decl_stmt|;
name|CodahaleMetricsProvider
operator|.
name|setupJMXReporter
argument_list|(
name|b
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|b
operator|.
name|setExtension
argument_list|(
name|registry
argument_list|,
name|MetricRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|SOAPService
name|ss
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdlURL
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Client
argument_list|>
name|c
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Client
name|client
decl_stmt|;
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"Tom"
argument_list|,
name|ss
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
name|client
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"Rob"
argument_list|,
name|ss
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
name|client
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"Vince"
argument_list|,
name|ss
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
name|client
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"Malcolm"
argument_list|,
name|ss
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
name|client
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|client
operator|=
operator|new
name|Client
argument_list|(
literal|"Jonas"
argument_list|,
name|ss
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
name|client
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sleeping on main thread for 60 seconds"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
for|for
control|(
name|Client
name|c2
range|:
name|c
control|)
block|{
name|c2
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
comment|//System.exit(0);
block|}
block|}
end_class

end_unit

