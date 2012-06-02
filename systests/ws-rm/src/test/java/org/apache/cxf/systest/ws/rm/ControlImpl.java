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
name|systest
operator|.
name|ws
operator|.
name|rm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|Source
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
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
name|Provider
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
name|Service
operator|.
name|Mode
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
name|ServiceMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|BusFactory
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
name|spring
operator|.
name|SpringBusFactory
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
name|helpers
operator|.
name|XPathUtils
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|interceptor
operator|.
name|LoggingOutInterceptor
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"ControlService"
argument_list|,
name|portName
operator|=
literal|"ControlPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.greeter_control.Control"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/greeter_control"
argument_list|)
specifier|public
class|class
name|ControlImpl
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|greeter_control
operator|.
name|ControlImpl
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ControlImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|dbName
init|=
literal|"rmdb"
decl_stmt|;
specifier|public
name|void
name|setDbName
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|dbName
operator|=
name|s
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|startGreeter
parameter_list|(
name|String
name|cfgResource
parameter_list|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"db.name"
argument_list|,
name|dbName
argument_list|)
expr_stmt|;
name|greeterBus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|cfgResource
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"db.name"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|greeterBus
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Initialised bus "
operator|+
name|greeterBus
operator|+
literal|" with cfg file resource: "
operator|+
name|cfgResource
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"greeterBus inInterceptors: "
operator|+
name|greeterBus
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|LoggingInInterceptor
name|logIn
init|=
operator|new
name|LoggingInInterceptor
argument_list|()
decl_stmt|;
name|LoggingOutInterceptor
name|logOut
init|=
operator|new
name|LoggingOutInterceptor
argument_list|()
decl_stmt|;
name|greeterBus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|logIn
argument_list|)
expr_stmt|;
name|greeterBus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|logOut
argument_list|)
expr_stmt|;
name|greeterBus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|logOut
argument_list|)
expr_stmt|;
if|if
condition|(
name|cfgResource
operator|.
name|indexOf
argument_list|(
literal|"provider"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Published greeter endpoint."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
operator|new
name|GreeterProvider
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Published greeter provider."
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"GreeterService"
argument_list|,
name|portName
operator|=
literal|"GreeterPort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/greeter_control"
argument_list|,
name|wsdlLocation
operator|=
literal|"/wsdl/greeter_control.wsdl"
argument_list|)
annotation|@
name|ServiceMode
argument_list|(
name|Mode
operator|.
name|PAYLOAD
argument_list|)
specifier|public
specifier|static
class|class
name|GreeterProvider
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|obj
parameter_list|)
block|{
name|Node
name|el
decl_stmt|;
try|try
block|{
name|el
operator|=
name|XMLUtils
operator|.
name|fromSource
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
if|if
condition|(
name|el
operator|instanceof
name|Document
condition|)
block|{
name|el
operator|=
operator|(
operator|(
name|Document
operator|)
name|el
operator|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"ns"
argument_list|,
literal|"http://cxf.apache.org/greeter_control/types"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xp
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|String
name|s
init|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:greetMe/ns:requestType"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|s
operator|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:greetMeOneWay/ns:requestType"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"greetMeOneWay arg: "
operator|+
name|s
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"greetMe arg: "
operator|+
name|s
argument_list|)
expr_stmt|;
name|String
name|resp
init|=
literal|"<greetMeResponse "
operator|+
literal|"xmlns=\"http://cxf.apache.org/greeter_control/types\">"
operator|+
literal|"<responseType>"
operator|+
name|s
operator|.
name|toUpperCase
argument_list|()
operator|+
literal|"</responseType>"
operator|+
literal|"</greetMeResponse>"
decl_stmt|;
return|return
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|resp
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

