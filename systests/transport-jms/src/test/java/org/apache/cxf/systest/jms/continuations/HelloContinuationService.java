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
operator|.
name|continuations
package|;
end_package

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
name|WebEndpoint
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
name|WebServiceClient
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|WebServiceClient
argument_list|(
name|name
operator|=
literal|"HelloService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/hello.wsdl"
argument_list|)
specifier|public
class|class
name|HelloContinuationService
extends|extends
name|Service
block|{
specifier|static
specifier|final
name|QName
name|SERVICE
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/systest/jaxws"
argument_list|,
literal|"HelloContinuationService"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|QName
name|HELLO_PORT
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/systest/jaxws"
argument_list|,
literal|"HelloContinuationPort"
argument_list|)
decl_stmt|;
specifier|public
name|HelloContinuationService
parameter_list|(
name|URL
name|wsdlLocation
parameter_list|,
name|QName
name|serviceName
parameter_list|)
block|{
name|super
argument_list|(
name|wsdlLocation
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebEndpoint
argument_list|(
name|name
operator|=
literal|"HelloContinuationPort"
argument_list|)
specifier|public
name|HelloContinuation
name|getHelloContinuationPort
parameter_list|()
block|{
return|return
operator|(
name|HelloContinuation
operator|)
name|super
operator|.
name|getPort
argument_list|(
name|HELLO_PORT
argument_list|,
name|HelloContinuation
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

