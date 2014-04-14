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
name|schema_validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|ws
operator|.
name|WebServiceProvider
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
name|SchemaValidation
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
import|;
end_import

begin_class
specifier|public
class|class
name|ValidationServer
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|ValidationServer
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Endpoint
argument_list|>
name|eps
init|=
operator|new
name|LinkedList
argument_list|<
name|Endpoint
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ValidationServer
parameter_list|()
block|{     }
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|SchemaValidationImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext"
decl_stmt|;
name|eps
operator|.
name|add
argument_list|(
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
operator|+
literal|"/SoapPort"
argument_list|,
name|implementor
argument_list|)
argument_list|)
expr_stmt|;
name|eps
operator|.
name|add
argument_list|(
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
operator|+
literal|"/SoapPortValidate"
argument_list|,
operator|new
name|ValidatingSchemaValidationImpl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|eps
operator|.
name|add
argument_list|(
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
operator|+
literal|"/PProvider"
argument_list|,
operator|new
name|PayloadProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|eps
operator|.
name|add
argument_list|(
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
operator|+
literal|"/MProvider"
argument_list|,
operator|new
name|MessageProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
while|while
condition|(
operator|!
name|eps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|eps
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SchemaValidationService"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.schema_validation.SchemaValidation"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/schema_validation"
argument_list|,
name|wsdlLocation
operator|=
literal|"classpath:/wsdl/schema_validation.wsdl"
argument_list|)
annotation|@
name|SchemaValidation
specifier|static
class|class
name|ValidatingSchemaValidationImpl
extends|extends
name|SchemaValidationImpl
block|{              }
annotation|@
name|WebServiceProvider
annotation|@
name|ServiceMode
argument_list|(
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
annotation|@
name|SchemaValidation
specifier|static
class|class
name|PayloadProvider
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|request
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|WebServiceProvider
annotation|@
name|ServiceMode
argument_list|(
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
argument_list|)
annotation|@
name|SchemaValidation
specifier|static
class|class
name|MessageProvider
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|request
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|ValidationServer
name|s
init|=
operator|new
name|ValidationServer
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

