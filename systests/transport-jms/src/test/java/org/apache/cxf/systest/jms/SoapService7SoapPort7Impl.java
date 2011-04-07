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
name|jms
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

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPService7"
argument_list|,
name|portName
operator|=
literal|"SoapPort7"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_doc_lit.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/hello_world_doc_lit.wsdl"
argument_list|)
specifier|public
class|class
name|SoapService7SoapPort7Impl
extends|extends
name|GreeterImplDocBase
block|{  }
end_class

end_unit

