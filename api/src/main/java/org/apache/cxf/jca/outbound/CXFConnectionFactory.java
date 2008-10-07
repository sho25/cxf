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
name|jca
operator|.
name|outbound
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|Referenceable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|ResourceException
import|;
end_import

begin_comment
comment|/**  * Provides methods to create a {@link CXFConnection} object that provides access  * to a Web Service defined from the supplied specifications.  A CXFConnectionFactory   * is returned from an environment naming context JNDI lookup by the Application   * Server.  */
end_comment

begin_interface
specifier|public
interface|interface
name|CXFConnectionFactory
extends|extends
name|Serializable
extends|,
name|Referenceable
block|{
comment|/**      * Creates a CXFConnection object which allows access to CXF web service based on       * the CXFConnectionSpec object.  Required CXFConnectionSpec fields are wsdlURL,       * serviceClass, endpointName, and serviceName.  Each connection returned by this       * method MUST be closed by calling the {@link CXFConnection#close()} when it is no       * longer needed.        *       * @param spec      * @return CXFConnection       * @throws ResourceException      */
name|CXFConnection
name|getConnection
parameter_list|(
name|CXFConnectionSpec
name|spec
parameter_list|)
throws|throws
name|ResourceException
function_decl|;
block|}
end_interface

end_unit

