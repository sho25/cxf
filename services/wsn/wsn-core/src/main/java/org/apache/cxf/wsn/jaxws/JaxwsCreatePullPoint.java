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
name|wsn
operator|.
name|jaxws
package|;
end_package

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
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|ws
operator|.
name|BindingType
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
name|wsn
operator|.
name|AbstractPullPoint
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
name|wsn
operator|.
name|jms
operator|.
name|JmsCreatePullPoint
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.oasis_open.docs.wsn.bw_2.CreatePullPoint"
argument_list|,
name|targetNamespace
operator|=
literal|"http://docs.oasis-open.org/wsn/bw-2"
argument_list|,
name|serviceName
operator|=
literal|"CreatePullPoint"
argument_list|,
name|portName
operator|=
literal|"CreatePullPointPort"
argument_list|)
annotation|@
name|BindingType
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
argument_list|)
specifier|public
class|class
name|JaxwsCreatePullPoint
extends|extends
name|JmsCreatePullPoint
block|{
specifier|public
name|JaxwsCreatePullPoint
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|manager
operator|=
operator|new
name|JaxwsEndpointManager
argument_list|()
expr_stmt|;
block|}
specifier|public
name|JaxwsCreatePullPoint
parameter_list|(
name|String
name|name
parameter_list|,
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|connectionFactory
argument_list|)
expr_stmt|;
name|manager
operator|=
operator|new
name|JaxwsEndpointManager
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|AbstractPullPoint
name|createPullPoint
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|JaxwsPullPoint
name|pullPoint
init|=
operator|new
name|JaxwsPullPoint
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|pullPoint
operator|.
name|setManager
argument_list|(
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|pullPoint
operator|.
name|setConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|pullPoint
operator|.
name|setAddress
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|getAddress
argument_list|()
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"pullpoints/"
operator|+
name|name
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|pullPoint
return|;
block|}
block|}
end_class

end_unit

