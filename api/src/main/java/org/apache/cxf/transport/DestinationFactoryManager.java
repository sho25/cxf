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
name|transport
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
name|BusException
import|;
end_import

begin_comment
comment|/**  * The DestinationFactoryManager provides an interface to register and retrieve  * transport factories.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DestinationFactoryManager
block|{
comment|/**      * Associates a name, often a URI, with a<code>DestinationFactory</code>      * when registering with the<code>Bus</code>'s<code>TransportRegistry</code>.      * @param name A string containing the name used to identify the      *<code>DestinationFactory</code>      * @param factory The<code>DestinationFactory</code> to be registered.      */
name|void
name|registerDestinationFactory
parameter_list|(
name|String
name|name
parameter_list|,
name|DestinationFactory
name|factory
parameter_list|)
function_decl|;
comment|/**      * Unregister a<code>DestinationFactory</code>.      * @param name A string containing the name of the      *<code>DestinationFactory</code>.      */
name|void
name|deregisterDestinationFactory
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the<code>DestinationFactory</code> registered with the specified name,       * loading the appropriate plugin if necessary.      *       * @param name      * @return the registered<code>DestinationFactory</code>      * @throws BusException      */
name|DestinationFactory
name|getDestinationFactory
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|BusException
function_decl|;
name|DestinationFactory
name|getDestinationFactoryForUri
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

