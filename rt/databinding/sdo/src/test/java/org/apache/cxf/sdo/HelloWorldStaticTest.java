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
name|sdo
package|;
end_package

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
name|namespace
operator|.
name|QName
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
name|frontend
operator|.
name|ServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|helloworld
operator|.
name|static_types
operator|.
name|sdo
operator|.
name|Structure
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|HelloWorldStaticTest
extends|extends
name|AbstractHelloWorldTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|createService
argument_list|(
name|Server
operator|.
name|class
argument_list|,
operator|new
name|Server
argument_list|()
argument_list|,
literal|"TestService"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://apache.org/cxf/databinding/sdo/hello_world_soap_http"
argument_list|,
name|name
operator|=
literal|"Greeter"
argument_list|,
name|serviceName
operator|=
literal|"TestService"
argument_list|,
name|endpointInterface
operator|=
literal|"helloworld.static_types.ws.Greeter"
argument_list|)
specifier|public
specifier|static
class|class
name|Server
implements|implements
name|helloworld
operator|.
name|static_types
operator|.
name|ws
operator|.
name|Greeter
block|{
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|sayHi
parameter_list|()
block|{
return|return
literal|"Hi!"
return|;
block|}
specifier|public
name|void
name|pingMe
parameter_list|()
block|{         }
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|s
parameter_list|)
block|{         }
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|greetMe
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|s
return|;
block|}
specifier|public
name|Structure
name|echoStruct
parameter_list|(
name|Structure
name|struct
parameter_list|)
block|{
return|return
name|struct
return|;
block|}
block|}
specifier|protected
name|ServerFactoryBean
name|createServiceFactory
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
parameter_list|,
name|Object
name|serviceBean
parameter_list|,
name|String
name|address
parameter_list|,
name|QName
name|name
parameter_list|,
name|SDODataBinding
name|binding
parameter_list|)
block|{
name|ServerFactoryBean
name|sf
init|=
name|super
operator|.
name|createServiceFactory
argument_list|(
name|serviceClass
argument_list|,
name|serviceBean
argument_list|,
name|address
argument_list|,
name|name
argument_list|,
name|binding
argument_list|)
decl_stmt|;
name|sf
operator|.
name|setWsdlLocation
argument_list|(
name|HelloWorldStaticTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl_sdo/HelloService_static.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/databinding/sdo/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/databinding/sdo/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sf
return|;
block|}
block|}
end_class

end_unit

