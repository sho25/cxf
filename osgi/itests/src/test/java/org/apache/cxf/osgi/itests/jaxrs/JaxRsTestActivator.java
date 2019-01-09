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
name|osgi
operator|.
name|itests
operator|.
name|jaxrs
package|;
end_package

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
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|osgi
operator|.
name|itests
operator|.
name|AbstractServerActivator
import|;
end_import

begin_class
specifier|public
class|class
name|JaxRsTestActivator
extends|extends
name|AbstractServerActivator
block|{
annotation|@
name|Override
specifier|protected
name|Server
name|createServer
parameter_list|()
block|{
comment|//        Bus bus = BusFactory.newInstance().createBus();
comment|//        bus.setExtension(JaxRsTestActivator.class.getClassLoader(), ClassLoader.class);
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
comment|//        sf.setBus(bus);
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"/jaxrs"
argument_list|)
expr_stmt|;
return|return
name|sf
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit

